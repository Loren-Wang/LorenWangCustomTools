package service.qtoolsbaby.official.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：用户权限表
 * 创建时间：2019-10-18 下午 16:14:13
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
open class SbcbflwBaseUserPermissionTb<T> : SbcbflwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SbcbflwBaseTableConfig.UserPermissionColumn.ID, nullable = false, columnDefinition = "${SbcbflwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment '权限id'")
    var permissionId: Long? = null
    /**
     * 权限名称
     */
    @Column(name = SbcbflwBaseTableConfig.UserPermissionColumn.PERMISSION_NAME, nullable = false, columnDefinition = "varchar(100) comment '权限名称'")
    var permissionName: String? = null

    /**
     * 权限类型
     */
    @Column(name = SbcbflwBaseTableConfig.CommonColumn.TYPE, nullable = false, columnDefinition = "int comment '角色类型'")
    var type: Int? = null

    /**
     * 角色名称
     */
    @Column(name = SbcbflwBaseTableConfig.UserPermissionColumn.PERMISSION_ROLE, nullable = false)
    @ManyToMany(mappedBy = SbcbflwBaseTableConfig.UserRoleColumn.ROLE_PERMISSION)
    var permissionRole: MutableSet<T>? = null
}
