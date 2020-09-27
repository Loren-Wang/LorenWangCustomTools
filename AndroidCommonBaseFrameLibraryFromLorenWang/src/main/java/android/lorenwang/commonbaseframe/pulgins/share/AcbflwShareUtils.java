package android.lorenwang.commonbaseframe.pulgins.share;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.lorenwang.commonbaseframe.AcbflwBaseApplication;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtils;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwActivityUtils;
import android.lorenwang.tools.app.AtlwPermissionRequestCallback;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.net.Uri;

import java.io.File;
import java.util.List;

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
 * @author wangliang
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
    private void sendShareData(final AcbflwShareDataBean shareDataBean, AcbflwPluginTargetTypeEnum targetType, AcbflwShareContentTypeEnum contentType) {
        AtlwLogUtils.logUtils.logI(TAG, "准备发起分享数据");
        assert targetType != null;
        assert contentType != null;
        assert shareDataBean.getShareCallBack() != null;
        AtlwLogUtils.logUtils.logI(TAG, "分享目标：" + targetType.getDes() + " 分享内容类型：" + contentType.getDes());
        switch (targetType) {
            //好友
            case SHARE_WE_CHAT_SESSION:
                //收藏
            case SHARE_WE_CHAT_FAVORITE:
                //朋友圈
            case SHARE_WE_CHAT_MOMENTS:
                if (AcbflwPluginUtils.getInstance().getApi() == null) {
                    getWeChatShare().callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INIT);
                } else if (AcbflwPluginUtils.getInstance().getApi().isWXAppInstalled()) {
                    switch (contentType) {
                        //文本
                        case TEXT:
                            getWeChatShare().sendTextShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //图片
                        case IMAGE:
                            getWeChatShare().sendImageShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //音频
                        case MUSIC:
                            getWeChatShare().sendMusicShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //视频
                        case VIDEO:
                            getWeChatShare().sendVideoShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //网页分享
                        case WEB_PAGE:
                            getWeChatShare().sendWebPageShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //文件分享
                        case FILE:
                            getWeChatShare().sendFileShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        case APP_EXTEND:
                            getWeChatShare().sendAppExtendShare(shareDataBean, AcbflwPluginTargetTypeEnum.getShareTargetType(targetType));
                            break;
                        //小程序分享
                        case MINI_PROGRAM:
                            getWeChatShare().sendMiniProgramShare(shareDataBean);
                            break;
                        default:
                            break;
                    }
                } else {
                    AtlwLogUtils.logUtils.logI(TAG, "分享失败：微信未安装");
                    getWeChatShare().callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INSTALL);
                }
                break;
            case SHARE_SAVE_LOCAL_IMAGE:
                //分享图片，需要保存图片并通知数据库更新
                if (shareDataBean.getSaveLocalImageBitmap() == null) {
                    shareDataBean.getShareCallBack().error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
                } else {
                    String dirPath =
                            AtlwConfig.nowApplication.getExternalFilesDir(null).getAbsolutePath();
                    final File file = new File(
                            dirPath.substring(0, dirPath.toLowerCase().indexOf("/android"))
                                    + "/" + AtlwActivityUtils.getInstance().getAppName() + "/" +
                                    (shareDataBean.getSaveLocalImageName() == null ?
                                            System.currentTimeMillis() + ".png" :
                                            shareDataBean.getSaveLocalImageName()));
                    final int code = hashCode() % 10000;
                    AtlwActivityUtils.getInstance().goToRequestPermissions(AcbflwBaseApplication.currentShowActivity,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, code,
                            new AtlwPermissionRequestCallback() {

                                /**
                                 * 请求成功权限列表
                                 *
                                 * @param permissionList         权限列表
                                 * @param permissionsRequestCode 请求码
                                 */
                                @Override
                                public void permissionRequestSuccessCallback(List<String> permissionList, int permissionsRequestCode) {
                                    if (permissionsRequestCode == code) {
                                        //保存图片
                                        if (AtlwFileOptionUtils.getInstance().writeToFile(true, file,
                                                shareDataBean.getSaveLocalImageBitmap(),
                                                Bitmap.CompressFormat.PNG)) {
                                            //保存图片后发送广播通知更新数据库
                                            Uri uri = Uri.fromFile(file);
                                            AtlwConfig.nowApplication.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                            //回调成功
                                            shareDataBean.getShareCallBack().info();
                                        } else {
                                            shareDataBean.getShareCallBack().error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
                                        }
                                    }
                                }

                                /**
                                 * 请求失败权限列表
                                 *
                                 * @param permissionList         权限列表
                                 * @param permissionsRequestCode 请求码
                                 */
                                @Override
                                public void permissionRequestFailCallback(List<String> permissionList, int permissionsRequestCode) {
                                    shareDataBean.getShareCallBack().error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
                                }
                            });

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
