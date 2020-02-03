package javabase.lorenwang.common_base_frame.plugins.aliyun

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.plugins.OssOptions
import java.io.InputStream


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
internal class SbcbflwALiYunOssUtils : OssOptions() {
    private val filePathRegex = Regex("^[hH][tT]{2}[p|P][sS]?://${SbcbflwCommonUtils.instance.aliYunPropertiesConfig.domain}")

    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    override fun getFilePathPrefix(): String {
        return "${SbcbflwCommonUtils.instance.propertiesConfig.ossTypeProtocol}://${SbcbflwCommonUtils.instance.aliYunPropertiesConfig.domain}"
    }

    /**
     * 文件地址前缀正则，最后不包含斜杠
     * @return 文件地址前缀正则，最后不包含斜杠
     */
    override fun getFilePathPrefixRegex(): Regex {
        return filePathRegex
    }

    /**
     * 上传文件流
     * @param inputStream 文件流
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     */
    override fun upLoadFile(inputStream: InputStream, savePath: String): Boolean {
        // 创建OSSClient实例。
        val ossClient = OSSClient(SbcbflwCommonUtils.instance.aliYunPropertiesConfig.endpoint,
                SbcbflwCommonUtils.instance.aliYunPropertiesConfig.accessKeyId,
                SbcbflwCommonUtils.instance.aliYunPropertiesConfig.accessKeySecret)

        return try { // 上传文件流。
            ossClient.putObject(SbcbflwCommonUtils.instance.aliYunPropertiesConfig.bucket, savePath, inputStream, ObjectMetadata())
            true
        } catch (e: Exception) {
            false
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown()
        }
    }

}
