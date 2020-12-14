package com.test.springboot.enums

/**
 * 功能作用：用户登录来源枚举
 * 创建时间：2019-10-17 下午 16:10:22
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
enum class UserLoginFromEnum constructor(var from: String) {
    /**
     * 来源是网页
     */
    WEB("WEB"),
    /**
     * 来源是苹果设备
     */
    IOS("IOS"),
    /**
     * 安卓设备
     */
    ANDROID("ANDROID"),
    /**
     * 微信小程序
     */
    APPLET_OF_WECHAT("APPLET_OF_WECHAT");

    companion object {
        val USER_LOGIN_FROM_REGEX = "(${WEB}|${IOS}|${ANDROID}${APPLET_OF_WECHAT})"
        /**
         * 根据字符串获取来源
         */
        fun getFrom(from: String): UserLoginFromEnum? {
            when (from) {
                WEB.from -> {
                    return WEB
                }
                IOS.from -> {
                    return IOS
                }
                ANDROID.from -> {
                    return ANDROID
                }
                APPLET_OF_WECHAT.from -> {
                    return APPLET_OF_WECHAT
                }
                else -> {
                    return null
                }
            }
        }
    }

}
