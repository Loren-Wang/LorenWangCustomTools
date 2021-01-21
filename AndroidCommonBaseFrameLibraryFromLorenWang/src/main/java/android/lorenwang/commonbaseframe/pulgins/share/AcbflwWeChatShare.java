package android.lorenwang.commonbaseframe.pulgins.share;

import android.graphics.Bitmap;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.File;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：微信分享
 * 创建时间：2019-12-25 18:36
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 3、发送文本分享---sendTextShare（bean，targetType）
 * 4、发送图片分享---sendImageShare（bean，targetType）
 * 5、发送音乐分享---sendMusicShare（bean，targetType）
 * 6、发送视频分享---sendVideoShare（bean，targetType）
 * 7、发送网页分享---sendWebPageShare（bean，targetType）
 * 8、发送文件分享---sendFileShare（bean，targetType）
 * 9、发送小程序分享---sendMiniProgramShare（bean）
 */

class AcbflwWeChatShare {

    private static final String TAG = "AcbflwWeChatShare";
    /**
     * 消息标题	限制长度不超过 512Bytes
     */
    private final int MAX_SIZE_TITLE = 512;
    /**
     * 消息描述	限制长度不超过 1KB
     */
    private final int MAX_SIZE_DESCRIPTION = 1024;
    /**
     * 文本数据	长度需大于 0 且不超过 10KB
     */
    private final int MAX_SIZE_TEXT = 10240;
    /**
     * 分享音频网址	长度需大于 0 且不超过 10KB
     */
    private final int MAX_SIZE_MUSIC_URL = 10240;
    /**
     * 缩略图的二进制数据	限制内容大小不超过 32kb
     */
    private final int MAX_SIZE_THUMB_DATA = 32768;
    /**
     * 分享视频网址	长度需大于 0 且不超过 10KB
     */
    private final int MAX_SIZE_VIDEO_URL = 10240;
    /**
     * 分享网页网址	长度需大于 0 且不超过 10KB
     */
    private final int MAX_SIZE_WEB_PAGE_URL = 10240;
    /**
     * 分享文件数据	长度需大于 0 且不超过 10M
     */
    private final Integer MAX_SIZE_FILE = 10485760;
    /**
     * 分享图片数据 内容大小不超过 10MB
     */
    private final Integer MAX_SIZE_IMAGE = 10485760;

    /**
     * 发送app扩展分享，暂时没有找到文档
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendAppExtendShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
    }

    /**
     * 发送文本分享
     *
     * @param shareDataBean   分享数据实体
     * @param shareTargetType 分享目标
     */
    public void sendTextShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        //判断分享文本是否为空，不为空继续处理
        if (!JtlwCheckVariateUtils.getInstance().isHaveEmpty(shareDataBean.getWxText())) {
            //检查描述内容
            if (!checkStrSize(shareDataBean.getWxText(), MAX_SIZE_TEXT)) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TEXT_IS_TOO_LONG);
                return;
            }
            //初始化一个 WXTextObject 对象，填写分享的文本内容
            WXTextObject textObj = new WXTextObject();
            textObj.text = shareDataBean.getWxText();
            sendMessage(shareDataBean, textObj, shareTargetType);
        }
    }

    /**
     * 发送图片分享
     *
     * @param shareDataBean   分享数据实体
     * @param shareTargetType 分享目标
     */
    public void sendImageShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxImageViewBitmap())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
        } else {
            //压缩图片
            try {
                sendMessage(shareDataBean, new WXImageObject(AtlwImageCommonUtil.getInstance()
                        .bitmapCompressToByte(shareDataBean.getWxImageViewBitmap(),
                                Bitmap.CompressFormat.JPEG, MAX_SIZE_IMAGE)), shareTargetType);
            } catch (Exception e) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
            }
        }
    }

    /**
     * 发送音频分享
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendMusicShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (checkStrSize(shareDataBean.getWxMusicUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicUrl = shareDataBean.getWxMusicUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getWxMusicLowBandUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandUrl = shareDataBean.getWxMusicLowBandUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getWxMusicLowBandUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandUrl = shareDataBean.getWxMusicLowBandUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getWxMusicLowBandDataUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandDataUrl = shareDataBean.getWxMusicLowBandDataUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_MUSIC_URL_EMPTY_OR_IS_TOO_LONG);
        }
    }

    /**
     * 发送视频分享
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendVideoShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (checkStrSize(shareDataBean.getWxVideoUrl(), MAX_SIZE_VIDEO_URL)) {
            //初始化一个WXVideoObject，填写url
            WXVideoObject video = new WXVideoObject();
            video.videoUrl = shareDataBean.getWxVideoUrl();
            //发送分享
            sendMessage(shareDataBean, video, shareTargetType);
        } else if (checkStrSize(shareDataBean.getWxVideoLowBandUrl(), MAX_SIZE_VIDEO_URL)) {
            //初始化一个WXVideoObject，填写url
            WXVideoObject video = new WXVideoObject();
            video.videoLowBandUrl = shareDataBean.getWxVideoLowBandUrl();
            //发送分享
            sendMessage(shareDataBean, video, shareTargetType);
        } else {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_VIDEO_URL_EMPTY_OR_IS_TOO_LONG);
        }
    }

    /**
     * 发送网页分享
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendWebPageShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (checkStrSize(shareDataBean.getWxWebPageUrl(), MAX_SIZE_WEB_PAGE_URL)) {
            //初始化一个WXWebpageObject，填写url
            WXWebpageObject webPage = new WXWebpageObject();
            webPage.webpageUrl = shareDataBean.getWxWebPageUrl();
            //发送分享
            sendMessage(shareDataBean, webPage, shareTargetType);
        } else {
            //异常回调
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG);
        }
    }

    /**
     * 发送文件分享
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendFileShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxFileData())) {
            if (MAX_SIZE_FILE.compareTo(shareDataBean.getWxFileData().length) > 0) {
                //发送分享
                sendMessage(shareDataBean, new WXFileObject(shareDataBean.getWxFileData()), shareTargetType);
            } else {
                //异常回调
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FILE_IS_TOO_LONG);
            }
        } else if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxFilePath())) {
            File file = new File(shareDataBean.getWxFilePath());
            if (Long.valueOf(MAX_SIZE_FILE).compareTo(file.length()) > 0) {
                //发送分享
                sendMessage(shareDataBean, new WXFileObject(shareDataBean.getWxFilePath()), shareTargetType);
            } else {
                //异常回调
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FILE_IS_TOO_LONG);
            }
        } else {
            //异常回调
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FILE_EMPTY);
        }

    }

    /**
     * 发送小程序分享
     *
     * @param shareDataBean 分享实体
     */
    public void sendMiniProgramShare(AcbflwShareDataBean shareDataBean) {
        //判断分享链接
        if (!checkStrSize(shareDataBean.getWxMiniProgramWebpageUrl(), MAX_SIZE_WEB_PAGE_URL)) {
            callBackError(shareDataBean,
                    AcbflwPluginErrorTypeEnum.SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG);
            return;
        }
        //判断缩略图
        if (shareDataBean.getWxMiniProgramThumbBitmap() == null) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
            return;
        }
        //小程序id检测
        if (JtlwCheckVariateUtils.getInstance().isEmpty(AcbflwPluginUtil.getInstance().getWeChatId())) {
            callBackError(shareDataBean,
                    AcbflwPluginErrorTypeEnum.SHARE_APPLET_OF_WECHAT_USER_NAME_EMPTY);
            return;
        }
        //小程序页面路径检测
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxMiniProgramPath())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_APPLET_OF_WECHAT_PATH_EMPTY);
            return;
        }
        //直接切换到会话，因为小程序分享只支持会话
        shareDataBean.targetType = AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_SESSION;
        //构造体
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        // 兼容低版本的网页链接
        miniProgramObj.webpageUrl = shareDataBean.getWxMiniProgramWebpageUrl();
        // 正式版:0，测试版:1，体验版:2
        miniProgramObj.miniprogramType = shareDataBean.getWxMiniProgramType();
        // 小程序原始id
        miniProgramObj.userName = AcbflwPluginUtil.getInstance().getWeChatApplyId();
        //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "
        miniProgramObj.path = shareDataBean.getWxMiniProgramPath();
        //发送分享
        sendMessage(shareDataBean, miniProgramObj,
                AcbflwPluginTargetTypeEnum.getShareTargetType(AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_SESSION));
    }

    /**
     * 分享消息
     *
     * @param shareDataBean 分享实体
     * @param mediaObject   分享构造obj
     * @param mTargetScene  分享目标
     */
    private void sendMessage(AcbflwShareDataBean shareDataBean, WXMediaMessage.IMediaObject mediaObject, int mTargetScene) {
        AtlwLogUtil.logUtils.logI(TAG, "微信分享已构造完分享请求类型实体");
        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = mediaObject;
        //检查描述
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxBaseDescription())) {
            if (!checkStrSize(shareDataBean.getWxBaseDescription(), MAX_SIZE_DESCRIPTION)) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_DESCRIPTION_IS_TOO_LONG);
            } else {
                msg.description = shareDataBean.getWxBaseDescription();
            }
        }
        //检查微信小程序标题
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxMiniProgramTitle())) {
            if (!checkStrSize(shareDataBean.getWxMiniProgramTitle(), MAX_SIZE_TITLE)) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TITLE_IS_TOO_LONG);
            } else {
                msg.title = shareDataBean.getWxMiniProgramTitle();
            }
        }
        //检查微信小程序缩略图
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getWxMiniProgramThumbBitmap())) {
            //压缩图片
            try {
                msg.thumbData = AtlwImageCommonUtil.getInstance()
                        .bitmapCompressToByte(shareDataBean.getWxMiniProgramThumbBitmap(),
                                Bitmap.CompressFormat.JPEG, MAX_SIZE_IMAGE);
            } catch (Exception e) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
            }
        }
        AtlwLogUtil.logUtils.logI(TAG, "微信分享msg已构造完成");
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //classname，hashcode，时间戳
        //transaction字段用与唯一标示一个请求
        req.transaction = "wx_share_" + msg.getClass().getName() + msg.hashCode() + System.currentTimeMillis();
        req.message = msg;
        req.scene = mTargetScene;
        AtlwLogUtil.logUtils.logI(TAG, "微信分享准备向微信发送分享");
        AcbflwPluginUtil.getInstance().getApi().sendReq(req);
        //记录回调
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getShareCallBack())) {
            AcbflwPluginUtil.getInstance().addCallBack(req.transaction, shareDataBean.getShareCallBack());
        }
    }

    /**
     * 检测字符串大小
     *
     * @param str  字符串
     * @param size 最大大小
     * @return 是否允许, false为不允许
     */
    private boolean checkStrSize(String str, int size) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(str)) {
            return false;
        }
        return size >= str.getBytes().length;
    }

    /**
     * 回调异常信息
     *
     * @param bean      分享实体
     * @param errorType 异常类型
     */
    public void callBackError(AcbflwShareDataBean bean, AcbflwPluginErrorTypeEnum errorType) {
        if (bean != null && bean.getShareCallBack() != null && errorType != null) {
            bean.getShareCallBack().error(errorType);
        }
    }
}
