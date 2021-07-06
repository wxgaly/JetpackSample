package wxgaly.android.jetpacksample.annotation

import wxgaly.android.jetpacksample.constant.GroupType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ActivityItem(
    val group: GroupType = GroupType.None,
    val name: String,
    val brief: String
)