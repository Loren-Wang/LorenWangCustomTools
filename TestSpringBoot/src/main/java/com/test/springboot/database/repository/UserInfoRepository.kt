package com.test.springboot.database.repository

import com.test.springboot.database.TableInfoConfig.TableName.USER_INFO
import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.UserInfoColumn
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserInfoRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * 功能作用：用户信息数据库操作
 * 创建时间：2020-07-02 10:53 上午
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
interface UserInfoRepository :SbcbflwUserInfoRepository<UserPermissionTb,UserRoleTb,UserInfoTb>{
    /**
     * 根据用户名以查询用户数据
     */
    @Query(value = "select * from $USER_INFO where ${UserInfoColumn.ACCOUNT}=:#{#account}", nativeQuery = true)
    fun getUserInfoTbByAccount(@Param("account") account : String) : List<UserInfoTb>?
}
