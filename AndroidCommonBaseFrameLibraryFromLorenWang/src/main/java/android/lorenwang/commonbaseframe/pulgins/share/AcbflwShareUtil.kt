package android.lorenwang.commonbaseframe.pulgins.share

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTargetTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil
import android.lorenwang.commonbaseframe.pulgins.share.AcbflwShareUtil
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.net.Uri
import java.io.File
import java.util.*

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
 *
 * @author wangliang
 */
class AcbflwShareUtil private constructor() {
    private val TAG = "AcbflwShareUtils"

    /**
     * 获取微信分享实例
     *
     * @return 微信分享实例
     */
    private var weChatShare: AcbflwWeChatShare? = null
        get() {
            if (field == null) {
                synchronized(AcbflwWeChatShare::class.java) {
                    if (field == null) {
                        field = AcbflwWeChatShare()
                    }
                }
            }
            return field
        }

    /**
     * 获取微博分享实例
     *
     * @return 微博分享实例
     */
    private var sinaShare: AcbflwSinaShare? = null
        get() {
            if (field == null) {
                synchronized(AcbflwWeChatShare::class.java) {
                    if (field == null) {
                        field = AcbflwSinaShare()
                    }
                }
            }
            return field
        }

    /**
     * 发送分享数据
     *
     * @param shareDataBean 分享数据实体
     */
    fun sendShareData(shareDataBean: AcbflwShareDataBean) {
        sendShareData(shareDataBean, shareDataBean.targetType, shareDataBean.contentType)
    }

    /**
     * 发送分享数据
     *
     * @param shareDataBean 分享数据实体
     * @param targetType    分享目标类型
     * @param contentType   分享内容类型
     */
    private fun sendShareData(
        shareDataBean: AcbflwShareDataBean, targetType: AcbflwPluginTargetTypeEnum?,
        contentType: AcbflwShareContentTypeEnum?,
    ) {
        AtlwLogUtil.logUtils.logI(TAG, "准备发起分享数据")
        assert(targetType != null)
        assert(contentType != null)
        assert(shareDataBean.shareCallBack != null)
        AtlwLogUtil.logUtils.logI(TAG, "分享目标：" + targetType!!.des + " 分享内容类型：" + contentType!!.des)
        when (targetType) {
            AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_SESSION, AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_FAVORITE, AcbflwPluginTargetTypeEnum.SHARE_WE_CHAT_MOMENTS -> if (AcbflwPluginUtil.getInstance(
                    AcbflwPluginTypeEnum.WECHAT) != null) {
                if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).api == null) {
                    weChatShare!!.callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INIT)
                } else if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).api.isWXAppInstalled) {
                    when (contentType) {
                        AcbflwShareContentTypeEnum.TEXT -> weChatShare!!.sendTextShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.IMAGE -> weChatShare!!.sendImageShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.MUSIC -> weChatShare!!.sendMusicShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.VIDEO -> weChatShare!!.sendVideoShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.WEB_PAGE -> weChatShare!!.sendWebPageShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.FILE -> weChatShare!!.sendFileShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.APP_EXTEND -> weChatShare!!.sendAppExtendShare(shareDataBean,
                            AcbflwPluginTargetTypeEnum.getShareTargetType(targetType))
                        AcbflwShareContentTypeEnum.MINI_PROGRAM -> weChatShare!!.sendMiniProgramShare(shareDataBean)
                        else -> {}
                    }
                } else {
                    AtlwLogUtil.logUtils.logI(TAG, "分享失败：微信未安装")
                    weChatShare!!.callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.WECHAT_NOT_INSTALL)
                }
            }
            AcbflwPluginTargetTypeEnum.SHARE_SAVE_LOCAL_IMAGE ->                 //分享图片，需要保存图片并通知数据库更新
                if (shareDataBean.saveLocalImageBitmap == null) {
                    shareDataBean.shareCallBack!!.error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY)
                } else {
                    val dirPath = AtlwConfig.nowApplication.getExternalFilesDir(null)!!.absolutePath
                    val file = File(dirPath.substring(0, dirPath.toLowerCase(Locale.ROOT).indexOf(
                        "/android")) + "/" + AtlwActivityUtil.getInstance().appName + "/" + if (shareDataBean.saveLocalImageName == null) System.currentTimeMillis()
                        .toString() + ".png" else shareDataBean.saveLocalImageName)
                    AtlwActivityUtil.getInstance().goToRequestPermissions(AcbflwBaseApplication.getCurrentShowActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), (Math.random() * 1000).toInt(),
                        object : AtlwPermissionRequestCallback {
                            /**
                             * 请求成功权限列表
                             *
                             * @param permissionList         权限列表
                             */
                            override fun permissionRequestSuccessCallback(permissionList: List<String>) {
                                //保存图片
                                if (AtlwFileOptionUtil.getInstance()
                                        .writeToFile(true, file, shareDataBean.saveLocalImageBitmap, Bitmap.CompressFormat.PNG)) {
                                    //保存图片后发送广播通知更新数据库
                                    val uri = Uri.fromFile(file)
                                    AtlwConfig.nowApplication.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                                    //回调成功
                                    shareDataBean.shareCallBack!!.info()
                                } else {
                                    shareDataBean.shareCallBack!!.error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR)
                                }
                            }

                            /**
                             * 请求失败权限列表
                             *
                             * @param permissionList         权限列表
                             */
                            override fun permissionRequestFailCallback(permissionList: List<String>) {
                                shareDataBean.shareCallBack!!.error(AcbflwPluginErrorTypeEnum.SHARE_IMAGE_ERROR)
                            }
                        })
                }
            AcbflwPluginTargetTypeEnum.SHARE_SINA -> when (contentType) {
                AcbflwShareContentTypeEnum.TEXT -> sinaShare!!.shareText(shareDataBean)
                AcbflwShareContentTypeEnum.IMAGE -> sinaShare!!.shareImageBitmap(shareDataBean)
                AcbflwShareContentTypeEnum.IMAGE_LIST -> sinaShare!!.shareImageList(shareDataBean)
                AcbflwShareContentTypeEnum.VIDEO -> sinaShare!!.shareVideo(shareDataBean)
                AcbflwShareContentTypeEnum.WEB_PAGE -> sinaShare!!.shareWeb(shareDataBean)
                else -> {}
            }
            else -> {}
        }
    }

    companion object {
        private var optionsInstance: AcbflwShareUtil? = null
        val instance: AcbflwShareUtil?
            get() {
                if (optionsInstance == null) {
                    synchronized(AcbflwShareUtil::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = AcbflwShareUtil()
                        }
                    }
                }
                return optionsInstance
            }
    }
}