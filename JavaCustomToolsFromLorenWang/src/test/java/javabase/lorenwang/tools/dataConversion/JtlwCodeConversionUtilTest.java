package javabase.lorenwang.tools.dataConversion;

import org.junit.Test;

/**
 * 功能作用：
 * 创建时间：2020-12-12 9:36 下午
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
public class JtlwCodeConversionUtilTest {

    @Test
    public void decodeUnicode() {
        System.out.println(JtlwCodeConversionUtil.getInstance().unicodeToChinese(
                "eSdiPHrCw70GwCK3DcsbIjLcwbYUhMqNmY6g1aV2u41+xOi/7XU34dgloSrf5B6vdsUpiJ4CPeaF6oT3SMLKjg\\u003d\\u003d"
        ));
        System.out.println(JtlwCodeConversionUtil.getInstance().unicodeToChinese(
                "\\u0065\\u0053\\u0064\\u0069\\u0050\\u0048\\u0072\\u0043\\u0077\\u0037\\u0030\\u0047\\u0077\\u0043\\u004b\\u0033\\u0044\\u0063" +
                        "\\u0073\\u0062\\u0049\\u006a\\u004c\\u0063\\u0077\\u0062\\u0059\\u0055\\u0068\\u004d\\u0071\\u004e\\u006d\\u0059\\u0036" +
                        "\\u0067\\u0031\\u0061\\u0056\\u0032\\u0075\\u0034\\u0031\\u002b\\u0078\\u004f\\u0069\\u002f\\u0037\\u0058\\u0055\\u0033" +
                        "\\u0034\\u0064\\u0067\\u006c\\u006f\\u0053\\u0072\\u0066\\u0035\\u0042\\u0036\\u0076\\u0064\\u0073\\u0055\\u0070\\u0069" +
                        "\\u004a\\u0034\\u0043\\u0050\\u0065\\u0061\\u0046\\u0036\\u006f\\u0054\\u0033\\u0053\\u004d\\u004c\\u004b\\u006a\\u0067" +
                        "\\u005c\\u0075\\u0030\\u0030\\u0033\\u0064\\u005c\\u0075\\u0030\\u0030\\u0033\\u0064"
        ));
    }

    @Test
    public void chineseToUnicode() {
        System.out.println(JtlwCodeConversionUtil.getInstance().chineseToUnicode(
                "eSdiPHrCw70GwCK3DcsbIjLcwbYUhMqNmY6g1aV2u41+xOi/7XU34dgloSrf5B6vdsUpiJ4CPeaF6oT3SMLKjg\\u003d\\u003d"
        ));
    }
}
