package android.lorenwang.graphic_code_scan;

/**
 * 功能作用：扫描结果返回
 * 创建时间：2019-12-17 15:20
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface ScanResultCallback {
    /**
     * 扫描结果
     *
     * @param result 结果内容
     */
    void scanResult(String result);

    /**
     * 扫描出错
     */
    void scanError();
}
