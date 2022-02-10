package file;

import javabase.lorenwang.tools.file.JtlwFileOptionUtil;

/**
 * 功能作用：清除指定目录空文件夹目录
 * 初始注释时间： 2022/2/7 10:34
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
public class ClearEmptyFileDir {
    public static void main(String[] args) {
        JtlwFileOptionUtil.getInstance().clearEmptyFileDir("/Volumes/DataStorege/私人/备份文件/手机备份",
                (filePath, status) -> System.out.println("清除文件夹：" + filePath + "，清除状态：" + status));
    }
}
