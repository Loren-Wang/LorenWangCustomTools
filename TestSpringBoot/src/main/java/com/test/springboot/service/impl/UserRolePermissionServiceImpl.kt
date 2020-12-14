package com.test.springboot.service.impl

import com.test.springboot.base.BaseHttpServletRequestWrapper
import com.test.springboot.database.repository.UserPermissionRepository
import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.enums.UserPermissionTypeEnum
import com.test.springboot.service.UserRolePermissionService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
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

    override fun checkUserHavePermission(request : BaseHttpServletRequestWrapper, userInfo : UserInfoTb, permission : UserPermissionTypeEnum) : SbcbflwBaseDataDisposeStatusBean {
        return SbcbflwBaseDataDisposeStatusBean(false)
    }

    /**
     * 获取用户权限数据库操作实体
     */
    override fun getUserPermissionRepository() : UserPermissionRepository {
        return permissionsRepository
    }
}
