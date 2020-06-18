package test.javabase.lorenwang.tools.dataConversion;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javabase.lorenwang.tools.dataConversion.JtlwDecimalConvertUtils;

/**
 * JtlwDecimalConvertUtils Tester.
 *
 * @version 1.0
 * @since <pre>6月 17, 2020</pre>
 */
public class JtlwDecimalConvertUtilsTest {

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
     * Method: decimal10To2(Integer num)
     */
    @Test
    public void testDecimal10To2() {

    }

    /**
     * Method: decimal10To8(Integer num)
     */
    @Test
    public void testDecimal10To8() {

    }

    /**
     * Method: decimal10To16(Integer num)
     */
    @Test
    public void testDecimal10To16() {
        System.out.println(JtlwDecimalConvertUtils.getInstance().decimal10To16(255));
        System.out.println(JtlwDecimalConvertUtils.getInstance().decimal10To16(100));
        System.out.println(JtlwDecimalConvertUtils.getInstance().decimal10To16(17));
        System.out.println(JtlwDecimalConvertUtils.getInstance().decimal10To16(2));
        System.out.println(JtlwDecimalConvertUtils.getInstance().decimal10To16(1));
    }

    /**
     * Method: decimal10To32(Integer num)
     */
    @Test
    public void testDecimal10To32() {

    }

    /**
     * Method: decimal10To62(Integer num)
     */
    @Test
    public void testDecimal10To62() {

    }

    /**
     * Method: decimal8To2(Integer num)
     */
    @Test
    public void testDecimal8To2() {

    }

    /**
     * Method: decimal8To10(Integer num)
     */
    @Test
    public void testDecimal8To10() {

    }

    /**
     * Method: decimal8To16(Integer num)
     */
    @Test
    public void testDecimal8To16() {

    }

    /**
     * Method: decimal8To32(Integer num)
     */
    @Test
    public void testDecimal8To32() {

    }

    /**
     * Method: decimal8To62(Integer num)
     */
    @Test
    public void testDecimal8To62() {

    }

    /**
     * Method: decimal2To8(Integer num)
     */
    @Test
    public void testDecimal2To8() {

    }

    /**
     * Method: decimal2To10(Integer num)
     */
    @Test
    public void testDecimal2To10() {

    }

    /**
     * Method: decimal2To16(Integer num)
     */
    @Test
    public void testDecimal2To16() {

    }

    /**
     * Method: decimal2To32(Integer num)
     */
    @Test
    public void testDecimal2To32() {

    }

    /**
     * Method: decimal16To2(String num)
     */
    @Test
    public void testDecimal16To2() {

    }

    /**
     * Method: decimal16To8(String num)
     */
    @Test
    public void testDecimal16To8() {

    }

    /**
     * Method: decimal16To10(String num)
     */
    @Test
    public void testDecimal16To10() {

    }


    /**
     * Method: decimalToAssign(int num, int assign, Integer minLength)
     */
    @Test
    public void testDecimalToAssign() {
    }

    /**
     * Method: decimal2ToOther(int num, int assign, Integer splitNum)
     */
    @Test
    public void testDecimal2ToOther() {

/*
try {
   Method method = JtlwDecimalConvertUtils.getClass().getMethod("decimal2ToOther", int.class, int
   .class, Integer.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

    /**
     * Method: getAssignDecimalStr(int assign, int strPosition)
     */
    @Test
    public void testGetAssignDecimalStr() {

/*
try {
   Method method = JtlwDecimalConvertUtils.getClass().getMethod("getAssignDecimalStr", int.class,
    int.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

}
