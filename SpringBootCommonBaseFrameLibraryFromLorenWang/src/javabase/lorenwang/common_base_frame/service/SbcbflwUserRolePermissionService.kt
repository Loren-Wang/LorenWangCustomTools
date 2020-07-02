package javabase.lorenwang.common_base_frame.service

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType

/**
 * 功能作用：用户角色权限service
 * 创建时间：2020-07-01 5:55 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
interface SbcbflwUserRolePermissionService :SbcbflwBaseService{
    /*
    * 检测是否有权限
    */
    fun checkUserHavePermission(request: SbcbflwBaseHttpServletRequestWrapper,
                                userInfo: SbcbflwBaseUserInfoTb<*, *>,
                                permission: SbcbflwBaseUserPermissionType): SbcbflwBaseDataDisposeStatusBean
}
