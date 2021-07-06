package wxgaly.android.jetpacksample.demo.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import wxgaly.android.jetpacksample.annotation.ActivityItem
import wxgaly.android.jetpacksample.constant.GroupType

@ActivityItem(
    group = GroupType.ViewPager2,
    name = "ViewPager2Fragment",
    brief = "简单的FragmentAdapter演示"
)
class ViewPager2FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}