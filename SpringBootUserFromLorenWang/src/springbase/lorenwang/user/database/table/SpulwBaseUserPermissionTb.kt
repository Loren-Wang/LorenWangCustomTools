package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.tools.common.JtlwCommonUtil
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：用户权限表
 * 创建时间：2019-10-18 下午 16:14:13
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
open class SpulwBaseUserPermissionTb<T> : SpblwBaseTb(), Serializable, Cloneable {

    /**
     * 权限id
     */
    @Id
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.ID,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment '权限id'")
    var permissionId: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 权限名称
     */
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.PERMISSION_NAME, nullable = false, columnDefinition = "varchar(100) comment '权限名称'")
    var permissionName: String? = null

    /**
     * 权限类型
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.TYPE, nullable = false, columnDefinition = "int comment '角色类型'")
    var type: Int? = null

    /**
     * 角色名称
     */
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.PERMISSION_ROLE, nullable = false)
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = SpulwBaseTableConfig.UserRoleColumn.ROLE_PERMISSION)
    var permissionRole: MutableSet<T>? = null
}
