package springbase.lorenwang.user.enums

/**
 * 功能作用：用户登录类型枚举
 * 创建时间：2022/4/16 10:16
 * 创建者：王亮（Loren）
 * 备注：
 */
enum class SpulwUserLoginTypeEnum {
    /**
     * 账户密码
     */
    ACCOUNT_PASSWORT,

    /**
     * 邮箱密码
     */
    EMAIL_PASSWORT,

    /**
     * 邮箱验证码
     */
    EMAIL_CODE,

    /**
     * 手机号密码
     */
    PHONE_PASSWORT,

    /**
     * 手机号验证码
     */
    PHONE_CODE,

    /**
     * 微信登录
     */
    WECHAT,

    /**
     * 微信小程序
     */
    WECHAT_SMALL_PROGRAM,

    /**
     * 微博登录
     */
    SINA,

    /**
     * QQ登录
     */
    QQ
}