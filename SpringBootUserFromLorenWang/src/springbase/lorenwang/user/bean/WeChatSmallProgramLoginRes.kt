package springbase.lorenwang.user.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能作用：微信小程序登录响应
 * 初始注释时间： 2022/4/17 10:49
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
class WeChatSmallProgramLoginRes {
    /**
     * 用户唯一标识
     */
    var openid: String? = null

    /**
     * 会话密钥
     */
    @SerializedName("session_key")
    var sessionKey: String? = null

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     */
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