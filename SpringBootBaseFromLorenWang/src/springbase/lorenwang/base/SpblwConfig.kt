package springbase.lorenwang.base

import springbase.lorenwang.base.utils.SpblwLog

/**
 * 功能作用：文件配置
 * 初始注释时间： 2022/2/12 17:57
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
object SpblwConfig {
    /**
     * 日志工具类
     */
    @JvmStatic
    lateinit var logUtils: SpblwLog

    /**
     * 当前是否是debug模式
     */
    @JvmStatic
    var currentIsDebug = false
}