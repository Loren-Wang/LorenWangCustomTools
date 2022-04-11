package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.tools.common.JtlwCommonUtil
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * 功能作用：角色权限对照表
 * 创建时间：2019-10-18 下午 16:09:32
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
@Table(name = SpulwBaseTableConfig.TableName.INTERMEDIATE_USER_ROLE_PERMISSION)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.INTERMEDIATE_USER_ROLE_PERMISSION, comment = "角色权限对照表")
class SpulwRolePermissionTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * 权限id
     */
    @Id
    @Column(name = SpblwBaseTableConfig.CommonColumn.PARENT_ID,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment 'id'")
    var id: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 角色id
     */
    @Column(name = SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.ROLE, columnDefinition = "int comment '角色id'")
    var role: Int? = null

    /**
     * 权限id
     */
    @Column(name = SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.PERMISSION, columnDefinition = "int comment '权限id'")
    var permission: Int? = null

}
