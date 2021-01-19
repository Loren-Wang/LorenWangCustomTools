package android.lorenwang.tools.location;

import android.Manifest;
import android.lorenwang.tools.location.config.AtlwLocationConfig;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：定位基础接口
 * 创建时间：2021-01-19 10:42 上午
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
interface AtlwLocationLibraryBase {
    String TAG = "AtlwLocation";

    /**
     * 如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
     */
    String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    String[] NEED_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    /**
     * 包含后台定位权限的权限数组
     */
    String[] NEED_PERMISSIONS_AND_BACK = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
            BACKGROUND_LOCATION_PERMISSION};

    /**
     * 手机位置改变距离监听的范围
     */
    int LOCATION_DISTANCE_INTERVAL = 0;

    /**
     * 有效定位时间间隔
     */
    int LOCATION_VALID_TIME_INTERVAL = 5000;

    /**
     * 定位超时时间
     */
    int LOCATION_TIMEOUT = 15000;

    /**
     * 检测权限
     *
     * @param config 配置信息
     */
    boolean checPermissions(@NotNull AtlwLocationConfig config);

    /**
     * 使用网络定位
     *
     * @param config 配置信息
     */
    void startNetworkPositioning(@NotNull AtlwLocationConfig config);

    /**
     * 使用设备进行定位
     *
     * @param config 配置信息
     */
    void startDevicesPositioning(@NotNull AtlwLocationConfig config);

    /**
     * 使用精准定位
     *
     * @param config 定位配置信息
     */
    void startAccuratePositioning(@NotNull AtlwLocationConfig config);

    /**
     * 停止循环定位
     */
    void stopLoopPositioning();
}
