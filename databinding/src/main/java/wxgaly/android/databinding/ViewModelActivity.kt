package wxgaly.android.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import wxgaly.android.databinding.data.ProfileLiveDataViewModel
import wxgaly.android.databinding.databinding.ViewmodelProfileBinding

/**
 *  wxgaly.android.databinding.
 *
 * @author Created by WXG on 2020/7/23 3:56 PM.
 * @version V1.0
 */
class ViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain binding
        val binding: ViewmodelProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.viewmodel_profile)

        // Obtain ViewModel from ViewModelProviders
        val viewModel = ViewModelProvider(this).get(ProfileLiveDataViewModel::class.java)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

    }

}