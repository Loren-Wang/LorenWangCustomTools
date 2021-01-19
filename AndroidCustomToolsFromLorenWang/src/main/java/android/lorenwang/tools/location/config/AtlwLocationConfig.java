package android.lorenwang.tools.location.config;

/**
 * 功能作用：启动定位时的配置信息
 * 创建时间：2021-01-19 10:51 上午
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
public class AtlwLocationConfig {
    /**
     * 是否需要后台定位权限，只有当sdk大于28才生效
     */
    private boolean needBackLocation;
    /**
     * 定位回调
     */
    private AtlwLocationCallback locationsCallback;
    /**
     * 手机定位时间间隔
     */
    private int locationTimeInterval;

    private AtlwLocationConfig() {
    }

    public AtlwLocationCallback getLocationsCallback() {
        return locationsCallback;
    }

    public int getLocationTimeInterval() {
        return locationTimeInterval;
    }

    public boolean isNeedBackLocation() {
        return needBackLocation;
    }

    public static class Build {
        /**
         * 是否需要后台定位权限，只有当sdk大于28才生效
         */
        private boolean needBackLocation = false;
        /**
         * 定位回调
         */
        private AtlwLocationCallback locationsCallback;
        /**
         * 手机定位时间间隔
         */
        private int locationTimeInterval = 0;

        public Build setNeedBackLocation(boolean needBackLocation) {
            this.needBackLocation = needBackLocation;
            return this;
        }

        public Build setLocationsCallback(AtlwLocationCallback locationsCallback) {
            this.locationsCallback = locationsCallback;
            return this;
        }

        public Build setLocationTimeInterval(int locationTimeInterval) {
            this.locationTimeInterval = locationTimeInterval;
            return this;
        }

        public AtlwLocationConfig build() {
            AtlwLocationConfig config = new AtlwLocationConfig();
            config.needBackLocation = needBackLocation;
            config.locationsCallback = locationsCallback;
            config.locationTimeInterval = locationTimeInterval;
            return config;
        }
    }
}
