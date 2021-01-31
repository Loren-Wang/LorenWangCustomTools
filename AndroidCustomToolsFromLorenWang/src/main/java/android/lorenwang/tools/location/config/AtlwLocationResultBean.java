package android.lorenwang.tools.location.config;

import java.io.Serializable;

/**
 * 功能作用：定位信息返回实体
 * 创建时间：2021-01-19 2:33 下午
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
public class AtlwLocationResultBean implements Serializable {
    /**
     * 纬度
     */
    private double latitude = 0.0;
    /**
     * 经度
     */
    private double longitude = 0.0;
    /**
     * 城市信息
     */
    private String cityName;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
