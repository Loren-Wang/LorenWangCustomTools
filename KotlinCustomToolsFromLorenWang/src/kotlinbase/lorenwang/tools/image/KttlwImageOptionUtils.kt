package kotlinbase.lorenwang.tools.image

import javabase.lorenwang.tools.JtlwLogUtils
import javabase.lorenwang.tools.file.JtlwFileOptionUtils
import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * 功能作用：图片处理工具类
 * 创建时间：2019-11-21 下午 22:21:31
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class KttlwImageOptionUtils private constructor() {
    private val TAG = "kttlwImageOptionUtils"

    companion object {
        private var optionsInstance: KttlwImageOptionUtils? = null
        @JvmStatic
        val instance: KttlwImageOptionUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class) {
                        if (optionsInstance == null) {
                            optionsInstance = KttlwImageOptionUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 图片文件转base64字符串
     * @param isCheckFile 是否检测文件，检测文件是否存在以及文件是否是图片
     * @param filePath 文件地址
     * @return 文件转码后的base64字符串
     */
    fun imageFileToBase64String(isCheckFile: Boolean, filePath: String): String {
        return getBase64Encoder().encodeToString(JtlwFileOptionUtils.getInstance().readImageFileGetBytes(isCheckFile, filePath)).kttlwEmptyCheck({
            JtlwLogUtils.logUtils.logI(this.TAG, "图片转换失败,失败原因：文件读取异常")
            ""
        }, {
            it
        })
    }

    /**
     * base64图片字符串转byte数组
     */
    fun base64ImageStringToBytes(base64Data: String): ByteArray {
        var out: ByteArrayOutputStream? = null
        try {
            // Base64解码
            return getBase64Decoder().decode(base64Data).kttlwEmptyCheck({
                byteArrayOf()
            }, {
                // 调整异常数据
                it.forEachIndexed { index, byte ->
                    if (byte < 0) {
                        it[index] = (byte + 256).toByte()
                    }
                }
                //输入到流中
                out = ByteArrayOutputStream()
                out?.write(it)
                out?.flush()
                it
            })
        } catch (e: Exception) {
            return byteArrayOf()
        } finally {
            out?.close()
        }

    }

    /**
     * 获取base64编码实例
     */
    private fun getBase64Encoder(): Base64.Encoder {
        return Base64.getEncoder()!!
    }

    /**
     * 获取base64解码实例
     */
    private fun getBase64Decoder(): Base64.Decoder {
        return Base64.getDecoder()!!
    }
}
