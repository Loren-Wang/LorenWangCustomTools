package android.lorenwang.graphic_code_scan;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

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
public class AgcslwScan extends AgcslwCamera {
    private final String TAG = "AgcslwScanUtils";
    /**
     * 扫描结果回调
     */
    private AgcslwScanResultCallback scanResultCallback;
    //二维码控制属性
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    /**
     * 扫描内容view
     */
    private View scanView;
    /**
     * 扫描类型
     */
    private int decodeMode = DecodeThread.ALL_MODE;
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

    @RequiresPermission(Manifest.permission.CAMERA)
    @Override
    public boolean onActResumeChange() {
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        handler = null;
        if (super.onActResumeChange()) {
            inactivityTimer.onResume();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当Activity失去焦点
     */
    @Override
    public void onActPauseChange() {
        super.onActPauseChange();
        if (inactivityTimer != null) {
            inactivityTimer.onPause();
        }
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (beepManager != null) {
            beepManager.close();
        }
    }

    /**
     * 当Activity结束
     */
    @Override
    public void onActFinish() {
        super.onActFinish();
        if (inactivityTimer != null) {
            inactivityTimer.release();
            inactivityTimer = null;
        }
        if (handler != null) {
            handler.release();
            handler = null;
        }
        if (beepManager != null) {
            beepManager.release();
            beepManager = null;
        }
        showCropRect = null;
        imageCropRect = null;
        scanView = null;
    }


    /**
     * 开始扫描
     *
     * @param activity activity实例
     * @param sFVScan  surfaceView
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean startScan(Activity activity, SurfaceView sFVScan) {
        return startScan(activity, sFVScan, true, true, true, true, true, 90);
    }

    /**
     * 开始扫描
     *
     * @param activity         activity实例
     * @param sFVScan          surfaceView
     * @param playBeep         扫描结束是否播放声音
     * @param vibrate          扫描结束后是否震动
     * @param scanQrCode       是否扫描二维码
     * @param scanBarCode      是否扫描条形码
     * @param returnScanBitmap 是否返回扫描结果的位图
     * @param degree           屏幕方向角度
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean startScan(Activity activity, SurfaceView sFVScan, boolean playBeep, boolean vibrate, boolean scanQrCode, boolean scanBarCode,
            boolean returnScanBitmap, int degree) {
        if (setCameraConfig(activity, sFVScan, degree)) {
            //权限检测
            if (!checkPermissions(Manifest.permission.CAMERA)) {
                return false;
            }
            //初始化二维码控制属性
            inactivityTimer = new InactivityTimer(activity);
            this.returnScanBitmap = returnScanBitmap;
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
        } else {
            return false;
        }
    }

    /**
     * 重置扫描
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean restartPreviewAfterDelay() {
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        if (handler != null) {
            handler.sendEmptyMessage(ScanCameraCommon.restart_preview);
        }
        return true;
    }


    /*-----------------------------------公开方法-----------------------------------*/

    /**
     * 扫描相册图片
     *
     * @param path 图片地址
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
                scanBitmap.getPixels(intArray, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
                RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), intArray);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                DecodeThread decodeThread = new DecodeThread(decodeMode, this);
                Result result = reader.decode(bitmap1, decodeThread.getHints());
                if (returnScanBitmap) {
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("barcode_bitmap", AtlwImageCommonUtil.getInstance().getBitmapBytes(scanBitmap));
                    handleDecode(result, bundle);
                } else {
                    handleDecode(result, null);
                }
                decodeThread.release();
                reader.release();
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
     * 扫描视图裁剪矩阵变化,回调使用
     *
     * @param rect 裁剪矩阵位置,仅相对于扫描控件scanView的坐标
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
     * @param sFVScan       surfaceView
     * @return 显示的裁剪区域
     */
    public Rect parseShowCropRect(float leftPercent, float topPercent, float rightPercent, float bottomPercent, boolean square, SurfaceView sFVScan) {
        //如果使用的适scanView的话则裁剪区域为裁剪扫描控件属性
        if (showCropRect == null && scanView == null) {
            int cropWidth = (int) (getBaseWidth(sFVScan) * (1 - leftPercent - rightPercent));
            int cropHeight = (int) (getBaseHeight(sFVScan) * (1 - topPercent - bottomPercent));
            //绘制阴影区域
            int cropLeft = (int) (getBaseWidth(sFVScan) * leftPercent);
            int cropTop = (int) (getBaseHeight(sFVScan) * topPercent);
            if (square) {
                cropWidth = cropHeight = Math.min(cropWidth, cropHeight);
                cropLeft += (getBaseWidth(sFVScan) * (1 - leftPercent - rightPercent) - cropWidth) / 2;
                cropTop += (getBaseHeight(sFVScan) * (1 - topPercent - bottomPercent) - cropHeight) / 2;
            }
            return showCropRect = new Rect(cropLeft, cropTop, cropLeft + cropWidth, cropTop + cropHeight);
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


    /*-----------------------------------同一文件下其他类调用方法-----------------------------------*/

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
            AtlwLogUtil.logUtils.logD(TAG, "扫描结果:::" + resultText);
            if (scanResultCallback != null) {
                scanResultCallback.scanResult(resultText);
                if (returnScanBitmap) {
                    try {
                        byte[] barcodeBitmaps = bundle.getByteArray("barcode_bitmap");
                        assert barcodeBitmaps != null;
                        scanResultCallback.scanResultBitmap(BitmapFactory.decodeByteArray(barcodeBitmaps, 0, barcodeBitmaps.length));
                    } catch (Exception e) {
                        AtlwLogUtil.logUtils.logD("处理返回的扫描结果位图数据异常：" + (e.getMessage() != null ? e.getMessage() : ""));
                    }
                }
            }

        } else {
            if (scanResultCallback != null) {
                scanResultCallback.scanDecodeError();
            }
        }
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
        CameraManager cameraManager = getCameraManager();
        try {
            cameraWidth = cameraManager.getCameraResolution() != null ?
                    degree / 90 % 2 != 0 ? cameraManager.getCameraResolution().y : cameraManager.getCameraResolution().x : getScreenWidth();
        } catch (Exception ignored) {
            cameraWidth = getScreenWidth();
        }
        try {
            cameraHeight = cameraManager.getCameraResolution() != null ?
                    degree / 90 % 2 != 0 ? cameraManager.getCameraResolution().x : cameraManager.getCameraResolution().y : getScreenHeight();
        } catch (Exception ignored) {
            cameraHeight = getScreenHeight();
        }
        //裁剪区域的属性
        if (scanView != null) {
            //获取布局中扫描框的位置信息
            int[] location = new int[2];
            scanView.getLocationInWindow(location);
            int cropLeft = location[0];
            int cropTop = location[1] - AtlwScreenUtil.getInstance().getStatusBarHeight();
            int cropWidth = getBaseWidth(scanView);
            int cropHeight = getBaseHeight(scanView);
            //修改裁剪区域为裁剪扫描框属性
            showCropRect = new Rect(cropLeft, cropTop, cropLeft + cropWidth, cropTop + cropHeight);
        } else if (showCropRect == null) {
            return;
        }
        //获取布局容器的宽高
        int containerWidth = getBaseWidth(sFVScan) > 0 ? getBaseWidth(sFVScan) : getScreenWidth();
        int containerHeight = getBaseHeight(sFVScan) > 0 ? getBaseHeight(sFVScan) : getScreenHeight();

        //计算最终截取的矩形的左上角顶点x坐标
        int x = showCropRect.left * cameraWidth / containerWidth;
        //计算最终截取的矩形的左上角顶点y坐标
        int y = showCropRect.top * cameraHeight / containerHeight;

        //计算最终截取的矩形的宽度
        int width = showCropRect.width() * cameraWidth / containerWidth;
        //计算最终截取的矩形的高度
        int height = showCropRect.height() * cameraHeight / containerHeight;

        //生成最终截取的矩形
        imageCropRect = new Rect(x, y, width + x, height + y);
    }


    /**
     * 初始化相机结束后的扫描相关
     */
    @Override
    protected void initCameraScan() throws Exception {
        super.initCameraScan();
        if (handler == null) {
            handler = new CaptureActivityHandler(getCameraManager(), decodeMode, this);
        }

        initCrop();
    }
}
