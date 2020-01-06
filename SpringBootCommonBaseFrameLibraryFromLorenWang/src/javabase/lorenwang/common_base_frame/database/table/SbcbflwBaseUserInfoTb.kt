package javabase.lorenwang.common_base_frame.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserStatusEnum
import service.qtoolsbaby.official.database.table.SbcbflwBaseUserRoleTb
import javax.persistence.*

/**
 * 功能作用：用户表
 * 创建时间：2019-09-19 下午 12:06:21
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@JsonAutoDetect
class SbcbflwBaseUserInfoTb : SbcbflwBaseTb() {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.USER_ID)
    var userId: Long? = null
    /**
     * 乐观锁锁数据
     */
    @Version
    @Column(name = SbcbflwBaseTableConfig.CommonColumn.OPTIMISTIC_LOCKING)
    var version: Int = 0
    /**
     * 用户账户
     */
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.ACCOUNT, nullable = false, columnDefinition = "varchar(200) comment '用户账户'")
    var account: String? = null
    /**
     * 用户密码
     */
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.PASSWORD, nullable = false, columnDefinition = "text comment '用户密码'")
    var password: String? = null
    /**
     * 联系方式
     */
    @Column(name = SbcbflwBaseTableConfig.CommonColumn.PHONE_NUM, nullable = false, columnDefinition = "${SbcbflwBaseTableConfig.ColumnType.PHONE_NUM} comment '联系电话'")
    var phoneNum: String? = null// varchar(31) DEFAULT NULL,
    /**
     * 邮箱
     */
    @Column(name = SbcbflwBaseTableConfig.CommonColumn.EMAIL, nullable = false, columnDefinition = "${SbcbflwBaseTableConfig.ColumnType.EMAIL} comment '邮箱'")
    var email: String? = null
    /**
     * security加密的密码种子，和md5加密的密码类似
     */
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.SECURITY_SALT, nullable = false, columnDefinition = "text comment 'security加密的密码种子，和md5加密的密码类似'")
    var securitySalt: String? = null
    /**
     * 用户角色
     */
    @JoinColumn(name = SbcbflwBaseTableConfig.UserInfoColumn.USER_ROLE, nullable = false, columnDefinition = "bigint comment '用户角色'")
    @ManyToOne
    var userRole: SbcbflwBaseUserRoleTb? = null
    /**
     * 用户昵称
     */
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.NICK_NAME, nullable = false, columnDefinition = "varchar(200) comment '用户昵称'")
    var nickName: String? = null
    /**
     * 用户token
     */
    @Column(name = SbcbflwBaseTableConfig.UserInfoColumn.ACCESSTOKEN, columnDefinition = "varchar(200) comment '用户token'")
    var accessToken: String? = null

    /**
     * 用户状态
     */
    @Column(name = SbcbflwBaseTableConfig.CommonColumn.STATUS, nullable = false, columnDefinition = "${SbcbflwBaseTableConfig.ColumnType.STATUS} comment '用户状态'")
    var status = SbcbflwBaseUserStatusEnum.ENABLE.status

}
