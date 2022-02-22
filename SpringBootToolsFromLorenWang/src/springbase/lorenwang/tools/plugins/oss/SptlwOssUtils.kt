package springbase.lorenwang.tools.plugins.oss

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.plugins.oss.aliyun.SptlwALiYunOssConfig
import springbase.lorenwang.tools.plugins.oss.aliyun.SptlwALiYunOssUtils
import springbase.lorenwang.tools.plugins.oss.qiniu.SptlwQiNiuOssConfig
import springbase.lorenwang.tools.plugins.oss.qiniu.SptlwQiNiuOssUtils
import java.io.InputStream

/**
 * 功能作用：oss单例工具类
 * 创建时间：2020-02-03 下午 17:30:12
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class SptlwOssUtils private constructor() : AbstractOssOptions() {
    /**
     * oss操作实例
     */
    private var ossOptions: AbstractOssOptions? = null

    companion object {
        @Volatile
        private var optionsInstance: SptlwOssUtils? = null

        @JvmStatic
        val instance: SptlwOssUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = SptlwOssUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 初始化oss相关配置
     */
    fun initOssConfig(ossConfig: Any) {
        when (ossConfig) {
            is SptlwALiYunOssConfig -> {
                ossOptions = SptlwALiYunOssUtils(ossConfig)
            }
            is SptlwQiNiuOssConfig -> {
                ossOptions = SptlwQiNiuOssUtils(ossConfig)
            }
        }
    }

    /**
     * 获取文件地址前缀，最后不包含斜杠
     * @return 地址前缀
     */
    override fun getFilePathPrefix(): String {
        return ossOptions.kttlwEmptyCheck({
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
        return ossOptions.kttlwEmptyCheck({
            Regex("")
        }, {
            it.getFilePathPrefixRegex()
        })
    }

    /**
     * 上传文件流
     * @param inputStream 文件流
     * @param savePath 存储文件地址，从存储空间后面的路径开始，例如：a/keyPrefix/resume/fileName.jpg其中a是存储空间
     * @return 返回结果
     */
    override fun upLoadFile(inputStream: InputStream, savePath: String): SpblwBaseDataDisposeStatusBean {
        return ossOptions.kttlwEmptyCheck({
            SpblwBaseDataDisposeStatusBean(false)
        }, {
            it.upLoadFile(inputStream, savePath)
        })
    }


}
