package android.lorenwang.commonbaseframe.pulgins.share;

import android.graphics.Bitmap;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.net.Uri;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

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
 */
class AcbflwSinaShare {
    /**
     * 分享文本
     */
    public void shareText(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaText())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TEXT_EMPTY);
            return;
        }
        WeiboMultiMessage message = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = shareDataBean.getSinaText();
        message.textObject = textObject;
        AcbflwPluginUtil.getInstance().getSinaApi(shareDataBean.getActivity()).shareMessage(message, false);
        AcbflwPluginUtil.getInstance().addCallBack(AcbflwPluginUtil.getInstance().sinaKey(shareDataBean.getActivity()),
                shareDataBean.getShareCallBack());
    }

    /**
     * 分享图片位图
     *
     * @param shareDataBean 分享信息
     */
    public void shareImageBitmap(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }

        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaImageViewBitmap())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
        } else {
            //压缩图片
            try {
                WeiboMultiMessage message = new WeiboMultiMessage();
                ImageObject imageObject = new ImageObject();
                imageObject.setImageData(shareDataBean.getSinaImageViewBitmap());
                message.imageObject = imageObject;
                AcbflwPluginUtil.getInstance().getSinaApi(shareDataBean.getActivity()).shareMessage(message, false);
                AcbflwPluginUtil.getInstance().addCallBack(AcbflwPluginUtil.getInstance().sinaKey(shareDataBean.getActivity()),
                        shareDataBean.getShareCallBack());
            } catch (Exception e) {
                //回调异常
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR);
            }
        }
    }

    /**
     * 分享网页
     *
     * @param shareDataBean 分享信息
     */
    public void shareWeb(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaTitle())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_TITLE_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaDes())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_DESCRIPTION_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaImageViewBitmap())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaWebUrl())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG);
            return;
        }
        WeiboMultiMessage message = new WeiboMultiMessage();
        WebpageObject webObject = new WebpageObject();
        webObject.identify = UUID.randomUUID().toString();
        webObject.title = shareDataBean.getSinaTitle();
        webObject.description = shareDataBean.getSinaDes();
        try {
            webObject.thumbData = AtlwImageCommonUtil.getInstance().bitmapCompressToByte(shareDataBean.getSinaImageViewBitmap(),
                    Bitmap.CompressFormat.JPEG, 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webObject.actionUrl = shareDataBean.getSinaWebUrl();
        webObject.defaultText = JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaText()) ? "" : shareDataBean.getSinaText();
        message.mediaObject = webObject;
        AcbflwPluginUtil.getInstance().getSinaApi(shareDataBean.getActivity()).shareMessage(message, false);
        AcbflwPluginUtil.getInstance().addCallBack(AcbflwPluginUtil.getInstance().sinaKey(shareDataBean.getActivity()),
                shareDataBean.getShareCallBack());
    }

    /**
     * 分享图片列表
     *
     * @param shareDataBean 分享信息
     */
    public void shareImageList(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }
        if (shareDataBean.getSinaImagePathList() == null || shareDataBean.getSinaImagePathList().isEmpty()) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
            return;
        }
        WeiboMultiMessage message = new WeiboMultiMessage();
        MultiImageObject multiImageObject = new MultiImageObject();
        ArrayList<Uri> list = new ArrayList<>();
        for (String item : shareDataBean.getSinaImagePathList()) {
            list.add(Uri.fromFile(new File(item)));
        }
        multiImageObject.imageList = list;
        message.multiImageObject = multiImageObject;
        AcbflwPluginUtil.getInstance().getSinaApi(shareDataBean.getActivity()).shareMessage(message, false);
        AcbflwPluginUtil.getInstance().addCallBack(AcbflwPluginUtil.getInstance().sinaKey(shareDataBean.getActivity()),
                shareDataBean.getShareCallBack());
    }

    /**
     * 分享视频
     *
     * @param shareDataBean 分享信息
     */
    public void shareVideo(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getSinaVideoPath())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_VIDEO_URL_EMPTY_OR_IS_TOO_LONG);
            return;
        }
        WeiboMultiMessage message = new WeiboMultiMessage();
        VideoSourceObject videoObject = new VideoSourceObject();
        videoObject.videoPath = Uri.fromFile(new File(shareDataBean.getSinaVideoPath()));
        message.videoSourceObject = videoObject;
        AcbflwPluginUtil.getInstance().getSinaApi(shareDataBean.getActivity()).shareMessage(message, false);
        AcbflwPluginUtil.getInstance().addCallBack(AcbflwPluginUtil.getInstance().sinaKey(shareDataBean.getActivity()),
                shareDataBean.getShareCallBack());
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
