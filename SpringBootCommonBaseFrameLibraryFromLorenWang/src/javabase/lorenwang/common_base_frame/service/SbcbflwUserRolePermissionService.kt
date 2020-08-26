package javabase.lorenwang.common_base_frame.service

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb

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
interface SbcbflwUserRolePermissionService<R : SbcbflwBaseHttpServletRequestWrapper,
        P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>,
        U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType> : SbcbflwBaseService {
    /*
    * 检测是否有权限
    */
    fun checkUserHavePermission(request: R, userInfo: U, permission: PT):
            SbcbflwBaseDataDisposeStatusBean
}
