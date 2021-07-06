package wxgaly.android.jetpacksample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startActivity(Intent(this, ViewPager2Activity::class.java))
        initData()
    }

    private fun initData() {

    }

    private fun findAllItem() {

    }
}