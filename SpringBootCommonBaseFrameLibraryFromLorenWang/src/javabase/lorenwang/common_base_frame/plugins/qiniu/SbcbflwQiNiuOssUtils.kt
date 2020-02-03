package javabase.lorenwang.common_base_frame.plugins.qiniu

import com.qiniu.common.QiniuException
import com.qiniu.http.Response
import com.qiniu.storage.Configuration
import com.qiniu.storage.Region
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.util.Auth
import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.plugins.OssOptions
import javabase.lorenwang.dataparse.JdplwJsonUtils
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.UnsupportedEncodingException


/**
 * 功能作用：七牛云存储单例类
 * 创建时间：2020-02-03 下午 14:34:51
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
internal class SbcbflwQiNiuOssUtils : OssOptions() {
    private val filePathRegex = Regex("^[hH][tT]{2}[p|P][sS]?://${SbcbflwCommonUtils.instance.qiNiuPropertiesConfig.domain}")

    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    override fun getFilePathPrefix(): String {
        return "${SbcbflwCommonUtils.instance.propertiesConfig.ossTypeProtocol}://${SbcbflwCommonUtils.instance.qiNiuPropertiesConfig.domain}"
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
        //构造一个带指定 Region 对象的配置类
        val cfg = Configuration(Region.region0())
        val uploadManager = UploadManager(cfg)

        try {
            val auth = Auth.create(SbcbflwCommonUtils.instance.qiNiuPropertiesConfig.accessKey,
                    SbcbflwCommonUtils.instance.qiNiuPropertiesConfig.secretKey)
            val upToken = auth.uploadToken(SbcbflwCommonUtils.instance.qiNiuPropertiesConfig.bucket)
            try {
                val response: Response = uploadManager.put(inputStream, savePath, upToken, null, null)
                //解析上传成功的结果
                val putRet: DefaultPutRet = JdplwJsonUtils.fromJson(response.bodyString(), DefaultPutRet::class.java)
                return true
            } catch (ex: QiniuException) {
                val r: Response = ex.response
                System.err.println(r.toString())
                try {
                    System.err.println(r.bodyString())
                } catch (ex2: QiniuException) { //ignore
                }
                return false
            }
        } catch (ex: UnsupportedEncodingException) { //ignore
            return false
        }
    }
}
