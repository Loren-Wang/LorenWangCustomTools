package springbase.lorenwang.user.enums

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck

/**
 * 功能作用：用户登录类型枚举
 * 创建时间：2022/4/16 10:16
 * 创建者：王亮（Loren）
 * 备注：
 */
enum class SpulwUserLoginTypeEnum(val type: String) {
    /**
     * 账户密码
     */
    ACCOUNT_PASSWORT("ACCOUNT_PASSWORT"),

    /**
     * 邮箱密码
     */
    EMAIL_PASSWORT("EMAIL_PASSWORT"),

    /**
     * 邮箱验证码
     */
    EMAIL_CODE("EMAIL_CODE"),

    /**
     * 手机号密码
     */
    PHONE_PASSWORT("PHONE_PASSWORT"),

    /**
     * 手机号验证码
     */
    PHONE_CODE("PHONE_CODE"),

    /**
     * 微信登录
     */
    WECHAT("WECHAT"),

    /**
     * 微信小程序
     */
    WECHAT_SMALL_PROGRAM("WECHAT_SMALL_PROGRAM"),

    /**
     * 微博登录
     */
    SINA("SINA"),

    /**
     * QQ登录
     */
    QQ("QQ");

    companion object {
        /**
         * 获取登录类型
         */
        fun getLoginType(type: String?): SpulwUserLoginTypeEnum? {
            return type.kttlwEmptyCheck({ null }, {
                for (value in values()) {
                    if (value.type == it) {
                        return value
                    }
                }
                null
            })
        }
    }
}