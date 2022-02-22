package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.tools.common.JtlwCommonUtil
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：用户表
 * 创建时间：2019-09-19 下午 12:06:21
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@JsonAutoDetect
@MappedSuperclass
open class SpulwBaseUserInfoTb<T, ROLE_TB : SpulwBaseUserRoleTb<T>> : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.USER_ID, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '用户id'")
    var userId: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 乐观锁锁数据
     */
    @Version
    @Column(name = SpblwBaseTableConfig.CommonColumn.OPTIMISTIC_LOCKING, nullable = false, columnDefinition = "int comment '用户乐观锁'")
    var version: Int = 0

    /**
     * 用户账户
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.ACCOUNT, nullable = false, columnDefinition = "varchar(200) comment '用户账户'")
    var account: String? = null

    /**
     * 用户密码
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.PASSWORD, nullable = false, columnDefinition = "text comment '用户密码'")
    var password: String? = null

    /**
     * 联系方式
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.PHONE_NUM, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.PHONE_NUMBER} comment '联系电话'")
    var phoneNum: String? = null// varchar(31) DEFAULT NULL,

    /**
     * 邮箱
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.EMAIL, columnDefinition = "${SpblwBaseTableConfig.ColumnType.EMAIL} comment '邮箱'")
    var email: String? = null

    /**
     * security加密的密码种子，和md5加密的密码类似
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.SECURITY_SALT, nullable = false,
        columnDefinition = "text comment 'security加密的密码种子，和md5加密的密码类似'")
    var securitySalt: String? = null

    /**
     * 用户角色
     */
    @JoinColumn(name = SpulwBaseTableConfig.UserInfoColumn.USER_ROLE, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '用户角色'")
    @ManyToOne
    var userRole: ROLE_TB? = null

    /**
     * 用户昵称
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.NICK_NAME, nullable = false, columnDefinition = "varchar(200) comment '用户昵称'")
    var nickName: String? = null

    /**
     * 用户token
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.ACCESS_TOKEN, columnDefinition = "varchar(200) " + "comment '用户token'")
    var accessToken: String? = null

    /**
     * 用户状态
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.STATUS, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.STATUS} comment '用户状态'")
    var status: Int? = null

}
