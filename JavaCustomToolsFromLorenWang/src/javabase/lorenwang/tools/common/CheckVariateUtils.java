package javabase.lorenwang.tools.common;

import java.text.NumberFormat;
import java.util.List;

import javabase.lorenwang.tools.base.BaseUtils;


/**
 * 创建时间：2019-01-28 下午 14:02:18
 * 创建人：王亮（Loren wang）
 * 功能作用：变量检测工具类
 * 思路：
 * 方法：1、判断字符串是否为空
 *      2、判断是否符合指定的正则表达式
 *      3、判断字符串是否是整型
 *      4、判断字符串是否是长整型
 *      5、判断字符串是否是浮点数
 *      6、字符串是否超过指定长度
 *      7、Double类型是否超过指定长度(小数点前位数)
 *      8、判断字符串是否在列表中
 *      9、判断对象是否在数组中
 *      10、检查传入的路径是否是图片
 *      11、检查传入的路径是否是视频
 *      12、
 *      13、
 *      14、销毁当前单例
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class CheckVariateUtils extends BaseUtils {
    private final String TAG = "RegularOptionsUtils";

    private CheckVariateUtils() {
    }

    public static CheckVariateUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new CheckVariateUtils();
        }
        return (CheckVariateUtils) baseUtils;
    }


    /**
     * 判断变量是否为空
     *
     * @param str String
     * @return boolean
     */
    public<T> boolean isEmpty(T str) {
        return (str == null || "".equals(str)) ? true : false;
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
        return str.length() > len ? true : false;
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
        if (formatter.format(d.doubleValue()).length() > len) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否在列表中
     *
     * @param item
     * @param list
     * @return
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
     * @param <T>
     * @param item
     * @param list
     * @return
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
     * @param path
     * @return
     */
    public boolean checkIsImage(String path){
        if(path != null) {
            if(path.length() > 4){
                if(path.toLowerCase().substring(path.length() - 4).contains(".jpg")
                        || path.toLowerCase().substring(path.length() - 4).contains(".png")
                        || path.toLowerCase().substring(path.length() - 4).contains(".bmp")
                        || path.toLowerCase().substring(path.length() - 4).contains(".gif")
                        || path.toLowerCase().substring(path.length() - 4).contains(".psd")
                        || path.toLowerCase().substring(path.length() - 4).contains(".swf")
                        || path.toLowerCase().substring(path.length() - 4).contains(".svg")
                        || path.toLowerCase().substring(path.length() - 4).contains(".pcx")
                        || path.toLowerCase().substring(path.length() - 4).contains(".dxf")
                        || path.toLowerCase().substring(path.length() - 4).contains(".wmf")
                        || path.toLowerCase().substring(path.length() - 4).contains(".emf")
                        || path.toLowerCase().substring(path.length() - 4).contains(".lic")
                        || path.toLowerCase().substring(path.length() - 4).contains(".eps")
                        || path.toLowerCase().substring(path.length() - 4).contains(".tga")){
                    return true;
                }else if(path.length() > 5){
                    if(path.toLowerCase().substring(path.length() - 5).contains(".jpeg")
                            || path.toLowerCase().substring(path.length() - 5).contains(".tiff")){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 检查传入的路径是否是视频
     * @param path
     * @return
     */
    public boolean checkIsVideo(String path){
        if(path != null) {
            if(path.length() > 4){
                if(path.toLowerCase().substring(path.length() - 4).contains(".mp4")){
                    return true;
                }
            }
        }
        return false;
    }


}
