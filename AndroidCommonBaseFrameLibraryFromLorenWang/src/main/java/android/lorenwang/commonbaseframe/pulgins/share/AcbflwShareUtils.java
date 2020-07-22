package android.lorenwang.commonbaseframe.pulgins.share;


import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtils;
import android.lorenwang.tools.base.AtlwLogUtils;

/**
 * 功能作用：分享基础工具类
 * 创建时间：2019-12-24 18:38
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、设置微信 weChatId---setWeChatId（weChatId）
 * 2、设置微信CheckSignature---setWeChatCheckSignature（boolean）
 * 3、发送分享数据---sendShareData（bean，targetType，contentType）
 * 4、分享文本到微信---shareTextToWeChat（text，targetType，callback）
 * 5、分享图片到微信---shareImageToWeChat（bitmap，targetType，callback）
 * 6、分享网页到微信---shareWebPageToWeChat（url，bitmap，title，des，targetType，callback）
 * 7、分享到微信小程序---shareAppletOfWeChat（url，path，miniProgramTypel，bitmap，title，des，callback）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwShareUtils {
    private final String TAG = "AcbflwShareUtils";
    private static AcbflwShareUtils optionsInstance;
    /**
     * 微信分享实例
     */
    private AcbflwWeChatShare weChatShare;

    private AcbflwShareUtils() {
    }

    public static AcbflwShareUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwShareUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwShareUtils();
                }
            }
        }
        return optionsInstance;
    }


    /**
     * 发送分享数据
     *
     * @param shareDataBean 分享数据实体
     */
    public void sendShareData(AcbflwShareDataBean shareDataBean) {
        sendShareData(shareDataBean, shareDataBean.getTargetType(), shareDataBean.getContentType());
    }

    /**
     * 发送分享数据
     *
     * @param shareDataBean 分享数据实体
     * @param targetType    分享目标类型
     * @param contentType   分享内容类型
     */
    private void sendShareData(AcbflwShareDataBean shareDataBean, AcbflwPluginTargetTypeEnum targetType, AcbflwShareContentTypeEnum contentType) {
        AtlwLogUtils.logI(TAG, "准备发起分享数据");
        assert targetType != null;
        assert contentType != null;
        assert shareDataBean.getShareCallBack() != null;
        AtlwLogUtils.logI(TAG, "分享目标：" + targetType.getDes() + " 分享内容类型：" + contentType.getDes());
        switch (targetType) {
            case SHARE_WE_CHAT_SESSION://好友
            case SHARE_WE_CHAT_FAVORITE://收藏
            case SHARE_WE_CHAT_MOMENTS://朋友圈
                if (AcbflwPluginUtils.getInstance().getApi() == null) {
                    getWeChatShare().callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INIT);
                } else if (AcbflwPluginUtils.getInstance().getApi().isWXAppInstalled()) {
                    switch (contentType) {
                        case TEXT://文本
                            getWeChatShare().sendTextShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case IMAGE://图片
                            getWeChatShare().sendImageShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case MUSIC://音频
                            getWeChatShare().sendMusicShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case VIDEO://视频
                            getWeChatShare().sendVideoShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case WEB_PAGE://网页分享
                            getWeChatShare().sendWebPageShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case FILE://文件分享
                            getWeChatShare().sendFileShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case APP_EXTEND:
                            getWeChatShare().sendAppExtendShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case MINI_PROGRAM://小程序分享
                            getWeChatShare().sendMiniProgramShare(shareDataBean);
                            break;
                        default:
                            break;
                    }
                } else {
                    AtlwLogUtils.logI(TAG, "分享失败：微信未安装");
                    getWeChatShare().callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INSTALL);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取微信分享实例
     *
     * @return 微信分享实例
     */
    private AcbflwWeChatShare getWeChatShare() {
        if (weChatShare == null) {
            synchronized (AcbflwWeChatShare.class) {
                if (weChatShare == null) {
                    weChatShare = new AcbflwWeChatShare();
                }
            }
        }
        return weChatShare;
    }
}
