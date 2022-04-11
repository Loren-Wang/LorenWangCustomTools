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
@JsonAutoDetect
@Entity
@Table(name = SpulwBaseTableConfig.TableName.USER_PERMISSION)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.USER_PERMISSION, comment = "用户权限表")
class SpulwUserPermissionTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.ID,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment 'id'")
    var id: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 权限名称
     */
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.PERMISSION_NAME, nullable = false, columnDefinition = "varchar(100) comment '权限名称'")
    var permissionName: String? = null

    /**
     * 权限类型
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.TYPE, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.TYPE} comment '角色类型'")
    var type: Int? = null
}
