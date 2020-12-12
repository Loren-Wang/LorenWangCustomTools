package javabase.lorenwang.tools.safe;

import org.junit.Test;

import javabase.lorenwang.tools.dataConversion.JtlwDecimalConvertUtils;

import static org.junit.Assert.*;

/**
 * 功能作用：
 * 创建时间：2020-12-11 5:49 下午
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
public class JtlwEncryptDecryptUtilsTest {

    @Test
    public void encrypt() {
        System.out.println(JtlwEncryptDecryptUtils.getInstance().encrypt(
                "c57d7de31cfd446393f9181349a93cf8-251f21ba-WEB-1607679461987",
                "6pcWBA9PyapDv3Q1", "6pcWBA9PyapDv3Q1"
        ));
    }

    @Test
    public void decrypt() {
        System.out.println(JtlwEncryptDecryptUtils.getInstance().decrypt(
                "eSdiPHrCw70GwCK3DcsbIjLcwbYUhMqNmY6g1aV2u41+xOi/7XU34dgloSrf5B6vdsUpiJ4CPeaF6oT3SMLKjg\\u003d\\u003d",
                "6pcWBA9PyapDv3Q1", "6pcWBA9PyapDv3Q1"
        ));
    }
}
