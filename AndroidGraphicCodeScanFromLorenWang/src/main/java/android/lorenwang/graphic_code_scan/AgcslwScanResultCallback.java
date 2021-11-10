package android.lorenwang.graphic_code_scan;

import android.graphics.Bitmap;
import android.graphics.Rect;

import androidx.annotation.NonNull;

/**
 * 功能作用：扫描结果返回
 * 创建时间：2019-12-17 15:20
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、扫描文本结果---scanResult（result）
 * 2、扫描图片结果---scanResultBitmap（bitmap）
 * 3、扫描出错---scanError（）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class AgcslwScanResultCallback extends AgcslwCameraOptionsCallback {
    /**
     * 扫描视图裁剪矩阵变化
     *
     * @param cropRect 裁剪矩阵位置,仅相对于扫描控件scanview的坐标
     */
   public abstract void scanViewCropRectChange(@NonNull Rect cropRect);

    /**
     * 扫描文本结果
     *
     * @param result 结果内容
     */
    public abstract void scanResult(String result);

    /**
     * 返回扫描结果的位图
     *
     * @param bitmap 扫描结果位图
     */
    public void scanResultBitmap(Bitmap bitmap) {

    }

    /**
     * 扫描解码出错
     */
    public  void scanDecodeError() {

    }

    /**
     * 扫描相册图片异常
     *
     * @param path              传递的图片地址
     * @param isPathNotExists   图片地址代表的文件不存在
     * @param isScanDecodeError 扫描解码异常
     */
    public  void scanPhotoAlbumImageError(String path, boolean isPathNotExists, boolean isScanDecodeError) {

    }

}
