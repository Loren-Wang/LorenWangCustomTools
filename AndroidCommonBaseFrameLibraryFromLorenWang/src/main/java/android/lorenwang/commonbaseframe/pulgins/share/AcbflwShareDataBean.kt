package android.lorenwang.commonbaseframe.pulgins.share

import android.app.Activity
import android.graphics.Bitmap
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginCallBack
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum
import lombok.Builder

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
@Builder
class AcbflwShareDataBean {
    /**
     * 分享回调
     */
    var shareCallBack: AcbflwPluginCallBack? = null

    /**
     * 分享目标
     */
    var targetType: AcbflwPluginTargetTypeEnum? = null

    /**
     * 分享内容类型
     */
    var contentType: AcbflwShareContentTypeEnum? = null

    /**
     * 页面实例
     */
    var activity: Activity? = null

    /**
     * 微信分享描述
     */
    var wxBaseDescription: String? = null

    /**
     * 微信文本分享文本
     */
    var wxText: String? = null

    /**
     * 微信分享的图片
     */
    var wxImageViewBitmap: Bitmap? = null

    /**
     * 微信小程序分享地址
     */
    var wxMiniProgramWebpageUrl: String? = null

    /**
     * 微信小程序分享小程序路径
     */
    var wxMiniProgramPath: String? = null

    /**
     * 微信小程序分享的小程序消息标题
     */
    var wxMiniProgramTitle: String? = null

    /**
     * 微信小程序缩略图
     */
    var wxMiniProgramThumbBitmap: Bitmap? = null

    /**
     * 小程序的类型，默认正式版
     * 正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
     * 测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
     * 预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
     */
    var wxMiniProgramType: Int? = null

    /**
     * 保存到本地的图片位图
     */
    var saveLocalImageBitmap: Bitmap? = null

    /**
     * 保存到本地的图片名称
     */
    var saveLocalImageName: String? = null

    /**
     * 音频网页的 URL 地址	限制长度不超过 10KB
     */
    var wxMusicUrl: String? = null

    /**
     * 供低带宽环境下使用的音频网页 URL 地址	限制长度不超过 10KB
     */
    var wxMusicLowBandUrl: String? = null

    /**
     * 音频数据的 URL 地址	限制长度不超过 10KB
     */
    var wxMusicDataUrl: String? = null

    /**
     * 供低带宽环境下使用的音频数据 URL 地址	限制长度不超过 10KB
     */
    var wxMusicLowBandDataUrl: String? = null

    /**
     * 视频链接	限制长度不超过 10KB
     */
    var wxVideoUrl: String? = null

    /**
     * 供低带宽的环境下使用的视频链接	限制长度不超过 10KB
     */
    var wxVideoLowBandUrl: String? = null

    /**
     * html 链接	限制长度不超过 10KB
     */
    var wxWebPageUrl: String? = null

    /**
     * 文件地址
     */
    var wxFilePath: String? = null

    /**
     * 文件数据
     */
    var wxFileData: ByteArray? = null

    /**
     * 微博分享文本
     */
    var sinaText: String? = null

    /**
     * 微博分享的图片
     */
    var sinaImageViewBitmap: Bitmap? = null

    /**
     * 新浪分享标题
     */
    var sinaTitle: String? = null

    /**
     * 新浪分享描述
     */
    var sinaDes: String? = null

    /**
     * 分享的网址
     */
    var sinaWebUrl: String? = null

    /**
     * 分享的图片列表
     */
    var sinaImagePathList: List<String>? = null

    /**
     * 分享视频路径
     */
    var sinaVideoPath: String? = null

    /**
     * qq分享图片本地地址
     */
    var qqImageLocalPath: String? = null
}