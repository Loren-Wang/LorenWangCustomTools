package android.lorenwang.tools.location;

import android.lorenwang.tools.app.AtlwActivityUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;

/**
 * 功能作用：定位单例
 * 创建时间：2021-01-19 10:35 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 检测权限相关--checPermissions(config)
 * 使用网络定位--startNetworkPositioning(config)
 * 使用设备定位--startDevicesPositioning(config)
 * 使用精度定位--startAccuratePositioning(config)
 * 停止循环定位--stopLoopPositioning()
 * 请求权限--requestPermissions(context,config)
 * 设置定位库类型--locationLibraryType(config)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwLocationUtil extends AtlwLocationLibraryBase {
    private final String TAG = getClass().getName();
    private static volatile AtlwLocationUtil optionsInstance;

    /**
     * 定位类型
     */
    private AtlwLocationLibraryBase locationLibrary = new AtlwLocationLibraryDefault();

    /**
     * 使用的定位库
     */
    private AtlwLocationTypeEnum libraryTypeEnum = AtlwLocationTypeEnum.DEVICES;

    private AtlwLocationUtil() {
    }

    public static AtlwLocationUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwLocationUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwLocationUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 检测权限相关
     *
     * @param config 配置新
     * @return 有权限返回true
     */
    @Override
    public boolean checkPermissions(AtlwLocationConfig config) {
        return locationLibrary.checkPermissions(config);
    }

    /**
     * 使用网络定位
     *
     * @param config 配置信息
     */
    @Override
    public void startNetworkPositioning(AtlwLocationConfig config) {
        stopLoopPositioning();
        setLocationLibraryType(libraryTypeEnum);
        locationLibrary.startNetworkPositioning(config);
    }

    /**
     * 使用设备定位
     *
     * @param config 配置信息
     */
    @Override
    public void startDevicesPositioning(AtlwLocationConfig config) {
        stopLoopPositioning();
        setLocationLibraryType(libraryTypeEnum);
        locationLibrary.startDevicesPositioning(config);
    }

    /**
     * 使用精度定位
     *
     * @param config 定位配置信息
     */
    @Override
    public void startAccuratePositioning(AtlwLocationConfig config) {
        stopLoopPositioning();
        setLocationLibraryType(libraryTypeEnum);
        locationLibrary.startAccuratePositioning(config);
    }

    /**
     * 停止循环定位
     */
    @Override
    public void stopLoopPositioning() {
        locationLibrary.stopLoopPositioning();
    }

    /**
     * 请求权限
     *
     * @param context 上下文
     * @param config  配置信息
     */
    public void requestPermissions(Object context, AtlwLocationConfig config) {
        if (config != null) {
            String[] permissions;
            if (config.isNeedBackLocation()) {
                permissions = NEED_PERMISSIONS_AND_BACK;
            } else {
                permissions = NEED_PERMISSIONS;
            }
            AtlwActivityUtil.getInstance().goToRequestPermissions(context, permissions,
                    config.getLocationsCallback() != null ? config.getLocationsCallback().permissionRequestCode : context.hashCode() % 10000,
                    config.getLocationsCallback());        }
    }

    /**
     * 设置定位库类型
     *
     * @param locationLibraryType 定位库类型
     */
    public void setLocationLibraryType(AtlwLocationTypeEnum locationLibraryType) {
        libraryTypeEnum = locationLibraryType;
        if (locationLibrary != null) {
            locationLibrary.stopLoopPositioning();
            locationLibrary.release();
        }
        switch (locationLibraryType.getLocationUseLibrary()) {
            case BAIDU:
                this.locationLibrary = new AtlwLocationLibraryBaiDu();
                break;
            case GAODE:
                this.locationLibrary = new AtlwLocationLibraryGaoDe();
                break;
            case TENCENT:
                this.locationLibrary = new AtlwLocationLibraryTencent();
                break;
            case DEVICES:
            default:
                this.locationLibrary = new AtlwLocationLibraryDefault();
                break;
        }
    }
}
