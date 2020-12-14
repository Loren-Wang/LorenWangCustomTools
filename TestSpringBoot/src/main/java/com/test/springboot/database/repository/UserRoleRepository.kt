package com.test.springboot.database.repository

import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserRoleRepository

/**
 * 功能作用：用户信息数据库操作
 * 创建时间：2020-07-02 10:53 上午
 * 创建人：王亮（Loren ）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren ）
 */
interface UserRoleRepository : SbcbflwUserRoleRepository<UserPermissionTb, UserRoleTb>
