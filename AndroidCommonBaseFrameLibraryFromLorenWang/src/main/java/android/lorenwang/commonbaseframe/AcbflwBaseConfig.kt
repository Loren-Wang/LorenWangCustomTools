package android.lorenwang.commonbaseframe

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
     * 当前项目包名
     */
    var applicationIdPackageNameCurrent = ""

    /**
     * 当前编译类型是否是正式环境
     *
     * @return true为是正式环境编译
     */

    @JvmStatic
    fun appCompileTypeIsRelease(appCompileType: Int): Boolean {
        return APP_COMPILE_TYPE_RELEASE.compareTo(appCompileType) == 0
    }

    /**
     * 当前编译类型是否是开发环境
     *
     * @return true为是开发环境编译
     */
    @JvmStatic
    fun appCompileTypeIsDebug(appCompileType: Int): Boolean {
        return APP_COMPILE_TYPE_DEBUG.compareTo(appCompileType) == 0
    }

    /**
     * 当前编译类型是否是测试环境
     *
     * @return true为是测试环境编译
     */
    @JvmStatic
    fun appCompileTypeIsTest(appCompileType: Int): Boolean {
        return APP_COMPILE_TYPE_TEST.compareTo(appCompileType) == 0
    }
}
