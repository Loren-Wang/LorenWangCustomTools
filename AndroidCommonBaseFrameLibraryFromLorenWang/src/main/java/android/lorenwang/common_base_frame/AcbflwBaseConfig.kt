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


    /**
     * app编译类型之debug模式
     */
    private const val APP_COMPILE_TYPE_DEBUG = 0

    /**
     * app编译类型之test模式
     */
    private const val APP_COMPILE_TYPE_TEST = 1

    /**
     * app编译类型之release模式
     */
    private const val APP_COMPILE_TYPE_RELEASE = 2

    /**
     * 当前编译类型是否是正式环境
     *
     * @return true为是正式环境编译
     */
    fun appCompileTypeIsRelease(): Boolean {
        return APP_COMPILE_TYPE_RELEASE.compareTo(BuildConfig.APP_COMPILE_TYPE) == 0
    }

    /**
     * 当前编译类型是否是开发环境
     *
     * @return true为是开发环境编译
     */
    fun appCompileTypeIsDebug(): Boolean {
        return APP_COMPILE_TYPE_DEBUG.compareTo(BuildConfig.APP_COMPILE_TYPE) == 0
    }

    /**
     * 当前编译类型是否是测试环境
     *
     * @return true为是测试环境编译
     */
    fun appCompileTypeIsTest(): Boolean {
        return APP_COMPILE_TYPE_TEST.compareTo(BuildConfig.APP_COMPILE_TYPE) == 0
    }
}
