package android.lorenwang.tools.location;

import android.app.Activity;
import android.lorenwang.tools.app.AtlwActivityUtils;
import android.lorenwang.tools.location.config.AtlwLocationConfig;
import android.lorenwang.tools.location.enums.AtlwLocationLibraryTypeEnum;
import android.lorenwang.tools.mobile.AtlwMobileOptionsUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：定位单例
 * 创建时间：2021-01-19 10:35 上午
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

    @Override
    public void startNetworkPositioning(@NotNull AtlwLocationConfig config) {
        locationLibrary.startNetworkPositioning(config);
    }

    @Override
    public void startDevicesPositioning(@NotNull AtlwLocationConfig config) {
        locationLibrary.startDevicesPositioning(config);
    }

    @Override
    public void startAccuratePositioning(@NotNull AtlwLocationConfig config) {
        locationLibrary.startAccuratePositioning(config);
    }

    @Override
    public void stopLoopPositioning() {
        locationLibrary.stopLoopPositioning();
    }

    /**
     * 请求权限
     *
     * @param activity 上下文
     * @param config   配置信息
     */
    public void requestPermissions(@NotNull Activity activity, @NotNull AtlwLocationConfig config) {
        String[] permissions;
        if (config.isNeedBackLocation()) {
            permissions = NEED_PERMISSIONS_AND_BACK;
        } else {
            permissions = NEED_PERMISSIONS;
        }
        AtlwActivityUtils.getInstance().goToRequestPermissions(activity, permissions,
                config.getLocationsCallback() != null ? config.getLocationsCallback().permissionRequestCode : activity.hashCode() % 10000,
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

    /**
     * 跳转到系统设置
     */
    public void goToSystemSetting(@NotNull Activity activity) {
        AtlwMobileOptionsUtils.getInstance().jumpToAppPermissionSettingPage(activity, activity.getApplication().getPackageName());
    }
}
