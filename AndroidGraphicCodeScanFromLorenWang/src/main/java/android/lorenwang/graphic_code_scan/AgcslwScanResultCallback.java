package android.lorenwang.graphic_code_scan;

import android.graphics.Bitmap;

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

public interface AgcslwScanResultCallback {
    /**
     * 扫描文本结果
     *
     * @param result 结果内容
     */
    void scanResult(String result);

    /**
     * 返回扫描结果的位图
     *
     * @param bitmap 扫描结果位图
     */
    void scanResultBitmap(Bitmap bitmap);

    /**
     * 扫描出错
     */
    void scanError();

    /**
     * 无扫描权限
     *
     * @param permissions 权限集合
     */
    void notPermissions(String... permissions);
}
