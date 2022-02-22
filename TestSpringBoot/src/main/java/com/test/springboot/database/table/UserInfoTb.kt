package com.test.springboot.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.test.springboot.database.TableInfoConfig
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * 功能作用：用户信息表
 * 初始注释时间： 2020/12/9 10:34 上午
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
@JsonAutoDetect
@Entity
@Table(name = TableInfoConfig.TableName.USER_INFO,
    uniqueConstraints = [UniqueConstraint(columnNames = [SpulwBaseTableConfig.UserInfoColumn.ACCOUNT]), UniqueConstraint(
        columnNames = [SpblwBaseTableConfig.CommonColumn.PHONE_NUM])])
@org.hibernate.annotations.Table(appliesTo = TableInfoConfig.TableName.USER_INFO, comment = "用户表")
class UserInfoTb : SpulwBaseUserInfoTb<UserPermissionTb, UserRoleTb>()
