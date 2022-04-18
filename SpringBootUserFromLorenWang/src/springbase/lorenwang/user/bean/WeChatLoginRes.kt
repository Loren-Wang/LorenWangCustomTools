package springbase.lorenwang.user.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能作用：微信登录响应
 * 初始注释时间： 2022/4/16 11:24
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
internal class WeChatLoginRes {
    /**
     * 接口调用凭证
     */
    @SerializedName("access_token")
    var accessToken: String? = null

    /**
     * access_token 接口调用凭证超时时间，单位（秒）
     */
    @SerializedName("expires_in")
    var expiresIn: Long? = null

    /**
     * 用户刷新 access_token
     */
    @SerializedName("refresh_token")
    var refreshToken: String? = null

    /**
     * 授权用户唯一标识
     */
    @SerializedName("openid")
    var openid: String? = null

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    @SerializedName("scope")
    var scope: String? = null

    /**
     * 当且仅当该移动应用已获得该用户的 userinfo 授权时，才会出现该字段
     */
    @SerializedName("unionid")
    var unionid: String? = null

    /**
     * 错误码
     */
    var errcode: String? = null

    /**
     * 错误信息
     */
    var errmsg: String? = null
}