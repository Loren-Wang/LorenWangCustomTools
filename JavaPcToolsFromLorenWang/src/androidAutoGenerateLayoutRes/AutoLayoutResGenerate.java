package androidAutoGenerateLayoutRes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 自动布局生成工具，用来生成自动布局的res资源文件，其中会将传入的所有分辨率再生成一套高度减一的默认资源宽高使用的单位是dp为单位
 *
 * @author wangliang
 */
public class AutoLayoutResGenerate {

    //默认分辨率（已基本包含所有）
//	private final String SUPPORT_DIMESION = "1080,2030;";
    private final String SUPPORT_DIMESION = "480,800;480,854;540,960;600,1024;720,1184;"
            + "720,1196;720,1280;768,1024;800,1280;1080,1776;1080,1812;1080,1920;1440,2560;"
            + "1080,2160;1440,2960;1080,2040;1080,2048;2160,3840;1440,2880;1440,2560;900,1440;"
            + "769,1280;1200,1920;1080,2030;";
    //生成的目标资源文件宽度格式
    private final String WTemplate = "    <dimen name=\"x{0}\">{1}px</dimen>\n";
    //生成的目标资源文件高度格式
    private final String HTemplate = "    <dimen name=\"y{0}\">{1}px</dimen>\n";
    //生成的目标资源文件宽度格式(默认资源文件)
    private final String ValuesdefaultWTemplate = "    <dimen name=\"x{0}\">{1}dp</dimen>\n";
    //生成的目标资源文件高度格式(默认资源文件)
    private final String ValuesdefaultHTemplate = "    <dimen name=\"y{0}\">{1}dp</dimen>\n";
    //生成的目标资源文件的文件夹名称
    private final String VALUE_TEMPLATE = "values-{0}x{1}";


    private int baseW;//要生成的基础分辨率宽度
    private int baseH;//要生成的基础分辨率高度
    private String supportStr = SUPPORT_DIMESION;//要生成的目标分辨率列表
    private String dirStr = "autoLayoutResGenerate/res";//生成的文件的目标存储路径（pc中的）


    /**
     * @param baseX      要生成的基础分辨率宽度
     * @param baseY      要生成的基础分辨率高度
     * @param supportStr 要生成的所有目标的分辨率
     */
    public AutoLayoutResGenerate(int baseX, int baseY, String supportStr) {
        this.baseW = baseX;
        this.baseH = baseY;

        //判断传入的目标分辨率是否为空，为空则使用默认的
        if (supportStr != null && !supportStr.isEmpty()) {
            this.supportStr = supportStr;
        }

        //基础必须生成，如果不包含基础的则要添加基础的近期
        if (!this.supportStr.contains(baseX + "," + baseY)) {
            this.supportStr += baseX + "," + baseY + ";";
        }

        //把传过来的带逗号“，”和下划线“_”的额外支持的处理一下
        this.supportStr += validateInput(this.supportStr);

        //根据UI给的图的标准的baseW和baseY（如果没有的话默认320和400）
        System.out.println(this.supportStr);
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdir();

        }
        System.out.println(dir.getAbsoluteFile());

//        //获取基础像素密度
//        getBasedensity(baseX,baseY);

        //开始生成目标分辨率的px值文件以及相对应的高度减一的默认dp值文件
        String[] vals = this.supportStr.split(";");
        for (String val : vals) {
            String[] wh = val.split(",");
            generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
            generateDefaultXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
        }


    }


    /**
     * 格式化目标分辨率字符串中的特殊字符
     *
     * @param supportStr
     * @return
     */
    private String validateInput(String supportStr) {
        StringBuffer sb = new StringBuffer();
        String[] vals = supportStr.split("_");
        int w = -1;
        int h = -1;
        String[] wh;
        for (String val : vals) {
            try {
                if (val == null || val.trim().length() == 0)
                    continue;

                wh = val.split(",");
                w = Integer.parseInt(wh[0]);
                h = Integer.parseInt(wh[1]);
            } catch (Exception e) {
                System.out.println("skip invalidate params : w,h = " + val);
                continue;
            }
            sb.append(w + "," + h + ";");
        }

        return sb.toString();
    }

//	/**
//	 * 获取基础像素密度，判断五种density    利用PX = density * DP来计算dp
//	 * @param baseW
//	 * @param baseH
//	 */
//    private void getBasedensity(int baseW,int baseH){
//        if (baseW == 240 && baseH == 320){
//            this.basedensity = 0.75f;
//        }else if (baseW == 320 && baseH == 480){
//            this.basedensity = 1f;
//        }else if (baseW == 480 && baseH == 800){
//            this.basedensity = 1.5f;
//        }else if (baseW == 720 && baseH == 1280){
//            this.basedensity = 2f;
//        }else if (baseW == 1080 && baseH == 1920){
//            this.basedensity = 3f;
//        }else if (baseW == 1440 && baseH == 2560){
//            this.basedensity = 4f;
//        }
//    }

    /**
     * 可能是控制精度把？？？
     *
     * @param a
     * @return
     */
    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    /**
     * 生成目标资源文件
     *
     * @param w 目标宽度
     * @param h 目标高度度
     */
    private void generateXmlFile(int w, int h) {

        //输出内容（宽度）
        StringBuffer sbForWidth = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!--生成的相应的宽度的px值目标资源文件-->\n<resources>\n");
        float cellw = w * 1.0f / baseW;

        System.out.println("width : " + w + "," + baseW + "," + cellw);
        for (int i = 1; i < baseW; i++) {
            sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sbForWidth.append(WTemplate.replace("{0}", baseW + "").replace("{1}",
                w + ""));
        sbForWidth.append("</resources>");


        //输出内容（高度）
        StringBuffer sbForHeight = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!--生成的相应的高度的px值目标资源文件-->\n<resources>\n");
        float cellh = h * 1.0f / baseH;
        System.out.println("height : " + h + "," + baseH + "," + cellh);
        for (int i = 1; i < baseH; i++) {
            sbForHeight.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sbForHeight.append(HTemplate.replace("{0}", baseH + "").replace("{1}",
                h + ""));
        sbForHeight.append("</resources>");


        //开始讲内容存储
        File fileDir = new File(dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", h + "")//
                .replace("{1}", w + ""));
        fileDir.mkdirs();


        File layxFile = new File(fileDir.getAbsolutePath(), "lay_x.xml");
        File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sbForHeight.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成目标资源文件（默认情况下的）其文件夹名称是分辨率（分辨率高度减一，因为匹配不到是会向下兼容的）
     *
     * @param w 目标宽度
     * @param h 目标高度度
     */
    private void generateDefaultXmlFile(int w, int h) {
        StringBuffer sbForWidth = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!--生成的相应的dp值默认目标资源文件-->\n<resources>\n");

        float cellw = w * 1.0f / baseW;

        System.out.println("width : " + w + "," + baseW + "," + cellw);
        for (int i = 1; i < baseW; i++) {
            sbForWidth.append(ValuesdefaultWTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sbForWidth.append(ValuesdefaultWTemplate.replace("{0}", baseW + "").replace("{1}",
                w + ""));


        float cellh = h * 1.0f / baseH;
        System.out.println("height : " + h + "," + baseH + "," + cellh);
        for (int i = 1; i < baseH; i++) {
            sbForWidth.append(ValuesdefaultHTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sbForWidth.append(ValuesdefaultHTemplate.replace("{0}", baseH + "").replace("{1}",
                h + ""));
        sbForWidth.append("</resources>");

        //开始讲内容存储
        File fileDir = new File(dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", (h - 1) + "")//
                .replace("{1}", w + ""));
        fileDir.mkdirs();

        File layxFile = new File(fileDir.getAbsolutePath(), "lay_default.xml");

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth.toString());
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
