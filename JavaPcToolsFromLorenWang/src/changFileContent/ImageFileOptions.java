package changFileContent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javabase.lorenwang.dataparse.JdplwJsonUtil;
import javabase.lorenwang.tools.file.JtlwFileOptionUtil;

public class ImageFileOptions {
    /**
     * 基础地址
     */
    private static final String baseFilePath = "/Users/wangliang/Desktop/imaegssssss/";

    /**
     * 原始底子
     */
    private static final String readFileDirs = "/Users/wangliang/Desktop/imaegssssss/org/";

    /**
     * map集合
     */
    private static final Map<String, String> mapNameUrl = new HashMap<>();
    private static final Map<String, String> mapUrlID = new HashMap<>();

    class DataItemBean {
        String enclos_url;
        String shiId;
    }

    public static void main(String[] args) {
        String data = JtlwFileOptionUtil.getInstance().readFileContent(baseFilePath + "/data.json", Charset.defaultCharset());
        final List<DataItemBean> list = JdplwJsonUtil.fromJsonArray(data.replace("\n", ""), DataItemBean.class);
        StringBuilder builder = new StringBuilder();
        for (DataItemBean bean : list) {
            if(bean.enclos_url != null && !"".equals(bean.enclos_url)){
                builder.append(bean.enclos_url).append("\n");
            }
            mapUrlID.put(bean.enclos_url, bean.shiId);
            mapNameUrl.put(bean.enclos_url.substring(bean.enclos_url.lastIndexOf("/") + 1), bean.enclos_url);
        }
        moveFiles();
    }

    /**
     * 文件移动
     */
    private static void moveFiles() {
        File originFile = new File(readFileDirs);
        String dir;
        String url;
        String id;
        for (File file : originFile.listFiles()) {
            url = mapNameUrl.get(file.getName());
            id = mapUrlID.get(url);
            if (url != null && id != null) {
                url = url.substring(0, url.lastIndexOf("/"));
                dir = url.substring(url.lastIndexOf("/") + 1);
                addWater(file, dir, id);
            }else {
                System.out.println(file.getAbsolutePath());
                System.out.println(url);
            }
        }
    }

    private static void addWater(File file, String dir, String id) {
        if (id != null && !"".equals(id)) {
            mark(file.getAbsolutePath(), baseFilePath + dir + "/" + file.getName(), Color.WHITE, id);
        }
    }

    /**
     * 图片添加水印
     *
     * @param srcImgPath       需要添加水印的图片的路径
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     */
    public static void mark(String srcImgPath, String outImgPath, Color markContentColor, String waterMarkContent) {
        try {
            File outFile = new File(outImgPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // Font font = new Font("Courier New", Font.PLAIN, 12);
            Font font = new Font("宋体", Font.PLAIN, 40);
            g.setColor(markContentColor); // 根据图片的背景设置水印颜色

            g.setFont(font);
            int x = (int) (srcImgWidth * 0.01F);
            int y = (int) (srcImgHeight * 0.1F);
            // int x = (srcImgWidth - getWatermarkLength(watermarkStr, g)) / 2;
            // int y = srcImgHeight / 2;
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, outFile.getAbsolutePath().substring(outFile.getAbsolutePath().lastIndexOf(".") + 1), outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取水印文字总长度
     *
     * @param waterMarkContent 水印的文字
     * @param g
     * @return 水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
}