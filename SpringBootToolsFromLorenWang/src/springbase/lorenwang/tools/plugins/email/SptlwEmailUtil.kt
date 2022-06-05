package springbase.lorenwang.tools.plugins.email

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import springbase.lorenwang.tools.sptlwConfig
import java.util.*
import javax.activation.CommandMap
import javax.activation.MailcapCommandMap
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


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
open class SptlwEmailUtil(private val config: Config) {
    init {
        getSmsSendClient()
    }

    /**
     * 发送邮件
     * @param content 邮件内容
     * @param toEmail 收件人，多个邮箱地址以半角逗号分隔
     * @param ccEmail 抄送，多个邮箱地址以半角逗号分隔
     * @param mailSubject 邮件主题
     * @return 邮件发送结果，true成功
     */
    fun sendEmailMessage(content: String, toEmail: String, ccEmail: String? = null, mailSubject: String? = null): Boolean {
        return getSmsSendClient().kttlwEmptyCheck({
            sptlwConfig.getLogUtil().logI(javaClass, "向${toEmail}发送邮件失败,邮件发送实例无法初始化")
            false
        }, {
            try {
                sptlwConfig.getLogUtil().logI(javaClass, "邮件相关信息->目标人员:${toEmail},抄送人员：${ccEmail},邮件主题：${mailSubject},邮件内容：${content}")
                //收件人
                it.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                //抄送
                if (!ccEmail.isNullOrEmpty()) {
                    it.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail))
                }
                //邮件主题
                if (!mailSubject.isNullOrEmpty()) {
                    it.subject = mailSubject
                }
                //邮件内容
                it.setContent(content, "text/html;charset=UTF-8")
                //发送邮件
                it.saveChanges()
                sptlwConfig.getLogUtil().logI(javaClass, "向${toEmail}开始发送邮件")
                solveError()
                Transport.send(it)
                sptlwConfig.getLogUtil().logI(javaClass, "向${toEmail}发送邮件成功")
                true
            } catch (e: Exception) {
                sptlwConfig.getLogUtil().logI(javaClass, "向${toEmail}发送邮件失败：${e.message}")
                false
            }
        })
    }

    /**
     * no object DCH for MIME type multipart/mixed报错解决
     */
    private fun solveError() {
        val mc: MailcapCommandMap = CommandMap.getDefaultCommandMap() as MailcapCommandMap
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true")
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822")
        CommandMap.setDefaultCommandMap(mc)
        Thread.currentThread().contextClassLoader = javaClass.classLoader
    }


    /**
     * 配置文件
     * @param emailHost 邮箱端口
     * @param emailUserName 邮箱用户名称
     * @param emailUserPassword 邮箱密码
     */
    class Config(
        var emailUserName: String,
        var emailUserPassword: String,
        var emailHost: String,
        var port: Int = 994,
        var auth: Boolean = true,
    )

    /**
     * 获取短信发送的发送源
     */
    private fun getSmsSendClient(config: Config = this.config): MimeMessage? {
        var mimeMsg: MimeMessage?
        try {
            // 1. 创建参数配置, 用于连接邮件服务器的参数配置
            val props = Properties()                    // 参数配置
            props["mail.transport.protocol"] = "smtp"   // 使用的协议（JavaMail规范要求）
            props["mail.smtp.host"] = config.emailHost  // 发件人的邮箱的 SMTP 服务器地址
            props["mail.smtp.auth"] = config.auth       // 需要请求认证
            props["mail.smtp.port"] = config.port
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.socketFactory.fallback"] = "false"
            props["mail.smtp.socketFactory.port"] = config.port
            // 建立会话
            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                    return javax.mail.PasswordAuthentication(config.emailUserName.substring(0, config.emailUserName.lastIndexOf("@")),
                        config.emailUserPassword)
                }
            })
            // 置true可以在控制台（console)上看到发送邮件的过程
            session.debug = sptlwConfig.currentIsDebug()
            // 用session对象来创建并初始化邮件对象
            mimeMsg = MimeMessage(session)
            mimeMsg.setFrom(InternetAddress(config.emailUserName))
            sptlwConfig.getLogUtil().logI(javaClass, "邮件发送客户端初始化结束")
        } catch (e: Exception) {
            mimeMsg = null
            sptlwConfig.getLogUtil().logE(javaClass, "邮件发送客户端初始化失败，失败原因：${e.message}")
        }
        return mimeMsg
    }
}
