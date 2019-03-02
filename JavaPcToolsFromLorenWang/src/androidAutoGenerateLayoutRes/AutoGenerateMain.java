package androidAutoGenerateLayoutRes;

/**
 * 创建时间：2019-01-29 下午 15:18:11
 * 创建人：王亮（Loren wang）
 * 功能作用：自动生成安卓布局资源文件
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AutoGenerateMain {
    public static void main(String[] args){
        int baseW = 375;
        int baseH = 667;
        String addition = "";
        try {
            if (args.length >= 3) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
                addition = args[2];
            } else if (args.length >= 2) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
            } else if (args.length >= 1) {
                addition = args[0];
            }
        } catch (NumberFormatException e) {

            System.err
                    .println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;");
            e.printStackTrace();
            System.exit(-1);
        }

        new AutoLayoutResGenerate(baseW, baseH, addition);
    }
}
