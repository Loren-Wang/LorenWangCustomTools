package javabase.lorenwang.common_base_frame.propertiesConfig

import org.springframework.beans.factory.annotation.Value

/**
 * 功能作用：属性配置文件
 * 创建时间：2019-12-16 11:13
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwPropertiesConfig {
    /**
     * 文件最大大小，单位是字节
     */
    @Value("\${Sbcbflw.oss.type.fileMaxSize}")
    open var ossTypeFileMaxSize = 5242880L
    /**
     * oss系统协议，通用参数
     */
    @Value("\${Sbcbflw.oss.type.protocol}")
    open var ossTypeProtocol = ""
    /**
     * 是否是阿里云存储
     */
    @Value("\${Sbcbflw.oss.type.aliYun}")
    open var ossTypeAliYun = false
    /**
     * 是否是七牛存储
     */
    @Value("\${Sbcbflw.oss.type.qiNiu}")
    open var ossTypeQiNiu = false

    /*
   * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
   */
    @Value("\${Sbcbflw.encryptDecrypt.Key}")
    open var encryptDecryptKey = ""
    /**
     * 加密解密的算法参数
     */
    @Value("\${Sbcbflw.encryptDecrypt.IvParameter}")
    open var encryptDecryptIvParameter = ""

    /**
     * 是否显示打印log日志
     */
    @Value("\${Sbcbflw.showLog}")
    open var showLog = false
    /**
     * 是否是debug环境
     */
    @Value("\${Sbcbflw.isDebug}")
    open var isDebug = false

    /**
     * 邮箱端口
     */
    @Value("\${Sbcbflw.emai.host}")
    open var emailHost = ""
    /**
     * 邮箱用户名称
     */
    @Value("\${Sbcbflw.emai.UserName}")
    open var emailUserName = ""
    /**
     * 邮箱密码
     */
    @Value("\${Sbcbflw.emai.UserPassword}")
    open var emailUserPassword = ""

    /**
     * 当前编译器环境
     */
    @Value("\${Sbcbflw.runCompilingEnvironment}")
    open var runCompilingEnvironment = 0

    /**
     * 默认每页大小
     */
    @Value("\${Sbcbflw.default.page.size}")
    open var defaultRequestPageSize = 10;
    /**
     * 默认第一页
     */
    @Value("\${Sbcbflw.default.page.index}")
    open var defaultRequestPageIndex = 0;

}
