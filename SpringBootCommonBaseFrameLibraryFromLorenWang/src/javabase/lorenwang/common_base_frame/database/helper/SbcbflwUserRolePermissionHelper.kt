package javabase.lorenwang.common_base_frame.database.helper

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserInfoRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionTypeEnum

/**
 * 功能作用：用户角色权限帮助类
 * 创建时间：2020-01-06 17:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class SbcbflwUserRolePermissionHelper {
    companion object {
        lateinit var instance: SbcbflwUserRolePermissionHelper
    }
    /**
     * 检测是否有权限
     */
    abstract fun checkUserHavePermission(sbcbflwBaseHttpServletRequestWrapper: SbcbflwBaseHttpServletRequestWrapper,
                                         userInfo: SbcbflwBaseUserInfoTb,
                                         permission: SbcbflwBaseUserPermissionTypeEnum): SbcbflwBaseDataDisposeStatusBean

}
