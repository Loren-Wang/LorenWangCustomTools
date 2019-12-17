package android.lorenwang.graphic_code_scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.lorenwang.tools.AtlwSetting;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.Result;

import java.io.IOException;

import androidx.annotation.RequiresPermission;

/**
 * 功能作用：扫描工具类
 * 创建时间：2019-12-17 12:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、开始扫描---startScan(act,scanView,surfaceView,playBeep,vibrate,scanBarCode,scanQrCode)---需要权限
 * 2、重置扫描---restartPreviewAfterDelay---需要权限
 * 3、手动对焦---manualFocus
 * 4、开启闪光灯---openFlashLight
 * 5、关闭闪光灯---closeFlashLight
 * 6、切换闪光灯状态---changeFlashLightStatus
 * 7、设置扫描结果回调---setScanResultCallback(callback)
 * 8、Activity获取焦点调用---onActResumeChange---需要权限---重要
 * 9、Activity失去焦点调用---onActPauseChange---需要权限---重要
 * 10、Activity结束销毁调用---onActFinish---需要权限---重要
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AgcslwScanUtils implements SurfaceHolder.Callback {
    private final String TAG = "AgcslwScanUtils";
    private static AgcslwScanUtils optionsUtils;
    /**
     * 扫描结果回调
     */
    private ScanResultCallback scanResultCallback;

    private AgcslwScanUtils() {
    }

    public static AgcslwScanUtils getInstance() {
        if (optionsUtils == null) {
            synchronized (AgcslwScanUtils.class) {
                if (optionsUtils == null) {
                    optionsUtils = new AgcslwScanUtils();
                }
            }
        }
        return optionsUtils;
    }

    //二维码控制属性
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    //界面属性
    private Rect mCropRect;
    private boolean isHasSurface = false;
    private SurfaceView sFVScan;
    /**
     * 扫描内容view
     */
    private View scanView;
    /**
     * 扫描类型
     */
    private int decodeMode = DecodeThread.ALL_MODE;
    /**
     * 闪光灯状态，默认关闭
     */
    private boolean flashLightStatus = false;


    /**
     * Activity获取焦点
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public void onActResumeChange() {
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(AtlwSetting.nowApplication);
        handler = null;
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(sFVScan.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            sFVScan.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    /**
     * 当Activity失去焦点
     */
    public void onActPauseChange() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (inactivityTimer != null) {
            inactivityTimer.onPause();
        }
        if (beepManager != null) {
            beepManager.close();
        }
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
        if (!isHasSurface) {
            sFVScan.getHolder().removeCallback(this);
        }
    }

    /**
     * 当Activity结束
     */
    public void onActFinish() {
        if (cameraManager != null) {
            cameraManager.release();
            cameraManager = null;
        }
        if (handler != null) {
            handler.release();
            handler = null;
        }
        if (inactivityTimer != null) {
            inactivityTimer.release();
            inactivityTimer = null;
        }
        if (beepManager != null) {
            beepManager.release();
            beepManager = null;
        }
        sFVScan = null;
        mCropRect = null;
    }

    /**
     * 开始扫描
     *
     * @param activity    activity实例
     * @param scanView    扫描区域view
     * @param sFVScan     surfaceview
     * @param playBeep    扫描结束是否播放声音
     * @param vibrate     扫描结束后是否震动
     * @param scanBarCode 是否扫描条形码
     * @param scanQrCode  是否扫描二维码
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public void startScan(Activity activity, View scanView, SurfaceView sFVScan,
                          boolean playBeep, boolean vibrate,
                          boolean scanQrCode, boolean scanBarCode) {
        this.sFVScan = sFVScan;
        this.scanView = scanView;
        //初始化二维码控制属性
        inactivityTimer = new InactivityTimer(activity);
        beepManager = new BeepManager(activity);
        //设置是否播放声音
        beepManager.setPlayBeep(playBeep);
        //设置是否震动
        beepManager.setVibrate(vibrate);
        //设置扫描类型
        if (!scanBarCode && scanQrCode) {
            decodeMode = DecodeThread.QRCODE_MODE;
        } else if (scanBarCode && !scanQrCode) {
            decodeMode = DecodeThread.BARCODE_MODE;
        } else {
            decodeMode = DecodeThread.ALL_MODE;
        }
    }

    /**
     * 重置扫描
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public void restartPreviewAfterDelay() {
        if (handler != null) {
            handler.sendEmptyMessage(SacnCameraCommon.restart_preview);
        }
    }

    /**
     * 手动对焦
     */
    public void manualFocus() {
        if (cameraManager != null) {
            cameraManager.manualFocus();
        }
    }

    /**
     * 开启闪光灯
     */
    public void openFlashLight() {
        if (cameraManager != null) {
            flashLightStatus = cameraManager.openFlashLight();
        }
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlashLight() {
        if (cameraManager != null) {
            flashLightStatus = !cameraManager.openFlashLight();
        }
    }

    /**
     * 修改闪光灯状态
     */
    public void changeFlashLightStatus() {
        if (cameraManager != null) {
            if (flashLightStatus) {
                closeFlashLight();
            } else {
                openFlashLight();
            }
        }
    }

    /**
     * 设置扫描结果回调
     *
     * @param scanResultCallback 扫描结果回调
     */
    public void setScanResultCallback(ScanResultCallback scanResultCallback) {
        this.scanResultCallback = scanResultCallback;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    /**
     * 获取相机管理器
     *
     * @return 相机管理器
     */
    protected CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 获取处理的handle
     *
     * @return 处理的handle
     */
    protected CaptureActivityHandler getHandler() {
        return handler;
    }

    /**
     * 获取要裁剪的图片区域
     *
     * @return 要裁剪的图片区域
     */
    protected Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 扫描结果返回值
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    protected void handleDecode(final Result rawResult, final Bundle bundle) {
        if (inactivityTimer != null) {
            inactivityTimer.onActivity();
        }
        if (beepManager != null) {
            beepManager.playBeepSoundAndVibrate();
        }
        if (rawResult != null && rawResult.getText() != null) {
            String resultText = rawResult.getText();
            AtlwLogUtils.logD(TAG, "扫描结果:::" + resultText);
            if (scanResultCallback != null) {
                scanResultCallback.scanResult(resultText);
            }
        } else {
            if (scanResultCallback != null) {
                scanResultCallback.scanError();
            }
        }
    }

    /**
     * 设置返回
     *
     * @param resultOk
     * @param obj
     */
    protected void setResult(int resultOk, Intent obj) {
    }


    /*********************************************相机初始化***************************************/
    /**
     * 相机初始化
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            AtlwLogUtils.logD(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(cameraManager, decodeMode);
            }

            initCrop();
        } catch (IOException ioe) {
            AtlwLogUtils.logD(TAG, ioe.getMessage());
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            AtlwLogUtils.logD(TAG, "Unexpected error initializing camera", e);
        }
    }

    /**
     * 初始化扫描截取区域
     */
    private void initCrop() {
        //判断是否有初始化设置
        if (scanView == null || sFVScan == null) {
            mCropRect = new Rect(0, 0, 0, 0);
            return;
        }
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - AtlwScreenUtils.getInstance().getStatusBarHeight();

        int cropWidth = scanView.getWidth();
        int cropHeight = scanView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = sFVScan.getWidth();
        int containerHeight = sFVScan.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }
}
