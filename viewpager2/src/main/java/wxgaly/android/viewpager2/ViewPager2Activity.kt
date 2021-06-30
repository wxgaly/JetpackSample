package wxgaly.android.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_pager2.*

class ViewPager2Activity : AppCompatActivity() {

    private val list = listOf("hello", "world", ", ", "you")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2)
        initView()
    }

    private fun initView() {
        view_pager.adapter = CardAdapter(list)
    }

}