package com.test.springboot.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.test.springboot.database.TableInfoConfig
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb
import javax.persistence.Entity
import javax.persistence.Table

/**
 * 功能作用：用户角色表
 * 创建时间：2020-07-02 10:50 上午
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
@JsonAutoDetect
@Entity
@Table(name = TableInfoConfig.TableName.USER_ROLE)
@org.hibernate.annotations.Table(appliesTo = TableInfoConfig.TableName.USER_ROLE, comment = "用户角色表")
class UserRoleTb : SbcbflwBaseUserRoleTb<UserPermissionTb>()
