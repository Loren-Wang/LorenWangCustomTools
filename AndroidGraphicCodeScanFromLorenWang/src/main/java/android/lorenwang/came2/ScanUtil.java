package android.lorenwang.came2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.view.Surface;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import androidx.annotation.NonNull;

/**
 * 功能作用：扫描工具
 * 初始注释时间： 2021/5/25 15:41
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
class ScanUtil {
    private final String TAG = getClass().getName();
    private static volatile ScanUtil optionsInstance;
    private CameraManager cameraManager = null;

    private ScanUtil() {

    }

    public static ScanUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (ScanUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new ScanUtil();
                }
            }
        }
        return optionsInstance;
    }

    @SuppressLint("MissingPermission")
    public void openCamera(@NotNull Context context, @NotNull final Surface surface, @NotNull final ScanCallback scanCallback,
            final CameraCaptureSession.CaptureCallback callback, final Handler handler) {
        if (cameraManager == null) {
            cameraManager = (CameraManager) context.getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
        }
        try {
            cameraManager.openCamera(String.valueOf(CameraCharacteristics.LENS_FACING_BACK), new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    try {
                        //获取构造体
                        final CaptureRequest.Builder captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequestBuilder.addTarget(surface);
                        camera.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                                try {
                                    session.setRepeatingRequest(captureRequestBuilder.build(), callback, handler);
                                } catch (CameraAccessException cameraAccessException) {
                                    cameraAccessException.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                                scanCallback.initFail();
                            }
                        }, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        scanCallback.initFail();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    scanCallback.disconnected();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    scanCallback.initFail();
                }
            }, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            scanCallback.initFail();
        }
    }


    //    /**
    //     * 解码图片
    //     *
    //     * @param characteristics 相机信息
    //     * @param data            被解码的数据
    //     */
    //    public void decode(@NotNull Size characteristics, byte[] data) {
    //characteristics.get
    //    }
    //
    //    /**
    //     * Decode the data within the viewfinder rectangle, and time how long it
    //     * took. For efficiency, reuse the same reader objects from one decode to
    //     * the next.
    //     *
    //     * @param data   The YUV preview frame.
    //     * @param width  The width of the preview frame.
    //     * @param height The height of the preview frame.
    //     */
    //    private void decode(byte[] data, int width, int height) {
    //        CameraManager cameraManager = agcslwScan.getCameraManager();
    //        if (cameraManager != null) {
    //            Camera.Size size = cameraManager.getPreviewSize();
    //            if (size != null) {
    //                // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
    //                byte[] rotatedData = new byte[data.length];
    //                for (int y = 0; y < size.height; y++) {
    //                    for (int x = 0; x < size.width; x++) {
    //                        rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
    //                    }
    //                }
    //
    //                // 宽高也要调整
    //                int tmp = size.width;
    //                size.width = size.height;
    //                size.height = tmp;
    //
    //                Result rawResult = null;
    //                PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, size.width, size.height);
    //                if (source != null && multiFormatReader != null) {
    //                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    //                    try {
    //                        rawResult = multiFormatReader.decodeWithState(bitmap);
    //                    } catch (ReaderException re) {
    //                        // continue
    //                    } finally {
    //                        multiFormatReader.reset();
    //                    }
    //                }
    //
    //                Handler handler = agcslwScan.getHandler();
    //                if (rawResult != null) {
    //                    // Don't log the barcode contents for security.
    //                    if (handler != null) {
    //                        Message message = Message.obtain(handler, SacnCameraCommon.decode_succeeded, rawResult);
    //                        Bundle bundle = new Bundle();
    //                        bundleThumbnail(source, bundle);
    //                        message.setData(bundle);
    //                        message.sendToTarget();
    //                    }
    //                } else {
    //                    if (handler != null) {
    //                        Message message = Message.obtain(handler, SacnCameraCommon.decode_failed);
    //                        message.sendToTarget();
    //                    }
    //                }
    //            }
    //        }
    //    }
}
