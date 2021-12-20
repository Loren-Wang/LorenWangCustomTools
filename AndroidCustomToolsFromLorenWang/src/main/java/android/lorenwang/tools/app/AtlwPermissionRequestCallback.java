package android.lorenwang.tools.app;

import java.util.List;

/**
 * Created by LorenWang on 2018/7/30 0030.
 * 创建时间：2018/7/30 0030 下午 03:21
 * 创建人：王亮（Loren wang）
 * 功能作用：权限请求回调
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public interface AtlwPermissionRequestCallback {
    void permissionRequestSuccessCallback(List<String> permissionList);//请求成功权限列表
    void permissionRequestFailCallback(List<String> permissionList);//请求失败权限列表
}
