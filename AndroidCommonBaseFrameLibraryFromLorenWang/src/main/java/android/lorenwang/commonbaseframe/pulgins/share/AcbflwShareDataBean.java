package android.lorenwang.commonbaseframe.pulgins.share;

import android.graphics.Bitmap;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginCallBack;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum;

import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

/**
 * 功能作用：分享数据实体
 * 创建时间：2019-12-25 18:15
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1：分享回调---shareCallBack
 * 配置参数2：分享标题---title
 * 配置参数3：分享描述---description
 * 配置参数4：分享文本---text
 * 配置参数5：分享图片位图---imgBitmap
 * 配置参数6：分享图片缩略图位图---thumbBmp
 * 配置参数7：分享图片地址---imgPath
 * 配置参数8：音频网页的 URL 地址---musicUrl---（限制长度不超过 10KB）
 * 配置参数9：供低带宽环境下使用的音频网页 URL 地址---musicLowBandUrl---（限制长度不超过 10KB）
 * 配置参数10：音频数据的 URL 地址---musicDataUrl---（限制长度不超过 10KB）
 * 配置参数11：供低带宽环境下使用的音频数据 URL 地址---musicLowBandDataUrl---（限制长度不超过 10KB）
 * 配置参数12：视频链接---videoUrl---（限制长度不超过 10KB）
 * 配置参数13：供低带宽的环境下使用的视频链接---videoLowBandUrl---（限制长度不超过 10KB）
 * 配置参数14：文件地址---filePath
 * 配置参数15：文件数据---fileData
 * 配置参数16：html 链接---webPageUrl---（限制长度不超过 10KB）
 * 配置参数17：小程序的原始 id---userName---（小程序原始 ID 获取方法：登录小程序管理后台-设置-基本设置-帐号信息）
 * 配置参数18：小程序的 path---path---（小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"）
 * 配置参数19：是否使用带 shareTicket 的分享---withShareTicket---（通常开发者希望分享出去的小程序被二次打开时可以获取到更多信息，例如群的标识。可以设置 withShareTicket 为 true，当分享卡片在群聊中被其他用户打开时，可以获取到 shareTicket，用于获取更多分享信息。详见小程序获取更多分享信息 ，最低客户端版本要求：6.5.13）
 * 配置参数20：小程序的类型，默认正式版---miniProgramType---（默认正式版 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE; 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW）
 */

public class AcbflwShareDataBean {
    /**
     * 分享回调
     */
    private AcbflwPluginCallBack shareCallBack;
    /**
     * 分享目标
     */
    AcbflwPluginTargetTypeEnum targetType;
    /**
     * 分享内容类型
     */
    AcbflwShareContentTypeEnum contentType;

    /**
     * 微信分享描述
     */
    private String wxBaseDescription;
    /**
     * 微信文本分享文本
     */
    private String wxText;
    /**
     * 微信分享的图片
     */
    private Bitmap wxImageViewBitmap;
    /**
     * 微信小程序分享地址
     */
    private String wxMiniProgramWebpageUrl;
    /**
     * 微信小程序分享小程序路径
     */
    private String wxMiniProgramPath;
    /**
     * 微信小程序分享的小程序消息标题
     */
    private String wxMiniProgramTitle;
    /**
     * 微信小程序缩略图
     */
    private Bitmap wxMiniProgramThumbBitmap;
    /**
     * 小程序的类型，默认正式版
     * 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
     * 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
     * 预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
     */
    private int wxMiniProgramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;

    /**
     * 保存到本地的图片位图
     */
    private Bitmap saveLocalImageBitmap;
    /**
     * 保存到本地的图片名称
     */
    private String saveLocalImageName;

    /**
     * 音频网页的 URL 地址	限制长度不超过 10KB
     */
    private String wxMusicUrl;
    /**
     * 供低带宽环境下使用的音频网页 URL 地址	限制长度不超过 10KB
     */
    private String wxMusicLowBandUrl;
    /**
     * 音频数据的 URL 地址	限制长度不超过 10KB
     */
    private String wxMusicDataUrl;
    /**
     * 供低带宽环境下使用的音频数据 URL 地址	限制长度不超过 10KB
     */
    private String wxMusicLowBandDataUrl;

    /**
     * 视频链接	限制长度不超过 10KB
     */
    private String wxVideoUrl;
    /**
     * 供低带宽的环境下使用的视频链接	限制长度不超过 10KB
     */
    private String wxVideoLowBandUrl;

    /**
     * html 链接	限制长度不超过 10KB
     */
    private String wxWebPageUrl;

    /**
     * 文件地址
     */
    private String wxFilePath;
    /**
     * 文件数据
     */
    private byte[] wxFileData;

    public AcbflwPluginCallBack getShareCallBack() {
        return shareCallBack;
    }

    public AcbflwPluginTargetTypeEnum getTargetType() {
        return targetType;
    }

    public AcbflwShareContentTypeEnum getContentType() {
        return contentType;
    }

    public String getWxBaseDescription() {
        return wxBaseDescription;
    }

    public String getWxText() {
        return wxText;
    }

    public Bitmap getWxImageViewBitmap() {
        return wxImageViewBitmap;
    }

    public String getWxMiniProgramWebpageUrl() {
        return wxMiniProgramWebpageUrl;
    }

    public String getWxMiniProgramPath() {
        return wxMiniProgramPath;
    }

    public String getWxMiniProgramTitle() {
        return wxMiniProgramTitle;
    }

    public Bitmap getWxMiniProgramThumbBitmap() {
        return wxMiniProgramThumbBitmap;
    }

    public int getWxMiniProgramType() {
        return wxMiniProgramType;
    }

    public Bitmap getSaveLocalImageBitmap() {
        return saveLocalImageBitmap;
    }

    public String getSaveLocalImageName() {
        return saveLocalImageName;
    }

    public String getWxMusicUrl() {
        return wxMusicUrl;
    }

    public String getWxMusicLowBandUrl() {
        return wxMusicLowBandUrl;
    }

    public String getWxMusicDataUrl() {
        return wxMusicDataUrl;
    }

    public String getWxMusicLowBandDataUrl() {
        return wxMusicLowBandDataUrl;
    }

    public String getWxVideoUrl() {
        return wxVideoUrl;
    }

    public String getWxVideoLowBandUrl() {
        return wxVideoLowBandUrl;
    }

    public String getWxFilePath() {
        return wxFilePath;
    }

    public byte[] getWxFileData() {
        return wxFileData;
    }

    public String getWxWebPageUrl() {
        return wxWebPageUrl;
    }

    public static class Build {
        /**
         * 分享回调
         */
        private AcbflwPluginCallBack shareCallBack;
        /**
         * 分享目标
         */
        AcbflwPluginTargetTypeEnum targetType;
        /**
         * 分享内容类型
         */
        AcbflwShareContentTypeEnum contentType;

        /**
         * 微信分享描述
         */
        private String wxBaseDescription;
        /**
         * 微信文本分享文本
         */
        private String wxText;
        /**
         * 微信分享的图片
         */
        private Bitmap wxImageViewBitmap;
        /**
         * 微信小程序分享地址
         */
        private String wxMiniProgramWebpageUrl;
        /**
         * 微信小程序分享小程序路径
         */
        private String wxMiniProgramPath;
        /**
         * 微信小程序分享的小程序消息标题
         */
        private String wxMiniProgramTitle;
        /**
         * 微信小程序缩略图
         */
        private Bitmap wxMiniProgramThumbBitmap;
        /**
         * 小程序的类型，默认正式版
         * 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
         * 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
         * 预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
         */
        private int wxMiniProgramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;

        /**
         * 保存到本地的图片位图
         */
        private Bitmap saveLocalImageBitmap;
        /**
         * 保存到本地的图片名称
         */
        private String saveLocalImageName;

        /**
         * 音频网页的 URL 地址	限制长度不超过 10KB
         */
        private String wxMusicUrl;
        /**
         * 供低带宽环境下使用的音频网页 URL 地址	限制长度不超过 10KB
         */
        private String wxMusicLowBandUrl;
        /**
         * 音频数据的 URL 地址	限制长度不超过 10KB
         */
        private String wxMusicDataUrl;
        /**
         * 供低带宽环境下使用的音频数据 URL 地址	限制长度不超过 10KB
         */
        private String wxMusicLowBandDataUrl;

        /**
         * 视频链接	限制长度不超过 10KB
         */
        private String wxVideoUrl;
        /**
         * 供低带宽的环境下使用的视频链接	限制长度不超过 10KB
         */
        private String wxVideoLowBandUrl;

        /**
         * html 链接	限制长度不超过 10KB
         */
        private String wxWebPageUrl;

        /**
         * 文件地址
         */
        private String wxFilePath;
        /**
         * 文件数据
         */
        private byte[] wxFileData;

        public Build setShareCallBack(AcbflwPluginCallBack shareCallBack) {
            this.shareCallBack = shareCallBack;
            return this;
        }

        public Build setTargetType(AcbflwPluginTargetTypeEnum targetType) {
            this.targetType = targetType;
            return this;
        }

        public Build setContentType(AcbflwShareContentTypeEnum contentType) {
            this.contentType = contentType;
            return this;
        }

        public Build setWxBaseDescription(String wxBaseDescription) {
            this.wxBaseDescription = wxBaseDescription;
            return this;
        }

        public Build setWxText(String wxText) {
            this.wxText = wxText;
            return this;
        }

        public Build setWxImageViewBitmap(Bitmap wxImageViewBitmap) {
            this.wxImageViewBitmap = wxImageViewBitmap;
            return this;
        }

        public Build setWxMiniProgramWebpageUrl(String wxMiniProgramWebpageUrl) {
            this.wxMiniProgramWebpageUrl = wxMiniProgramWebpageUrl;
            return this;
        }

        public Build setWxMiniProgramPath(String wxMiniProgramPath) {
            this.wxMiniProgramPath = wxMiniProgramPath;
            return this;
        }

        public Build setWxMiniProgramTitle(String wxMiniProgramTitle) {
            this.wxMiniProgramTitle = wxMiniProgramTitle;
            return this;
        }

        public Build setWxMiniProgramThumbBitmap(Bitmap wxMiniProgramThumbBitmap) {
            this.wxMiniProgramThumbBitmap = wxMiniProgramThumbBitmap;
            return this;
        }

        public Build setWxMiniProgramType(int wxMiniProgramType) {
            this.wxMiniProgramType = wxMiniProgramType;
            return this;
        }

        public Build setSaveLocalImageBitmap(Bitmap saveLocalImageBitmap) {
            this.saveLocalImageBitmap = saveLocalImageBitmap;
            return this;
        }

        public Build setSaveLocalImageName(String saveLocalImageName) {
            this.saveLocalImageName = saveLocalImageName;
            return this;
        }

        public Build setWxMusicUrl(String wxMusicUrl) {
            this.wxMusicUrl = wxMusicUrl;
            return this;
        }

        public Build setWxMusicLowBandUrl(String wxMusicLowBandUrl) {
            this.wxMusicLowBandUrl = wxMusicLowBandUrl;
            return this;
        }

        public Build setWxMusicDataUrl(String wxMusicDataUrl) {
            this.wxMusicDataUrl = wxMusicDataUrl;
            return this;
        }

        public Build setWxMusicLowBandDataUrl(String wxMusicLowBandDataUrl) {
            this.wxMusicLowBandDataUrl = wxMusicLowBandDataUrl;
            return this;
        }

        public Build setWxVideoUrl(String wxVideoUrl) {
            this.wxVideoUrl = wxVideoUrl;
            return this;
        }

        public Build setWxVideoLowBandUrl(String wxVideoLowBandUrl) {
            this.wxVideoLowBandUrl = wxVideoLowBandUrl;
            return this;
        }

        public Build setWxFilePath(String wxFilePath) {
            this.wxFilePath = wxFilePath;
            return this;
        }

        public Build setWxFileData(byte[] wxFileData) {
            this.wxFileData = wxFileData;
            return this;
        }

        public Build setWxWebPageUrl(String wxWebPageUrl) {
            this.wxWebPageUrl = wxWebPageUrl;
            return this;
        }

        public AcbflwShareDataBean build() {
            AcbflwShareDataBean bean = new AcbflwShareDataBean();
            bean.shareCallBack = this.shareCallBack;
            bean.targetType = this.targetType;
            bean.contentType = this.contentType;
            bean.wxBaseDescription = this.wxBaseDescription;
            bean.wxText = this.wxText;
            bean.wxImageViewBitmap = this.wxImageViewBitmap;
            bean.wxMiniProgramWebpageUrl = this.wxMiniProgramWebpageUrl;
            bean.wxMiniProgramPath = this.wxMiniProgramPath;
            bean.wxMiniProgramTitle = this.wxMiniProgramTitle;
            bean.wxMiniProgramThumbBitmap = this.wxMiniProgramThumbBitmap;
            bean.wxMiniProgramType =  this.wxMiniProgramType;
            bean.saveLocalImageBitmap = this.saveLocalImageBitmap;
            bean.saveLocalImageName = this.saveLocalImageName;
            bean.wxMusicUrl = this.wxMusicUrl;
            bean.wxMusicLowBandUrl = this.wxMusicLowBandUrl;
            bean.wxMusicDataUrl = this.wxMusicDataUrl;
            bean.wxMusicLowBandDataUrl = this.wxMusicLowBandDataUrl;
            bean.wxVideoUrl = this.wxVideoUrl;
            bean.wxVideoLowBandUrl = this.wxVideoLowBandUrl;
            bean.wxFilePath = this.wxFilePath;
            bean.wxFileData = this.wxFileData;
            bean.wxWebPageUrl = this.wxWebPageUrl;
            return bean;
        }

    }

}
