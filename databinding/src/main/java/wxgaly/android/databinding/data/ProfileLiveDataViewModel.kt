package wxgaly.android.databinding.data

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 *  wxgaly.android.databinding.data.
 *
 * @author Created by WXG on 2020/7/23 4:27 PM.
 * @version V1.0
 */
class ProfileLiveDataViewModel : ViewModel() {

    private val _name = MutableLiveData<String>().apply { value = "Ada" }
    private val _lastName = MutableLiveData<String>().apply { value = "Lovelace" }
    private val _likes =  MutableLiveData<Int>().apply { value = 0 }

    val name: LiveData<String> = _name
    val lastName: LiveData<String> = _lastName
    val likes: LiveData<Int> = _likes

    // popularity is exposed as LiveData using a Transformation instead of a @Bindable property.
    val popularity: LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }

}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}

private fun ObservableInt.increment() {
    set(get() + 1)
}