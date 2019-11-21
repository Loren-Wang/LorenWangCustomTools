package kotlinbase.lorenwang.tools.image

import javabase.lorenwang.tools.JtlwLogUtils
import javabase.lorenwang.tools.file.JtlwFileOptionUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import sun.misc.BASE64Encoder

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
    private val TAG = "";
    private var basE64Encoder: BASE64Encoder? = null;

    companion object {
        private var optionsInstance: KttlwImageOptionUtils? = null
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
        return getBase64Encoder().encode(JtlwFileOptionUtils.getInstance().readImageFileGetBytes(isCheckFile, filePath)).emptyCheck({
            JtlwLogUtils.logI(this.TAG, "图片转换失败,失败原因：文件读取异常")
            ""
        }, {
            it
        })
    }

    /**
     * 获取base64编码实例
     */
    private fun getBase64Encoder(): BASE64Encoder {
        if (basE64Encoder == null) {
            synchronized(BASE64Encoder::class) {
                if (basE64Encoder == null) {
                    basE64Encoder = BASE64Encoder()
                }
            }
        }
        return basE64Encoder!!
    }

}
