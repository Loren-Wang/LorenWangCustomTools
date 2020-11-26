package com.test.springboot.service.impl

import com.test.springboot.base.BaseHttpServletRequestWrapper
import com.test.springboot.database.repository.UserPermissionRepository
import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import com.test.springboot.enums.UserPermissionType
import com.test.springboot.service.UserRolePermissionService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserPermissionRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import org.springframework.beans.factory.annotation.Autowired
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
class UserRolePermissionServiceImpl : UserRolePermissionService() {

    override fun checkUserHavePermission(request : BaseHttpServletRequestWrapper, userInfo : UserInfoTb, permission : UserPermissionType) : SbcbflwBaseDataDisposeStatusBean {
        return SbcbflwBaseDataDisposeStatusBean(false)
    }

    /**
     * 获取用户权限数据库操作实体
     */
    override fun getUserPermissionRepository() : UserPermissionRepository {
        return permissionsRepository
    }
}
