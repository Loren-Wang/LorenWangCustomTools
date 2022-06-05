package springbase.lorenwang.tools.plugins.sms

import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.plugins.sms.aliyun.SptlwALiYunSmsConfig
import springbase.lorenwang.tools.plugins.sms.aliyun.SptlwALiYunSmsUtil

/**
 * 功能作用：Sms单例工具类
 * 创建时间：2020-02-03 下午 17:30:12
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class SptlwSmsUtil private constructor() : AbstractSMSOptions() {
    /**
     * oss操作实例
     */
    private var ossOptions: AbstractSMSOptions? = null

    companion object {
        @Volatile
        private var optionsInstance: SptlwSmsUtil? = null

        @JvmStatic
        val instance: SptlwSmsUtil
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = SptlwSmsUtil()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 初始化Sms相关配置
     */
    fun initSmsConfig(ossConfig: Any) {
        when (ossConfig) {
            is SptlwALiYunSmsConfig -> {
                ossOptions = SptlwALiYunSmsUtil(ossConfig)
            }
        }
    }

    /**
     * 发送短信
     * @param mobile 发送的目标手机号
     * @param content 发送的短信内容，短信内各配置参数的集合
     * @param templateCode 发送使用的短信模板
     */
    override fun sendSms(mobile: String, content: Map<String, Any>, templateCode: String): SpblwBaseDataDisposeStatusBean {
        return ossOptions?.sendSms(mobile, content, templateCode).kttlwGetNotEmptyData { SpblwBaseDataDisposeStatusBean(false) }
    }

}
