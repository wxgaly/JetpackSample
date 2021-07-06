package wxgaly.android.jetpacksample

import android.app.Application
import android.util.Log
import wxgaly.android.jetpacksample.utils.ClassUtils

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initItems()
    }

    private fun initItems() {
        val fileNameByPackageName =
            ClassUtils.getFileNameByPackageName(this, "wxgaly.android.jetpacksample.demo")
        fileNameByPackageName.forEach {
            Log.d("wxg", "initItems: $it")
        }
    }

}