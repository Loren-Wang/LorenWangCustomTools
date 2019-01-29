package android.lorenwang.tools.image;

import android.content.Context;
import android.lorenwang.tools.base.CheckUtils;
import android.lorenwang.tools.base.LogUtils;
import android.lorenwang.tools.file.FileOptionUtils;
import android.util.Base64;


/**
 * 创建时间：2018-11-16 上午 10:15:2
 * 创建人：王亮（Loren wang）
 * 功能作用：图片处理通用类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class ImageCommonUtils {
    private final String TAG = getClass().getName();
    private static ImageCommonUtils imageCommonUtils;
    public static ImageCommonUtils getInstance(){
        if(imageCommonUtils == null){
            imageCommonUtils = new ImageCommonUtils();
        }
        return imageCommonUtils;
    }


    /**
     * 将图片文件转换为base64字符串
     * @param filePath
     * @return
     */
    public String imageFileToBase64String(Context context,String filePath){
        if(CheckUtils.getInstance().checkFileOptionsPermisstion(context)
                && CheckUtils.getInstance().checkFileIsExit(filePath)
                && CheckUtils.getInstance().checkFileIsImage(filePath)){
            LogUtils.logI(TAG,"图片文件地址有效性检测成功，开始获取文件字节");
            byte[] bytes = FileOptionUtils.Companion.getInstance()
                    .readImageFileGetBytes(context, false, false, filePath);
            String base64Str = null;
            if(bytes != null) {
                base64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
                LogUtils.logI(TAG, "图片转换成功，转换后数据：" + base64Str);
            }else {
                LogUtils.logI(TAG, "图片转换失败,失败原因：文件读取异常");
            }
            return base64Str;
        }else {
            LogUtils.logI(TAG,"图片文件转换失败，失败原因可能是以下几点：1、未拥有文件权限；2、文件不存在；3、传输的地址非图片地址");
            return null;
        }
    }
}
