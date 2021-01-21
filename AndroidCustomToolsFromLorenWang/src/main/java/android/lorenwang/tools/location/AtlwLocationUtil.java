package android.lorenwang.tools.location;

import android.lorenwang.tools.app.AtlwActivityUtil;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.enums.AtlwLocationLibraryTypeEnum;

import org.jetbrains.annotations.NotNull;

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
public class AtlwLocationUtil implements AtlwLocationLibraryBase {
    private final String TAG = getClass().getName();
    private static volatile AtlwLocationUtil optionsInstance;

    /**
     * 定位类型
     */
    private AtlwLocationLibraryBase locationLibrary = new AtlwLocationLibraryDefault();

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
    public boolean checPermissions(@NotNull AtlwLocationConfig config) {
        return locationLibrary.checPermissions(config);
    }

    /**
     * 使用网络定位
     *
     * @param config 配置信息
     */
    @Override
    public void startNetworkPositioning(@NotNull AtlwLocationConfig config) {
        locationLibrary.startNetworkPositioning(config);
    }

    /**
     * 使用设备定位
     *
     * @param config 配置信息
     */
    @Override
    public void startDevicesPositioning(@NotNull AtlwLocationConfig config) {
        locationLibrary.startDevicesPositioning(config);
    }

    /**
     * 使用精度定位
     *
     * @param config 定位配置信息
     */
    @Override
    public void startAccuratePositioning(@NotNull AtlwLocationConfig config) {
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
     * @param config   配置信息
     */
    public void requestPermissions(@NotNull Object context, @NotNull AtlwLocationConfig config) {
        String[] permissions;
        if (config.isNeedBackLocation()) {
            permissions = NEED_PERMISSIONS_AND_BACK;
        } else {
            permissions = NEED_PERMISSIONS;
        }
        AtlwActivityUtil.getInstance().goToRequestPermissions(context, permissions,
                config.getLocationsCallback() != null ? config.getLocationsCallback().permissionRequestCode : context.hashCode() % 10000,
                config.getLocationsCallback());
    }

    /**
     * 设置定位库类型
     *
     * @param locationLibraryType 定位库类型
     */
    public void setLocationLibraryType(AtlwLocationLibraryTypeEnum locationLibraryType) {
        switch (locationLibraryType) {
            case BAIDU:
                locationLibrary = new AtlwLocationLibraryBaiDu();
                break;
            case GAODE:
                locationLibrary = new AtlwLocationLibraryGaoDe();
                break;
            case TENCENT:
                locationLibrary = new AtlwLocationLibraryTencent();
            case DEFAULT:
            default:
                break;

        }
    }
}
