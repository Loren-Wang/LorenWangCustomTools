package android.lorenwang.tools.location.config;

import android.lorenwang.tools.app.AtlwPermissionRequestCallback;
import android.lorenwang.tools.location.AtlwLocationTypeEnum;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：定位回调
 * 创建时间：2021-01-19 10:33 上午
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
public abstract class AtlwLocationCallback implements AtlwPermissionRequestCallback {

    /**
     * 定位信息获取成功
     *
     * @param type
     * @param config
     * @param bean             定位信息
     * @param locationIsChange 定位信息是否有改变
     */
    public abstract void locationResultSuccess(AtlwLocationTypeEnum type, AtlwLocationConfig config, @NotNull AtlwLocationResultBean bean, boolean locationIsChange);
}
