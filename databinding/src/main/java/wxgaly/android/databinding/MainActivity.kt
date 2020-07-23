package wxgaly.android.databinding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import wxgaly.android.databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.user = User("Test", "User")
        binding.viewmodelActivityButton.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }
    }
}
