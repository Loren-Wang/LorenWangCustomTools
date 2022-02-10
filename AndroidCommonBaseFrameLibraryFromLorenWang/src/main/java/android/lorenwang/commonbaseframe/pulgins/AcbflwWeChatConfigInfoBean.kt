package android.lorenwang.commonbaseframe.pulgins

import lombok.Builder

/**
 * 功能作用：微信配置信息
 * 创建时间：2019-12-31 14:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Builder
class AcbflwWeChatConfigInfoBean {
    /**
     * 微信id
     */
    var weChatId: String? = null

    /**
     * 微信Secret
     */
    var weiChatSecret: String? = null

    /**
     * 微信开放平台审核通过的应用APPID
     */
    var appid: String? = null

    /**
     * 小程序原始id
     */
    var weChatApplyId: String? = null
    var checkSignature = false
}