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
@JsonAutoDetect
@Entity
@Table(name = SpulwBaseTableConfig.TableName.USER_ROLE)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.USER_ROLE, comment = "用户角色表")
class SpulwUserRoleTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * 权限id
     */
    @Id
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ID, columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment 'id'")
    var id: String = JtlwCommonUtil.getInstance().generateUuid(true)

    /**
     * 角色类型
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.TYPE, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.TYPE} comment '角色类型'")
    var type: Int? = null

    /**
     * 角色名称
     */
    @Column(name = SpulwBaseTableConfig.UserRoleColumn.ROLE_NAME, nullable = false, columnDefinition = "varchar(100) comment '角色名称'")
    var name: String? = null
}
