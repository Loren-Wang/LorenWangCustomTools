package android.lorenwang.common_base_frame

import androidx.annotation.DrawableRes
import androidx.customview.R

/**
 * 功能作用：基础配置文件
 * 创建时间：2019-12-12 10:26
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
object AcbflwBaseConfig {
    /**
     * 标题栏高度
     */
    var titleBarHeadViewHeight = 0
    /**
     * 基础底部栏高度高度
     */
    var baseBottomViewHeight = 0
    /**
     * 基础debugzhuangt
     */
    var baseDebugStatus = BuildConfig.DEBUG
    /**
     * 图片加载失败图片
     */
    @DrawableRes
    @kotlin.jvm.JvmField
    var imageLoadingFailResId = R.drawable.notification_bg_low
    /**
     * 图片加载加载中图片
     */
    @DrawableRes
    @kotlin.jvm.JvmField
    var imageLoadingLoadResId = R.drawable.notification_bg_low
}
