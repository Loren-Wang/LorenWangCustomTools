package springbase.lorenwang.tools.plugins.email

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import springbase.lorenwang.base.SpblwConfig
import java.util.*


/**
 * 功能作用：email相关操作
 * 创建时间：2019-12-16 12:13
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 1、发送邮件---sendEmailMessage(title,content,tiEmail)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SptlwEmailUtils {
    /**
     * 邮件发送者
     */
    private var javaMailSender: JavaMailSenderImpl? = null

    /**
     * 初始化配置
     */
    fun initConfig(config: Config) {
        try {
            val properties = Properties()
            properties["mail.smtp.auth"] = config.auth
            properties["mail.smtp.timeout"] = config.timeout
            properties["mail.smtp.port"] = config.port
            properties["mail.smtp.socketFactory.port"] = config.socketFactoryPort
            properties["mail.smtp.socketFactory.fallback"] = config.socketFactoryFallback
            properties["mail.smtp.socketFactory.class"] = config.socketFactoryClass

            javaMailSender = JavaMailSenderImpl()
            javaMailSender!!.javaMailProperties = properties
            javaMailSender!!.host = config.emailHost
            javaMailSender!!.username = config.emailUserName
            javaMailSender!!.password = config.emailUserPassword
            javaMailSender!!.testConnection()
            SpblwConfig.logUtils.logI(this::class.java, "邮件发送工具初始化结束，测试连接状态")
        } catch (e: Exception) {
            javaMailSender = null
            SpblwConfig.logUtils.logE(this::class.java, "邮件发送工具初始化失败，失败原因：${e.message}")
        }
    }

    /**
     * 发送邮件
     * @param title 邮件标题
     * @param content 邮件内容
     * @param toEmail 目标邮件
     * @return 邮件发送结果，true成功
     */
    fun sendEmailMessage(title: String, content: String, toEmail: String): Boolean {
        return javaMailSender.kttlwEmptyCheck({
            SpblwConfig.logUtils.logI(this::class.java, "向${toEmail}发送邮件失败,邮件发送实例无法初始化")
            false
        }, {
            try {
                SpblwConfig.logUtils.logI(this::class.java, "开始向${toEmail}发送邮件")
                val message = it.createMimeMessage()
                val messageHelper = MimeMessageHelper(message, true, "utf-8")
                messageHelper.setFrom(javaMailSender!!.username)
                messageHelper.setTo(toEmail)
                messageHelper.setSubject(title)
                messageHelper.setText(content)
                it.send(message)
                SpblwConfig.logUtils.logI(this::class.java, "向${toEmail}发送邮件成功")
                true
            } catch (e: Exception) {
                SpblwConfig.logUtils.logI(this::class.java, "向${toEmail}发送邮件失败：${e.message}")
                false
            }
        })
    }

    /**
     * 配置文件
     * @param emailHost 邮箱端口
     * @param emailUserName 邮箱用户名称
     * @param emailUserPassword 邮箱密码
     */
    class Config(
        var emailHost: String,
        var emailUserName: String,
        var emailUserPassword: String,
        var auth: Boolean = true, var timeout: Long = 25000, var port: Int = 465, var socketFactoryPort: Int = 465,
        var socketFactoryFallback: Boolean = false, var socketFactoryClass: String = "javax.net.ssl.SSLSocketFactory",
    )
}
