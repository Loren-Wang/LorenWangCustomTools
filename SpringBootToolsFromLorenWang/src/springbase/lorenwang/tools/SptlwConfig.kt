package springbase.lorenwang.tools

import springbase.lorenwang.base.SpblwConfig
import springbase.lorenwang.base.spblwConfig
import springbase.lorenwang.tools.plugins.email.SptlwEmailUtil
import springbase.lorenwang.tools.plugins.oss.SptlwOssUtil
import springbase.lorenwang.tools.utils.SptlwFileOptionsUtil

/**
 * 功能作用：SpringBoot工具库接口
 * 初始注释时间： 2022/2/28 10:03
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
abstract class SptlwConfig : SpblwConfig() {
    init {
        spblwConfig = this
    }

    /**
     * 文件操作工具类
     */
    abstract fun getFileOptionsUtil(): SptlwFileOptionsUtil

    /**
     * 邮件工具类
     */
    open fun getEmailUtil(): SptlwEmailUtil? = null

    /**
     * 获取oss工具
     */
    open fun getOssUtil(): SptlwOssUtil? = null
}

/**
 * 工具库config
 */
lateinit var sptlwConfig: SptlwConfig