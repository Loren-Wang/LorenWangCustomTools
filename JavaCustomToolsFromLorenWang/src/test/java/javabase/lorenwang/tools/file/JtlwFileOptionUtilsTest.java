package javabase.lorenwang.tools.file;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 功能作用：
 * 创建时间：2020-12-06 2:06 下午
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
public class JtlwFileOptionUtilsTest {

    @Test
    public void copyFileDir() {
        JtlwFileOptionUtils.getInstance().copyFileDir("/Volumes/DataStorege/蠢蛋蛋", "/Volumes/DataStorege/test");
    }
}
