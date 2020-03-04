package android.lorenwang.common_base_frame.pulgins.share;

import com.qtools.base.pulgins.QtPluginErrorTypeEnum;
import com.qtools.base.pulgins.QtPluginTargetTypeEnum;
import com.qtools.base.pulgins.QtPluginUtils;
import com.qtools.base.utils.QtLogUtils;

import static com.qtools.base.pulgins.share.QtShareContentTypeEnum.*;

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

public class QtShareUtils {
    private final String TAG = "QtShareUtils";
    private static QtShareUtils optionsInstance;
    /**
     * 微信分享实例
     */
    private QtWeChatShare weChatShare;

    private QtShareUtils() {
    }

    public static QtShareUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (QtShareUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new QtShareUtils();
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
    private void sendShareData(AcbflwShareDataBean shareDataBean, QtPluginTargetTypeEnum targetType, AcbflwShareContentTypeEnum contentType) {
        QtLogUtils.i(TAG, "准备发起分享数据");
        assert targetType != null;
        assert contentType != null;
        assert shareDataBean.getShareCallBack() != null;
        QtLogUtils.i(TAG, "分享目标：" + targetType.getDes() + " 分享内容类型：" + contentType.getDes());
        switch (targetType) {
            case SHARE_WE_CHAT_SESSION://好友
            case SHARE_WE_CHAT_FAVORITE://收藏
            case SHARE_WE_CHAT_MOMENTS://朋友圈
                if (QtPluginUtils.getInstance().getApi() == null) {
                    getWeChatShare().callBackError(shareDataBean, QtPluginErrorTypeEnum.WECHAT_NOT_INIT);
                } else if (QtPluginUtils.getInstance().getApi().isWXAppInstalled()) {
                    switch (contentType) {
                        case TEXT://文本
                            getWeChatShare().sendTextShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case IMAGE://图片
                            getWeChatShare().sendImageShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case MUSIC://音频
                            getWeChatShare().sendMusicShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case VIDEO://视频
                            getWeChatShare().sendVideoShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case WEB_PAGE://网页分享
                            getWeChatShare().sendWebPageShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case FILE://文件分享
                            getWeChatShare().sendFileShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case APP_EXTEND:
                            getWeChatShare().sendAppExtendShare(shareDataBean, QtPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case MINI_PROGRAM://小程序分享
                            getWeChatShare().sendMiniProgramShare(shareDataBean);
                            break;
                        default:
                            break;
                    }
                } else {
                    QtLogUtils.i(TAG, "分享失败：微信未安装");
                    getWeChatShare().callBackError(shareDataBean, QtPluginErrorTypeEnum.WECHAT_NOT_INSTALL);
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
    private QtWeChatShare getWeChatShare() {
        if (weChatShare == null) {
            synchronized (QtWeChatShare.class) {
                if (weChatShare == null) {
                    weChatShare = new QtWeChatShare();
                }
            }
        }
        return weChatShare;
    }
}
