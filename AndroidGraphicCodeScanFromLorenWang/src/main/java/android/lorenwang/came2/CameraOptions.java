package android.lorenwang.came2;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

 class CameraOptions {
    private final String TAG = "CameraOptions";

    /**
     * 获取摄像机管理器
     *
     * @param context 上下文
     * @return 管理器，可能为空
     */
    public CameraManager getCameraManager(@NotNull Context context) {
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            if (cameraIdList == null || cameraIdList.length == 0) {
                Log.i(TAG, "没有可用相机");
                return null;
            }
            return mCameraManager;
        } catch (CameraAccessException e) {
            Log.e(TAG, "相机获取失败");
            return null;
        }
    }

    /**
     * 获取要使用的相机id
     *
     * @param cameraManager 相机管理器
     * @param type          使用的方向类型
     * @return 要使用的相机id
     */
    public String getCameraId(@NotNull CameraManager cameraManager, int type) {
        try {
            CameraCharacteristics cameraCharacteristics;
            for (String cameraId : cameraManager.getCameraIdList()) {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (type == cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)) {
                    return cameraId;
                }
            }
        } catch (Exception ignore) {
        }
        Log.i(TAG, "相机id获取失败");
        return null;
    }

    /**
     * 获取要使用的相机参数
     *
     * @param cameraManager 相机管理器
     * @param type          使用的方向类型
     * @return 要使用的相机参数
     */
    public CameraCharacteristics getCameraCharacteristics(@NotNull CameraManager cameraManager, int type) {
        try {
            CameraCharacteristics cameraCharacteristics = null;
            for (String cameraId : cameraManager.getCameraIdList()) {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (type == cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)) {
                    break;
                }
            }
            if (cameraCharacteristics != null) {
                Integer supportLevel = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                if (supportLevel != CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    return cameraCharacteristics;
                } else {
                    Log.i(TAG, "相机硬件不支持新特性");
                }
            }
        } catch (Exception ignore) {
        }
        Log.i(TAG, "相机配置参数获取失败");
        return null;
    }

    /**
     * 获取摄像头方向
     *
     * @param characteristics 摄像头信息
     * @return 方向角度
     */
    public Integer getCameraSensorOrientation(@NotNull CameraCharacteristics characteristics) {
        return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
    }

    /**
     * 获取相机的预览尺寸
     *
     * @param characteristics 摄像头信息
     * @return 预览尺寸
     */
    public <T> Size[] getCameraPreviewSize(@NotNull CameraCharacteristics characteristics, Class<T> cls) {
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        return map.getOutputSizes(cls);
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     *
     * @param displayRotation   屏幕方向
     * @param sensorOrientation 相机方向
     * @return 是否交换宽高
     */
    public boolean exchangeWidthHeight(int displayRotation, int sensorOrientation) {
        boolean exchange = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true;
                }
                break;
            default:
                break;
        }
        Log.i(TAG, "屏幕方向 " + displayRotation);
        Log.i(TAG, "相机方向 " + sensorOrientation);
        return exchange;
    }

    /**
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     *
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @param maxWidth     最大宽度(即TextureView的宽度)
     * @param maxHeight    最大高度(即TextureView的高度)
     * @param selectList   支持的Size列表
     * @return 返回与指定宽高相等或最接近的尺寸
     */
    public Size getBestSize(int targetWidth, int targetHeight, int maxWidth, int maxHeight, Size[] selectList) {
        if (selectList == null || selectList.length == 0) {
            return new Size(0, 0);
        }
        //比指定宽高大的Size列表
        List<Size> bigEnoughList = new ArrayList<>();
        //比指定宽高小的Size列表
        List<Size> notBigEnoughList = new ArrayList<>();

        for (Size size : selectList) {
            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.getWidth() <= maxWidth && size.getHeight() <= maxHeight && size.getWidth() == size.getHeight() * targetWidth / targetHeight) {
                if (size.getWidth() >= targetWidth && size.getHeight() >= targetHeight) {
                    bigEnoughList.add(size);
                } else {
                    notBigEnoughList.add(size);
                }
            }
            Log.i(TAG, "系统支持的尺寸: " + size.getWidth() + "*" + size.getHeight() + ", 比例：" + (size.getWidth() * 1.0F / size.getHeight()));
        }
        Log.i(TAG, "最大尺寸: " + maxWidth + "*" + maxHeight + ", 比例：" + (targetWidth * 1.0F / targetHeight));
        Log.i(TAG, "目标尺寸: " + targetWidth + "*" + targetHeight + ", 比例：" + (targetWidth * 1.0F / targetHeight));

        //选择bigEnough中最小的值  或 notBigEnough中最大的值
        if (bigEnoughList.size() > 0) {
            return Collections.min(bigEnoughList, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return Integer.compare(o1.getWidth(), o2.getWidth());
                }
            });
        } else if (notBigEnoughList.size() > 0) {
            return Collections.min(notBigEnoughList, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return Integer.compare(o1.getWidth(), o2.getWidth());
                }
            });
        } else {
            return selectList[0];
        }
    }
}
