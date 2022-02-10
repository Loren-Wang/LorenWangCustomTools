package android.lorenwang.tools.location;

import android.location.Location;
import android.location.LocationListener;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.config.AtlwLocationResultBean;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;

/**
 * 功能作用：定位信息改变监听
 * 初始注释时间： 2021/1/19 1:52 下午
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
class AtlwLocationChangeListener extends BDAbstractLocationListener implements LocationListener, AMapLocationListener, TencentLocationListener {
    /**
     * 定位信息配置
     */
    protected AtlwLocationConfig config;
    /**
     * 上一次的位置信息
     */
    protected volatile AtlwLocationResultBean lastLocationBean;
    /**
     * 循环定位标记
     */
    protected volatile boolean loopPositioning = false;
    /**
     * 定位类型
     */
    protected AtlwLocationTypeEnum type;

    @Override
    public void onLocationChanged(Location location) {
        onResult(location.getLatitude(), location.getLongitude(), location.getProvider());
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

    /**
     * 开始返回
     *
     * @param latitude  纬度
     * @param longitude 精度
     * @param cityName  城市名称
     */
    protected void onResult(double latitude, double longitude, String cityName) {
        if (!loopPositioning) {
            AtlwLocationUtil.getInstance().stopLoopPositioning();
        }
        //定位相关信息
        final AtlwLocationResultBean bean = new AtlwLocationResultBean();
        bean.setLatitude(latitude);
        bean.setLongitude(longitude);
        bean.setCityName(cityName);
        AtlwLogUtil.logUtils.logI("AtlwLocation", "定位信息值:::" + bean.getLongitude() + "_" + bean.getLatitude());
        //回调定位
        if (config != null && config.getLocationsCallback() != null) {
            AtlwThreadUtil.getInstance().postOnUiThread(
                    () -> config.getLocationsCallback().locationResultSuccess(type, config, bean, judgeLocationResultBean(bean)));
        }
    }

    /**
     * 判断位置信息返回
     *
     * @param bean 当前位置信息
     * @return 需要返回则返回true
     */
    private boolean judgeLocationResultBean(AtlwLocationResultBean bean) {
        if (bean != null && lastLocationBean != null && Double.compare(lastLocationBean.getLatitude(), bean.getLatitude()) == 0 && Double.compare(
                lastLocationBean.getLongitude(), bean.getLongitude()) == 0) {
            AtlwLogUtil.logUtils.logI("AtlwLocation", "当前位置信息未发生变更");
            return false;
        }
        lastLocationBean = bean;
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        onResult(aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getCity());
    }

    /*---腾讯使用的---*/
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        onResult(tencentLocation.getLatitude(), tencentLocation.getLongitude(), tencentLocation.getCity());
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    /*---百度使用的---*/
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        onResult(bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getCity());
    }
}
