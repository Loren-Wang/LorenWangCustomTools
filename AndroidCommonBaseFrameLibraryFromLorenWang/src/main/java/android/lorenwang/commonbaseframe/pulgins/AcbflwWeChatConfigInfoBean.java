package android.lorenwang.commonbaseframe.pulgins;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
@Getter
public class AcbflwWeChatConfigInfoBean {
    /**
     * 微信id
     */
    private final String weChatId;
    /**
     * 微信Secret
     */
    private final String weiChatSecret;
    /**
     * 微信开放平台审核通过的应用APPID
     */
    private final String appid;
    /**
     * 小程序原始id
     */
    private final String weChatApplyId;
    private final boolean checkSignature;
}
