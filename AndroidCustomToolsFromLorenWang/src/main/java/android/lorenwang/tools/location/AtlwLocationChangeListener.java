package android.lorenwang.tools.location;

import android.location.Location;
import android.location.LocationListener;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.enums.AtlwLocationResultFromTypeEnum;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

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
abstract class AtlwLocationChangeListener implements LocationListener, AMapLocationListener {
    /**
     * 定位信息配置
     */
    protected AtlwLocationConfig config;
    /**
     * 定位类型
     */
    protected AtlwLocationResultFromTypeEnum type;

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

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
}
