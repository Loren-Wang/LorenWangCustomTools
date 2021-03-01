package changFileContent;

import java.io.File;
import java.nio.charset.Charset;

import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

/**
 * 功能作用：
 * 创建时间：2020-05-25 2:57 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */

public class ChangeFileName {
    public static void main(String[] args) {
        //        changeFileName(new File("/Users/wangliang/Desktop/src/api"), false, ".js", ".ts");
    }

    static class a {
        int w;
        int h;
        int x;

        public a(int w, int h, int x) {
            this.w = w;
            this.h = h;
            this.x = x;
        }
    }

    /**
     * 文件名下划线该为中划线
     *
     * @param dirFile     文件夹file
     * @param changeChild 是否修复子文件名称
     */
    public static void inTheLineToTheUnderline(File dirFile, boolean changeChild) {
        if (dirFile != null && dirFile.isDirectory() && dirFile.listFiles() != null) {
            for (File file : dirFile.listFiles()) {
                if (file.isFile()) {
                    JtlwFileOptionUtils.getInstance().renameFile(file, file.getName().replace("-", "_"));
                } else {
                    inTheLineToTheUnderline(file, changeChild);
                }
            }
        }
    }

    /**
     * 修改文件名称
     *
     * @param dirFile     文件夹file
     * @param changeChild 是否修复子文件名称
     */
    public static void changeFileName(File dirFile, boolean changeChild, String target, String replacement) {
        if (dirFile != null && dirFile.isDirectory() && dirFile.listFiles() != null) {
            for (File file : dirFile.listFiles()) {
                if (file.isFile()) {
                    System.out.println(file.getAbsolutePath());
                    JtlwFileOptionUtils.getInstance().renameFile(file, file.getName().replace(target, replacement));
                } else {
                    changeFileName(file, changeChild, target, replacement);
                }
            }
        }
    }



}
