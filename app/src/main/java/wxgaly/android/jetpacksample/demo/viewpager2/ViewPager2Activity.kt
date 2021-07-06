package wxgaly.android.jetpacksample.demo.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.viewpager2_activity_view_pager2.*
import wxgaly.android.jetpacksample.R
import wxgaly.android.jetpacksample.annotation.ActivityItem
import wxgaly.android.jetpacksample.constant.GroupType
import wxgaly.android.viewpager2.CardAdapter

@ActivityItem(
    group = GroupType.ViewPager2,
    name = "ViewPager2",
    brief = "简单的ViewPager2演示"
)
class ViewPager2Activity : AppCompatActivity() {

    private val list = listOf("hello", "world", ", ", "you")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager2_activity_view_pager2)
        initView()
    }

    private fun initView() {
        view_pager.adapter = CardAdapter(list)
    }

}