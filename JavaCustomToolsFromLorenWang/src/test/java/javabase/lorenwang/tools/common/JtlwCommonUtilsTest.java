package javabase.lorenwang.tools.common;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Locale;

import javabase.lorenwang.tools.common.JtlwCommonUtils;

/**
 * JtlwCommonUtils Tester.
 *
 * @author LorenWang
 * @version 1.0
 * @since <pre>6月 2, 2020</pre>
 */
public class JtlwCommonUtilsTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void toSeparatedCase() {
        System.out.println(JtlwCommonUtils.getInstance().toSeparatedCase("aBCD_","_").toLowerCase(Locale.ROOT));
    }

    @Test
    public void toCamelCase() {
        System.out.println(JtlwCommonUtils.getInstance().toCamelCase("a_b_c_d_"));
    }

    /**
     * Method: getInstance()
     */
    @Test
    public void testGetInstance() {
    }

    /**
     * Method: generateUuid(boolean isRemoveSpecialChar)
     */
    @Test
    public void testGenerateUuid() {
        System.out.println("uuid带中划线长度");
        System.out.println(JtlwCommonUtils.getInstance().generateUuid(false).length());
        System.out.println("uuid不带中划线长度");
        System.out.println(JtlwCommonUtils.getInstance().generateUuid(true).length());
        System.out.println("uuid带中划线生成");
        System.out.println(JtlwCommonUtils.getInstance().generateUuid(false));
        System.out.println("uuid不带中划线生成");
        System.out.println(JtlwCommonUtils.getInstance().generateUuid(true));
    }

    /**
     * Method: bytesToHexString(byte[] src)
     */
    @Test
    public void testBytesToHexString() {
    }

}
