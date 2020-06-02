package test.javabase.lorenwang.tools.common;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Scanner;

import javabase.lorenwang.tools.JtlwMatchesRegularCommon;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;

/**
 * JtlwDateTimeUtils Tester.
 *
 * @author LorenWang
 * @version 1.0
 * @since <pre>6月 2, 2020</pre>
 */
public class JtlwDateTimeUtilsTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Method: getInstance()
     */
    @Test
    public void testGetInstance() {
    }

    /**
     * Method: getMillisecond()
     */
    @Test
    public void testGetMillisecond() {
    }

    /**
     * Method: getSecond()
     */
    @Test
    public void testGetSecond() {
    }

    /**
     * Method: getFormatDateTime(String pattern, long dateTime)
     */
    @Test
    public void testGetFormatDateTime() {
    }

    /**
     * Method: getFormatDateNowTime(String pattern)
     */
    @Test
    public void testGetFormatDateNowTime() {
    }

    /**
     * Method: getFormatedNowTimeToMillisecond(String pattern)
     */
    @Test
    public void testGetFormatedNowTimeToMillisecond() {
    }

    /**
     * Method: getMillisecond(String dateAndTime, String dateAndTimeFormat)
     */
    @Test
    public void testGetMillisecondForDateAndTimeDateAndTimeFormat() {
    }

    /**
     * Method: getSecond(String dateAndTime, String dateAndTimeFormat)
     */
    @Test
    public void testGetSecondForDateAndTimeDateAndTimeFormat() {
    }

    /**
     * Method: isLeapYear(Integer year)
     */
    @Test
    public void testIsLeapYear() {
        System.out.println("判断年份是否是闰年");
        System.out.println("年份为：" + 2000 + "  结果为：" + JtlwDateTimeUtils.getInstance().isLeapYear(2000));
        System.out.println("年份为：" + 2004 + "  结果为：" + JtlwDateTimeUtils.getInstance().isLeapYear(2004));
        System.out.println("年份为：" + 2003 + "  结果为：" + JtlwDateTimeUtils.getInstance().isLeapYear(2003));
        System.out.println("年份为：" + 2040 + "  结果为：" + JtlwDateTimeUtils.getInstance().isLeapYear(2040));
    }

    /**
     * Method: getConstellation(int m, int d)
     */
    @Test
    public void testGetConstellation() {
    }


}
