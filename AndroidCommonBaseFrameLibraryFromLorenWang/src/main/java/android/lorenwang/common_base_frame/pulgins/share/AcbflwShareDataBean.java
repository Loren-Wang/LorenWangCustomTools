package android.lorenwang.common_base_frame.pulgins.share;

import android.graphics.Bitmap;

import com.qtools.base.pulgins.QtPluginCallBack;
import com.qtools.base.pulgins.QtPluginTargetTypeEnum;
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
    private QtPluginCallBack shareCallBack;
    /**
     * 分享目标
     */
    QtPluginTargetTypeEnum targetType;
    /**
     * 分享内容类型
     */
    AcbflwShareContentTypeEnum contentType;

    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享描述
     */
    private String description;

    /**
     * 分享文本
     */
    private String text;


    /**
     * 分享图片位图
     */
    private Bitmap imgBitmap;
    /**
     * 分享图片缩略图位图
     */
    private Bitmap thumbBmp;
    /**
     * 分享图片地址
     */
    private String imgPath;


    /**
     * 音频网页的 URL 地址	限制长度不超过 10KB
     */
    private String musicUrl;
    /**
     * 供低带宽环境下使用的音频网页 URL 地址	限制长度不超过 10KB
     */
    private String musicLowBandUrl;
    /**
     * 音频数据的 URL 地址	限制长度不超过 10KB
     */
    private String musicDataUrl;
    /**
     * 供低带宽环境下使用的音频数据 URL 地址	限制长度不超过 10KB
     */
    private String musicLowBandDataUrl;

    /**
     * 视频链接	限制长度不超过 10KB
     */
    private String videoUrl;
    /**
     * 供低带宽的环境下使用的视频链接	限制长度不超过 10KB
     */
    private String videoLowBandUrl;

    /**
     * 文件地址
     */
    private String filePath;
    /**
     * 文件数据
     */
    private byte[] fileData;

    /**
     * html 链接	限制长度不超过 10KB
     */
    private String webPageUrl;
    /**
     * 小程序的原始 id	小程序原始 ID 获取方法：登录小程序管理后台-设置-基本设置-帐号信息
     */
    private String userName;
    /**
     * 小程序的 path	小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
     */
    private String path;
    /**
     * 是否使用带 shareTicket 的分享	通常开发者希望分享出去的小程序被二次打开时可以获取到更多信息，例如群的标识。可以设置 withShareTicket 为 true，当分享卡片在群聊中被其他用户打开时，可以获取到 shareTicket，用于获取更多分享信息。详见小程序获取更多分享信息 ，最低客户端版本要求：6.5.13
     */
    private boolean withShareTicket = false;
    /**
     * 小程序的类型，默认正式版
     * 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
     * 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
     * 预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
     */
    private int miniProgramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;

    public QtPluginCallBack getShareCallBack() {
        return shareCallBack;
    }

    public QtPluginTargetTypeEnum getTargetType() {
        return targetType;
    }

    public AcbflwShareContentTypeEnum getContentType() {
        return contentType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public Bitmap getThumbBmp() {
        return thumbBmp;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public String getMusicLowBandUrl() {
        return musicLowBandUrl;
    }

    public String getMusicDataUrl() {
        return musicDataUrl;
    }

    public String getMusicLowBandDataUrl() {
        return musicLowBandDataUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoLowBandUrl() {
        return videoLowBandUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getWebPageUrl() {
        return webPageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPath() {
        return path;
    }

    public boolean isWithShareTicket() {
        return withShareTicket;
    }

    public int getMiniProgramType() {
        return miniProgramType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public static class Build {
        /**
         * 分享回调
         */
        private QtPluginCallBack shareCallBack;
        /**
         * 分享目标
         */
        QtPluginTargetTypeEnum targetType;
        /**
         * 分享内容类型
         */
        AcbflwShareContentTypeEnum contentType;

        /**
         * 分享标题
         */
        private String title;
        /**
         * 分享描述
         */
        private String description;

        /**
         * 分享文本
         */
        private String text;


        /**
         * 分享图片位图
         */
        private Bitmap imgBitmap;
        /**
         * 分享图片缩略图位图
         */
        private Bitmap thumbBmp;
        /**
         * 分享图片地址
         */
        private String imgPath;


        /**
         * 音频网页的 URL 地址	限制长度不超过 10KB
         */
        private String musicUrl;
        /**
         * 供低带宽环境下使用的音频网页 URL 地址	限制长度不超过 10KB
         */
        private String musicLowBandUrl;
        /**
         * 音频数据的 URL 地址	限制长度不超过 10KB
         */
        private String musicDataUrl;
        /**
         * 供低带宽环境下使用的音频数据 URL 地址	限制长度不超过 10KB
         */
        private String musicLowBandDataUrl;

        /**
         * 视频链接	限制长度不超过 10KB
         */
        private String videoUrl;
        /**
         * 供低带宽的环境下使用的视频链接	限制长度不超过 10KB
         */
        private String videoLowBandUrl;

        /**
         * 文件地址
         */
        private String filePath;
        /**
         * 文件数据
         */
        private byte[] fileData;

        /**
         * html 链接	限制长度不超过 10KB
         */
        private String webPageUrl;
        /**
         * 小程序的原始 id	小程序原始 ID 获取方法：登录小程序管理后台-设置-基本设置-帐号信息
         */
        private String userName;
        /**
         * 小程序的 path	小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
         */
        private String path;
        /**
         * 是否使用带 shareTicket 的分享	通常开发者希望分享出去的小程序被二次打开时可以获取到更多信息，例如群的标识。可以设置 withShareTicket 为 true，当分享卡片在群聊中被其他用户打开时，可以获取到 shareTicket，用于获取更多分享信息。详见小程序获取更多分享信息 ，最低客户端版本要求：6.5.13
         */
        private boolean withShareTicket = false;
        /**
         * 小程序的类型，默认正式版
         * 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
         * 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
         * 预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
         */
        private int miniProgramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;

        public Build setShareCallBack(QtPluginCallBack shareCallBack) {
            this.shareCallBack = shareCallBack;
            return this;
        }

        public Build setTargetType(QtPluginTargetTypeEnum targetType) {
            this.targetType = targetType;
            return this;
        }

        public Build setContentType(AcbflwShareContentTypeEnum contentType) {
            this.contentType = contentType;
            return this;
        }

        public Build setTitle(String title) {
            this.title = title;
            return this;
        }

        public Build setDescription(String description) {
            this.description = description;
            return this;
        }

        public Build setText(String text) {
            this.text = text;
            return this;
        }

        public Build setImgBitmap(Bitmap imgBitmap) {
            this.imgBitmap = imgBitmap;
            return this;
        }

        public Build setThumbBmp(Bitmap thumbBmp) {
            this.thumbBmp = thumbBmp;
            return this;
        }

        public Build setImgPath(String imgPath) {
            this.imgPath = imgPath;
            return this;
        }

        public Build setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
            return this;
        }

        public Build setMusicLowBandUrl(String musicLowBandUrl) {
            this.musicLowBandUrl = musicLowBandUrl;
            return this;
        }

        public Build setMusicDataUrl(String musicDataUrl) {
            this.musicDataUrl = musicDataUrl;
            return this;
        }

        public Build setMusicLowBandDataUrl(String musicLowBandDataUrl) {
            this.musicLowBandDataUrl = musicLowBandDataUrl;
            return this;
        }

        public Build setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Build setVideoLowBandUrl(String videoLowBandUrl) {
            this.videoLowBandUrl = videoLowBandUrl;
            return this;
        }

        public Build setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Build setFileData(byte[] fileData) {
            this.fileData = fileData;
            return this;
        }

        public Build setWebPageUrl(String webPageUrl) {
            this.webPageUrl = webPageUrl;
            return this;
        }

        public Build setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Build setPath(String path) {
            this.path = path;
            return this;
        }

        public Build setWithShareTicket(boolean withShareTicket) {
            this.withShareTicket = withShareTicket;
            return this;
        }

        public Build setMiniProgramType(int miniProgramType) {
            this.miniProgramType = miniProgramType;
            return this;
        }

        public AcbflwShareDataBean build() {
            AcbflwShareDataBean bean = new AcbflwShareDataBean();
            bean.shareCallBack = this.shareCallBack;
            bean.targetType = this.targetType;
            bean.contentType = this.contentType;
            bean.title = this.title;
            bean.description = this.description;
            bean.text = this.text;
            bean.imgBitmap = this.imgBitmap;
            bean.thumbBmp = this.thumbBmp;
            bean.imgPath = this.imgPath;
            bean.musicUrl = this.musicUrl;
            bean.musicLowBandUrl = this.musicLowBandUrl;
            bean.musicDataUrl = this.musicDataUrl;
            bean.musicLowBandDataUrl = this.musicLowBandDataUrl;
            bean.videoUrl = this.videoUrl;
            bean.videoLowBandUrl = this.videoLowBandUrl;
            bean.filePath = this.filePath;
            bean.fileData = this.fileData;
            bean.webPageUrl = this.webPageUrl;
            bean.userName = this.userName;
            bean.path = this.path;
            bean.withShareTicket = this.withShareTicket;
            bean.miniProgramType = this.miniProgramType;
            return bean;
        }

    }

}
