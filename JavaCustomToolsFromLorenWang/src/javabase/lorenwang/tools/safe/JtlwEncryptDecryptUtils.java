package javabase.lorenwang.tools.safe;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：加密解密工具类
 * 初始注释时间： 2020/12/11 6:24 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwEncryptDecryptUtils {
    private static volatile JtlwEncryptDecryptUtils optionUtils;

    /**
     * 加密方式
     */
    private final String KEY_ALGORITHM = "AES";

    /**
     * 默认的加密算法
     */
    private final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

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
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(srcData.getBytes()));
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
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            byte[] bytes = Base64.getDecoder().decode(srcData);
            return new String(cipher.doFinal(bytes));
        } catch (Exception e) {
            return "";
        }
    }


}
