package javabase.lorenwang.common_base_frame.safe

import javabase.lorenwang.common_base_frame.SbcbflwPropertiesConfig
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
class SbcbflwEncryptDecryptUtils private constructor() {
    companion object {
        private var optionsUtils: SbcbflwEncryptDecryptUtils? = null
        @JvmStatic
        val instance: SbcbflwEncryptDecryptUtils
            get() {
                if (optionsUtils == null) {
                    synchronized(this::class.java) {
                        if (optionsUtils == null) {
                            optionsUtils = SbcbflwEncryptDecryptUtils()
                        }
                    }
                }
                return optionsUtils!!
            }
    }

    /**
     * 加密字符串
     * @param sSrc 字符串
     * @return 返回加密后字符串，失败返回空null
     */
    fun encrypt(sSrc: String): String? {
        return encrypt(sSrc, SbcbflwPropertiesConfig.encryptDecryptKey, SbcbflwPropertiesConfig.encryptDecryptIvParameter)
    }

    /**
     * 解密字符串
     * @param sSrc 字符串
     * @return 返回解密后字符串，失败返回空null
     */
    fun decrypt(sSrc: String): String? {
        return decrypt(sSrc, SbcbflwPropertiesConfig.encryptDecryptKey, SbcbflwPropertiesConfig.encryptDecryptIvParameter)
    }

    /**
     * 加密字符串
     * @param sSrc 字符串
     * @param key 加密的key
     * @param ivs 加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    fun encrypt(encData: String, key: String?, ivs: String): String? {
        return try {
            if (key == null) {
                return null
            }
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val raw = key.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            val iv = IvParameterSpec(ivs.toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            String(cipher.doFinal(Base64.getEncoder().encode(encData.toByteArray())), charset("utf-8"))
        } catch (e: java.lang.Exception) {
            null
        }
    }

    /**
     * 解密字符串
     * @param sSrc 字符串
     * @param key 加密的key
     * @param ivs 加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    fun decrypt(sSrc: String, key: String, ivs: String): String? {
        return try {
            val raw = key.toByteArray(charset("ASCII"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val iv = IvParameterSpec(ivs.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val encrypted1 = Base64.getDecoder().decode(sSrc)// 先用base64解密
            val original = cipher.doFinal(encrypted1)
            String(Base64.getDecoder().decode(String(original)))
        } catch (ex: Exception) {
            null
        }
    }
}
