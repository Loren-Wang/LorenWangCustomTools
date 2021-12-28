package javabase.lorenwang.tools.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 功能作用：时间工具类
 * 初始注释时间： 2019-01-28 下午 18:30:6
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 每天的时间毫秒数--DAY_TIME_MILLISECOND
 * 每小时的时间毫秒数--HOUR_TIME_MILLISECOND
 * 获取当前时间的毫秒值--getMillisecond()
 * 获取当前时间的秒值--getSecond()
 * 格式化指定时间到指定格式--getFormatDateTime(pattern,dateTime)
 * 格式化时间--getFormatDateTime(pattern,date)
 * 格式化当前时间到指定格式--getFormatDateNowTime(pattern)
 * 格式化当前时间到指定格式，并返回该格式的相应毫秒值--getFormatNowTimeToMillisecond(pattern)
 * 根据日期时间获得毫秒数--getMillisecond(dateAndTime,dateAndTimeFormat)
 * 根据日期时间获得秒数--getSecond(dateAndTime,dateAndTimeFormat)
 * 根据输入的年份判断该年份是否是闰年，是则返回true--isLeapYear(year)
 * 根据输入的年份判断该年份是否是闰年，是则返回true--isLeapYearForTime(yearTime)
 * 根据日期获取 星期--dateToWeek(time)
 * 获取一个月的所有时间列表--getMonthTimeList(monthTime,firstWeek,onlyMonth)
 * 是否是同一天时间--isOneDay(timeOne, timeTwo)
 * 是否是同一小时时间--isOneHour(timeOne, timeTwo)
 * 是否是同一分钟时间--isOneMinutes(timeOne, timeTwo)
 * 获取指定时间下个月第一天的时间--getNextMonthStartDayTime(time)
 * 获取指定时间上个月第一天的时间--getLastMonthStartDayTime(time)
 * 获取指定时间月份的结束天数--getMonthDays(showTime)
 * 获取倒计时天数--getCountdownDay(millisecondTime)
 * 获取倒计时小时，总小时，可能会超过24小时以上--getCountdownHours(millisecondTime)
 * 获取倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时--getCountdownHours(millisecondTime,useOneDay)
 * 获取倒计时分钟，总小时，可能会超过24小时以上--getCountdownMinutes(millisecondTime)
 * 获取倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60小时--getCountdownMinutes(millisecondTime,useOnHours)
 * 获取倒计时秒，总秒数，可能会超过60s以上--getCountdownMillisecond(millisecondTime)
 * 获取倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上--getCountdownMillisecond(millisecondTime,useOnMinutes)
 * 获取年份列表--getYearList(leftYearCount,rightYearCount)
 * 获取年份列表--getYearList(startTime,endTime)
 * 获取月份列表--getMonthList(yearTime,asOfCurrent)
 * 获取月份列表--getMonthList(startTime,endTime,showTime,)
 * 获取日期列表--getDayList(monthTime,asOfCurrent)
 * 获取日期列表--getDayList(startTime,endTime,showTime,onlyCurrentMonth)
 * 获取小时列表--getHourList(startTime,endTime,showTime,onlyCurrentDay)
 * 获取分钟列表--getMinutesList(startTime,endTime,showTime,onlyCurrentHour)
 * 获取秒钟列表--getSecondsList(startTime,endTime,showTime,onlyCurrentMinutes)
 * 获取年龄--getAge(birthTime,isReal)
 * 根据时间获取星座--getConstellation(time)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwDateTimeUtil {
    private static volatile JtlwDateTimeUtil optionUtils;

    /**
     * 每天的时间毫秒数
     */
    public static long DAY_TIME_MILLISECOND = 86400000;

    /**
     * 每小时的时间毫秒数
     */
    public static long HOUR_TIME_MILLISECOND = 3600000;

    /**
     * 年份默认格式
     */
    public static String YEAR_PATTERN = "yyyy";

    /**
     * 月份默认格式
     */
    public static String MONTH_PATTERN = "MM";

    /**
     * 日期默认格式
     */
    public static String DAY_PATTERN = "dd";

    /**
     * 私有构造
     */
    private JtlwDateTimeUtil() {
    }

    public static JtlwDateTimeUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwDateTimeUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwDateTimeUtil();
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
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间的秒值
     *
     * @return 秒值
     */
    public Long getSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 格式化指定时间到指定格式
     * <p>
     * yyyy.MM.dd G 'at' hh:mm:ss z 如 '2002-1-1 AD at 22:10:59 PSD'
     * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'
     * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'
     * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00'
     * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am'
     *
     * @param pattern  时间格式正则
     * @param dateTime 时间戳
     * @return 转换后字符串
     */
    public String getFormatDateTime(String pattern, long dateTime) {
        return getFormatDateTime(pattern, new Date(dateTime));
    }

    /**
     * 格式化时间
     *
     * @param pattern 格式
     * @param date    时间
     * @return 成功返回时间字符串，否则返回null
     */
    public String getFormatDateTime(String pattern, Date date) {
        if (pattern == null || "".equals(pattern) || date == null) {
            return null;
        } else {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return sDateFormat.format(date);
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
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return sDateFormat.format(new Date(getMillisecond()));
    }

    /**
     * 格式化当前时间到指定格式，并返回该格式的相应毫秒值
     *
     * @param pattern 时间格式正则
     * @return 转换后字符串
     */
    public Long getFormatNowTimeToMillisecond(String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
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
     * @param dateAndTimeFormat 日期时间的格式："yyyy MM dd hh mm"
     * @return 返回毫秒数
     */
    public long getMillisecond(String dateAndTime, String dateAndTimeFormat) {
        if (dateAndTime == null || "".equals(dateAndTime) || dateAndTimeFormat == null || "".equals(dateAndTimeFormat)) {
            return 0L;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateAndTimeFormat, Locale.getDefault());
        long millionSeconds = 0L;
        try {
            //毫秒
            millionSeconds = sdf.parse(dateAndTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    /**
     * 根据日期时间获得秒数
     *
     * @param dateAndTime       日期时间："201104141302"
     * @param dateAndTimeFormat 日期时间的格式："yyyy  MM dd hh mm"
     * @return 返回秒数
     */
    public long getSecond(String dateAndTime, String dateAndTimeFormat) {
        return getMillisecond(dateAndTime, dateAndTimeFormat) / 1000;
    }

    /**
     * 根据输入的年份判断该年份是否是闰年，是则返回true
     *
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
     * 根据输入的年份判断该年份是否是闰年，是则返回true
     *
     * @param yearTime 要输入的年份
     * @return 是闰年返回true，否则返回false
     */
    public boolean isLeapYearForTime(long yearTime) {
        return isLeapYear(Integer.parseInt(getFormatDateTime(YEAR_PATTERN, yearTime)));
    }

    /**
     * 根据日期获取 星期
     *
     * @param time 时间
     * @return 从星期日开始的下标
     */
    public int dateToWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 获取一个月的所有时间列表
     *
     * @param monthTime 月份时间
     * @param firstWeek 开始的星期，也就是星期几开始,0代表星期日，1代表星期一，依次类推
     * @param onlyMonth 是否只有当月的
     * @return 月度列表，如果不返回当月的则非当月的位置元素会为空
     */
    public List<Long> getMonthTimeList(long monthTime, int firstWeek, boolean onlyMonth) {
        //处理星期使用
        firstWeek = firstWeek % 7;
        List<Long> timeList = new ArrayList<>();
        //获取当月的第一天时间
        Long monthStartTime = JtlwDateTimeUtil.getInstance().getMillisecond(JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy-MM", monthTime),
                "yyyy-MM");
        long nextMonthStartTime = getNextMonthStartDayTime(monthTime);
        //获取第一天的星期,0代表星期日，1代表星期一，依次类推
        int weekDay = dateToWeek(monthStartTime);
        //填充之前的时间
        int addFirst = 0;
        if (weekDay > firstWeek) {
            addFirst = weekDay - firstWeek;
        } else if (weekDay < firstWeek) {
            addFirst = weekDay + 7 - firstWeek;
        }
        if (onlyMonth) {
            for (int i = 0; i < addFirst; i++) {
                timeList.add(null);
            }
        } else {
            for (int i = addFirst; i > 0; i--) {
                timeList.add(monthStartTime - DAY_TIME_MILLISECOND * i);
            }
        }
        //开始填充当月时间
        while (monthStartTime.compareTo(nextMonthStartTime) < 0) {
            timeList.add(monthStartTime);
            monthStartTime += DAY_TIME_MILLISECOND;
        }
        //开始填充结束时间列表,不需要当月的那后续位置也不用填充了
        if (!onlyMonth) {
            //当月最后一天星期
            int lastWeekDay = dateToWeek(nextMonthStartTime - DAY_TIME_MILLISECOND);
            int addLast = firstWeek + 7 - lastWeekDay - 1;
            for (int i = 0; i < addLast; i++) {
                timeList.add(nextMonthStartTime);
                nextMonthStartTime += DAY_TIME_MILLISECOND;
            }
        }

        return timeList;
    }

    /**
     * 是否是同一天时间
     *
     * @param timeOne 时间1
     * @param timeTwo 时间2
     * @return 是同一天返回true
     */
    public boolean isOneDay(long timeOne, long timeTwo) {
        return getFormatDateTime("yyyyMMdd", timeOne).equals(getFormatDateTime("yyyyMMdd", timeTwo));
    }

    /**
     * 是否是同一小时时间
     *
     * @param timeOne 时间1
     * @param timeTwo 时间2
     * @return 是同一小时返回true
     */
    public boolean isOneHour(long timeOne, long timeTwo) {
        return getFormatDateTime("yyyyMMddHH", timeOne).equals(getFormatDateTime("yyyyMMddHH", timeTwo));
    }

    /**
     * 是否是同一分钟时间
     *
     * @param timeOne 时间1
     * @param timeTwo 时间2
     * @return 是同一小时返回true
     */
    public boolean isOneMinutes(long timeOne, long timeTwo) {
        return getFormatDateTime("yyyyMMddHHmm", timeOne).equals(getFormatDateTime("yyyyMMddHHmm", timeTwo));
    }

    /**
     * 获取指定时间下个月第一天的时间
     *
     * @param time 指定时间
     * @return 下个月1号的时间
     */
    public long getNextMonthStartDayTime(long time) {
        String month = JtlwDateTimeUtil.getInstance().getFormatDateTime("MM", time);
        String year = JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy", time);
        switch (month) {
            case "01":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "02", "yyyyMM");
            case "02":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "03", "yyyyMM");
            case "03":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "04", "yyyyMM");
            case "04":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "05", "yyyyMM");
            case "05":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "06", "yyyyMM");
            case "06":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "07", "yyyyMM");
            case "07":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "08", "yyyyMM");
            case "08":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "09", "yyyyMM");
            case "09":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "10", "yyyyMM");
            case "10":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "11", "yyyyMM");
            case "11":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "12", "yyyyMM");
            case "12":
                return JtlwDateTimeUtil.getInstance().getMillisecond((Integer.parseInt(year) + 1) + "01", "yyyyMM");
            default:
                return -1;
        }
    }

    /**
     * 获取指定时间上个月第一天的时间
     *
     * @param time 指定时间
     * @return 上个月1号的时间
     */
    public long getLastMonthStartDayTime(long time) {
        String month = JtlwDateTimeUtil.getInstance().getFormatDateTime("MM", time);
        String year = JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy", time);
        switch (month) {
            case "01":
                return JtlwDateTimeUtil.getInstance().getMillisecond((Integer.parseInt(year) - 1) + "12", "yyyyMM");
            case "02":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "01", "yyyyMM");
            case "03":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "02", "yyyyMM");
            case "04":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "03", "yyyyMM");
            case "05":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "04", "yyyyMM");
            case "06":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "05", "yyyyMM");
            case "07":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "06", "yyyyMM");
            case "08":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "07", "yyyyMM");
            case "09":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "08", "yyyyMM");
            case "10":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "09", "yyyyMM");
            case "11":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "10", "yyyyMM");
            case "12":
                return JtlwDateTimeUtil.getInstance().getMillisecond(year + "11", "yyyyMM");
            default:
                return -1;
        }
    }

    /**
     * 获取指定时间月份的结束天数
     *
     * @param showTime 当前月的某一个时间
     * @return 总的天数
     */
    public int getMonthDays(long showTime) {
        final String year = getFormatDateTime("yyyy", showTime);
        final String month = getFormatDateTime("MM", showTime);
        int showYear = Integer.parseInt(year);
        int showMonth = Integer.parseInt(month);
        if (showMonth == 1 || showMonth == 3 || showMonth == 5 || showMonth == 7 || showMonth == 8 || showMonth == 10 || showMonth == 12) {
            return 31;
        } else if (showMonth == 2) {
            if (isLeapYear(showYear)) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return 30;
        }
    }

    /**
     * 获取倒计时天数
     *
     * @param millisecondTime 时间毫秒数
     * @return 倒计时天数
     */
    public int getCountdownDay(long millisecondTime) {
        return (int) (millisecondTime / DAY_TIME_MILLISECOND);
    }

    /**
     * 获取倒计时小时，总小时，可能会超过24小时以上
     *
     * @param millisecondTime 时间毫秒数
     * @return 倒计时小时，总小时，可能会超过24小时以上
     */
    public int getCountdownHours(long millisecondTime) {
        return getCountdownHours(millisecondTime, false);
    }

    /**
     * 获取倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时
     *
     * @param millisecondTime 时间毫秒数
     * @param useOneDay       是否使用一天做处理
     * @return 倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时
     */
    public int getCountdownHours(long millisecondTime, boolean useOneDay) {
        if (useOneDay) {
            return (int) (millisecondTime / HOUR_TIME_MILLISECOND % 24);
        } else {
            return (int) (millisecondTime / HOUR_TIME_MILLISECOND);
        }
    }

    /**
     * 获取倒计时分钟，总小时，可能会超过24小时以上
     *
     * @param millisecondTime 时间毫秒数
     * @return 倒计时分钟，总分钟，可能会超过60分钟以上
     */
    public int getCountdownMinutes(long millisecondTime) {
        return getCountdownMinutes(millisecondTime, false);
    }

    /**
     * 获取倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60小时
     *
     * @param millisecondTime 时间毫秒数
     * @param useOnHours      是否使用一天做处理
     * @return 倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60分钟以上
     */
    public int getCountdownMinutes(long millisecondTime, boolean useOnHours) {
        if (useOnHours) {
            return (int) (millisecondTime / 60000 % 60);
        } else {
            return (int) (millisecondTime / 60000);
        }
    }

    /**
     * 获取倒计时秒，总秒数，可能会超过60s以上
     *
     * @param millisecondTime 时间毫秒数
     * @return 倒计时秒，总秒数，可能会超过60s以上
     */
    public int getCountdownMillisecond(long millisecondTime) {
        return getCountdownMillisecond(millisecondTime, false);
    }

    /**
     * 获取倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上
     *
     * @param millisecondTime 时间毫秒数
     * @param useOnMinutes    是否使用一分钟做处理
     * @return 倒计时倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上
     */
    public int getCountdownMillisecond(long millisecondTime, boolean useOnMinutes) {
        if (useOnMinutes) {
            return (int) (millisecondTime / 1000 % 60);
        } else {
            return (int) (millisecondTime / 1000);
        }
    }

    /**
     * 获取年份列表
     *
     * @param leftYearCount  左侧年份需要补充的数量，不包含当当年
     * @param rightYearCount 右侧年份需要补充的数量，不包含当年
     * @return 年份列表
     */
    public List<Long> getYearList(int leftYearCount, int rightYearCount) {
        List<Long> list = new ArrayList<>();
        String year = getFormatDateNowTime(YEAR_PATTERN);
        list.add(getMillisecond(year, YEAR_PATTERN));
        int yearNum = Integer.parseInt(year);
        //左侧
        for (int i = 1; i <= leftYearCount; i++) {
            list.add(0, getMillisecond(String.valueOf(yearNum - i), YEAR_PATTERN));
        }
        //右侧
        for (int i = 1; i <= rightYearCount; i++) {
            list.add(getMillisecond(String.valueOf(yearNum + i), YEAR_PATTERN));
        }
        return list;
    }

    /**
     * 获取年份列表
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 年份列表
     */
    public List<Long> getYearList(long startTime, long endTime) {
        List<Long> list = new ArrayList<>();
        if (startTime <= endTime) {
            final int start = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, startTime));
            final int end = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, endTime));
            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(String.valueOf(index), YEAR_PATTERN));
            }
        }
        return list;
    }

    /**
     * 获取月份列表
     *
     * @param yearTime    年份时间
     * @param asOfCurrent 是否截止到当前
     * @return 月份列表
     */
    public List<Long> getMonthList(long yearTime, boolean asOfCurrent) {
        String monthPattern = YEAR_PATTERN + MONTH_PATTERN;
        String year = getFormatDateTime(YEAR_PATTERN, yearTime);
        List<Long> list = new ArrayList<>();
        if (asOfCurrent) {
            int month = Integer.parseInt(getFormatDateNowTime(MONTH_PATTERN));
            for (int i = 1; i <= month; i++) {
                list.add(getMillisecond(year + i, monthPattern));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                list.add(getMillisecond(year + i, monthPattern));
            }
        }
        return list;
    }

    /**
     * 获取月份列表
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param showTime  需要显示的时间
     * @return 月份列表
     */
    public List<Long> getMonthList(long startTime, long endTime, long showTime) {
        List<Long> list = new ArrayList<>();
        if (showTime >= startTime && showTime <= endTime) {
            final int startYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, startTime));
            final int endYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, endTime));
            final int showYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, showTime));
            int start = 1;
            int end = 12;
            if (startYear == endYear) {
                start = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, startTime));
                end = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, endTime));
            } else {
                if (showYear == endYear) {
                    end = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, endTime));
                } else if (showYear == startYear) {
                    start = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, startTime));
                }
            }

            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(showYear + (index < 10 ? "0" + index : String.valueOf(index)), YEAR_PATTERN + MONTH_PATTERN));
            }
        }
        return list;
    }

    /**
     * 获取日期列表
     *
     * @param monthTime   年份时间
     * @param asOfCurrent 是否截止到当前
     * @return 日期列表
     */
    public List<Long> getDayList(long monthTime, boolean asOfCurrent) {
        String dayPattern = YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN;
        String yearMonth = getFormatDateTime(YEAR_PATTERN + MONTH_PATTERN, monthTime);
        int month = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, monthTime));
        List<Long> list = new ArrayList<>();
        //最大日期
        int maxDay;
        //获取指定时间月份的最大天数
        if (asOfCurrent) {
            //截止当前处理
            maxDay = Integer.parseInt(getFormatDateNowTime(DAY_PATTERN));
        } else {
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                maxDay = 31;
            } else if (month == 2) {
                if (isLeapYearForTime(monthTime)) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
            } else {
                maxDay = 30;
            }
        }
        for (int i = 1; i <= maxDay; i++) {
            list.add(getMillisecond(yearMonth + i, dayPattern));
        }
        return list;
    }

    /**
     * 获取日期列表
     *
     * @param startTime        起始时间
     * @param endTime          结束时间
     * @param showTime         需要显示的时间
     * @param onlyCurrentMonth 是否仅仅是当月
     * @return 月份列表
     */
    public List<Long> getDayList(long startTime, long endTime, long showTime, boolean onlyCurrentMonth) {
        List<Long> list = new ArrayList<>();
        if (showTime >= startTime && showTime <= endTime) {
            final int startYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, startTime));
            final int endYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, endTime));
            final int showYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, showTime));
            final int startMonth = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, startTime));
            final int endMonth = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, endTime));
            final int showMonth = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, showTime));
            int start = 1;
            int end = getMonthDays(showTime);
            if (startYear == endYear) {
                if (startMonth == endMonth) {
                    start = Integer.parseInt(getFormatDateTime(DAY_PATTERN, startTime));
                    end = Integer.parseInt(getFormatDateTime(DAY_PATTERN, endTime));
                } else {
                    if (showMonth == endMonth) {
                        end = Integer.parseInt(getFormatDateTime(DAY_PATTERN, endTime));
                    } else if (showMonth == startMonth) {
                        start = Integer.parseInt(getFormatDateTime(DAY_PATTERN, startTime));
                    }
                }
            } else {
                if (showYear == endYear) {
                    if (showMonth == endMonth) {
                        end = Integer.parseInt(getFormatDateTime(DAY_PATTERN, endTime));
                    }
                } else if (showYear == startYear) {
                    if (showMonth == startMonth) {
                        start = Integer.parseInt(getFormatDateTime(DAY_PATTERN, startTime));
                    }
                }
            }
            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(showYear + (showMonth < 10 ? "0" + showMonth : String.valueOf(showMonth)) +
                        (index < 10 ? "0" + index : String.valueOf(index)), YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN));
            }
            //非仅当月要继续向下加数据
            if (!onlyCurrentMonth) {
                //最后一天数据继续加
                long first = startTime;
                if (list.size() > 0) {
                    first = list.get(list.size() - 1);
                }
                for (long time = first + DAY_TIME_MILLISECOND; time < endTime; time += DAY_TIME_MILLISECOND) {
                    list.add(time);
                }
            }
        }
        return list;
    }

    /**
     * 获取小时列表
     *
     * @param startTime      起始时间
     * @param endTime        结束时间
     * @param showTime       需要显示的时间
     * @param onlyCurrentDay 是否仅仅是当天
     * @return 小时列表
     */
    public List<Long> getHourList(long startTime, long endTime, long showTime, boolean onlyCurrentDay) {
        List<Long> list = new ArrayList<>();
        if (showTime >= startTime && showTime <= endTime) {
            final String showDay = getFormatDateTime(YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN, showTime);
            int start = 0;
            int end = 23;
            if (isOneDay(showTime, endTime)) {
                end = Integer.parseInt(getFormatDateTime("HH", endTime));
            } else if (isOneDay(showTime, startTime)) {
                start = Integer.parseInt(getFormatDateTime("HH", startTime));
            }

            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(showDay + (index < 10 ? "0" + index : String.valueOf(index)),
                        YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN + "HH"));
            }
        }
        //非仅当天要继续向下加数据
        if (!onlyCurrentDay) {
            //最后一天数据继续加
            long first = startTime;
            if (list.size() > 0) {
                first = list.get(list.size() - 1);
            }
            for (long time = first + HOUR_TIME_MILLISECOND; time < endTime; time += HOUR_TIME_MILLISECOND) {
                list.add(time);
            }
        }
        return list;
    }

    /**
     * 获取分钟列表
     *
     * @param startTime       起始时间
     * @param endTime         结束时间
     * @param showTime        需要显示的时间
     * @param onlyCurrentHour 是否仅仅是当前小时
     * @return 分钟列表
     */
    public List<Long> getMinutesList(long startTime, long endTime, long showTime, boolean onlyCurrentHour) {
        List<Long> list = new ArrayList<>();
        if (showTime >= startTime && showTime <= endTime) {
            final String showDay = getFormatDateTime(YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN + "HH", showTime);
            int start = 0;
            int end = 59;
            if (isOneHour(showTime, endTime)) {
                end = Integer.parseInt(getFormatDateTime("mm", endTime));
            } else if (isOneHour(showTime, startTime)) {
                start = Integer.parseInt(getFormatDateTime("mm", startTime));
            }

            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(showDay + (index < 10 ? "0" + index : String.valueOf(index)),
                        YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN + "HHmm"));
            }
        }
        //非仅当小时要继续向下加数据
        if (!onlyCurrentHour) {
            //最后一天数据继续加
            long first = startTime;
            if (list.size() > 0) {
                first = list.get(list.size() - 1);
            }
            for (long time = first + 60000; time < endTime; time += 60000) {
                list.add(time);
            }
        }
        return list;
    }

    /**
     * 获取秒钟列表
     *
     * @param startTime          起始时间
     * @param endTime            结束时间
     * @param showTime           需要显示的时间
     * @param onlyCurrentMinutes 是否仅仅当前分钟
     * @return 分钟列表
     */
    public List<Long> getSecondsList(long startTime, long endTime, long showTime, boolean onlyCurrentMinutes) {
        List<Long> list = new ArrayList<>();
        if (showTime >= startTime && showTime <= endTime) {
            final String showDay = getFormatDateTime(YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN + "HHmm", showTime);
            int start = 0;
            int end = 59;
            if (isOneMinutes(showTime, endTime)) {
                end = Integer.parseInt(getFormatDateTime("ss", endTime));
            } else if (isOneMinutes(showTime, startTime)) {
                start = Integer.parseInt(getFormatDateTime("ss", startTime));
            }

            for (int index = start; index <= end; index++) {
                list.add(getMillisecond(showDay + (index < 10 ? "0" + index : String.valueOf(index)),
                        YEAR_PATTERN + MONTH_PATTERN + DAY_PATTERN + "HHmmss"));
            }
        }
        //非仅当前分钟要继续向下加数据
        if (!onlyCurrentMinutes) {
            //最后一天数据继续加
            long first = startTime;
            if (list.size() > 0) {
                first = list.get(list.size() - 1);
            }
            for (long time = first + 1000; time < endTime; time += 1000) {
                list.add(time);
            }
        }
        return list;
    }

    /**
     * 根据时间获取星座
     *
     * @param time 时间
     * @return 星座顺序下标
     */
    public int getConstellation(long time) {
        int day = Integer.parseInt(getFormatDateTime(DAY_PATTERN, time));
        int month = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, time));
        //        final String[] constellationArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        final int[] constellationEdgeDay = {20, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};
        if (day <= constellationEdgeDay[month - 1]) {
            month = month - 1;
        }
        return month;
    }

    /**
     * 获取年龄
     *
     * @param birthTime 出生日期
     * @param isReal    是否获取实岁，true获取实岁，false获取虚岁
     * @return 年龄整数
     */
    public int getAge(long birthTime, boolean isReal) {
        int currentYear = Integer.parseInt(getFormatDateNowTime(YEAR_PATTERN));
        int birthYear = Integer.parseInt(getFormatDateTime(YEAR_PATTERN, birthTime));
        int year = currentYear - birthYear + 1;
        if (isReal) {
            int currentMonth = Integer.parseInt(getFormatDateNowTime(MONTH_PATTERN));
            int birthMonth = Integer.parseInt(getFormatDateTime(MONTH_PATTERN, birthTime));
            if (currentMonth < birthMonth) {
                year--;
            } else if (currentMonth == birthMonth) {
                int currentDay = Integer.parseInt(getFormatDateNowTime(DAY_PATTERN));
                int birthDay = Integer.parseInt(getFormatDateTime(DAY_PATTERN, birthTime));
                if (currentDay < birthDay) {
                    year--;
                }
            }
        }
        return year;
    }
}
