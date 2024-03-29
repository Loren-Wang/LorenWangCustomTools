package javabase.lorenwang.tools.common;

import org.junit.Test;

import java.util.List;

/**
 * 功能作用：
 * 创建时间：2020-11-27 10:20 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwDateTimeUtilsTest {

    @Test
    public void getAge() {
        System.out.println(JtlwDateTimeUtil.getInstance().getAge(
                JtlwDateTimeUtil.getInstance().getMillisecond("1994-05-12","yyyy-MM-dd"), true));
    }

    @Test
    public void getYearList() {
        List<Long> longList = JtlwDateTimeUtil.getInstance().getYearList(20, 20);
        for (Long item : longList) {
            System.out.println(JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy", item));
        }
    }

    @Test
    public void getMonthList() {
        List<Long> longList = JtlwDateTimeUtil.getInstance().getMonthList(System.currentTimeMillis(), true);
        for (Long item : longList) {
            System.out.println(JtlwDateTimeUtil.getInstance().getFormatDateTime("MM", item));
        }
    }

    @Test
    public void getDayList() {
        List<Long> longList = JtlwDateTimeUtil.getInstance().getDayList(System.currentTimeMillis(), true);
        for (Long item : longList) {
            System.out.println(JtlwDateTimeUtil.getInstance().getFormatDateTime("dd", item));
        }
    }

    @Test
    public void getMonthTimeList() {
        List<Long> longList = JtlwDateTimeUtil.getInstance().getMonthTimeList(System.currentTimeMillis(), 0, false);
        System.out.println(JtlwDateTimeUtil.getInstance().getFormatDateTime("yyy-MM-dd", longList.get(0)));
    }

    public void testIsLeapYear() {
        System.out.println("判断年份是否是闰年");
        System.out.println("年份为：" + 2000 + "  结果为：" + JtlwDateTimeUtil.getInstance().isLeapYear(2000));
        System.out.println("年份为：" + 2004 + "  结果为：" + JtlwDateTimeUtil.getInstance().isLeapYear(2004));
        System.out.println("年份为：" + 2003 + "  结果为：" + JtlwDateTimeUtil.getInstance().isLeapYear(2003));
        System.out.println("年份为：" + 2040 + "  结果为：" + JtlwDateTimeUtil.getInstance().isLeapYear(2040));
    }

    @Test
    public void getCountdownDay() {
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownDay(86400000));
    }

    @Test
    public void getCountdownHours() {
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownHours(86400000));
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownHours(86400000, true));
    }

    @Test
    public void getCountdownMinutes() {
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownMinutes(86400000));
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownMinutes(86400000, true));
    }

    @Test
    public void getCountdownMmillisecond() {
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownMillisecond(86400000));
        System.out.println(JtlwDateTimeUtil.getInstance().getCountdownMillisecond(86400000, true));
    }

}
