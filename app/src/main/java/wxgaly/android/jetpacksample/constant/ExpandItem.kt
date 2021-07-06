package wxgaly.android.jetpacksample.constant

data class ExpandItem<T>(
    val name: String,
    val childrenList: List<ExpandItem<T>>,
    val data: T
)
