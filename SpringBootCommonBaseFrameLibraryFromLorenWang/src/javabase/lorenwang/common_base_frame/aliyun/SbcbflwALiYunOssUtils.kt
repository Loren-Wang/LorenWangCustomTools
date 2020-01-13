package javabase.lorenwang.common_base_frame.aliyun

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import javabase.lorenwang.common_base_frame.SbcbflwBASE64DecodedMultipartFile
import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.SbcbflwPropertiesConfig
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * 功能作用：阿里云oss存储工具类
 * 创建时间：2019-09-12 下午 16:15:17
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、获取图片地址---getImageUrl(isFullPath,imgPath)
 * 2、清除图片域名信息---clearImageDomain(imgPath)
 * 3、是否有地址域名---haveImageDomain(imgPath)
 * 4、上传MultipartFile文件---upLoadFile(file,savePath)
 * 5、上传文件流---upLoadFile(inputStream,savePath)
 * 6、下载其他网络文件并将其上传到oss当中---downLoadAndUpLoadFile(downLoadPath,savePath)
 * 7、上传字节数组文件---upLoadFile(bytes,contentType,savePath)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Service
open class SbcbflwALiYunOssUtils private constructor() {
    private val filePathRegex = Regex("^[hH][tT]{2}[p|P][sS]?://${SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssDomain}/")

    companion object {
        private var aLiYunOssUtils: SbcbflwALiYunOssUtils? = null

        @JvmStatic
        val instance: SbcbflwALiYunOssUtils
            get() {
                if (aLiYunOssUtils == null) {
                    synchronized(SbcbflwALiYunOssUtils::class.java) {
                        if (aLiYunOssUtils == null) {
                            aLiYunOssUtils = SbcbflwALiYunOssUtils()
                        }
                    }
                }
                return aLiYunOssUtils as SbcbflwALiYunOssUtils
            }
    }

    /**
     * 获取图片地址
     * @param isFullPath 是否要返回完整地址
     * @param imgPath 图片地址 可能为完整也可能不为完整地址
     * @return 图片地址
     */
    fun getImageUrl(isFullPath: Boolean, imgPath: String?): String {
        return if (JtlwCheckVariateUtils.getInstance().isEmpty(imgPath)) {
            ""
        } else {
            if (isFullPath) {
                "http://${SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssDomain}/${clearImageDomain(imgPath)}"
            } else imgPath!!
        }
    }

    /**
     * 清除图片域名信息
     * @param imgPath 图片地址 可能为完整也可能不为完整地址
     * @return 图片地址
     */
    fun clearImageDomain(imgPath: String?): String {
        return if (JtlwCheckVariateUtils.getInstance().isEmpty(imgPath)) {
            ""
        } else {
            imgPath!!.replace(filePathRegex, "")
        }
    }

    /**
     * 是否有地址域名
     */
    fun haveImageDomain(imgPath: String?): Boolean {
        return if (JtlwCheckVariateUtils.getInstance().isEmpty(imgPath)) {
            false
        } else {
            imgPath!!.contains(filePathRegex)
        }
    }

    /**
     * 上传MultipartFile文件
     * @param file 上传文件
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     */
    fun upLoadFile(file: MultipartFile, savePath: String): Boolean {
        return try { // 上传文件流。
            upLoadFile(file.inputStream, savePath)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 上传文件流
     * @param inputStream 文件流
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     */
    fun upLoadFile(inputStream: InputStream, savePath: String): Boolean {
        // 创建OSSClient实例。
        val ossClient = OSSClient(SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssEndpoint,
                SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssAccessKeyId,
                SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssAccessKeySecret)

        return try { // 上传文件流。
            ossClient.putObject(SbcbflwCommonUtils.instance.propertiesConfig.aLiYunOssBucket, savePath, inputStream, ObjectMetadata())
            true
        } catch (e: Exception) {
            false
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown()
        }
    }

    /**
     * 下载其他网络文件并将其上传到oss当中
     * @param downLoadPath 下载地址
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     */
    fun downLoadAndUpLoadFile(downLoadPath: String, savePath: String): Boolean {
        return try { // 上传文件流。
            /**
             * 获取外部文件流
             */
            val url = URL(downLoadPath)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3 * 1000
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
            upLoadFile(conn.inputStream, savePath)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 上传字节数组文件
     * @param bytes 文件字节
     * @param contentType 文件类型
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     */
    fun upLoadFile(bytes: ByteArray, contentType: String, savePath: String): Boolean {
        return this.upLoadFile(SbcbflwBASE64DecodedMultipartFile(bytes, contentType), savePath)
    }
}
