package android.lorenwang.tools.location;

import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.location.config.AtlwLocationConfig;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import static android.lorenwang.tools.location.AtlwLocationTypeEnum.BAIDU_BATTERY_SAVING;

/**
 * 功能作用：百度定位相关
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
class AtlwLocationLibraryBaiDu extends AtlwLocationLibraryBase {
    /**
     * 定位客户端
     */
    private LocationClient mLocationClient;

    AtlwLocationLibraryBaiDu() {
        //声明LocationClient类
        mLocationClient = new LocationClient(AtlwConfig.nowApplication);
        mLocationClient.registerLocationListener(mChangeListener);
    }

    @Override
    public void startDevicesPositioning(AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.BAIDU_DEVICE_SENSORS);
    }

    @Override
    public void startNetworkPositioning(AtlwLocationConfig config) {
        startPositioning(config, BAIDU_BATTERY_SAVING);
    }

    @Override
    public void startAccuratePositioning(AtlwLocationConfig config) {
        startPositioning(config, AtlwLocationTypeEnum.BAIDU_HIGHT_ACCURACY);
    }

    protected void startPositioning(AtlwLocationConfig config, AtlwLocationTypeEnum type) {
        //权限判断
        if (config != null && type != null && checkPermissions(config)) {
            LocationClientOption.LocationMode mode = null;
            switch (type) {
                case BAIDU_BATTERY_SAVING:
                    mode = LocationClientOption.LocationMode.Battery_Saving;
                    break;
                case BAIDU_DEVICE_SENSORS:
                    mode = LocationClientOption.LocationMode.Device_Sensors;
                    break;
                case BAIDU_HIGHT_ACCURACY:
                    mode = LocationClientOption.LocationMode.Hight_Accuracy;
                    break;
                default:
                    break;
            }
            if (!mChangeListener.loopPositioning && mode != null) {
                mChangeListener.config = config;
                mChangeListener.type = type;
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(mode);
                //可选，设置返回经纬度坐标类型，默认GCJ02  GCJ02：国测局坐标；BD09ll：百度经纬度坐标；BD09：百度墨卡托坐标；海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
                option.setCoorType("bd09ll");
                //可选，设置发起定位请求的间隔，int类型，单位ms  如果设置为0，则代表单次定位，即仅定位一次，默认为0  如果设置非0，需设置1000ms以上才有效
                option.setScanSpan(config.getLocationTimeInterval());
                //可选，设置是否使用gps，默认false  使用高精度和仅用设备两种定位模式的，参数必须设置为true
                option.setOpenGps(type != BAIDU_BATTERY_SAVING);
                //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
                option.setLocationNotify(true);
                //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
                option.setIgnoreKillProcess(false);
                //可选，设置是否收集Crash信息，默认收集，即参数为false
                option.SetIgnoreCacheException(false);
                //可选，如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
                option.setWifiCacheTimeOut(5 * 60 * 1000);
                //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
                option.setEnableSimulateGps(false);
                //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
                option.setNeedNewVersionRgc(true);
                //可选，是否需要地址信息，默认为不需要，即参数为false  如果开发者需要获得当前点的地址信息，此处必须为true
                option.setIsNeedAddress(true);
                /*mLocationClient为第二步初始化过的LocationClient对象
                需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明*/
                mLocationClient.setLocOption(option);
                //调用LocationClient的start()方法，便可发起定位请求
                mLocationClient.start();
            }
        }
    }

    @Override
    public synchronized void stopLoopPositioning() {
        mLocationClient.stop();
    }

    @Override
    public void release() {
        super.release();
        mLocationClient = null;
    }
}
