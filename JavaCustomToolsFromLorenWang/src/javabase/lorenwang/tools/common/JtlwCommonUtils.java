package javabase.lorenwang.tools.common;

import java.util.UUID;

/**
 * 创建时间：2019-01-29 下午 16:15:18
 * 创建人：王亮（Loren wang）
 * 功能作用：通用方法
 * 思路：
 * 方法：1、uuid产生器
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwCommonUtils {
    private final String TAG = "CommonUtils";
    private static JtlwCommonUtils baseUtils;
    private JtlwCommonUtils(){}
    public static JtlwCommonUtils getInstance(){
        if(baseUtils == null){
            baseUtils = new JtlwCommonUtils();
        }
        return (JtlwCommonUtils) baseUtils;
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
}
