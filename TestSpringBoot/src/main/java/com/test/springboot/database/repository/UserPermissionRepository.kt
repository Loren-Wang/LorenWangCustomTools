package com.test.springboot.database.repository

import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import springbase.lorenwang.user.database.repository.SpulwUserPermissionRepository

/**
 * 功能作用：用户权限数据库操作
 * 创建时间：2020-11-26 4:09 下午
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
interface UserPermissionRepository : SpulwUserPermissionRepository<UserPermissionTb, UserRoleTb>
