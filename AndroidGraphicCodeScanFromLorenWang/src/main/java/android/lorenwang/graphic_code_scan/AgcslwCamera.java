package android.lorenwang.graphic_code_scan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 功能作用：相机处理使用
 * 初始注释时间： 2021/8/18 11:33
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 手动对焦---manualFocus
 * 开启闪光灯---openFlashLight
 * 关闭闪光灯---closeFlashLight
 * 切换闪光灯状态---changeFlashLightStatus
 * 设置扫描结果回调---setOptionsCallback(callback)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AgcslwCamera implements SurfaceHolder.Callback {
    private final String TAG = "AgcslwCamera";
    /**
     * 拍照code
     */
    private final int TAKE_PHOTO_CODE = 1;
    /**
     * 系统数据处理
     */
    private final int TAKE_PHOTO_SYS_DATA_PARAMS_CODE = 2;
    /**
     * 页面实例
     */
    private Activity activity;
    /**
     * 相机管理器
     */
    private CameraManager cameraManager;
    /**
     * 扫描加载的控件
     */
    protected SurfaceView sFVScan;
    /**
     * 闪光灯状态，默认关闭
     */
    private boolean flashLightStatus = false;
    /**
     * 屏幕方向角度
     */
    protected int degree;
    /**
     * 是否设置了surface
     */
    private boolean isHasSurface = false;
    /**
     * 操作回调
     */
    private AgcslwCameraOptionsCallback optionsCallback;

    /**
     * 操作handler
     */
    private final Handler optionsHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //系统返回拍照数据
                case TAKE_PHOTO_CODE:
                    paramsCameraTakePhotoData((byte[]) msg.obj, this, TAKE_PHOTO_SYS_DATA_PARAMS_CODE);
                    break;
                //接收处理后的系统照片数据
                case TAKE_PHOTO_SYS_DATA_PARAMS_CODE:
                    if (optionsCallback != null) {
                        byte[] data = (byte[]) msg.obj;
                        try {
                            optionsCallback.takePhotoCurrent(BitmapFactory.decodeByteArray(data, 0, data.length));
                        } catch (Exception ignored) {
                            optionsCallback.takePhotoCurrent(null);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
     * 设置回调
     *
     * @param optionsCallback 回调
     */
    public void setOptionsCallback(AgcslwCameraOptionsCallback optionsCallback) {
        this.optionsCallback = optionsCallback;
    }

    /**
     * 获取相机管理器
     *
     * @return 相机管理器
     */
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 获取方向
     */
    public int getDegree() {
        return degree;
    }

    /**
     * 获取当前预览图片
     */
    public void takePhoto() {
        cameraManager.requestPreviewFrame(optionsHandler, TAKE_PHOTO_CODE);
    }

    /*-----------------------------------扫描主方法-----------------------------------*/

    /**
     * Activity获取焦点
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
        // wrong size and partially offscreen.
        cameraManager = new CameraManager(AtlwConfig.nowApplication, degree);
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. There for e
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(sFVScan.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            if (sFVScan != null && sFVScan.getHolder() != null) {
                sFVScan.getHolder().addCallback(this);
            }
        }

        return true;
    }

    /**
     * 当Activity失去焦点
     */
    public void onActPauseChange() {
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
        if (sFVScan != null && sFVScan.getHolder() != null) {
            sFVScan.getHolder().removeCallback(this);
            sFVScan = null;
        }
    }

    /**
     * 初始化相机配置
     *
     * @param activity activity实例
     * @param sFVScan  surfaceView
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean setCameraConfig(Activity activity, SurfaceView sFVScan) {
        return setCameraConfig(activity, sFVScan, 90);
    }

    /**
     * 初始化相机配置
     *
     * @param activity activity实例
     * @param sFVScan  surfaceView
     * @param degree   屏幕方向角度
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public boolean setCameraConfig(Activity activity, SurfaceView sFVScan, int degree) {
        this.activity = activity;
        this.degree = degree;
        //权限检测
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            return false;
        }
        this.sFVScan = sFVScan;
        return true;
    }

    /**
     * 检测权限
     */
    protected boolean checkPermissions(String... permissions) {
        if (activity == null) {
            optionsCallback.notPermissions(false, permissions);
            return false;
        } else {
            List<String> notPermissions = new ArrayList<>();
            //是否能显示自定义权限弹窗
            boolean shouldShowRequestPermissionRationale = false;
            for (String permission : permissions) {
                shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) ||
                        shouldShowRequestPermissionRationale;
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    notPermissions.add(permission);
                }
            }
            if (notPermissions.isEmpty()) {
                return true;
            } else {
                optionsCallback.notPermissions(shouldShowRequestPermissionRationale, notPermissions.toArray(new String[0]));
                return false;
            }
        }
    }

    /**
     * 初始化相机结束后的扫描相关
     */
    protected void initCameraScan() throws Exception {}

    /**
     * 格式化相机图片数据
     *
     * @param data    系统返回的相机图片数据
     * @param handler 要接收数据的handler
     * @param code    要接受数据的code
     */
    protected void paramsCameraTakePhotoData(byte[] data, @NotNull Handler handler, int code) {
        if (cameraManager != null) {
            Camera.Size size = cameraManager.getPreviewSize();
            if (size != null) {
                Message message = Message.obtain();
                message.what = code;
                if (degree / 90 % 2 != 0) {
                    // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
                    byte[] rotatedData = new byte[data.length];
                    for (int y = 0; y < size.height; y++) {
                        for (int x = 0; x < size.width; x++) {
                            rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
                        }
                    }
                    message.obj = rotatedData;
                    message.arg1 = size.height;
                    message.arg2 = size.width;
                } else {
                    message.obj = data;
                    message.arg1 = size.width;
                    message.arg2 = size.height;
                }
                handler.sendMessage(message);
            }
        }
    }

    /*-----------------------------------私有方法-----------------------------------*/

    /**
     * 获取基础宽度
     *
     * @param view 控件
     * @return 基础宽度
     */
    protected int getBaseWidth(View view) {
        if (view == null) {
            return getScreenWidth();
        }
        return view.getMeasuredWidthAndState() > 0 ? view.getMeasuredWidthAndState() : getScreenWidth();
    }

    /**
     * 获取基础高度
     *
     * @param view 控件
     * @return 基础高度
     */
    protected int getBaseHeight(View view) {
        if (view == null) {
            return getScreenHeight();
        }
        return view.getMeasuredHeightAndState() > 0 ? view.getMeasuredHeightAndState() : getScreenHeight();
    }

    /**
     * 获取当前屏幕宽度
     */
    protected int getScreenWidth() {
        return degree / 90 % 2 != 0 ? AtlwScreenUtil.getInstance().getScreenWidth() : AtlwScreenUtil.getInstance().getScreenHeight();
    }

    /**
     * 获取当前屏幕高度
     */
    protected int getScreenHeight() {
        return degree / 90 % 2 != 0 ? AtlwScreenUtil.getInstance().getScreenHeight() : AtlwScreenUtil.getInstance().getScreenWidth();
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
            AtlwLogUtil.logUtils.logD(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            initCameraScan();
            cameraManager.startPreview();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            AtlwLogUtil.logUtils.logD(TAG, "Unexpected error initializing camera", e);
            if (optionsCallback != null) {
                optionsCallback.cameraInitError();
            }
        } catch (Exception ioe) {
            AtlwLogUtil.logUtils.logD(TAG, ioe.getMessage());
            if (optionsCallback != null) {
                optionsCallback.cameraInitError();
            }
        }
    }


    /*-----------------------------------导入接口方法-----------------------------------*/

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
}
