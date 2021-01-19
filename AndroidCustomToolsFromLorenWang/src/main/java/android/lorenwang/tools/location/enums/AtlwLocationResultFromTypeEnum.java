package android.lorenwang.tools.location.enums;

/**
 * 功能作用：定位结果来源枚举
 * 创建时间：2021-01-19 12:09 下午
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
public enum AtlwLocationResultFromTypeEnum {
    /**
     * 设置网络定位
     */
    DEVICES_NETWORK("network"),
    /**
     * 设备gps定位
     */
    DEVICES_GPS("gps"),
    /**
     * 设备精准定位,首先使用gps定位
     */
    DEVICES_ACCURATE("gps"),
    /**
     * 高德低功耗定位
     */
    GAODE_BATTERY_SAVING("gaode_battery_saving"),
    /**
     * 高德自身设备定位
     */
    GAODE_DEVICE_SENSORS("gaode_device_sensors"),
    /**
     * 高德高功耗定位
     */
    GAODE_HIGHT_ACCURACY("gaode_hight_accuracy");

    /**
     * 定位来源
     */
    private String from;

    AtlwLocationResultFromTypeEnum(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
