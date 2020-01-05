package javabase.lorenwang.tools.common;

import java.util.UUID;

/**
 * 创建时间：2019-01-29 下午 16:15:18
 * 创建人：王亮（Loren wang）
 * 功能作用：通用方法
 * 思路：
 * 方法：1、uuid产生器
 * 2、byte数组转字符串
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwCommonUtils {
    private final String TAG = getClass().getName();
    private static volatile JtlwCommonUtils optionUtils;

    /**
     * 私有构造
     */
    private JtlwCommonUtils() {
    }

    public static JtlwCommonUtils getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwCommonUtils.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwCommonUtils();
                }
            }
        }
        return optionUtils;
    }

    /**
     * uuid产生器
     *
     * @param isRemoveSpecialChar 是否移除特殊字符，中划线
     */
    public String generateUuid(boolean isRemoveSpecialChar) {
        String uuid = UUID.randomUUID().toString();
        if (isRemoveSpecialChar) {
            uuid = uuid.replaceAll("-", "");
        }
        return uuid;
    }

    /**
     * byte数组转字符串
     *
     * @param src 要读取byte数组
     * @return 转换后字符串
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte aSrc : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式
            hv = Integer.toHexString(aSrc & 0xFF);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
