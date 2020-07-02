package com.test.springboot.service

import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import com.test.springboot.enums.UserPermissionType
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.service.SbcbflwUserRolePermissionService

/**
 * 功能作用：用户角色权限service
 * 创建时间：2020-07-02 9:49 上午
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
interface UserRolePermissionService :
        SbcbflwUserRolePermissionService<SbcbflwBaseHttpServletRequestWrapper, UserPermissionTb,
                UserRoleTb, UserInfoTb, UserPermissionType>
