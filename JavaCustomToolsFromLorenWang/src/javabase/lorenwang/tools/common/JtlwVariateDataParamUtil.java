package javabase.lorenwang.tools.common;

import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能作用：变量数据格式化
 * 创建时间：2019-01-28 下午 20:02:56
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 格式化double变量的小数部分为指定数量--paramsDoubleToNum(data,num)
 * 格式化长整形到指定位数--paramsLongToNum(time,num)
 * 除去末尾的0字符操作--clearEndZeroAndParamsForDouble(time,num)
 * 去掉回车换行符--clearStringBlank(str)
 * 数组转集合--paramesArrayToList(arrays)
 * 将map的所有key值转成集合--paramsHashMapKeyToArrayList(map)
 * 生成一个范围随机数--generateRandom(min,max)
 * 获取首字母的拼音--getFirstPinYin(input)
 * 汉字转为拼音--getPinYin(input)
 * 布尔值转int值--booleanToInt(value)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwVariateDataParamUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwVariateDataParamUtil optionUtils;

    /**
     * 私有构造
     */
    private JtlwVariateDataParamUtil() {
    }

    public static JtlwVariateDataParamUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwVariateDataParamUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwVariateDataParamUtil();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 格式化double变量的小数部分为指定数量
     *
     * @param data double 变量数据
     * @param num  小数点位数
     * @return 返回格式化后字符串，小数点部分为指定数量
     */
    public String paramsDoubleToNum(Double data, int num) {
        StringBuilder decimalsPattern = new StringBuilder(".");
        for (int i = 0; i < num; i++) {
            decimalsPattern.append("0");
        }
        if (data == null || data == 0) {
            return "0" + decimalsPattern.toString();
        }
        DecimalFormat df = new DecimalFormat("###,###" + decimalsPattern.toString());
        if (data > 0 && data < 1) {
            return "0" + df.format(data);
        } else {
            return df.format(data);
        }
    }

    /**
     * 格式化长整形到指定位数
     *
     * @param time 时间
     * @param num  位数
     * @return 格式化后数据
     */
    public Long paramsLongToNum(Long time, Integer num) {
        if (time != null && num != null) {
            int length = String.valueOf(time).length();
            int compareTo = num.compareTo(length);
            if (compareTo == 0) {
                return time;
            } else if (compareTo < 0) {
                return Double.valueOf(time / Math.pow(10, (length - num))).longValue();
            } else {
                return Double.valueOf(time * Math.pow(10, (length - num))).longValue();
            }
        }
        return 0L;
    }

    /**
     * 除去末尾的0 字符操作
     *
     * @param doubleNum     传入所要格式化的值
     * @param maxDecimalNum 所保留的最大的非0的小数点后的位数
     * @return 去除后操作
     */
    public String clearEndZeroAndParamsForDouble(Double doubleNum, Integer maxDecimalNum) {
        return clearEndZeroAndParamsForDouble(paramsDoubleToNum(doubleNum, maxDecimalNum));
    }

    /**
     * 除去末尾的0 字符操作
     *
     * @param str 要格式化的字符串
     * @return 去除后操作
     */
    public String clearEndZeroAndParamsForDouble(String str) {
        if (str.indexOf(".") > 0) {
            str = str.replaceAll("\\.0+$", "");//去掉多余的0
            if (str.contains("\\.\\d*0*$")) {
                str = str.replaceAll("0+$", "");//去掉多余的0
            }
        }
        return str;
    }

    /**
     * 去掉回车换行符
     *
     * @param str 原始字符串
     * @return 去掉后字符串
     */
    public String clearStringBlank(String str) {
        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        } else {
            return str;
        }
    }

    /**
     * 数组转集合
     *
     * @param arrays 数组
     * @param <T>    泛型
     * @return 集合数据
     */
    public <T> List<T> paramesArrayToList(T[] arrays) {
        List<T> list = new ArrayList<>();
        if (arrays != null) {
            list.addAll(Arrays.asList(arrays));
        }
        return list;
    }

    /**
     * 将map的所有key值转成集合
     *
     * @param map map数据集合
     * @param <T> 泛型
     * @param <K> 泛型
     * @return 集合
     */
    public <K, T> List<K> paramsHashMapKeyToArrayList(Map<K, List<T>> map) {
        List<K> list = new ArrayList<>();
        if (map == null) {
            return list;
        }
        for (Map.Entry<K, List<T>> kListEntry : map.entrySet()) {
            list.add(kListEntry.getKey());
        }
        return list;
    }

    /**
     * 生成一个范围随机数
     *
     * @param min 最小数据
     * @param max 最大数值
     * @return 生成后数据
     */
    public long generateRandom(long min, long max) {
        return min + (((long) (new Random().nextDouble() * (max - min))));
    }

    /**
     * 获取首字母的拼音
     *
     * @param source 文本字符串
     * @return 首字母拼音
     */
    public char getFirstPinYin(String source) {
        if (JtlwCheckVariateUtil.getInstance().isEmpty(source)) {
            return '#';
        }
        Locale newChina = new Locale("zh", "HANS", "CN");//这部分是我做的修复，解决HTC兼容性问题
        if (!(Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)
                || Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINESE)
                || Arrays.asList(Collator.getAvailableLocales()).contains(newChina))
                || (source != null && !"".equals(source) && (source.charAt(0) <= 'Z' && source.charAt(0) >= 'A') || (Objects.requireNonNull(source).charAt(0) <= 'z' && source.charAt(0) >= 'a'))) {
            return source.length() > 0 ? source.toLowerCase().charAt(0) : '#';
        }
        ArrayList<JtlwHanziToPinyinUtil.Token> tokens = JtlwHanziToPinyinUtil.getInstance().get(source);
        if (tokens == null || tokens.size() == 0) {
            return source.toLowerCase().charAt(0);
        }
        JtlwHanziToPinyinUtil.Token token = tokens.get(0);
        if (token.type == JtlwHanziToPinyinUtil.Token.PINYIN) {
            return token.target.toLowerCase().charAt(0);
        }
        return '#';
    }

    /**
     * 汉字转为拼音
     *
     * @param input 要转成拼音的汉字
     * @return 返回拼音字符串
     */
    public String getPinYin(String input) {
        ArrayList<JtlwHanziToPinyinUtil.Token> tokens = JtlwHanziToPinyinUtil.getInstance().get(input);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (JtlwHanziToPinyinUtil.Token token : tokens) {
                if (JtlwHanziToPinyinUtil.Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 布尔值转int值
     *
     * @param value 布尔值
     * @return 为空或者falese的时候返回0，其他返回1
     */
    public int booleanToInt(Boolean value) {
        return (value == null || !value) ? 0 : 1;
    }

}
