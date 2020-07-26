package javabase.lorenwang.tools.common;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import javabase.lorenwang.tools.JtlwLogUtils;


/**
 * 功能作用：变量检测工具类
 * 创建时间：2019-01-28 下午 14:02:18
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：1、判断字符串是否为空
 * 2、判断是否符合指定的正则表达式
 * 3、判断字符串是否是整型
 * 4、判断字符串是否是长整型
 * 5、判断字符串是否是浮点数
 * 6、字符串是否超过指定长度
 * 7、Double类型是否超过指定长度(小数点前位数)
 * 8、判断字符串是否在列表中
 * 9、判断对象是否在数组中
 * 10、检查传入的路径是否是图片
 * 11、检查传入的路径是否是视频
 * 12、
 * 13、
 * 14、销毁当前单例
 * 15、检查文件是否存在
 * 16、检测文件是否是图片
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwCheckVariateUtils {
    private final String TAG = getClass().getName();
    private static volatile JtlwCheckVariateUtils optionUtils;

    /**
     * 私有构造
     */
    private JtlwCheckVariateUtils() {
    }

    public static JtlwCheckVariateUtils getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwCheckVariateUtils.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwCheckVariateUtils();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 判断变量是否为空
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
     * @param <T> 变量泛型
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
                if (start.contains(".jpg") || start.contains(".png")
                        || start.contains(".bmp") || start.contains(".gif")
                        || start.contains(".psd") || start.contains(".swf")
                        || start.contains(".svg") || start.contains(".pcx")
                        || start.contains(".dxf") || start.contains(".wmf")
                        || start.contains(".emf") || start.contains(".lic")
                        || start.contains(".eps") || start.contains(".tga")) {
                    return true;
                } else if (path.length() > 5) {
                    start = path.toLowerCase().substring(path.length() - 5);
                    return start.contains(".jpeg") || start.contains(".tiff");
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
            JtlwLogUtils.logI(TAG, "被检查文件地址为空，不通过检测");
            return false;
        }
        File file = new File(filePath);
        boolean isExit = false;//文件是否存在记录
        if (file.isDirectory()) {
            JtlwLogUtils.logI(TAG, "被检查文件为空或被检测的地址为文件夹，不通过检测");
            return false;
        }
        if (file.exists()) {
            isExit = true;
            JtlwLogUtils.logI(TAG, "被检查文件存在");
        } else {
            JtlwLogUtils.logI(TAG, "被检查文件不存在");
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
                JtlwLogUtils.logI(TAG, "被检测地址为图片地址：");
                return true;
            } else {
                JtlwLogUtils.logI(TAG, "被检测地址为空或文件为非图片");
                return false;
            }
        } else {
            JtlwLogUtils.logI(TAG, "被检测地址为空或文件为非图片");
            return false;
        }
    }

}
