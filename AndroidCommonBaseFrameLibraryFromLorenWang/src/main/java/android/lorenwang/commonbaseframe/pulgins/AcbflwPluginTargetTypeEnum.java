package android.lorenwang.commonbaseframe.pulgins;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * 功能作用：插件目标类型枚举
 * 创建时间：2019-12-25 18:00
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * SHARE_WE_CHAT_MOMENTS(1, "WeChat Moments"),
 * SHARE_WE_CHAT_FAVORITE(2, "WeChat Favorite"),
 * SHARE_WE_CHAT_SESSION(3, "WeChat Session");
 */

public enum AcbflwPluginTargetTypeEnum {
    /**
     * 微信朋友圈
     */
    SHARE_WE_CHAT_MOMENTS(1, "WeChat Moments"),
    /**
     * 微信收藏
     */
    SHARE_WE_CHAT_FAVORITE(2, "WeChat Favorite"),
    /**
     * 微信好友会话
     */
    SHARE_WE_CHAT_SESSION(3, "WeChat Session"),
    /**
     * 分享图片，需要进行保存并通知操作
     */
    SHARE_SAVE_LOCAL_IMAGE(4, "Share Image"),
    /**
     * 新浪微博分享
     */
    SHARE_SINA(5,"Sina Text"),
    /**
     * 微信支付
     */
    PAY_WE_CHAT(5, "WeChat Pay"),
    /**
     * 支付宝支付
     */
    PAY_ALI(6, "ALiPay");

    private final int type;
    private final String des;

    AcbflwPluginTargetTypeEnum(int type, String des) {
        this.type = type;
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    /**
     * 获取分享目标类型
     *
     * @param type 分享目标类型
     * @return 目标类型
     */
    public static int getShareTargetType(AcbflwPluginTargetTypeEnum type) {
        switch (type) {
            case SHARE_WE_CHAT_MOMENTS:
                return SendMessageToWX.Req.WXSceneTimeline;
            case SHARE_WE_CHAT_FAVORITE:
                return SendMessageToWX.Req.WXSceneFavorite;
            case SHARE_WE_CHAT_SESSION:
                return SendMessageToWX.Req.WXSceneSession;
            default:
                return 0;
        }
    }
}
