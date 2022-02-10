package javabase.lorenwang.tools.common;


import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import javabase.lorenwang.tools.JtlwLogUtil;
import javabase.lorenwang.tools.JtlwMatchesRegularCommon;


/**
 * 功能作用：变量检测工具类
 * 创建时间：2019-01-28 下午 14:02:18
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 判断变量是否为空--isEmpty(T)
 * 判断变量是否为不为空--isNotEmpty(T)
 * 判断变量集合当中是否存在空--isHaveEmpty(Object...)
 * 判断是否符合指定的正则表达式--matches(str, patternStr)
 * 判断字符串是否是整型--isInteger(str)
 * 判断字符串是否是长整型--isLong(str)
 * 判断字符串是否是浮点数--isDouble(str)
 * 字符串是否超过指定长度--isOverLength(str,len)
 * Double类型是否超过指定长度(小数点前位数)--isOverLength(d, len)
 * 判断字符串是否在列表中--isInList(item, list)
 * 判断对象是否在数组中--isInArray(item, T[])
 * 检查传入的路径是否是图片--checkIsImage(path)
 * 检查传入的路径是否是视频--checkIsVideo(path)
 * 检查文件是否存在--checkFileIsExit(filePath)
 * 检测文件是否是图片--checkFileIsImage(filePath)
 * 检测国内身份证号是否正确，支持15位至18位--checkChineseIdCard(idCard)\
 * 通过身份证号检测年龄是否超过限制--checkAgeMoreThanLimitByIdCard(idCard,limit, judgeYear)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class JtlwCheckVariateUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwCheckVariateUtil optionUtils;

    /**
     * 私有构造
     */
    private JtlwCheckVariateUtil() {
    }

    public static JtlwCheckVariateUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwCheckVariateUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwCheckVariateUtil();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 判断变量是否为空
     *
     * @param <T> 变量泛型
     * @param str String
     * @return boolean
     */
    public <T> boolean isEmpty(T str) {
        if (str instanceof String) {
            return "".equals(str);
        } else {
            return str == null;
        }
    }

    /**
     * 判断变量是否为不为空
     *
     * @param <T> 变量泛型
     * @param str String
     * @return boolean
     */
    public <T> boolean isNotEmpty(T str) {
        return !isEmpty(str);
    }

    /**
     * 判断变量集合当中是否存在空
     *
     * @param objects 集合数据
     * @return 存在空返回true
     */
    public boolean isHaveEmpty(Object... objects) {
        for (Object object : objects) {
            if (isEmpty(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否符合指定的正则表达式 eg: [^0-9A-Za-z]
     *
     * @param str        String
     * @param patternStr String
     * @return boolean
     */
    public boolean matches(String str, String patternStr) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches(patternStr);
    }

    /**
     * 判断字符串是否是整型
     *
     * @param str String
     * @return boolean
     */
    public boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是长整型
     *
     * @param str String
     * @return boolean
     */
    public boolean isLong(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Long.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     *
     * @param str String
     * @return boolean
     */
    public boolean isDouble(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Double.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字符串是否超过指定长度
     *
     * @param str String
     * @param len int
     * @return boolean
     */
    public boolean isOverLength(String str, int len) {
        if (isEmpty(str)) {
            return false;
        }
        return str.length() > len;
    }

    /**
     * Double类型是否超过指定长度(小数点前位数)
     *
     * @param d   Double
     * @param len int
     * @return boolean
     */
    public boolean isOverLength(Double d, int len) {
        if (d == null) {
            return false;
        }
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setGroupingUsed(false); // 是否对结果分组（即使用","分组）
        formatter.setMaximumFractionDigits(0); // 小数位数最大值
        formatter.setMinimumFractionDigits(0); // 小数位数最小值
        return formatter.format(d.doubleValue()).length() > len;
    }

    /**
     * 判断字符串是否在列表中
     *
     * @param <T>  变量泛型
     * @param item item数据
     * @param list 列表
     * @return 存在返回true
     */
    public <T> boolean isInList(T item, List<T> list) {
        for (T listItem : list) {
            if (item.equals(listItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否在数组中
     *
     * @param <T>  泛型
     * @param item 对象数据
     * @param list 集合数据
     * @return 存在返回true
     */
    public <T> boolean isInArray(T item, T[] list) {
        for (T listItem : list) {
            if (item.equals(listItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查传入的路径是否是图片
     *
     * @param path 传入路径
     * @return 是图片返回true
     */
    public boolean checkIsImage(String path) {
        if (path != null) {
            if (path.length() > 4) {
                String start = path.toLowerCase().substring(path.length() - 4);
                if (start.contains(".jpg") || start.contains(".png") || start.contains(".bmp") || start.contains(".gif") || start.contains(".psd") ||
                        start.contains(".swf") || start.contains(".svg") || start.contains(".pcx") || start.contains(".dxf") || start.contains(
                        ".wmf") || start.contains(".emf") || start.contains(".lic") || start.contains(".eps") || start.contains(".tga")) {
                    return true;
                } else if (path.length() > 5) {
                    start = path.toLowerCase().substring(path.length() - 5);
                    return start.contains(".jpeg") || start.contains(".tiff") || start.contains(".heic");
                }
            }
        }

        return false;
    }

    /**
     * 检查传入的路径是否是视频
     *
     * @param path 传入路径
     * @return 是视频返回true
     */
    public boolean checkIsVideo(String path) {
        if (path != null) {
            if (path.length() > 4) {
                return path.toLowerCase().substring(path.length() - 4).contains(".mp4");
            }
        }
        return false;
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件地址
     * @return 存在返回true
     */
    public boolean checkFileIsExit(String filePath) {
        if (isEmpty(filePath)) {
            JtlwLogUtil.logUtils.logI(TAG, "被检查文件地址为空，不通过检测");
            return false;
        }
        File file = new File(filePath);
        boolean isExit = false;//文件是否存在记录
        if (file.isDirectory()) {
            JtlwLogUtil.logUtils.logI(TAG, "被检查文件为空或被检测的地址为文件夹，不通过检测");
            return false;
        }
        if (file.exists()) {
            isExit = true;
            JtlwLogUtil.logUtils.logI(TAG, "被检查文件存在");
        } else {
            JtlwLogUtil.logUtils.logI(TAG, "被检查文件不存在");
        }
        return isExit;
    }

    /**
     * 检测文件是否是图片
     *
     * @param filePath 文件地址
     * @return 是图片返回true
     */
    public boolean checkFileIsImage(String filePath) {
        if (!isEmpty(filePath)) {
            if (checkIsImage(filePath.toLowerCase())) {
                JtlwLogUtil.logUtils.logI(TAG, "被检测地址为图片地址：");
                return true;
            } else {
                JtlwLogUtil.logUtils.logI(TAG, "被检测地址为空或文件为非图片");
                return false;
            }
        } else {
            JtlwLogUtil.logUtils.logI(TAG, "被检测地址为空或文件为非图片");
            return false;
        }
    }

    /**
     * 检测国内身份证号是否正确，支持15位至18位
     *
     * @param idCard 身份证号
     * @return 0 通过，1 格式错误，2 地址编码错误，3 身份证号不合法
     */
    public int checkChineseIdCard(String idCard) {
        //身份证位数正则匹配
        if (isEmpty(idCard) || !idCard.matches(JtlwMatchesRegularCommon.ID_CARD_CHINESE)) {
            return 1;
        }

        //城市code
        String[] cityCodes = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};
        String city = idCard.substring(0, 2);
        boolean haveCode = false;
        for (String cityCode : cityCodes) {
            if (cityCode.equals(city)) {
                haveCode = true;
                break;
            }
        }
        if (!haveCode) {
            return 2;
        }

        //加权验证
        if (idCard.length() == 18) {
            Integer[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            Character[] parity = new Character[]{'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};
            int sum = 0;
            int wi;
            int ai;
            char[] chars = idCard.toCharArray();
            for (int i = 0; i < 17; i++) {
                ai = Integer.parseInt(String.valueOf(chars[i]));
                wi = factor[i];
                sum += ai * wi;
            }
            Character last = parity[sum % 11];
            if (last.compareTo(String.valueOf(idCard.charAt(17)).toLowerCase().charAt(0)) != 0) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * 通过身份证号检测年龄是否超过限制
     *
     * @param idCard    身份证号
     * @param limit     限制
     * @param judgeYear 是否单纯按照年份判断
     * @return 是则是超过限制
     */
    public boolean checkAgeMoreThanLimitByIdCard( String idCard, int limit, boolean judgeYear) {
        if (checkChineseIdCard(idCard) == 0) {
            //身份证是正常的，获取年龄
            int idTime = 0;
            int currentTime = Integer.parseInt(JtlwDateTimeUtil.getInstance().getFormatDateNowTime(JtlwDateTimeUtil.YEAR_PATTERN));
            if (idCard.length() == 18) {
                idTime = Integer.parseInt(idCard.substring(6, 10));
            } else if (idCard.length() == 15) {
                idTime = Integer.parseInt("19" + idCard.substring(6, 8));
            }
            if (judgeYear) {
                //只按照年份判断
                return currentTime - idTime >= limit;
            } else {
                //不单纯按照年份判断，是按照实际判断，先判断年份
                if (currentTime - idTime > limit) {
                    return true;
                } else if (currentTime - idTime < limit) {
                    return false;
                } else {
                    //判断月份
                    idTime = 0;
                    currentTime = Integer.parseInt(JtlwDateTimeUtil.getInstance().getFormatDateNowTime(JtlwDateTimeUtil.MONTH_PATTERN));
                    if (idCard.length() == 18) {
                        idTime = Integer.parseInt(idCard.substring(10, 12));
                    } else if (idCard.length() == 15) {
                        idTime = Integer.parseInt(idCard.substring(8, 10));
                    }
                    //判断月份是否满足
                    if (currentTime - idTime > 0) {
                        return true;
                    } else if (currentTime - idTime < 0) {
                        return false;
                    } else {
                        //月份相同，判断时间
                        idTime = 0;
                        currentTime = Integer.parseInt(JtlwDateTimeUtil.getInstance().getFormatDateNowTime(JtlwDateTimeUtil.DAY_PATTERN));
                        if (idCard.length() == 18) {
                            idTime = Integer.parseInt(idCard.substring(12, 14));
                        } else if (idCard.length() == 15) {
                            idTime = Integer.parseInt(idCard.substring(10, 12));
                        }
                        return currentTime - idTime >= 0;
                    }
                }
            }
        }
        return false;
    }
}
