package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.tools.common.JtlwCommonUtil
import org.hibernate.annotations.Cascade
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.database.SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.PERMISSION_ID
import springbase.lorenwang.user.database.SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.ROLE_ID
import springbase.lorenwang.user.database.SpulwBaseTableConfig.TableName.INTERMEDIATE_USER_ROLE_PERMISSION
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：用户角色表
 * 创建时间：2019-10-18 下午 16:09:32
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@MappedSuperclass
@JsonAutoDetect
open class SpulwBaseUserRoleTb<T> : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * 角色id
     */
    @Id
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ID, columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment '角色id'")
    var roleId: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 角色类型
     */
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ROLE_TYPE, nullable = false, columnDefinition = "int comment '角色类型'")
    var roleType: Int? = null

    /**
     * 角色名称
     */
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ROLE_NAME, nullable = false, columnDefinition = "varchar(50) comment '角色名称'")
    var roleName: String? = null

    /**
     * 角色名称
     */
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ROLE_PERMISSION, nullable = false)
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = INTERMEDIATE_USER_ROLE_PERMISSION,
        joinColumns = [JoinColumn(name = ROLE_ID, referencedColumnName = SpulwBaseTableConfig.UserRoleColumn.ID)],
        inverseJoinColumns = [JoinColumn(name = PERMISSION_ID, referencedColumnName = SpulwBaseTableConfig.UserPermissionColumn.ID)])
    @Cascade(value = [org.hibernate.annotations.CascadeType.ALL])
    var permission: MutableSet<T>? = null


}
