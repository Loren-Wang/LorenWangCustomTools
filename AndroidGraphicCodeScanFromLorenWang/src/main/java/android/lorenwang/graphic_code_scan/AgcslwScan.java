package android.lorenwang.graphic_code_scan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 功能作用：扫描工具类
 * 创建时间：2019-12-17 12:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、开始扫描---startScan(act, sFVScan,playBeep,vibrate,scanBarCode,scanQrCode,returnScanBitmap)---需要权限
 * 2、重置扫描---restartPreviewAfterDelay---需要权限
 * 3、手动对焦---manualFocus
 * 4、开启闪光灯---openFlashLight
 * 5、关闭闪光灯---closeFlashLight
 * 6、切换闪光灯状态---changeFlashLightStatus
 * 7、设置扫描结果回调---setScanResultCallback(callback)
 * 8、Activity获取焦点调用---onActResumeChange---需要权限---重要
 * 9、Activity失去焦点调用---onActPauseChange---需要权限---重要
 * 10、Activity结束销毁调用---onActFinish---需要权限---重要
 * 11、设置描裁裁剪区域属性---setScanCropView(  scanView)
 * 12、清空扫描裁剪区域属性相关---clearScanCropRect()
 * 13、格式化显示的裁剪区域---parseShowCropRect（leftPercent,topPercent,rightPercent,bottomPercent,square,
 * surfaceView）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AgcslwScan implements SurfaceHolder.Callback {
    private final String TAG = "AgcslwScanUtils";
    /**
     * 扫描结果回调
     */
    private AgcslwScanResultCallback scanResultCallback;

    private Activity activity;
    //二维码控制属性
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
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
     * 是否返回扫描结果的位图
     */
    private boolean returnScanBitmap = false;
    /**
     * 显示的裁剪区域
     */
    private Rect showCropRect;
    /**
     * 实际图片裁剪区域
     */
    private Rect imageCropRect;

    /***************************************扫描主方法**********************************************/
    /**
     * Activity获取焦点
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean onActResumeChange() {
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(AtlwConfig.nowApplication);
        handler = null;
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(sFVScan.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            if (sFVScan != null && sFVScan.getHolder() != null) {
                sFVScan.getHolder().addCallback(this);
            }
        }

        inactivityTimer.onResume();
        return true;
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
        if (!isHasSurface && sFVScan != null && sFVScan.getHolder() != null) {
            sFVScan.getHolder().removeCallback(this);
        }
        if (flashLightStatus) {
            closeFlashLight();
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
        if (sFVScan != null && sFVScan.getHolder() != null) {
            sFVScan.getHolder().removeCallback(this);
            sFVScan = null;
        }
        showCropRect = null;
        imageCropRect = null;
        scanView = null;
    }

    /**
     * 开始扫描
     *
     * @param activity         activity实例
     * @param sFVScan          surfaceview
     * @param playBeep         扫描结束是否播放声音
     * @param vibrate          扫描结束后是否震动
     * @param scanQrCode       是否扫描二维码
     * @param scanBarCode      是否扫描条形码
     * @param returnScanBitmap 是否返回扫描结果的位图
     * @return
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean startScan(Activity activity, SurfaceView sFVScan,
                             boolean playBeep, boolean vibrate,
                             boolean scanQrCode, boolean scanBarCode,
                             boolean returnScanBitmap) {
        this.activity = activity;
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        this.sFVScan = sFVScan;
        this.returnScanBitmap = returnScanBitmap;
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
        return true;
    }

    /**
     * 重置扫描
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean restartPreviewAfterDelay() {
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        if (handler != null) {
            handler.sendEmptyMessage(SacnCameraCommon.restart_preview);
        }
        return true;
    }


    /*****************************************公开方法**********************************************/

    /**
     * 扫描相册图片
     *
     * @param path 图片地址
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public boolean scanPhotoAlbumImage(String path) {
        if (checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) && scanResultCallback != null) {
            //有权限继续处理
            if (path == null || !new File(path).exists()) {
                //地址为空或者文件不存在
                scanResultCallback.scanPhotoAlbumImageError(path, true, false);
                return false;
            }
            try {
                //文件正常，开始扫描
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false; // 获取新的大小
                int sampleSize = (int) (options.outHeight / (float) 200);
                if (sampleSize <= 0) {
                    sampleSize = 1;
                }
                options.inSampleSize = sampleSize;
                Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
                int[] intArray = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
                scanBitmap.getPixels(intArray, 0, scanBitmap.getWidth(), 0, 0,
                        scanBitmap.getWidth(), scanBitmap.getHeight());
                RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(),
                        scanBitmap.getHeight(), intArray);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                DecodeThread decodeThread = new DecodeThread(decodeMode, this);
                Result result = reader.decode(bitmap1, decodeThread.getHints());
                if (returnScanBitmap) {
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("barcode_bitmap",
                            AtlwImageCommonUtils.getInstance().getBitmapBytes(scanBitmap));
                    handleDecode(result, bundle);
                } else {
                    handleDecode(result, null);
                }
                decodeThread.release();
                decodeThread = null;
                reader.release();
                reader = null;
                return true;
            } catch (Exception e) {
                scanResultCallback.scanPhotoAlbumImageError(path, false, true);
            }
        }
        return false;
    }

    /**
     * 生成二维码
     *
     * @param str        二维码字符串
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoBitmap logo图片位图
     * @return 生成的二维码位图
     */
    public Bitmap generateQrCode(String str, int width, int height, Bitmap logoBitmap) {
        return CodeCreator.createQrCode(str, width, height, logoBitmap);
    }

    /**
     * 生成条形码
     *
     * @param content 条形码内容
     * @param width   条形码宽度
     * @param height  条形码高度
     * @return 条形码位图
     */
    public Bitmap generateBarCode(String content, int width, int height) {
        return CodeCreator.createBarCode(content, width, height);
    }

    /**
     * 设置扫描结果回调
     *
     * @param scanResultCallback 扫描结果回调
     */
    public void setScanResultCallback(AgcslwScanResultCallback scanResultCallback) {
        this.scanResultCallback = scanResultCallback;
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
     * 扫描视图裁剪矩阵变化,回调使用
     *
     * @param rect 裁剪矩阵位置,仅相对于扫描控件scanview的坐标
     */
    public void scanViewCropRectChange(@NonNull Rect rect) {
        if (scanResultCallback != null) {
            scanResultCallback.scanViewCropRectChange(rect);
        }
    }

    /**
     * 格式化显示的裁剪区域
     *
     * @param leftPercent   左侧百分比
     * @param topPercent    顶部百分比
     * @param rightPercent  右侧百分比
     * @param bottomPercent 底部百分比
     * @param square        是否是证方形
     * @param sFVScan       surfaceview
     * @return 显示的裁剪区域
     */
    public Rect parseShowCropRect(float leftPercent, float topPercent, float rightPercent,
                                  float bottomPercent, boolean square, SurfaceView sFVScan) {
        //如果使用的适scanview的话则裁剪区域为裁剪扫描控件属性
        if (showCropRect == null && scanView == null) {
            int cropWidth = (int) (getBaseWidth(sFVScan) * (1 - leftPercent - rightPercent));
            int cropHeight = (int) (getBaseHeight(sFVScan) * (1 - topPercent - bottomPercent));
            if (square) {
                cropWidth = cropHeight = Math.min(cropWidth, cropHeight);
            }

            //绘制阴影区域
            int cropLeft = (int) (getBaseWidth(sFVScan) * leftPercent);
            int cropTop = (int) (getBaseHeight(sFVScan) * topPercent);
            return showCropRect = new Rect(cropLeft, cropTop, cropLeft + cropWidth,
                    cropTop + cropHeight);
        } else {
            return showCropRect;
        }
    }

    /**
     * 设置裁剪扫描区域属性
     *
     * @param scanView 扫描区域view
     */
    public void setScanCropView(View scanView) {
        if (scanView != null) {
            clearScanCropRect();
            this.scanView = scanView;
        }
    }

    /**
     * 清空扫描裁剪区域属性相关
     */
    public void clearScanCropRect() {
        this.scanView = null;
        imageCropRect = null;
        showCropRect = null;
    }

    /***************************************导入接口方法********************************************/

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

    /**********************************同一文件下其他类调用方法**************************************/
    /**
     * 获取要裁剪的图片区域
     *
     * @return 要裁剪的图片区域
     */
    protected Rect getCropRect() {
        if (imageCropRect == null) {
            initCrop();
        }
        return imageCropRect;
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
            AtlwLogUtils.logUtils.logD(TAG, "扫描结果:::" + resultText);
            if (scanResultCallback != null) {
                scanResultCallback.scanResult(resultText);
                if (returnScanBitmap) {
                    try {
                        byte[] barcodeBitmaps = bundle.getByteArray("barcode_bitmap");
                        assert barcodeBitmaps != null;
                        scanResultCallback.scanResultBitmap(BitmapFactory.decodeByteArray(barcodeBitmaps, 0, barcodeBitmaps.length));
                    } catch (Exception e) {
                        AtlwLogUtils.logUtils.logD("处理返回的扫描结果位图数据异常：" + (e.getMessage() != null ?
                                e.getMessage() : ""));
                    }
                }
            }

        } else {
            if (scanResultCallback != null) {
                scanResultCallback.scanDecodeError();
            }
        }
    }

    /****************************************私有方法**********************************************/

    /**
     * 获取基础宽度
     *
     * @param view 控件
     * @return 基础宽度
     */
    private int getBaseWidth(View view) {
        if (view == null) {
            return AtlwScreenUtils.getInstance().getScreenWidth();
        }
        return view.getMeasuredWidthAndState() > 0 ? view.getMeasuredWidthAndState() :
                AtlwScreenUtils.getInstance().getScreenWidth();
    }

    /**
     * 获取基础高度
     *
     * @param view 控件
     * @return 基础高度
     */
    private int getBaseHeight(View view) {
        if (view == null) {
            return AtlwScreenUtils.getInstance().getScreenHeight();
        }
        return view.getMeasuredHeightAndState() > 0 ? view.getMeasuredHeightAndState() :
                AtlwScreenUtils.getInstance().getScreenHeight();
    }

    /**
     * 初始化扫描截取区域
     */
    private void initCrop() {
        //判断是否有初始化设置
        if (sFVScan == null) {
            return;
        }

        //获取相机像素属性
        int cameraWidth;
        int cameraHeight;
        try {
            cameraWidth = cameraManager.getCameraResolution() != null ?
                    cameraManager.getCameraResolution().y :
                    AtlwScreenUtils.getInstance().getScreenWidth();
        } catch (Exception ignored) {
            cameraWidth = AtlwScreenUtils.getInstance().getScreenWidth();
        }
        try {
            cameraHeight = cameraManager.getCameraResolution() != null ?
                    cameraManager.getCameraResolution().x :
                    AtlwScreenUtils.getInstance().getScreenHeight();
        } catch (Exception ignored) {
            cameraHeight = AtlwScreenUtils.getInstance().getScreenHeight();
        }
        //裁剪区域的属性
        if (scanView != null) {
            //获取布局中扫描框的位置信息
            int[] location = new int[2];
            scanView.getLocationInWindow(location);
            int cropLeft = location[0];
            int cropTop = location[1] - AtlwScreenUtils.getInstance().getStatusBarHeight();
            int cropWidth = getBaseWidth(scanView);
            int cropHeight = getBaseHeight(scanView);
            //修改裁剪区域为裁剪扫描框属性
            showCropRect = new Rect(cropLeft, cropTop, cropLeft + cropWidth, cropTop + cropHeight);
        } else if (showCropRect == null) {
            return;
        }
        //获取布局容器的宽高
        int containerWidth = getBaseWidth(sFVScan) > 0 ? getBaseWidth(sFVScan) :
                AtlwScreenUtils.getInstance().getScreenWidth();
        int containerHeight = getBaseHeight(sFVScan) > 0 ? getBaseHeight(sFVScan) :
                AtlwScreenUtils.getInstance().getScreenHeight();

        //计算最终截取的矩形的左上角顶点x坐标
        int x = showCropRect.left * cameraWidth / containerWidth;
        //计算最终截取的矩形的左上角顶点y坐标
        int y = showCropRect.top * cameraHeight / containerHeight;

        //计算最终截取的矩形的宽度
        int width = showCropRect.width() * cameraWidth / containerWidth;
        //计算最终截取的矩形的高度
        int height = showCropRect.height() * cameraHeight / containerHeight;

        //生成最终的截取的矩形
        imageCropRect = new Rect(x, y, width + x, height + y);
    }

    /**
     * 相机初始化
     *
     * @param surfaceHolder surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            AtlwLogUtils.logUtils.logD(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(cameraManager, decodeMode, this);
            }

            initCrop();
        } catch (IOException ioe) {
            AtlwLogUtils.logUtils.logD(TAG, ioe.getMessage());
            if (scanResultCallback != null) {
                scanResultCallback.cameraInitError();
            }
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            AtlwLogUtils.logUtils.logD(TAG, "Unexpected error initializing camera", e);
            if (scanResultCallback != null) {
                scanResultCallback.cameraInitError();
            }
        }
    }

    /**
     * 检测权限
     */
    private boolean checkPermissions(String... permissions) {
        if (activity == null) {
            scanResultCallback.notPermissions(false, permissions);
            return false;
        } else {
            List<String> notPermissions = new ArrayList<>();
            //是否能显示自定义权限弹窗
            boolean shouldShowRequestPermissionRationale = false;
            for (String permission : permissions) {
                shouldShowRequestPermissionRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || shouldShowRequestPermissionRationale;
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    notPermissions.add(permission);
                }
            }
            if (notPermissions.isEmpty()) {
                return true;
            } else {
                scanResultCallback.notPermissions(shouldShowRequestPermissionRationale,
                        notPermissions.toArray(new String[0]));
                return false;
            }
        }
    }
}
