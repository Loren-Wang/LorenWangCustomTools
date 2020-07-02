package com.test.springboot.service.impl

import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.enums.UserPermissionType
import com.test.springboot.service.UserRolePermissionService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import org.springframework.stereotype.Service

/**
 * 功能作用：用户角色权限服务
 * 创建时间：2020-07-02 9:51 上午
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
@Service
class UserRolePermissionServiceImpl : UserRolePermissionService {

    override fun checkUserHavePermission(request: SbcbflwBaseHttpServletRequestWrapper, userInfo: UserInfoTb, permission: UserPermissionType): SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }
}
