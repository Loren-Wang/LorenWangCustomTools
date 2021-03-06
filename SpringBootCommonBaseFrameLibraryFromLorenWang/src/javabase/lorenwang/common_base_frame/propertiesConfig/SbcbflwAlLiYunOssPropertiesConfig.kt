package javabase.lorenwang.common_base_frame.propertiesConfig

import org.springframework.context.ConfigurableApplicationContext

/**
 * 功能作用：属性配置文件
 * 创建时间：2019-12-16 11:13
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1、阿里云oss系统域名---aLiYunOssDomain
 * 配置参数2、阿里云oss访问域名---aLiYunOssEndpoint
 * 配置参数3、阿里云oss系统keyId---aLiYunOssAccessKeyId
 * 配置参数4、阿里云oss系统密钥---aLiYunOssAccessKeySecret
 * 配置参数5、阿里云存储空间名---aLiYunOssBucket
 * 配置参数6、加密用的Key---encryptDecryptKey(可以用26个字母和数字组成 使用AES-128-CBC加密模式，key需要为16位。)
 * 配置参数7、加密解密的算法参数---encryptDecryptIvParameter
 * 配置参数8、邮箱端口---emailHost
 * 配置参数9、邮箱用户名称---emailUserName
 * 配置参数10、邮箱密码---emailUserPassword
 * 配置参数11、邮箱配置文件---emailProperties(配置文件名称：application-email.properties)
 * 配置参数12、当前编译器环境---runCompilingEnvironment
 * 配置参数13、默认每页大小---defaultRequestPageSize
 * 配置参数14、默认第一页---defaultRequestPageIndex
 */
internal class SbcbflwAlLiYunOssPropertiesConfig(applicationContext: ConfigurableApplicationContext) {

    /**
     * 阿里云oss系统域名
     */
    var domain = ""
    /**
     * 阿里云oss访问域名
     */
    var endpoint = ""
    /**
     * 阿里云oss系统keyId
     */
    var accessKeyId = ""
    /**
     * 阿里云oss系统密钥
     */
    var accessKeySecret = ""
    /**
     * 阿里云存储空间名
     */
    var bucket = ""

    init {
        this.domain = applicationContext.environment.getProperty("Sbcbflw.oss.type.aliYun.domain", "")
        this.endpoint = applicationContext.environment.getProperty("Sbcbflw.oss.type.aliYun.endpoint", "")
        this.accessKeyId = applicationContext.environment.getProperty("Sbcbflw.oss.type.aliYun.accessKeyId", "")
        this.accessKeySecret = applicationContext.environment.getProperty("Sbcbflw.oss.type.aliYun.accessKeySecret", "")
        this.bucket = applicationContext.environment.getProperty("Sbcbflw.oss.type.aliYun.bucket", "")
    }
}
