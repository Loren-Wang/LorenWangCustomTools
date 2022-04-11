package springbase.lorenwang.tools.safe

import javabase.lorenwang.tools.safe.JtlwEncryptDecryptUtil

/**
 * 功能作用：加密解密工具类
 * 创建时间：2019-12-16 11:49
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、加密字符串---encrypt(str)
 * 2、解密字符串---decrypt(str)
 * 3、带key和加密参数的加密字符串---encrypt(str,ketStr,ivsStr)
 * 4、带key和加密参数的解密字符串---decrypt(str,ketStr,ivsStr)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SptlwEncryptDecryptUtil private constructor() {
    companion object {
        private var optionsUtils: SptlwEncryptDecryptUtil? = null

        @JvmStatic
        val instance: SptlwEncryptDecryptUtil
            get() {
                if (optionsUtils == null) {
                    synchronized(this::class.java) {
                        if (optionsUtils == null) {
                            optionsUtils = SptlwEncryptDecryptUtil()
                        }
                    }
                }
                return optionsUtils!!
            }
    }

    /**
     * 加密字符串
     * @param encData 字符串
     * @param key 加密的key
     * @param ivs 加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    fun encrypt(key: String?, ivs: String, encData: String): String? {
        return JtlwEncryptDecryptUtil.getInstance().encrypt(encData, key, ivs)
    }

    /**
     * 解密字符串
     * @param sSrc 字符串
     * @param key 加密的key
     * @param ivs 加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    fun decrypt(key: String, ivs: String, sSrc: String): String? {
        return JtlwEncryptDecryptUtil.getInstance().decrypt(sSrc, key, ivs)
    }
}
