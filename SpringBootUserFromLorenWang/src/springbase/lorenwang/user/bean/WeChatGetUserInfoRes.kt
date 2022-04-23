package springbase.lorenwang.user.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能作用：微信获取用户信息响应
 * 初始注释时间： 2022/4/16 13:59
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
class WeChatGetUserInfoRes {
    /**
     * 普通用户个人资料填写的城市
     */
    @SerializedName("city")
    var city: String? = null

    /**
     * 国家，如中国为 CN
     */
    @SerializedName("country")
    var country: String? = null

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
     */
    @SerializedName("headimgurl", alternate = ["avatarUrl"])
    var headimgurl: String? = null

    /**
     * 普通用户昵称
     */
    @SerializedName("nickname", alternate = ["nickName"])
    var nickname: String? = null

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    @SerializedName("openid")
    var openid: String? = null

    /**
     * 普通用户个人资料填写的省份
     */
    @SerializedName("province")
    var province: String? = null

    /**
     * 普通用户性别，1 为男性，2 为女性
     */
    @SerializedName("sex", alternate = ["gender"])
    var sex: Int? = null

    /**
     * 当且仅当该移动应用已获得该用户的 userinfo 授权时，才会出现该字段
     */
    @SerializedName("unionid")
    var unionid: String? = null
}