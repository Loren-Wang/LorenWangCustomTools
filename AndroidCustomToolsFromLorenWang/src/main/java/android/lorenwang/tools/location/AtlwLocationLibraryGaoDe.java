package android.lorenwang.tools.location;

import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

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
class AtlwLocationLibraryGaoDe extends AtlwLocationLibraryBase {

    /**
     * 声明AMapLocationClient类对象
     */
    private AMapLocationClient mLocationClient;

    {
        try {
            mLocationClient = new AMapLocationClient(AtlwConfig.nowApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AtlwLocationLibraryGaoDe() {
        //设置定位回调监听
        mLocationClient.setLocationListener(mChangeListener);
    }

    @Override
    public void startDevicesPositioning(AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.GAODE_DEVICE_SENSORS);
    }

    @Override
    public void startNetworkPositioning(AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.GAODE_BATTERY_SAVING);
    }

    @Override
    public void startAccuratePositioning(AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.GAODE_HIGHT_ACCURACY);
    }

    @Override
    public synchronized void stopLoopPositioning() {
        mChangeListener.loopPositioning = false;
        //注销监听
        mLocationClient.unRegisterLocationListener(mChangeListener);
        //停止定位后，本地定位服务并不会被销毁
        mLocationClient.stopLocation();
        AtlwLogUtil.logUtils.logI(TAG, "停止定位");
    }

    /**
     * 开始定位
     *
     * @param config 配置信息
     * @param type   定位类型
     */
    protected void startPositioning(AtlwLocationConfig config, AtlwLocationTypeEnum type) {
        if (config != null && type != null) {
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
            if (!mChangeListener.loopPositioning && mode != null) {
                //AMapLocationClientOption对象,发起定位的模式和相关参数。
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                mLocationOption.setLocationMode(mode);
                if (config.getLocationTimeInterval() > 0) {
                    mChangeListener.loopPositioning = true;
                    //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
                    mLocationOption.setInterval(config.getLocationTimeInterval());
                } else {
                    //获取一次定位结果
                    mLocationOption.setOnceLocation(true);
                    //获取最近3s内精度最高的一次定位结果
                    mLocationOption.setOnceLocationLatest(true);
                }
                //设置返回地址信息
                mLocationOption.setNeedAddress(true);
                mChangeListener.config = config;
                mChangeListener.type = type;
                mLocationClient.setLocationListener(mChangeListener);
                mLocationClient.startLocation();
            }
        }
    }

    @Override
    public void release() {
        super.release();
        mLocationClient = null;
    }
}
