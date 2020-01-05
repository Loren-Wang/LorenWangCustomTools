package javabase.lorenwang.tools.safe;

import javax.crypto.Cipher;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 功能作用：加密解密工具类
 * 创建时间：2020-01-05 下午 22:43:16
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwEncryptDecryptUtils {
    private static volatile JtlwEncryptDecryptUtils optionUtils;

    /**
     * 私有构造
     */
    private JtlwEncryptDecryptUtils() {
    }

    public static JtlwEncryptDecryptUtils getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwEncryptDecryptUtils.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwEncryptDecryptUtils();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 加密字符串
     *
     * @param srcData 字符串
     * @param key     加密的key
     * @param ivs     加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    public String encrypt(String srcData, String key, String ivs) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(srcData)
                || JtlwCheckVariateUtils.getInstance().isEmpty(key)
                || JtlwCheckVariateUtils.getInstance().isEmpty(ivs)) {
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return new String(cipher.doFinal(Base64.getEncoder().encode(srcData.getBytes())), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密字符串
     *
     * @param srcData 字符串
     * @param key     加密的key
     * @param ivs     加密解密的算法参数
     * @return 返回加密后字符串，失败返回空null
     */
    public String decrypt(String srcData, String key, String ivs) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(srcData)
                || JtlwCheckVariateUtils.getInstance().isEmpty(key)
                || JtlwCheckVariateUtils.getInstance().isEmpty(ivs)) {
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(srcData.getBytes())), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }


}
