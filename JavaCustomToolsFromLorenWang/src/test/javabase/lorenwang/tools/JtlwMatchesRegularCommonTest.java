package test.javabase.lorenwang.tools;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javabase.lorenwang.tools.JtlwMatchesRegularCommon;

import static javabase.lorenwang.tools.JtlwMatchesRegularCommon.EXP_URL;
import static javabase.lorenwang.tools.JtlwMatchesRegularCommon.EXP_URL_IP;
import static javabase.lorenwang.tools.JtlwMatchesRegularCommon.EXP_URL_STR;

/**
 * JtlwMatchesRegularCommon Tester.
 *
 * @author 正则测试类
 * @version 1.0
 * @since <pre>6月 3, 2020</pre>
 */
public class JtlwMatchesRegularCommonTest {

    @Before
    public void before() {
    }

    @After
    public void after() {

    }

    @Test
    public void testAll(){
        String strUrl = "http://tool.chinaz.com/regex/";
        String ipUrl = "http://192.168.2.31:91/regex/";

        System.out.println(JtlwMatchesRegularCommon.getRegexResultList(ipUrl, EXP_URL_IP).get(0));
        System.out.println(JtlwMatchesRegularCommon.getRegexResultList(strUrl, EXP_URL_STR).get(0));
        System.out.println(JtlwMatchesRegularCommon.getRegexResultList(ipUrl, EXP_URL).get(0));
        System.out.println(JtlwMatchesRegularCommon.getRegexResultList(strUrl, EXP_URL).get(0));
    }


}
