package service.qtoolsbaby.official.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.IntermediateUserRolePermissionColumn.PERMISSION_ID
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.IntermediateUserRolePermissionColumn.ROLE_ID
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.TableName.INTERMEDIATE_USER_ROLE_PERMISSION
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：用户角色表
 * 创建时间：2019-10-18 下午 16:09:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@MappedSuperclass
@JsonAutoDetect
open class SbcbflwBaseUserRoleTb<T> : SbcbflwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SbcbflwBaseTableConfig.UserRoleColumn.ID, nullable = false, columnDefinition = "${SbcbflwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '角色id'")
    var id: Long? = null
    /**
     * 角色类型
     */
    @Column(name = SbcbflwBaseTableConfig.UserRoleColumn.ROLE_TYPE, nullable = false, columnDefinition = "int comment '角色类型'")
    var roleType: Int? = null
    /**
     * 角色名称
     */
    @Column(name = SbcbflwBaseTableConfig.UserRoleColumn.ROLE_NAME, nullable = false, columnDefinition = "varchar(50) comment '角色名称'")
    var roleName: String? = null

    /**
     * 角色名称
     */
    @Column(name = SbcbflwBaseTableConfig.UserRoleColumn.ROLE_PERMISSION, nullable = false)
    @ManyToMany
    @JoinTable(name = INTERMEDIATE_USER_ROLE_PERMISSION,
            joinColumns = [JoinColumn(name = ROLE_ID, referencedColumnName = SbcbflwBaseTableConfig.UserRoleColumn.ID)],
            inverseJoinColumns = [JoinColumn(name = PERMISSION_ID, referencedColumnName = SbcbflwBaseTableConfig.UserPermissionColumn.ID)])
    var permission: MutableSet<T>? = null


}
