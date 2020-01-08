package changFileContent;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

public class ChangeJavaInstanceUtilsContent {
    private static final String FILE_PATH_DIR = "D:\\DevelopProject\\Custom\\LorenWangCustomTools";

    public static void main(String[] args) {
        changeKtFile();
    }

    /**
     * 修改java文件
     */
    private static void changeJavaFile() {
        List<File> fileList = JtlwFileOptionUtils.getInstance().getFileListForMatchRecursionScan(FILE_PATH_DIR,
                "\\S+.java");

        Charset codedFormat;
        for (File file : fileList) {
            try {
                codedFormat = JtlwFileOptionUtils.getInstance().getFileCodedFormat(file.getAbsolutePath());
                String content = JtlwFileOptionUtils.getInstance().readFileContent(file.getAbsolutePath(), codedFormat);
                if (content != null) {
                    Pattern pattern = Pattern.compile("private( )+static( )+" + file.getName().replace(".java", "") + "( )+\\S+Utils;");
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        content = content.replaceAll(pattern.pattern(), matcher.group(0).replaceAll("private( )+static( )+", "private static volatile "));
                    }
                }
                JtlwFileOptionUtils.getInstance().writeFilContent(file.getAbsolutePath(), StandardCharsets.UTF_8, content);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改kt文件
     */
    private static void changeKtFile() {
        List<File> fileList = JtlwFileOptionUtils.getInstance().getFileListForMatchRecursionScan(FILE_PATH_DIR, "\\S+.kt");

        Charset codedFormat;
        String data;
        for (File file : fileList) {
            try {
                codedFormat = JtlwFileOptionUtils.getInstance().getFileCodedFormat(file.getAbsolutePath());
                String content = JtlwFileOptionUtils.getInstance().readFileContent(file.getAbsolutePath(), codedFormat);
                if (content != null) {
                    Pattern pattern = Pattern.compile("(@JvmStatic\r\n( )+)*val instance");
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        content = content.replaceAll(pattern.pattern(), "@JvmStatic\n        val instance");
                        System.out.print(content + "\n");
                        JtlwFileOptionUtils.getInstance().writeFilContent(file.getAbsolutePath(), StandardCharsets.UTF_8, content);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
