package android.lorenwang.tools.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;

import static android.lorenwang.tools.location.AtlwLocationTypeEnum.DEVICES_ACCURATE;
import static android.lorenwang.tools.location.AtlwLocationTypeEnum.DEVICES_GPS;
import static android.lorenwang.tools.location.AtlwLocationTypeEnum.DEVICES_NETWORK;

/**
 * 功能作用：默认系统模式
 * 创建时间：2021-01-19 10:44 上午
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
class AtlwLocationLibraryDefault extends AtlwLocationLibraryBase {
    /**
     * 定位管理器
     */
    private LocationManager locationManager;

    /**
     * 使用网络进行一次定位
     *
     * @param config 配置信息
     */
    @SuppressLint("MissingPermission")
    @Override
    public void startNetworkPositioning(AtlwLocationConfig config) {
        if (config != null && !mChangeListener.loopPositioning) {
            mChangeListener.type = DEVICES_NETWORK;
            mChangeListener.config = config;
            startPositioning(config, DEVICES_NETWORK);
        }
    }

    /**
     * 使用设备进行一次定位
     *
     * @param config 配置信息
     */
    @SuppressLint("MissingPermission")
    @Override
    public void startDevicesPositioning(AtlwLocationConfig config) {
        if (config != null && !mChangeListener.loopPositioning) {
            mChangeListener.type = DEVICES_GPS;
            mChangeListener.config = config;
            startPositioning(config, DEVICES_GPS);
        }
    }

    /**
     * 使用精准定位
     *
     * @param config 定位配置信息
     */
    @Override
    public void startAccuratePositioning(AtlwLocationConfig config) {
        if (config != null && !mChangeListener.loopPositioning) {
            mChangeListener.type = DEVICES_ACCURATE;
            mChangeListener.config = config;
            startPositioning(config, DEVICES_ACCURATE);
        }
    }

    /**
     * 停止循环定位
     */
    @SuppressLint("MissingPermission")
    @Override
    public synchronized void stopLoopPositioning() {
        if (getLocationManager() != null) {
            try {
                getLocationManager().removeUpdates(mChangeListener);
            } catch (Exception ignore) {
            }
        }
        mChangeListener.type = null;
        mChangeListener.config = null;
        mChangeListener.loopPositioning = false;
        AtlwLogUtil.logUtils.logI(TAG, "停止定位");
    }

    /**
     * 获取定位管理器
     *
     * @return 定位管理器，可能为空
     */
    private LocationManager getLocationManager() {
        if (locationManager == null && AtlwConfig.nowApplication != null) {
            locationManager = (LocationManager) AtlwConfig.nowApplication.getSystemService(Context.LOCATION_SERVICE);
        }
        return locationManager;
    }

    /**
     * 开启定位
     *
     * @param config 配置信息
     * @param type   定位类型
     */
    @SuppressLint("MissingPermission")
    protected void startPositioning(AtlwLocationConfig config, AtlwLocationTypeEnum type) {
        //权限判断
        if (config != null && type != null && checkPermissions(config) && getLocationManager() != null) {
            try {
                if (config.getLocationTimeInterval() > 0) {
                    mChangeListener.loopPositioning = true;
                }
                //使用监听进行单次调用，获取结束后关闭监听
                getLocationManager().requestLocationUpdates(type.getFrom(), 0, LOCATION_DISTANCE_INTERVAL, mChangeListener);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void release() {
        super.release();
        locationManager = null;
    }
}
