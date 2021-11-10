package javabase.lorenwang.tools.dataConversion;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：进制转换工具类
 * 创建时间：2019-01-28 下午 14:21:38
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 十进制转二进制--decimal10To2(num)
 * 十进制转八进制--decimal10To8(num)
 * 十进制转十六进制--decimal10To16(num)
 * 十进制转三十二进制--decimal10To32(num)
 * 十进制转六十四进制--decimal10To64(num)
 * 八进制转二进制--decimal8To2(num)
 * 八进制转十进制--decimal8To10(num)
 * 八进制转16进制--decimal8To16(num)
 * 八进制转三十二进制--decimal8To32(num)
 * 八进制转六十四进制--decimal8To64(num)
 * 二进制转八进制--decimal2To8(num)
 * 二进制转十进制--decimal2To10(num)
 * 二进制转16进制--decimal2To16(num)
 * 二进制转三十二进制--decimal2To32(num)
 * 十六进制转二进制--decimal16To2(num)
 * 十六进制转八进制--decimal16To8(num)
 * 十六进制转二进制--decimal16To10(num)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwDecimalConvertUtil {
    //小写字母表
    private final char[] ALPHABET_LOWER_CASE = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    //大写字母表
    private final char[] ALPHABET_UPPER_CASE = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final String TAG = getClass().getName();
    private static volatile JtlwDecimalConvertUtil optionUtils;

    /**
     * 私有构造
     */
    private JtlwDecimalConvertUtil() {
    }

    public static JtlwDecimalConvertUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwDecimalConvertUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwDecimalConvertUtil();
                }
            }
        }
        return optionUtils;
    }


    /*--------------------------------十进制转其他进制--------------------------------*/

    /**
     * 十进制转二进制
     *
     * @param num 十进制数据
     * @return 二进制数据
     */
    public Integer decimal10To2(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            String result = decimalToAssign(num, 2, null);
            if (JtlwCheckVariateUtil.getInstance().isInteger(result)) {
                return Integer.valueOf(result);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 十进制转八进制
     *
     * @param num 十进制数据
     * @return 八进制数据
     */
    public Integer decimal10To8(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            String result = decimalToAssign(num, 8, null);
            if (JtlwCheckVariateUtil.getInstance().isInteger(result)) {
                return Integer.valueOf(result);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 十进制转十六进制
     *
     * @param num 十进制数据
     * @return 十六进制数据
     */
    public String decimal10To16(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 16, null);
        }
        return null;
    }

    /**
     * 十进制转三十二进制
     *
     * @param num 十进制数据
     * @return 三十二进制数据
     */
    public String decimal10To32(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 32, null);
        }
        return null;
    }

    /**
     * 十进制转六十四进制
     *
     * @param num 十进制数据
     * @return 六十四进制数据
     */
    public String decimal10To64(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 64, null);
        }
        return null;
    }


    /*----------------------------------------八进制转其他进制---------------------------------------*/

    /**
     * 八进制转二进制
     *
     * @param num 八进制数据
     * @return 二进制数据
     */
    public Integer decimal8To2(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            char[] chars = String.valueOf(num).toCharArray();
            StringBuilder result = new StringBuilder();
            for (char item : chars) {
                result.append(decimalToAssign(item - 48, 2, 3));
            }
            if (JtlwCheckVariateUtil.getInstance().isInteger(result.toString())) {
                return Integer.valueOf(result.toString());
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 八进制转十进制
     *
     * @param num 八进制数据
     * @return 十进制数据
     */
    public Integer decimal8To10(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            char[] chars = String.valueOf(num).toCharArray();
            int length = chars.length;
            if (length == 0) {
                return null;
            }
            int result = 0;
            for (int i = 0; i < length; i++) {
                result = (chars[i] - 48) * (int) Math.pow(8, length - i - 1) + result;
            }
            return result;
        }
        return null;
    }

    /**
     * 八进制转16进制
     *
     * @param num 八进制数据
     * @return 十六进制数据
     */
    public String decimal8To16(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To16(num);
        }
        return null;
    }

    /**
     * 八进制转三十二进制
     *
     * @param num 八进制数据
     * @return 三十二进制数据
     */
    public String decimal8To32(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To32(num);
        }
        return null;
    }

    /**
     * 八进制转六十四进制
     *
     * @param num 八进制数据
     * @return 六十四进制数据
     */
    public String decimal8To64(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To64(num);
        }
        return null;
    }


    /*----------------------------------------*****二进制转其他进制****************************************/

    /**
     * 二进制转八进制
     *
     * @param num 二进制数据
     * @return 八进制数据
     */
    public Integer decimal2To8(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            String result = decimal2ToOther(num, 8, 3);
            if (result != null && JtlwCheckVariateUtil.getInstance().isInteger(result)) {
                return Integer.valueOf(result);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 二进制转十进制
     *
     * @param num 二进制数据
     * @return 十进制数据
     */
    public Integer decimal2To10(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            String result = decimal2ToOther(num, 10, null);
            if (result != null && JtlwCheckVariateUtil.getInstance().isInteger(result)) {
                return Integer.valueOf(result);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 二进制转16进制
     *
     * @param num 二进制数据
     * @return 十六进制数据
     */
    public String decimal2To16(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            return decimal2ToOther(num, 16, 4);
        }
        return null;
    }

    /**
     * 二进制转三十二进制
     *
     * @param num 二进制数据
     * @return 三十二进制数据
     */
    public String decimal2To32(Integer num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            return decimal2ToOther(num, 32, 5);
        }
        return null;
    }


    /*--------------------------------十六进制转其他进制--------------------------------*/

    /**
     * 十六进制转二进制
     *
     * @param num 十六进制数据
     * @return 二进制数据
     */
    public Integer decimal16To2(String num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            num = num.toLowerCase();
            char[] chars = num.toCharArray();
            int length = chars.length;
            if (length == 0) {
                return null;
            }
            StringBuilder result = new StringBuilder();
            Character character;
            for (char aChar : chars) {
                character = aChar;
                if (character.compareTo('0') >= 0 && character.compareTo('9') <= 0) {
                    result.insert(0, decimalToAssign(character - 48, 2, 4));
                } else if (character.compareTo('a') >= 0 && character.compareTo('f') <= 0) {
                    result.insert(0, decimalToAssign(character - 87, 2, 4));
                }
            }
            if (JtlwCheckVariateUtil.getInstance().isInteger(result.toString())) {
                return Integer.valueOf(result.toString());
            }
        }
        return null;
    }

    /**
     * 十六进制转八进制
     *
     * @param num 十六进制数据
     * @return 八进制数据
     */
    public Integer decimal16To8(String num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            Integer integer = decimal16To2(num);
            return decimal2To8(integer);
        }
        return null;
    }

    /**
     * 十六进制转二进制
     *
     * @param num 十六进制数据
     * @return 二进制数据
     */
    public Integer decimal16To10(String num) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(num)) {
            num = num.toLowerCase();
            char[] chars = num.toCharArray();
            int length = chars.length;
            if (length == 0) {
                return null;
            }
            int result = 0;
            Character character;
            for (int i = 0; i < length; i++) {
                character = chars[i];
                if (character.compareTo('0') >= 0 && character.compareTo('9') <= 0) {
                    result += (character - 48) * (int) Math.pow(16, i);
                } else if (character.compareTo('a') >= 0 && character.compareTo('f') <= 0) {
                    result += (character - 87) * (int) Math.pow(16, i);
                }
            }
            return result;
        }
        return null;
    }


    /*----------------------------------------私有转换方法----------------------------------------*/

    /**
     * 指定目标进制转换
     *
     * @param num       要转换的数
     * @param assign    指定的其他进制数，转换的目标进制数
     * @param minLength 要返回的最小数据长度
     * @return 转换后的进制数据
     */
    private String decimalToAssign(int num, int assign, Integer minLength) {
        StringBuilder result = new StringBuilder();
        int remainder;
        while (num >= assign) {
            remainder = num % assign;
            if (remainder >= 10) {
                result.insert(0, getAssignDecimalStr(assign, remainder - 10));
            } else {
                result.insert(0, remainder);
            }
            num /= assign;
        }
        //拼接最后一个
        if (num >= 10) {
            result.insert(0, getAssignDecimalStr(assign, num - 10));
        } else {
            result.insert(0, num);
        }
        int diff;
        //判断位数是否为空
        if (minLength == null) {
            return result.toString();
        } else {
            //获取和要返回的位数相差的位数，大于0则代表着位于大于要返回的，不用管
            diff = result.length() - minLength;
            if (diff < 0) {
                diff = Math.abs(diff);
                for (int i = 0; i < diff; i++) {
                    result.insert(0, "0");
                }
            }
        }
//
//        //判断位数是否为空
//        if (minLength == null && maxLength == null) {
//            return result.toString();
//        } else if (minLength != null && maxLength == null) {
//            //获取和要返回的位数相差的位数，大于0则代表着位于大于要返回的，不用管
//            diff = result.length() - minLength;
//            if (diff < 0) {
//                diff = Math.abs(diff);
//                for (int i = 0; i < diff; i++) {
//                    result.insert(0, "0");
//                }
//            }
//        } else if (minLength == null && maxLength != null) {
//            //获取和要返回的位数相差的位数，大于0则代表着位于大于要返回的，要截取掉
//            diff = result.length() - maxLength;
//            if (diff > 0) {
//                result = new StringBuilder(result.substring(diff));
//            }
//        } else {
//            //判断最小是否小于最大,最小大于最大的话做交换
//            if (minLength > maxLength) {
//                int length = minLength;
//                minLength = maxLength;
//                maxLength = length;
//            }
//
//            //最大和最小都不为空那么只要返回这个区间的就行
//            diff = result.length() - minLength;
//            if (diff < 0) {
//                diff = Math.abs(diff);
//                for (int i = 0; i < diff; i++) {
//                    result.insert(0, "0");
//                }
//            }
//            diff = result.length() - maxLength;
//            if (diff > 0) {
//                result = new StringBuilder(result.substring(diff));
//            }
//        }
        return result.toString();
    }

    /**
     * 二进制转其他进制
     *
     * @param num      二进制数
     * @param assign   其他进制
     * @param splitNum 拆分每段数量
     * @return 其他进制数据
     */
    private String decimal2ToOther(int num, int assign, Integer splitNum) {
        char[] chars = String.valueOf(num).toCharArray();
        int length = chars.length;
        if (length == 0) {
            return null;
        }
        if (splitNum == null) {
            splitNum = length;
        }
        StringBuilder result = new StringBuilder();
        int value = 0;
        int posi;
        for (int i = length - 1; i >= 0; i--) {
            posi = (length - i - 1) % splitNum;
            value += (chars[i] - 48) * (int) Math.pow(2, posi);
            if (posi == splitNum - 1) {
                if (value >= 10) {
                    result.insert(0, getAssignDecimalStr(assign, value - 10));
                } else {
                    result.insert(0, value);
                }
                value = 0;
            } else if (i == 0) {
                if (value >= 10) {
                    result.insert(0, getAssignDecimalStr(assign, value - 10));
                } else {
                    result.insert(0, value);
                }
                value = 0;
            }
        }
        return result.toString();
    }

    /**
     * 获取目标进制所对应的字符串
     *
     * @param assign      目标进制
     * @param strPosition 目标进制在字符串当中的位置
     * @return 进制字符串
     */
    private String getAssignDecimalStr(int assign, int strPosition) {
        Character result;
        switch (assign) {
            case 10://目标是十进制就不单独处理了
                return String.valueOf(strPosition + 10);
            case 32://32进制不含ILOU字符，同时为大写
                result = ALPHABET_UPPER_CASE[strPosition];
                //判断是否是不含的字符串
                if (result.compareTo('I') <= 0) {
                    return result.toString();
                }
                result++;
                if (result.compareTo('L') <= 0) {
                    return result.toString();
                }
                result++;
                if (result.compareTo('O') <= 0) {
                    return result.toString();
                }
                result++;
                if (result.compareTo('U') <= 0) {
                    return result.toString();
                }
                result++;
                return result.toString();
            case 64://数字 + 小写字母 + 大写字母
                if (strPosition > 26) {
                    return String.valueOf(ALPHABET_UPPER_CASE[strPosition - 26]);
                } else {
                    return String.valueOf(ALPHABET_LOWER_CASE[strPosition]);
                }
            default:
                return String.valueOf(ALPHABET_LOWER_CASE[strPosition]);
        }
    }


}
