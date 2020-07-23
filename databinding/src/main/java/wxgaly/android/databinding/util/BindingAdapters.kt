package wxgaly.android.databinding.util

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import wxgaly.android.databinding.R
import wxgaly.android.databinding.data.Popularity

/**
 *  wxgaly.android.databinding.util.
 *
 * @author Created by WXG on 2020/7/23 5:31 PM.
 * @version V1.0
 */
object BindingAdapters {

    @BindingAdapter("app:progressTint")
    @JvmStatic fun tintPopularity(view: ProgressBar, popularity: Popularity) {

        val color = getAssociatedColor(popularity, view.context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.progressTintList = ColorStateList.valueOf(color)
        }
    }

    @BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
    @JvmStatic fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
        progressBar.progress = (likes * max / 5).coerceAtMost(max)
    }

    @BindingAdapter("app:hideIfZero")
    @JvmStatic fun hideIfZero(view: View, number: Int) {
        view.visibility = if (number == 0) View.GONE else View.VISIBLE
    }

    private fun getAssociatedColor(popularity: Popularity, context: Context): Int {
        return when (popularity) {
            Popularity.NORMAL -> context.theme.obtainStyledAttributes(
                intArrayOf(android.R.attr.colorForeground)).getColor(0, 0x000000)
            Popularity.POPULAR -> ContextCompat.getColor(context, R.color.popular)
            Popularity.STAR -> ContextCompat.getColor(context, R.color.star)
        }
    }

}