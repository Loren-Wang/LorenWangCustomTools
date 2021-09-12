package javabase.lorenwang.tools.dataConversion;

import org.jetbrains.annotations.NotNull;

import javabase.lorenwang.tools.JtlwMatchesRegularCommon;

/**
 * 功能作用：编码转换
 * 创建时间：2020-12-12 9:34 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 中文转unicode编码--chineseToUnicode(dataStr)
 * unicode编码转中文--unicodeToChinese(dataStr)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwCodeConversionUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwCodeConversionUtil optionsInstance;

    private JtlwCodeConversionUtil() {
    }

    public static JtlwCodeConversionUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (JtlwCodeConversionUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new JtlwCodeConversionUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 中文转unicode编码
     *
     * @param dataStr 原始数据
     * @return 转换后数据
     */
    public String chineseToUnicode(@NotNull String dataStr) {
        StringBuilder result = new StringBuilder();
        String item;
        for (char cha : dataStr.toCharArray()) {
            item = Integer.toHexString(cha);
            result.append("\\u");
            if (item.length() <= 2) {
                result.append("00");
            }
            result.append(item);
        }
        return result.toString();
    }

    /**
     * unicode编码转中文
     *
     * @param dataStr 原始数据
     * @return 编码后数据
     */
    public String unicodeToChinese(@NotNull String dataStr) {
        int start = 0;
        int end;
        for (String code : JtlwMatchesRegularCommon.getRegexResultList(dataStr,
                JtlwMatchesRegularCommon.EXP_CODE_CONVERSION_UNICODE, false)) {
            //16进制parse整形字符串
            dataStr = dataStr.replace(code, String.valueOf((char) Integer.parseInt(code.substring(2), 16)));
        }
        return dataStr;
    }
}
