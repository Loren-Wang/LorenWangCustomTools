package javabase.lorenwang.common_base_frame.plugins

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import javabase.lorenwang.common_base_frame.SbcbflwBASE64DecodedMultipartFile
import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 功能作用：oss操作接口
 * 创建时间：2020-02-03 下午 17:04:43
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class OssOptions {
    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    abstract fun getFilePathPrefix(): String

    /**
     * 文件地址前缀正则，最后不包含斜杠
     * @return 文件地址前缀正则，最后不包含斜杠
     */
    abstract fun getFilePathPrefixRegex(): Regex

    /**
     * 获取图片地址
     * @param isFullPath 是否要返回完整地址
     * @param imgPath 图片地址 可能为完整也可能不为完整地址
     * @return 图片地址
     */
    fun getImageUrl(isFullPath: Boolean, imgPath: String?): String {
        return imgPath.emptyCheck({
            ""
        }, {
            return@emptyCheck if (isFullPath) {
                "${getFilePathPrefix()}${clearImageDomain(imgPath)}"
            } else {
                it
            }
        })
    }

    /**
     * 清除图片域名信息
     * @param imgPath 图片地址 可能为完整也可能不为完整地址
     * @return 图片地址
     */
    fun clearImageDomain(imgPath: String?): String {
        return imgPath.emptyCheck({
            ""
        }, {
            it.replace(getFilePathPrefixRegex(), "")
        })
    }

    /**
     * 是否有地址域名
     */
    fun haveImageDomain(imgPath: String?): Boolean {
        return imgPath.emptyCheck({
            false
        }, {
            it.contains(getFilePathPrefixRegex())
        })
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

    /**
     * 上传文件流
     * @param inputStream 文件流
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     * @return 返回结果
     */
    abstract fun upLoadFile(inputStream: InputStream, savePath: String): Boolean
}
