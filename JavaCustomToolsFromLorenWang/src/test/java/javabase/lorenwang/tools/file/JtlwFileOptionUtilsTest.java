package javabase.lorenwang.tools.file;

import org.junit.Test;

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
        JtlwFileOptionUtil.getInstance().copyFileDir("/Volumes/DataStorege/蠢蛋蛋", "/Volumes/DataStorege/test");
    }

    @Test
    public void compressToZip() {
        JtlwFileOptionUtil.getInstance().compressToZip("/Volumes/DataStorege/蠢蛋蛋", "/Volumes/DataStorege/test.zip");

    }
}
