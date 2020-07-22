package android.lorenwang.commonbaseframe.pulgins.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;

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
        if (!JtlwCheckVariateUtils.getInstance().isHaveEmpty(shareDataBean.getText())) {
            //检查描述内容
            if (!checkStrSize(shareDataBean.getText(), MAX_SIZE_TEXT)) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TEXT_IS_TOO_LONG);
                return;
            }
            //描述为空则使用文本内容
            if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getDescription())) {
                shareDataBean.setDescription(shareDataBean.getText());
            }
            //初始化一个 WXTextObject 对象，填写分享的文本内容
            WXTextObject textObj = new WXTextObject();
            textObj.text = shareDataBean.getText();
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
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgBitmap())) {
            //分享图片，开始压缩图片
            shareDataBean.setImgBitmap(AtlwImageCommonUtils.getInstance().bitmapCompress(shareDataBean.getImgBitmap(), Bitmap.CompressFormat.JPEG, MAX_SIZE_IMAGE));
            sendMessage(shareDataBean, new WXImageObject(shareDataBean.getImgBitmap()), shareTargetType);
        } else if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgPath())) {
            byte[] bytes = AtlwFileOptionUtils.getInstance().readBytes(true, shareDataBean.getImgPath());
            if (bytes != null) {
                sendMessage(shareDataBean, new WXImageObject(AtlwImageCommonUtils.getInstance().bitmapCompress(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),
                        Bitmap.CompressFormat.JPEG, MAX_SIZE_IMAGE)), shareTargetType);
            } else {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
            }
        } else {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
        }


        //判断分享文本是否为空，不为空继续处理
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgBitmap())
                || !JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgPath())) {
            WXImageObject imgObj = null;
            //根据数据初始化图片实例
            if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgBitmap())) {
                imgObj = new WXImageObject(shareDataBean.getImgBitmap());
            } else if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getImgPath())) {
                imgObj = new WXImageObject();
                imgObj.setImagePath(shareDataBean.getImgPath());
            }
            sendMessage(shareDataBean, imgObj, shareTargetType);
        }
    }

    /**
     * 发送音频分享
     *
     * @param shareDataBean   分享实体
     * @param shareTargetType 分享目标
     */
    public void sendMusicShare(AcbflwShareDataBean shareDataBean, int shareTargetType) {
        if (checkStrSize(shareDataBean.getMusicUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicUrl = shareDataBean.getMusicUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getMusicLowBandUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandUrl = shareDataBean.getMusicLowBandUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getMusicLowBandUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandUrl = shareDataBean.getMusicLowBandUrl();
            //发送分享
            sendMessage(shareDataBean, music, shareTargetType);
        } else if (checkStrSize(shareDataBean.getMusicLowBandDataUrl(), MAX_SIZE_MUSIC_URL)) {
            //初始化一个WXMusicObject，填写url
            WXMusicObject music = new WXMusicObject();
            music.musicLowBandDataUrl = shareDataBean.getMusicLowBandDataUrl();
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
        if (checkStrSize(shareDataBean.getVideoUrl(), MAX_SIZE_VIDEO_URL)) {
            //初始化一个WXVideoObject，填写url
            WXVideoObject video = new WXVideoObject();
            video.videoUrl = shareDataBean.getVideoUrl();
            //发送分享
            sendMessage(shareDataBean, video, shareTargetType);
        } else if (checkStrSize(shareDataBean.getVideoLowBandUrl(), MAX_SIZE_VIDEO_URL)) {
            //初始化一个WXVideoObject，填写url
            WXVideoObject video = new WXVideoObject();
            video.videoLowBandUrl = shareDataBean.getVideoLowBandUrl();
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
        if (checkStrSize(shareDataBean.getWebPageUrl(), MAX_SIZE_WEB_PAGE_URL)) {
            //初始化一个WXWebpageObject，填写url
            WXWebpageObject webPage = new WXWebpageObject();
            webPage.webpageUrl = shareDataBean.getWebPageUrl();
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
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getFileData())) {
            if (MAX_SIZE_FILE.compareTo(shareDataBean.getFileData().length) > 0) {
                //发送分享
                sendMessage(shareDataBean, new WXFileObject(shareDataBean.getFileData()), shareTargetType);
            } else {
                //异常回调
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FILE_IS_TOO_LONG);
            }
        } else if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getFilePath())) {
            File file = new File(shareDataBean.getFilePath());
            if (Long.valueOf(MAX_SIZE_FILE).compareTo(file.length()) > 0) {
                //发送分享
                sendMessage(shareDataBean, new WXFileObject(shareDataBean.getFilePath()), shareTargetType);
            } else {
                //异常回调
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FILE_IS_TOO_LONG);
            }
            file = null;
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
        if (!checkStrSize(shareDataBean.getWebPageUrl(), MAX_SIZE_WEB_PAGE_URL)) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG);
            return;
        }
        //小程序id检测
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getUserName())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_APPLET_OF_WECHAT_USER_NAME_EMPTY);
            return;
        }
        //小程序页面路径检测
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getPath())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_APPLET_OF_WECHAT_PATH_EMPTY);
            return;
        }
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = shareDataBean.getWebPageUrl(); // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = shareDataBean.getMiniProgramType();// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = shareDataBean.getUserName();     // 小程序原始id
        miniProgramObj.path = shareDataBean.getPath();            //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "
        //发送分享
        sendMessage(shareDataBean, miniProgramObj, AcbflwPluginTargetTypeEnum.getShareTargetType(AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_SESSION));
    }

    /**
     * 分享消息
     *
     * @param shareDataBean 分享实体
     * @param mediaObject   分享构造obj
     * @param mTargetScene  分享目标
     */
    private void sendMessage(AcbflwShareDataBean shareDataBean, WXMediaMessage.IMediaObject mediaObject, int mTargetScene) {
        AtlwLogUtils.logI(TAG, "微信分享已构造完分享请求类型实体");
        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = mediaObject;
        //检查描述标题
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getText())
                && !checkStrSize(shareDataBean.getText(), MAX_SIZE_TITLE)) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TITLE_IS_TOO_LONG);
            return;
        } else {
            msg.title = shareDataBean.getTitle();
        }
        //检查描述内容
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getDescription())
                && !checkStrSize(shareDataBean.getText(), MAX_SIZE_DESCRIPTION)) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_DESCRIPTION_IS_TOO_LONG);
            return;
        } else {
            msg.description = shareDataBean.getDescription();
        }
        //设置缩略图
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getThumbBmp())) {
            msg.thumbData = AtlwImageCommonUtils.getInstance().getBitmapBytes(
                    AtlwImageCommonUtils.getInstance().bitmapCompress(shareDataBean.getThumbBmp(), Bitmap.CompressFormat.JPEG, MAX_SIZE_THUMB_DATA)
            );
        }
        AtlwLogUtils.logI(TAG, "微信分享msg已构造完成");
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //classname，hashcode，时间戳
        req.transaction = "wx_share_" + msg.getClass().getName() + msg.hashCode() + System.currentTimeMillis();  //transaction字段用与唯一标示一个请求
        req.message = msg;
        req.scene = mTargetScene;
        AtlwLogUtils.logI(TAG, "微信分享准备向微信发送分享");
        AcbflwPluginUtils.getInstance().getApi().sendReq(req);
        //记录回调
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getShareCallBack())) {
            AcbflwPluginUtils.getInstance().addCallBack(req.transaction, shareDataBean.getShareCallBack());
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
        if (Integer.valueOf(size).compareTo(str.getBytes().length) < 0) {
            return false;
        }
        return true;
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
