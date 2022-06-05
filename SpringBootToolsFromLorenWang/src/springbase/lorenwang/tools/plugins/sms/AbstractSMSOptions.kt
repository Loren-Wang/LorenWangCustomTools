package springbase.lorenwang.tools.plugins.sms

import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean

/**
 * 功能作用：Sms操作接口
 * 初始注释时间： 2022/6/5 12:54
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
abstract class AbstractSMSOptions {
    /**
     * 发送短信
     * @param mobile 发送的目标手机号
     * @param content 发送的短信内容，短信内各配置参数的集合
     * @param templateCode 发送使用的短信模板
     */
    abstract fun sendSms(mobile: String, content: Map<String, Any>, templateCode: String): SpblwBaseDataDisposeStatusBean
}