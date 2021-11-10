package android.lorenwang.tools.location;

import android.annotation.SuppressLint;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.os.Looper;

import com.baidu.location.LocationClientOption;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.jetbrains.annotations.NotNull;

import static android.lorenwang.tools.location.AtlwLocationTypeEnum.TENCENT_BATTERY_SAVING;

/**
 * 功能作用：腾讯定位相关
 * 创建时间：2021-01-19 10:45 上午
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
class AtlwLocationLibraryTencent extends AtlwLocationLibraryBase {
    /**
     * 定位管理器
     */
    private TencentLocationManager mLocationManager;

    AtlwLocationLibraryTencent() {
        mLocationManager = TencentLocationManager.getInstance(AtlwConfig.nowApplication);
    }

    @Override
    public void startDevicesPositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.TENCENT_DEVICE_SENSORS);
    }

    @Override
    public void startNetworkPositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, TENCENT_BATTERY_SAVING);
    }

    @Override
    public void startAccuratePositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.TENCENT_HIGHT_ACCURACY);
    }

    protected void startPositioning(@NotNull AtlwLocationConfig config, @NotNull AtlwLocationTypeEnum type) {
        //权限判断
        if (checkPermissions(config)) {
            LocationClientOption.LocationMode mode = null;
            switch (type) {
                case TENCENT_BATTERY_SAVING:
                    mode = LocationClientOption.LocationMode.Battery_Saving;
                    break;
                case TENCENT_DEVICE_SENSORS:
                    mode = LocationClientOption.LocationMode.Device_Sensors;
                    break;
                case TENCENT_HIGHT_ACCURACY:
                    mode = LocationClientOption.LocationMode.Hight_Accuracy;
                    break;
                default:
                    break;
            }
            if (!mChangeListener.loopPositioning && mode != null) {
                TencentLocationRequest request = TencentLocationRequest.create();
                mChangeListener.config = config;
                mChangeListener.type = type;
                //设置请求级别
                request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
                //是否允许使用GPS
                request.setAllowGPS(type != TENCENT_BATTERY_SAVING);
                //是否需要获取传感器方向
                request.setAllowDirection(true);
                //是否需要开启室内定位
                request.setIndoorLocationMode(true);
                if (config.isNeedBackLocation()) {
                    if (config.getBackLocationNotification() != null && config.getBackLocationNotificationId() != null) {
                        mLocationManager.enableForegroundLocation(config.getBackLocationNotificationId(), config.getBackLocationNotification());
                    }
                }
                int status;
                if (config.getLocationTimeInterval() > 0) {
                    //用户可以自定义定位间隔，时间单位为毫秒，不得小于1000毫秒:
                    request.setInterval(Math.max(config.getLocationTimeInterval(), 1000));
                    status = mLocationManager.requestLocationUpdates(request, mChangeListener);
                } else {
                    status = mLocationManager.requestSingleFreshLocation(request, mChangeListener, Looper.getMainLooper());
                }
                //0-成功注册监听器, 1-设备缺少使用腾讯定位服务需要的基本条件, 2-manifest 中配置的 key 不正确, 3-自动加载libtencentloc.so失败 4-ClassLoader获取或加载失败，5-加载类失败
                switch (status) {
                    case 0:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：成功注册监听器");
                        break;
                    case 1:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：设备缺少使用腾讯定位服务需要的基本条件");
                        break;
                    case 2:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：manifest 中配置的 key 不正确");
                        break;
                    case 3:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：自动加载libtencentloc.so失败");
                        break;
                    case 4:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：ClassLoader获取或加载失败");
                        break;
                    case 5:
                        AtlwLogUtil.logUtils.logI(TAG, "腾讯定位：加载类失败");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public synchronized void stopLoopPositioning() {
        try {
            mLocationManager.removeUpdates(mChangeListener);
            if (mChangeListener.config.isNeedBackLocation()) {
                mLocationManager.disableForegroundLocation(true);
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    public void release() {
        super.release();
        mLocationManager = null;
    }
}
