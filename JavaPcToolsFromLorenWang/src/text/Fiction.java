package text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import javabase.lorenwang.tools.file.JtlwFileOptionUtil;

/**
 * 功能作用：小说爬取
 * 初始注释时间： 2022/5/20 18:52
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
public class Fiction {
    private static int pageStart = 1;
    private static int pageEnd = 37;
    /**
     * 书籍编号
     */
    private static int textCode = 33259;

    public static void main(String[] args) {
        File savePath = new File("/Users/wangliang/Desktop/daoye.txt");
        try {
            for (int i = pageStart; i <= pageEnd; i++) {
                final Document document = Jsoup.connect("https://m.xcsg99.com/33/" + textCode + "_" + i + "/").get();
                final Elements tag = document.getElementsByClass("chapters").get(0).getElementsByTag("a");
                for (Element element : tag) {
                    String page = element.attributes().get("href");
                    page = page.substring(page.lastIndexOf("/") + 1).replace(".htm", "");
                    JtlwFileOptionUtil.getInstance().writeToFile(savePath,
                            paramsHtmlData("https://m.xcsg99.com/33_" + textCode + "-" + page + "_1.html"), "utf-8", true);
                    JtlwFileOptionUtil.getInstance().writeToFile(savePath,
                            paramsHtmlData("https://m.xcsg99.com/33_" + textCode + "-" + page + "_2.html"), "utf-8", true);
                }
            }
        } catch (Exception ignore) {

        }
    }

    private static String paramsHtmlData(String url) {
        //Jsoup解析html
        try {
            Document document = Jsoup.connect(url).get();
            final Elements tag = document.getElementById("novelcontent").getElementsByTag("p");
            final String text = tag.get(1).toString();
            try {
                if (text.indexOf("第") < text.indexOf("章")) {
                    System.out.println(text.substring(text.indexOf("第"), text.indexOf("章") + 1));
                }
            } catch (Exception ignore) {
            }
            return text.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("&nbsp;", "").replaceAll("<br><br>", "\n").replaceAll("<br>", "\n")
                    .replaceAll("本站提供最快最新的小说更新体验\\[rg\\]", "").replaceAll("\\[rg\\]", "");
        } catch (IOException e) {
            return null;
        }
    }
}
