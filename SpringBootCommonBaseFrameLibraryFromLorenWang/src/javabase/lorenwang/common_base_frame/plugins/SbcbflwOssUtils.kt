package javabase.lorenwang.common_base_frame.plugins

import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.plugins.aliyun.SbcbflwALiYunOssUtils
import javabase.lorenwang.common_base_frame.plugins.qiniu.SbcbflwQiNiuOssUtils
import javabase.lorenwang.common_base_frame.propertiesConfig.SbcbflwAlLiYunOssPropertiesConfig
import javabase.lorenwang.common_base_frame.propertiesConfig.SbcbflwQiNiuOssPropertiesConfig
import kotlinbase.lorenwang.tools.extend.emptyCheck
import java.io.InputStream

/**
 * 功能作用：oss单例工具类
 * 创建时间：2020-02-03 下午 17:30:12
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwOssUtils private constructor() : OssOptions() {
    /**
     * oss操作实例
     */
    private var ossOptions: OssOptions? = null

    init {
        if (SbcbflwCommonUtils.instance.propertiesConfig.ossTypeAliYun) {
            ossOptions = SbcbflwALiYunOssUtils()
        } else if (SbcbflwCommonUtils.instance.propertiesConfig.ossTypeQiNiu) {
            ossOptions = SbcbflwQiNiuOssUtils()
        }
    }

    companion object {
        @Volatile
        private var optionsInstance: SbcbflwOssUtils? = null
        @JvmStatic
        val instance: SbcbflwOssUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = SbcbflwOssUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    override fun getFilePathPrefix(): String {
        return ossOptions.emptyCheck({
            ""
        }, {
            it.getFilePathPrefix()
        })
    }

    /**
     * 文件地址前缀正则，最后不包含斜杠
     * @return 文件地址前缀正则，最后不包含斜杠
     */
    override fun getFilePathPrefixRegex(): Regex {
        return ossOptions.emptyCheck({
            Regex("")
        }, {
            it.getFilePathPrefixRegex()
        })
    }

    /**
     * 上传文件流
     * @param inputStream 文件流
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyprefix/resume/fileName.jpg其中a是存储空间
     * @return 返回结果
     */
    override fun upLoadFile(inputStream: InputStream, savePath: String): Boolean {
        return ossOptions.emptyCheck({
            false
        }, {
            it.upLoadFile(inputStream, savePath)
        })
    }


}
