package javabase.lorenwang.tools.common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 功能作用：
 * 创建时间：2021-03-11 15:35
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
public class JtlwCheckVariateUtilsTest {

    @Test
    public void checkChineseIdCard() {
        System.out.println(JtlwCheckVariateUtils.getInstance().checkChineseIdCard("150123201012129906"));
    }

    @Test
    public void checkAgeMoreThanLimitByIdCard() {
    }
}
