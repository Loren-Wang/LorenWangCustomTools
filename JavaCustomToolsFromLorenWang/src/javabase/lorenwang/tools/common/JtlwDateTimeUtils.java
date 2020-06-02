package javabase.lorenwang.tools.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建时间：2019-01-28 下午 18:30:6
 * 创建人：王亮（Loren wang）
 * 功能作用：时间工具类
 * 思路：
 * 方法：1、获取当前时间的毫秒值
 * 2、获取当前时间的秒值
 * 3、格式化指定时间到指定格式
 * 4、格式化当前时间到指定格式
 * 5、格式化当前时间到指定格式，并返回该格式的相应毫秒值
 * 6、根据日期时间获得毫秒数
 * 7、根据日期时间获得秒数
 * 8、根据输入的年份判断该年份是否是闰年，是则返回true
 * 9、根据月日判断星座
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwDateTimeUtils {
    private final String TAG = getClass().getName();
    private static volatile JtlwDateTimeUtils optionUtils;

    /**
     * 私有构造
     */
    private JtlwDateTimeUtils() {
    }

    public static JtlwDateTimeUtils getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwDateTimeUtils.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwDateTimeUtils();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @return 毫秒数
     */
    public Long getMillisecond() {
        return new Date().getTime();
    }

    /**
     * 获取当前时间的秒值
     *
     * @return 秒值
     */
    public Long getSecond() {
        return new Date().getTime() / 1000;
    }

    /**
     * yyyy.MM.dd G 'at' hh:mm:ss z 如 '2002-1-1 AD at 22:10:59 PSD'
     * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'
     * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'
     * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00'
     * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am'
     * 格式化指定时间到指定格式
     *
     * @param pattern  时间格式正则
     * @param dateTime 时间戳
     * @return 转换后字符串
     */
    public String getFormatDateTime(String pattern, long dateTime) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        } else {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
            return sDateFormat.format(new Date(dateTime));
        }
    }

    /**
     * 格式化当前时间到指定格式
     *
     * @param pattern 时间格式正则
     * @return 转换后字符串
     */
    public String getFormatDateNowTime(String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(getMillisecond()));
    }

    /**
     * 格式化当前时间到指定格式，并返回该格式的相应毫秒值
     *
     * @param pattern 时间格式正则
     * @return 转换后字符串
     */
    public Long getFormatedNowTimeToMillisecond(String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        Long time = null;
        try {
            time = sDateFormat.parse(sDateFormat.format(new Date(getMillisecond()))).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 根据日期时间获得毫秒数
     *
     * @param dateAndTime       日期时间："201104141302"
     * @param dateAndTimeFormat 日期时间的格式："yyyyMMddhhmm"
     * @return 返回毫秒数
     */
    public long getMillisecond(String dateAndTime, String dateAndTimeFormat) {
        if (dateAndTime == null || "".equals(dateAndTime) || dateAndTimeFormat == null || "".equals(dateAndTimeFormat)) {
            return 0L;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateAndTimeFormat);
        long millionSeconds = 0L;
        try {
            millionSeconds = sdf.parse(dateAndTime).getTime();//毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    /**
     * 根据日期时间获得秒数
     *
     * @param dateAndTime       日期时间："201104141302"
     * @param dateAndTimeFormat 日期时间的格式："yyyyMMddhhmm"
     * @return 返回秒数
     */
    public long getSecond(String dateAndTime, String dateAndTimeFormat) {
        return getMillisecond(dateAndTime, dateAndTimeFormat) / 1000;
    }

    /**
     * 根据输入的年份判断该年份是否是闰年，是则返回true
     * @param year 要输入的年份
     * @return 是闰年返回true，否则返回false
     */
    public boolean isLeapYear(Integer year) {
        if (year != null) {
            return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        }
        return false;
    }

    /**
     * 根据月日判断星座
     *
     * @param m 月
     * @param d 日
     * @return 星座字符串
     */
    public String getConstellation(int m, int d) {
        final String[] constellationArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座"
                , "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        final int[] constellationEdgeDay = {20, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};
        int month = m;
        if (d <= constellationEdgeDay[month - 1]) {
            month = month - 1;
        }
        return constellationArr[month];
    }

}
