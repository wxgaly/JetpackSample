package wxgaly.android.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 *  wxgaly.android.benchmark.
 *
 * @author Created by WXG on 2020/7/7 11:30 AM.
 * @version V1.0
 */
@RunWith(AndroidJUnit4::class)
class ViewBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    /**
     * Measure the cost of allocating a boxed integer that takes advantage of ART's cache.
     */
    @Test
    fun integerArtCacheAlloc() {
        var i = Integer(1000)
        benchmarkRule.measureRepeated {
            if (i < 100) {
                i = Integer(i.toInt() + 1)
            } else {
                i = Integer(0)
            }
        }
    }
}