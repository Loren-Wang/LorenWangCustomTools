package javabase.lorenwang.common_base_frame.utils

/**
 * 功能作用：基础全部工具类实例
 * 创建时间：2020-08-26 1:50 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
object SbcbfBaseAllUtils {
    /**
     * 日志工具类
     */
    @JvmStatic
    var logUtils: SbcbflwLogUtils = SbcbflwLogUtils()

    /**
     * 文件操作工具类
     */
    @JvmStatic
    var fileOptionsUtils: SbcbflwBaseFileOptionsUtils? = null
}
