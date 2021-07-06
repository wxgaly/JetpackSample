package wxgaly.android.jetpacksample.utils

import android.util.Log
import java.util.concurrent.*

private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
private val INIT_THREAD_COUNT = CPU_COUNT + 1
private val MAX_THREAD_COUNT = INIT_THREAD_COUNT
private const val SURPLUS_THREAD_LIFE = 30L

object DefaultPoolExecutor : ThreadPoolExecutor(
    INIT_THREAD_COUNT,
    MAX_THREAD_COUNT,
    SURPLUS_THREAD_LIFE,
    TimeUnit.SECONDS,
    ArrayBlockingQueue(64),
    DefaultThreadFactory(),
    RejectedExecutionHandler { _, _ ->
        Log.e(
            "DefaultPoolExecutor",
            "Task rejected, too many task!"
        )
    }
) {

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)
        var throwable = t
        if (throwable == null && r is Future<*>) {
            try {
                (r as Future<*>).get()
            } catch (ce: CancellationException) {
                throwable = ce
            } catch (ee: ExecutionException) {
                throwable = ee.cause
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt() // ignore/reset
            }
        }
        if (t != null) {
            Log.w(
                "DefaultPoolExecutor",
                "Running task appeared exception! Thread [${Thread.currentThread().name}], because[${t.message}]${
                    formatStackTrace(
                        t.stackTrace
                    )
                }".trimIndent()
            )
        }
    }

    private fun formatStackTrace(stackTrace: Array<StackTraceElement>): String? {
        val sb = StringBuilder()
        for (element in stackTrace) {
            sb.append("    at ").append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

}