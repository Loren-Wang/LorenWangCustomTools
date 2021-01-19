package android.lorenwang.tools.location;

import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwThreadUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.config.AtlwLocationResultBean;
import android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：高德库定位相关
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
class AtlwLocationLibraryGaoDe extends AtlwLocationLibraryDefault {

    /**
     * 声明AMapLocationClient类对象
     */
    private final AMapLocationClient mLocationClient = new AMapLocationClient(AtlwConfig.nowApplication);

    /**
     * AMapLocationClientOption对象,发起定位的模式和相关参数。
     */
    private final AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

    /**
     * 声明定位回调监听器
     */
    private final AtlwLocationChangeListener mLocationListener = new AtlwLocationChangeListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (!loopPositioning) {
                stopLoopPositioning();
            }
            //定位相关信息
            final AtlwLocationResultBean bean = new AtlwLocationResultBean();
            if (aMapLocation != null) {
                bean.setLatitude(aMapLocation.getLatitude());
                bean.setLongitude(aMapLocation.getLongitude());
            }
            if (judgeLocationResultBean(bean)) {
                AtlwLogUtils.logUtils.logI(TAG, "定位信息值:::" + bean.getLongitude() + "_" + bean.getLatitude());
                //回调定位
                if (config != null && config.getLocationsCallback() != null) {
                    AtlwThreadUtils.getInstance().postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            config.getLocationsCallback().locationResultSuccess(bean);
                        }
                    });
                }
            }
        }
    };

    public AtlwLocationLibraryGaoDe() {
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    @Override
    public void startDevicesPositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationResultFromTypeEnum.GAODE_DEVICE_SENSORS);
    }

    @Override
    public void startNetworkPositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationResultFromTypeEnum.GAODE_BATTERY_SAVING);
    }

    @Override
    public void startAccuratePositioning(@NotNull AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationResultFromTypeEnum.GAODE_HIGHT_ACCURACY);
    }

    @Override
    public synchronized void stopLoopPositioning() {
        loopPositioning = false;
        //注销监听
        mLocationClient.unRegisterLocationListener(mLocationListener);
        //停止定位后，本地定位服务并不会被销毁
        mLocationClient.stopLocation();
        AtlwLogUtils.logUtils.logI(TAG, "停止定位");
    }

    /**
     * 开始定位
     *
     * @param config 配置信息
     * @param type   定位类型
     */
    @Override
    protected void startPositioning(@NotNull AtlwLocationConfig config, @NotNull AtlwLocationResultFromTypeEnum type) {
        AMapLocationClientOption.AMapLocationMode mode = null;
        switch (type) {
            case GAODE_BATTERY_SAVING:
                mode = AMapLocationClientOption.AMapLocationMode.Battery_Saving;
                break;
            case GAODE_DEVICE_SENSORS:
                mode = AMapLocationClientOption.AMapLocationMode.Device_Sensors;
                break;
            case GAODE_HIGHT_ACCURACY:
                mode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
                break;
            default:
                break;
        }
        if (!loopPositioning && mode != null) {
            mLocationOption.setLocationMode(mode);
            if (config.getLocationTimeInterval() > 0) {
                loopPositioning = true;
                //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
                mLocationOption.setInterval(config.getLocationTimeInterval());
            } else {
                //获取一次定位结果
                mLocationOption.setOnceLocation(true);
                //获取最近3s内精度最高的一次定位结果
                mLocationOption.setOnceLocationLatest(true);
            }
            mLocationListener.config = config;
            mLocationListener.type = type;
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.startLocation();
        }
    }
}
