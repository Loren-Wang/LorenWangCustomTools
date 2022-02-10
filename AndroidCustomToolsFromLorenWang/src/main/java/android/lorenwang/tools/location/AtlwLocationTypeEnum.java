package android.lorenwang.tools.location;

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
public enum AtlwLocationTypeEnum {
    /**
     * 使用本机设备定位
     */
    DEVICES("本机设备", "Devices"),
    /**
     * 使用高德定位
     */
    GAODE("高德", "GaoDe"),
    /**
     * 使用百度定位
     */
    BAIDU("百度", "BaiDu"),
    /**
     * 使用腾讯定位
     */
    TENCENT("腾讯", "Tencent"),
    /**
     * 设置网络定位
     */
    DEVICES_NETWORK("network", DEVICES.from),
    /**
     * 设备gps定位
     */
    DEVICES_GPS("gps", DEVICES.from),
    /**
     * 设备精准定位,首先使用gps定位
     */
    DEVICES_ACCURATE("gps", DEVICES.from),
    /**
     * 高德低功耗定位
     */
    GAODE_BATTERY_SAVING("gaode_battery_saving", GAODE.from),
    /**
     * 高德自身设备定位
     */
    GAODE_DEVICE_SENSORS("gaode_device_sensors", GAODE.from),
    /**
     * 高德高功耗定位
     */
    GAODE_HIGHT_ACCURACY("gaode_hight_accuracy", GAODE.from),
    /**
     * 百度低功耗定位
     */
    BAIDU_BATTERY_SAVING("baidu_battery_saving", BAIDU.from),
    /**
     * 百度自身设备定位
     */
    BAIDU_DEVICE_SENSORS("baidu_device_sensors", BAIDU.from),
    /**
     * 百度高功耗定位
     */
    BAIDU_HIGHT_ACCURACY("baidu_hight_accuracy", BAIDU.from),
    /**
     * 腾讯低功耗定位
     */
    TENCENT_BATTERY_SAVING("tencent_battery_saving", TENCENT.from),
    /**
     * 腾讯自身设备定位
     */
    TENCENT_DEVICE_SENSORS("tencent_device_sensors", TENCENT.from),
    /**
     * 腾讯高功耗定位
     */
    TENCENT_HIGHT_ACCURACY("tencent_hight_accuracy", TENCENT.from);

    /**
     * 定位来源
     */
    private final String from;
    /**
     * 定位库
     */
    private final String parent;

    AtlwLocationTypeEnum(String from, String parent) {
        this.from = from;
        this.parent = parent;
    }

    public String getFrom() {
        return from;
    }

    public String getParent() {
        return parent;
    }

    /**
     * 获取使用的定位库
     *
     * @return 使用的定位库枚举
     */
    public AtlwLocationTypeEnum getLocationUseLibrary() {
        for (AtlwLocationTypeEnum value : values()) {
            //先判断子数据，因为父级数据肯定对不上
            if (value.from.equals(parent)) {
                return value;
            }
            //子数据没有判断是否就是父数据
            if (value == this) {
                return value;
            }
        }
        return DEVICES;
    }
}
