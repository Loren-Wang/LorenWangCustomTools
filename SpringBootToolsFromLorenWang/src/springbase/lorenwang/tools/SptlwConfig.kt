package springbase.lorenwang.tools

import springbase.lorenwang.tools.plugins.email.SptlwEmailUtils
import springbase.lorenwang.tools.utils.SptlwFileOptionsUtils

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
object SptlwConfig {

    /**
     * 文件操作工具类
     */
    @JvmStatic
    lateinit var fileOptionsUtils: SptlwFileOptionsUtils

    /**
     * 邮件工具类
     */
    @JvmStatic
    lateinit var emailUtils: SptlwEmailUtils

}