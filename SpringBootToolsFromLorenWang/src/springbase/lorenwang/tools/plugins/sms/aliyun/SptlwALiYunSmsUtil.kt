package springbase.lorenwang.tools.plugins.sms.aliyun

import com.aliyun.dysmsapi20170525.Client
import com.aliyun.dysmsapi20170525.models.SendSmsRequest
import com.aliyun.teaopenapi.models.Config
import com.aliyun.teautil.models.RuntimeOptions
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.plugins.sms.AbstractSMSOptions
import springbase.lorenwang.tools.sptlwConfig
import java.util.*


/**
 * 功能作用：阿里云Sms工具类
 * 创建时间：2019-09-12 下午 16:15:17
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * @param smsConfig 配置文件
 */
internal class SptlwALiYunSmsUtil(private val smsConfig: SptlwALiYunSmsConfig) : AbstractSMSOptions() {
    /**
     * 短信发送客户端
     */
    private var client: Client = Client(Config().also {
        it.accessKeyId = smsConfig.accessKeyId
        it.accessKeySecret = smsConfig.accessKeySecret
        it.endpoint = smsConfig.endpoint
    })

    /**
     * 发送短信
     */
    override fun sendSms(mobile: String, content: Map<String, Any>, templateCode: String): SpblwBaseDataDisposeStatusBean {
        //短信发送请求
        sptlwConfig.getLogUtil().logI(javaClass, "请求发送短信，目标号码：${mobile}，签名：${smsConfig.signName}，短信模板：${templateCode}，参数内容：${content}")
        val sendSmsRequest = SendSmsRequest().setPhoneNumbers(mobile).setSignName(smsConfig.signName).setTemplateCode(templateCode)
            .setTemplateParam(content.kttlwToJsonData())
        val runtime = RuntimeOptions()
        return try {
            // 复制代码运行请自行打印 API 的返回值
            val response = client.sendSmsWithOptions(sendSmsRequest, runtime)
            sptlwConfig.getLogUtil().logI(javaClass, "短信发送结束，响应code：${response.body.code}，响应消息：${response.body.message}")
            if ("ok" == response.body.code.toLowerCase(Locale.ROOT)) {
                SpblwBaseDataDisposeStatusBean(true)
            } else {
                SpblwBaseDataDisposeStatusBean(false, response.body.code, response.body.message, null)
            }
        } catch (error: Exception) {
            sptlwConfig.getLogUtil().logI(javaClass, "短信发送结束，响应异常内容：${error.message}")
            SpblwBaseDataDisposeStatusBean(false)
        }
    }

}
