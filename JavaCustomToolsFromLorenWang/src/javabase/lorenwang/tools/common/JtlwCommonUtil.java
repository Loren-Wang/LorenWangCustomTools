package javabase.lorenwang.tools.common;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能作用：通用方法
 * 初始注释时间： 2019-01-29 下午 16:15:18
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * uuid产生器--generateUuid(isRemoveSpecialChar)
 * byte数组转字符串--bytesToHexString(src)
 * 字符串转驼峰格式--toCamelCase(data)
 * 将字符串分离(以大写字母为分隔添加位置)--toSeparatedCase(data,separated)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwCommonUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwCommonUtil optionUtils;

    /**
     * 私有构造
     */
    private JtlwCommonUtil() {
    }

    public static JtlwCommonUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwCommonUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwCommonUtil();
                }
            }
        }
        return optionUtils;
    }

    /**
     * uuid产生器
     * uuid带中划线长度-------36
     * uuid不带中划线长度-----32
     *
     * @param isRemoveSpecialChar 是否移除特殊字符，中划线
     * @return 返回根据条件处理的uuid字符串
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

    /**
     * 字符串转驼峰格式
     *
     * @param data 字符串数据
     * @return 驼峰格式字符串
     */
    public String toCamelCase(String data) {
        if (data != null && !data.isEmpty()) {
            Pattern pattern = Pattern.compile("[-_].");
            Matcher matcher = pattern.matcher(data);
            String find;
            while (matcher.find()) {
                find = matcher.group();
                data = data.replace(find, find.substring(1).toUpperCase());
            }
            return data;
        }
        return null;
    }

    /**
     * 将字符串分离(以大写字母为分隔添加位置)
     *
     * @param data      驼峰字符串
     * @param separated 分离字符
     * @return 分离后字符
     */
    public String toSeparatedCase(String data, String separated) {
        if (data != null && !data.isEmpty() && separated != null && !separated.isEmpty()) {
            Pattern pattern = Pattern.compile("[A-Z]");
            Matcher matcher = pattern.matcher(data);
            String find;
            while (matcher.find()) {
                find = matcher.group();
                data = data.replace(find, separated + find);
            }
            return data;
        }
        return null;
    }
}
