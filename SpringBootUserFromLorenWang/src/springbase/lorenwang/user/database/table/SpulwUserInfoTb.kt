package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.GenericGenerator
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.enums.SpulwUserStatusEnum
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
@Entity
@Table(name = SpulwBaseTableConfig.TableName.USER_INFO)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.USER_INFO, comment = "用户信息表")
open class SpulwUserInfoTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "my", strategy = "uuid")  //声明一个主键生成策略,并设置一个引用名称,这里采用的是hibernate提供的预定策略
    @GeneratedValue(generator = "my")  //配置主键的生成策略为自己声明的那个生成策略
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.USER_ID, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '用户id'")
    var userId: String? = null

    /**
     * 乐观锁锁数据
     */
    @Version
    @Column(name = SpblwBaseTableConfig.CommonColumn.OPTIMISTIC_LOCKING, nullable = false, columnDefinition = "int comment '用户乐观锁'")
    var version: Int = 0

    /**
     * 用户账户
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.ACCOUNT, columnDefinition = "varchar(200) comment '用户账户'")
    var account: String? = null

    /**
     * 用户性别
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.SEX, columnDefinition = "int comment '用户性别'")
    var sex: Int? = null

    /**
     * 用户密码
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.PASSWORD, columnDefinition = "text comment '用户密码'")
    var password: String? = null

    /**
     * 联系方式
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.PHONE_NUM, columnDefinition = "${SpblwBaseTableConfig.ColumnType.PHONE_NUMBER} comment '联系电话'")
    var phoneNum: String? = null

    /**
     * 邮箱
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.EMAIL, columnDefinition = "${SpblwBaseTableConfig.ColumnType.EMAIL} comment '邮箱'")
    var email: String? = null

    /**
     * 头像
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.HEAD_IMAGE, columnDefinition = "${SpblwBaseTableConfig.ColumnType.IMAGE} comment '头像'")
    var headImage: String? = null

    /**
     * 微信登录的id,尽量使用微信返回的unionid，如果没有则为openId
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.WX_ID, columnDefinition = "varchar(100) comment '微信登录id,尽量使用微信返回的unionid，如果没有则为openId'")
    var wxId: String? = null

    /**
     * QQ登录的id
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.QQ_ID, columnDefinition = "varchar(100) comment 'QQ登录id'")
    var qqId: String? = null

    /**
     * 新浪微博登录的id
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.SINA_ID, columnDefinition = "varchar(100) comment '新浪微博登录id'")
    var sinaId: String? = null

    /**
     * security加密的密码种子，和md5加密的密码类似
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.SECURITY_SALT, columnDefinition = "text comment 'security加密的密码种子，和md5加密的密码类似'")
    var securitySalt: String? = null

    /**
     * 用户角色
     */
    @JoinColumn(name = SpulwBaseTableConfig.UserInfoColumn.USER_ROLE,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '用户角色'")
    var userRole: SpulwUserRoleTb? = null

    /**
     * 用户昵称
     */
    @Column(name = SpulwBaseTableConfig.UserInfoColumn.NICK_NAME, columnDefinition = "varchar(200) comment '用户昵称'")
    var nickName: String? = null

    /**
     * 用户状态
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.STATUS, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.STATUS} comment '用户状态'")
    var status: Int = SpulwUserStatusEnum.ENABLE.status

}
