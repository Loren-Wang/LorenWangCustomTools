package javabase.lorenwang.tools.dataConversion;

import javabase.lorenwang.tools.common.CheckVariateUtils;

/**
 * 创建时间：2019-01-28 下午 14:21:38
 * 创建人：王亮（Loren wang）
 * 功能作用：进制转换工具类
 * 思路：
 * 方法：1、十进制转二进制
 * 2、十进制转八进制
 * 3、十进制转十六进制
 * 4、十进制转三十二进制
 * 5、十进制转六十二进制
 * 6、八进制转二进制
 * 7、八进制转十进制
 * 8、八进制转16进制
 * 9、八进制转三十二进制
 * 10、八进制转六十二进制
 * 11、二进制转八进制
 * 12、二进制转十进制
 * 13、二进制转16进制
 * 14、二进制转三十二进制
 * 15、十六进制转二进制
 * 16、十六进制转八进制
 * 17、十六进制转二进制
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class DecimalConvertUtils {
    private final String TAG = "DecimalConvertUtils";
    private static DecimalConvertUtils baseUtils;
    //小写字母表
    private final char[] ALPHABET_LOWER_CASE = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    //大写字母表
    private final char[] ALPHABET_UPPER_CASE = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private DecimalConvertUtils() {
    }

    public static DecimalConvertUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new DecimalConvertUtils();
        }
        return (DecimalConvertUtils) baseUtils;
    }


    /***********************************十进制转其他进制*********************************************/

    /**
     * 十进制转二进制
     *
     * @param num
     * @return
     */
    public Integer decimal10To2(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            String result = decimalToAssign(num, 2, null, null);
            if (CheckVariateUtils.getInstance().isInteger(result)) {
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
     * @param num
     * @return
     */
    public Integer decimal10To8(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            String result = decimalToAssign(num, 8, null, null);
            if (CheckVariateUtils.getInstance().isInteger(result)) {
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
     * @param num
     * @return
     */
    public String decimal10To16(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 16, null, null);
        }
        return null;
    }

    /**
     * 十进制转三十二进制
     *
     * @param num
     * @return
     */
    public String decimal10To32(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 32, null, null);
        }
        return null;
    }

    /**
     * 十进制转六十二进制
     *
     * @param num
     * @return
     */
    public String decimal10To62(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            return decimalToAssign(num, 62, null, null);
        }
        return null;
    }


    /****************************************八进制转其他进制****************************************/

    /**
     * 八进制转二进制
     *
     * @param num
     * @return
     */
    public Integer decimal8To2(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            char[] chars = String.valueOf(num).toCharArray();
            StringBuffer result = new StringBuffer();
            for (char item : chars) {
                result.append(decimalToAssign(item - 48, 2, 3, null));
            }
            if (CheckVariateUtils.getInstance().isInteger(result.toString())) {
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
     * @param num
     * @return
     */
    public Integer decimal8To10(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
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
     * @param num
     * @return
     */
    public String decimal8To16(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To16(num);
        }
        return null;
    }

    /**
     * 八进制转三十二进制
     *
     * @param num
     * @return
     */
    public String decimal8To32(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To32(num);
        }
        return null;
    }

    /**
     * 八进制转六十二进制
     *
     * @param num
     * @return
     */
    public String decimal8To62(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            num = decimal8To10(num);
            return decimal10To62(num);
        }
        return null;
    }


    /****************************************二进制转其他进制****************************************/

    /**
     * 二进制转八进制
     *
     * @param num
     * @return
     */
    public Integer decimal2To8(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            String result = decimal2ToOther(num, 8, 3);
            if (CheckVariateUtils.getInstance().isInteger(result)) {
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
     * @param num
     * @return
     */
    public Integer decimal2To10(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            String result = decimal2ToOther(num, 10, null);
            if (CheckVariateUtils.getInstance().isInteger(result)) {
                return Integer.valueOf(result);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * '二进制转16进制
     *
     * @param num
     * @return
     */
    public String decimal2To16(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            return decimal2ToOther(num, 16, 4);
        }
        return null;
    }

    /**
     * '二进制转三十二进制
     *
     * @param num
     * @return
     */
    public String decimal2To32(Integer num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            return decimal2ToOther(num, 32, 5);
        }
        return null;
    }


    /**********************************十六进制转其他进制********************************************/

    /**
     * 十六进制转二进制
     *
     * @param num
     * @return
     */
    public Integer decimal16To2(String num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            num = num.toLowerCase();
            char[] chars = num.toCharArray();
            int length = chars.length;
            if (length == 0) {
                return null;
            }
            String result = "";
            Character character;
            for (int i = 0; i < length; i++) {
                character = chars[i];
                if(character.compareTo('0') >= 0 && character.compareTo('9') <= 0){
                    result = decimalToAssign(character - 48,2,4,null) + result;
                }else   if(character.compareTo('a') >= 0 && character.compareTo('f') <= 0){
                    result = decimalToAssign(character - 87,2,4,null) + result;
                }
            }
            if(CheckVariateUtils.getInstance().isInteger(result)){
                return Integer.valueOf(result);
            }
        }
        return null;
    }

    /**
     * 十六进制转八进制
     *
     * @param num
     * @return
     */
    public Integer decimal16To8(String num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
            Integer integer = decimal16To2(num);
            return decimal2To8(integer);
        }
        return null;
    }

    /**
     * 十六进制转二进制
     *
     * @param num
     * @return
     */
    public Integer decimal16To10(String num) {
        if (!CheckVariateUtils.getInstance().isEmpty(num)) {
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
                if(character.compareTo('0') >= 0 && character.compareTo('9') <= 0){
                    result += (character - 48) * (int) Math.pow(16, i);
                }else   if(character.compareTo('a') >= 0 && character.compareTo('f') <= 0){
                    result += (character - 87) * (int) Math.pow(16, i);
                }
            }
            return result;
        }
        return null;
    }




    /********************************************私有转换方法***************************************/

    /**
     * 指定目标进制转换
     *
     * @param num       要转换的数
     * @param assign    指定的其他进制数，转换的目标进制数
     * @param minLength 要返回的最小数据长度
     * @return
     */
    private String decimalToAssign(int num, int assign, Integer minLength, Integer maxLength) {
        String result = "";
        int remainder;
        while (num >= assign) {
            remainder = num % assign;
            if (remainder >= 10) {
                result = getAssignDecimalStr(assign, remainder - 10) + result;
            } else {
                result = remainder + result;
            }
            num /= assign;
        }
        //拼接最后一个
        if (num >= 10) {
            result = getAssignDecimalStr(assign, num - 10) + result;
        } else {
            result = num + result;
        }
        int diff;
        //判断位数是否为空
        if (minLength == null && maxLength == null) {
            return result;
        } else if (minLength != null && maxLength == null) {
            //获取和要返回的位数相差的位数，大于0则代表着位于大于要返回的，不用管
            diff = result.length() - minLength;
            if (diff < 0) {
                diff = Math.abs(diff);
                for (int i = 0; i < diff; i++) {
                    result = "0" + result;
                }
            }
        } else if (minLength == null && maxLength != null) {
            //获取和要返回的位数相差的位数，大于0则代表着位于大于要返回的，要截取掉
            diff = result.length() - maxLength;
            if (diff > 0) {
                result = result.substring(diff);
            }
        } else {
            //判断最小是否小于最大,最小大于最大的话做交换
            if (minLength > maxLength) {
                int length = minLength;
                minLength = maxLength;
                maxLength = length;
            }

            //最大和最小都不为空那么只要返回这个区间的就行
            diff = result.length() - minLength;
            if (diff < 0) {
                diff = Math.abs(diff);
                for (int i = 0; i < diff; i++) {
                    result = "0" + result;
                }
            }
            diff = result.length() - maxLength;
            if (diff > 0) {
                result = result.substring(diff);
            }
        }
        return result;
    }

    /**
     * 二进制转其他进制
     *
     * @param num      二进制数
     * @param assign   其他进制
     * @param splitNum 拆分每段数量
     * @return
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
        String result = "";
        int value = 0;
        int posi = 0;
        for (int i = length - 1; i >= 0; i--) {
            posi = (length - i - 1) % splitNum;
            value += (chars[i] - 48) * (int) Math.pow(2, posi);
            if (posi == splitNum - 1) {
                if (value >= 10) {
                    result = getAssignDecimalStr(assign, value - 10) + result;
                } else {
                    result = value + result;
                }
                value = 0;
            } else if (i == 0) {
                if (value >= 10) {
                    result = getAssignDecimalStr(assign, value - 10) + result;
                } else {
                    result = value + result;
                }
                value = 0;
            }
        }
        return result;
    }

    /**
     * 获取目标进制所对应的字符串
     *
     * @param assign  目标进制
     * @param strPosi 目标进制在字符串当中的位置
     * @return
     */
    private String getAssignDecimalStr(int assign, int strPosi) {
        Character result = null;
        switch (assign) {
            case 10://目标是十进制就不单独处理了
                return String.valueOf(strPosi + 10);
            case 32://32进制不含ILOU字符，同时为大写
                result = ALPHABET_UPPER_CASE[strPosi];
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
            case 62://数字 + 小写字母 + 大写字母
                if (strPosi > 26) {
                    return String.valueOf(ALPHABET_UPPER_CASE[strPosi - 26]);
                } else {
                    return String.valueOf(ALPHABET_LOWER_CASE[strPosi]);
                }
            default:
                return String.valueOf(ALPHABET_LOWER_CASE[strPosi]);
        }
    }




}
