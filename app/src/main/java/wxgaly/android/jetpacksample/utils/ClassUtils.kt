package wxgaly.android.jetpacksample.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dalvik.system.DexFile
import wxgaly.android.jetpacksample.BuildConfig
import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.regex.Matcher
import java.util.regex.Pattern

object ClassUtils {

    private val tag = "ClassUtils"

    private const val EXTRACTED_NAME_EXT = ".classes"
    private const val EXTRACTED_SUFFIX = ".zip"

    private val SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes"

    private const val PREFS_FILE = "multidex.version"
    private const val KEY_DEX_NUMBER = "dex.number"

    private const val VM_WITH_MULTIDEX_VERSION_MAJOR = 2
    private const val VM_WITH_MULTIDEX_VERSION_MINOR = 1

    fun getFileNameByPackageName(context: Context, packageName: String): Set<String> {
        val classNames: MutableSet<String> = HashSet()

        val paths = getSourcePaths(context)
        val parserCtl = CountDownLatch(paths.size)

        for (path in paths) {
            DefaultPoolExecutor.execute(Runnable {
                var dexfile: DexFile? = null
                try {
                    dexfile = if (path.endsWith(EXTRACTED_SUFFIX)) {
                        //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                        DexFile.loadDex(path, "$path.tmp", 0)
                    } else {
                        DexFile(path)
                    }
                    val dexEntries: Enumeration<String> = dexfile!!.entries()
                    while (dexEntries.hasMoreElements()) {
                        val className = dexEntries.nextElement()
                        if (className.startsWith(packageName)) {
                            classNames.add(className)
                        }
                    }
                } catch (ignore: Throwable) {
                    Log.e("ARouter", "Scan map file in dex files made error.", ignore)
                } finally {
                    if (null != dexfile) {
                        try {
                            dexfile.close()
                        } catch (ignore: Throwable) {
                        }
                    }
                    parserCtl.countDown()
                }
            })
        }

        parserCtl.await()

        Log.d(tag, "Filter " + classNames.size + " classes by packageName <" + packageName + ">")
        return classNames
    }

    private fun getMultiDexPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PREFS_FILE,
            Context.MODE_PRIVATE or Context.MODE_MULTI_PROCESS
        )
    }


    /**
     * get all the dex path
     *
     * @param context the application context
     * @return all the dex path
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     */
    @Throws(PackageManager.NameNotFoundException::class, IOException::class)
    fun getSourcePaths(context: Context): List<String> {
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
        val sourceApk = File(applicationInfo.sourceDir)
        val sourcePaths: MutableList<String> = ArrayList()
        sourcePaths.add(applicationInfo.sourceDir) //add the default apk path

        //the prefix of extracted file, ie: test.classes
        val extractedFilePrefix = sourceApk.name + EXTRACTED_NAME_EXT
        /** If MultiDex already supported by VM, we will not to load Classesx.zip from
         * Secondary Folder, because there is none. */
        if (!isVMMultidexCapable()) {
            //the total dex numbers
            val totalDexNumber: Int = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1)
            val dexDir = File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME)
            for (secondaryNumber in 2..totalDexNumber) {
                //for each dex file, ie: test.classes2.zip, test.classes3.zip...
                val fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX
                val extractedFile = File(dexDir, fileName)
                if (extractedFile.isFile) {
                    sourcePaths.add(extractedFile.absolutePath)
                    //we ignore the verify zip part
                } else {
                    throw IOException("Missing extracted secondary dex file '${extractedFile.path}'")
                }
            }
        }
        if (BuildConfig.DEBUG) { // Search instant run support only debuggable
            sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo))
        }
        return sourcePaths
    }

    /**
     * Get instant run dex path, used to catch the branch usingApkSplits=false.
     */
    private fun tryLoadInstantRunDexFile(applicationInfo: ApplicationInfo): List<String> {
        val instantRunSourcePaths: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != applicationInfo.splitSourceDirs) {
            // add the split apk, normally for InstantRun, and newest version.
            instantRunSourcePaths.addAll(applicationInfo.splitSourceDirs.asList())
            Log.d(tag, "Found InstantRun support")
        } else {
            try {
                // This man is reflection from Google instant run sdk, he will tell me where the dex files go.
                val pathsByInstantRun = Class.forName("com.android.tools.fd.runtime.Paths")
                val getDexFileDirectory: Method = pathsByInstantRun.getMethod(
                    "getDexFileDirectory",
                    String::class.java
                )
                val instantRunDexPath =
                    getDexFileDirectory.invoke(null, applicationInfo.packageName) as String
                val instantRunFilePath = File(instantRunDexPath)
                if (instantRunFilePath.exists() && instantRunFilePath.isDirectory) {
                    val dexFile: Array<File> = instantRunFilePath.listFiles()
                    for (file in dexFile) {
                        if (file.exists() && file.isFile && file.name
                                .endsWith(".dex")
                        ) {
                            instantRunSourcePaths.add(file.absolutePath)
                        }
                    }
                    Log.d(tag, "Found InstantRun support")
                }
            } catch (e: java.lang.Exception) {
                Log.e(tag, "InstantRun support error, " + e.message)
            }
        }
        return instantRunSourcePaths
    }


    private fun isVMMultidexCapable(): Boolean {
        var isMultidexCapable = false
        var vmName: String? = null
        try {
            if (isYunOS()) {    // YunOS need special judgment
                vmName = "'YunOS'"
                isMultidexCapable =
                    Integer.valueOf(System.getProperty("ro.build.version.sdk")) >= 21
            } else {    // Native Android system
                vmName = "'Android'"
                val versionString = System.getProperty("java.vm.version")
                if (versionString != null) {
                    val matcher: Matcher =
                        Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString)
                    if (matcher.matches()) {
                        try {
                            val major: Int = matcher.group(1).toInt()
                            val minor: Int = matcher.group(2).toInt()
                            isMultidexCapable = (major > VM_WITH_MULTIDEX_VERSION_MAJOR
                                    || (major == VM_WITH_MULTIDEX_VERSION_MAJOR
                                    && minor >= VM_WITH_MULTIDEX_VERSION_MINOR))
                        } catch (ignore: NumberFormatException) {
                            // let isMultidexCapable be false
                        }
                    }
                }
            }
        } catch (ignore: java.lang.Exception) {
        }
        Log.i(
            tag,
            "VM with name " + vmName + if (isMultidexCapable) " has multidex support" else " does not have multidex support"
        )
        return isMultidexCapable
    }


    private fun isYunOS(): Boolean {
        return try {
            val version = System.getProperty("ro.yunos.version")
            val vmName = System.getProperty("java.vm.name")
            (vmName != null && vmName.lowercase(Locale.getDefault()).contains("lemur")
                    || version != null && version.trim { it <= ' ' }.isNotEmpty())
        } catch (ignore: Exception) {
            false
        }
    }

}