package android.lorenwang.tools.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwCheckUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.config.AtlwLocationResultBean;
import android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import static android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum.DEVICES_ACCURATE;
import static android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum.DEVICES_GPS;
import static android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum.DEVICES_NETWORK;

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
class AtlwLocationLibraryDefault implements AtlwLocationLibraryBase {
    /**
     * 定位管理器
     */
    private LocationManager locationManager;

    /**
     * 循环定位标记
     */
    protected volatile boolean loopPositioning = false;
    /**
     * 上一次的位置信息
     */
    protected volatile AtlwLocationResultBean lastLocatioBean;

    /**
     * 定位改变回调
     */
    private final AtlwLocationChangeListener changeListener = new AtlwLocationChangeListener() {
        @Override
        public void onLocationChanged(Location location) {
            positioningResult(config, location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 循环定位线程
     */
    private final Runnable loopPositioningRunnable = new Runnable() {
        @Override
        public void run() {
            if (changeListener.config != null && changeListener.type != null) {
                startPositioning(changeListener.config, changeListener.type);
            }
        }
    };

    /**
     * 检测权限
     *
     * @param config 配置信息
     * @return 有权限返回true
     */
    @Override
    public boolean checPermissions(@NotNull AtlwLocationConfig config) {
        if (config.isNeedBackLocation()) {
            return AtlwCheckUtil.getInstance().checkAppPermission(NEED_PERMISSIONS_AND_BACK);
        } else {
            return AtlwCheckUtil.getInstance().checkAppPermission(NEED_PERMISSIONS);
        }
    }

    /**
     * 使用网络进行一次定位
     *
     * @param config 配置信息
     */
    @SuppressLint("MissingPermission")
    @Override
    public void startNetworkPositioning(@NotNull AtlwLocationConfig config) {
        if (!loopPositioning) {
            changeListener.type = DEVICES_NETWORK;
            changeListener.config = config;
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
    public void startDevicesPositioning(@NotNull AtlwLocationConfig config) {
        if (!loopPositioning) {
            changeListener.type = DEVICES_GPS;
            changeListener.config = config;
            startPositioning(config, DEVICES_GPS);
        }
    }

    /**
     * 使用精准定位
     *
     * @param config 定位配置信息
     */
    @Override
    public void startAccuratePositioning(@NotNull AtlwLocationConfig config) {
        if (!loopPositioning) {
            changeListener.type = DEVICES_ACCURATE;
            changeListener.config = config;
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
                getLocationManager().removeUpdates(changeListener);
            } catch (Exception ignore) {
            }
        }
        changeListener.type = null;
        changeListener.config = null;
        loopPositioning = false;
        AtlwLogUtil.logUtils.logI(TAG, "停止定位");
    }

    /**
     * 定位结果处理
     *
     * @param config   配置信息
     * @param location 定位数据
     */
    @SuppressLint("MissingPermission")
    private void positioningResult(final AtlwLocationConfig config, Location location) {
        //定位相关信息
        final AtlwLocationResultBean bean = new AtlwLocationResultBean();
        if (location != null) {
            bean.setLatitude(location.getLatitude());
            bean.setLongitude(location.getLongitude());
        }
        //判断定位类型，判断数据有消息，有效向下执行，无效重新请求网络定位
        if (changeListener.type == DEVICES_ACCURATE && bean.getLatitude() == 0.0d && bean.getLongitude() == 0.0d) {
            //移除定位监听
            if (getLocationManager() != null) {
                try {
                    getLocationManager().removeUpdates(changeListener);
                } catch (Exception ignore) {
                }
            }
            //当前是精准定位，同时数据无效，使用网络定位重新执行
            startPositioning(config, DEVICES_NETWORK);
            return;
        }
        //判断是否需要进行循环
        if (loopPositioning && config != null) {
            //移除定位监听
            if (getLocationManager() != null) {
                try {
                    getLocationManager().removeUpdates(changeListener);
                } catch (Exception ignore) {
                }
            }
            //当前是需要循环的
            AtlwThreadUtil.getInstance().postOnChildThreadDelayed(loopPositioningRunnable, (long) config.getLocationTimeInterval());
        } else {
            //停止定位
            stopLoopPositioning();
        }
        //返回数据逻辑处理
        AtlwLogUtil.logUtils.logI(TAG, "定位信息值:::" + bean.getLongitude() + "_" + bean.getLatitude());
        //回调定位
        if (config != null && config.getLocationsCallback() != null) {
            AtlwThreadUtil.getInstance().postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    config.getLocationsCallback().locationResultSuccess(bean,judgeLocationResultBean(bean));
                }
            });
        }
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
    protected void startPositioning(@NotNull AtlwLocationConfig config, @NotNull AtlwLocationResultFromTypeEnum type) {
        //权限判断
        if (checPermissions(config) && getLocationManager() != null) {
            try {
                if (config.getLocationTimeInterval() > 0) {
                    loopPositioning = true;
                }
                //使用监听进行单次调用，获取结束后关闭监听
                getLocationManager().requestLocationUpdates(type.getFrom(), 0, LOCATION_DISTANCE_INTERVAL, changeListener);
            } catch (Exception e) {
                positioningResult(config, null);
            }
        }
    }

    /**
     * 判断位置信息返回
     *
     * @param bean 当前位置信息
     * @return 需要返回则返回true
     */
    protected boolean judgeLocationResultBean(@NotNull AtlwLocationResultBean bean) {
        if (lastLocatioBean != null && Double.compare(lastLocatioBean.getLatitude(), bean.getLatitude()) == 0 && Double.compare(
                lastLocatioBean.getLongitude(), bean.getLongitude()) == 0) {
            AtlwLogUtil.logUtils.logI(TAG, "当前位置信息未发生变更");
            return false;
        }
        lastLocatioBean = bean;
        return true;
    }
}
