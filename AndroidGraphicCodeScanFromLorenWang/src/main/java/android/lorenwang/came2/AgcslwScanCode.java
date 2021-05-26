package android.lorenwang.came2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * 功能作用：Code扫描
 * 初始注释时间： 2021/5/25 17:17
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
class AgcslwScanCode {

    private final CameraOptions cameraOptions = new CameraOptions();

    private CameraManager cameraManager = null;

    @SuppressLint("MissingPermission")
    public void openCamera(@NotNull Activity activity, @NotNull final TextureView textureView, int previewWidth, int previewHeight,
            @NotNull final ScanCallback scanCallback, final CameraCaptureSession.CaptureCallback callback, final Handler handler) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.e("openCamera", "没有相关权限，无法开启");
            return;
        }
        //获取相机管理器
        cameraManager = cameraOptions.getCameraManager(activity);
        //获取指定的相机信息
        CameraCharacteristics cameraCharacteristics = cameraOptions.getCameraCharacteristics(cameraManager, CameraCharacteristics.LENS_FACING_BACK);
        //获取可使用列表
        Size[] previewSize = cameraOptions.getCameraPreviewSize(cameraCharacteristics, SurfaceTexture.class);
        //是否需要调转预览方向
        boolean exchange = cameraOptions.exchangeWidthHeight(activity.getWindowManager().getDefaultDisplay().getRotation(),
                cameraOptions.getCameraSensorOrientation(cameraCharacteristics));
        exchange = true;
        //获取最佳预览大小
        Size bestSize = cameraOptions.getBestSize(exchange ? previewHeight : previewWidth, exchange ? previewWidth : previewHeight,
                exchange ? textureView.getHeight() : textureView.getWidth(), exchange ? textureView.getWidth() : textureView.getHeight(),
                previewSize);
        textureView.getSurfaceTexture().setDefaultBufferSize(bestSize.getHeight(), bestSize.getWidth());

        final ImageReader mImageReader = ImageReader.newInstance(bestSize.getWidth(), bestSize.getHeight(), ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                scanCallback.onImageAvailable(reader);
            }
        }, handler);

        //开启相机
        try {
            cameraManager.openCamera(cameraOptions.getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_BACK),
                    new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            //开启预览回话
                            try {
                                Surface surface = new Surface(textureView.getSurfaceTexture());

                                //获取构造体
                                final CaptureRequest.Builder captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                captureRequestBuilder.addTarget(surface);
                                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);


                                camera.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                                        new CameraCaptureSession.StateCallback() {
                                            @Override
                                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                                try {
                                                    session.setRepeatingRequest(captureRequestBuilder.build(), callback, handler);
                                                    scanCallback.initFinish(captureRequestBuilder, session);
                                                } catch (CameraAccessException cameraAccessException) {
                                                    scanCallback.initFail();
                                                }
                                            }

                                            @Override
                                            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                                                scanCallback.initFail();
                                            }
                                        }, handler);
                            } catch (CameraAccessException e) {
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
        } catch (Exception ignore) {
            Log.e("openCamera", "相机开启失败");
            scanCallback.initFail();
        }


        //
        //
        //        //要使用的相机id
        //        final String cameraId = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
        //
        //        try {
        //            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
        //                @Override
        //                public void onOpened(@NonNull CameraDevice camera) {

        //                }
        //
        //                @Override
        //                public void onDisconnected(@NonNull CameraDevice camera) {
        //                    scanCallback.disconnected();
        //                }
        //
        //                @Override
        //                public void onError(@NonNull CameraDevice camera, int error) {
        //                    scanCallback.initFail();
        //                }
        //            }, handler);
        //        } catch (CameraAccessException e) {
        //            e.printStackTrace();
        //            scanCallback.initFail();
        //        }
    }
}
