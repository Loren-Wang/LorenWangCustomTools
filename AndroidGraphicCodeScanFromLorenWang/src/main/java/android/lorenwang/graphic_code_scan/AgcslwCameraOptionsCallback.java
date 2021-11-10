package android.lorenwang.graphic_code_scan;

import android.graphics.Bitmap;

/**
 * 功能作用：相机操作回调
 * 初始注释时间： 2021/8/18 11:37
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
public abstract class AgcslwCameraOptionsCallback {
    /**
     * 权限请求失败，保留方法，留给子类扩展，后续该框架也可能会使用
     *
     * @param permissions 权限列表
     */
    public abstract void permissionRequestFail(String... permissions);

    /**
     * 相机初始化异常
     */
    public abstract void cameraInitError();

    /**
     * 无扫描权限
     *
     * @param shouldShowRequestPermissionRationale 是否能显示自定义权限弹窗
     * @param permissions                          权限集合
     */
    public abstract void notPermissions(boolean shouldShowRequestPermissionRationale, String... permissions);

    /**
     * 显示当前拍照数据
     *
     * @param bitmap
     */
    public void takePhotoCurrent(Bitmap bitmap) {

    }
}
