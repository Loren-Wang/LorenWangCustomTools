package android.lorenwang.came2;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;

/**
 * 功能作用：扫描相关回调
 * 初始注释时间： 2021/5/25 17:09
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
interface ScanCallback {
    void disconnected();

    void initFail();

    void initFinish(CaptureRequest.Builder captureRequestBuilder, CameraCaptureSession captureSession);

    void onImageAvailable(ImageReader reader);
}
