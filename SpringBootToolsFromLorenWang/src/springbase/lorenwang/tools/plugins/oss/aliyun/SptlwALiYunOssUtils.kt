package springbase.lorenwang.tools.plugins.oss.aliyun

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.plugins.oss.AbstractOssOptions
import java.io.InputStream


/**
 * 功能作用：阿里云oss存储工具类
 * 创建时间：2019-09-12 下午 16:15:17
 * 创建人：王亮（Loren）
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
 * @param ossConfig 配置文件
 */
internal class SptlwALiYunOssUtils(val ossConfig: SptlwALiYunOssConfig) : AbstractOssOptions() {

    /**
     * 文件存储地址正则
     */
    private val filePathRegex by lazy { Regex("^[hH][tT]{2}[p|P][sS]?://${ossConfig.domain}") }


    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    override fun getFilePathPrefix(): String {
        return "${ossConfig.ossTypeProtocol}://${ossConfig.domain}"
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
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyPrefix/resume/fileName.jpg其中a是存储空间
     */
    override fun upLoadFile(inputStream: InputStream, savePath: String): SpblwBaseDataDisposeStatusBean {
        // 创建OSSClient实例。
        val ossClient = OSSClient(ossConfig.endpoint, ossConfig.accessKeyId, ossConfig.accessKeySecret)

        return try { // 上传文件流。
            ossClient.putObject(ossConfig.bucket, savePath, inputStream, ObjectMetadata())
            SpblwBaseDataDisposeStatusBean(true, savePath)
        } catch (e: Exception) {
            SpblwBaseDataDisposeStatusBean(false)
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown()
        }
    }

}
