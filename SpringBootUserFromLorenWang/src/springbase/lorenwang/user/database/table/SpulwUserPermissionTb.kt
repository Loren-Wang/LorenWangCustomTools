package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.GenericGenerator
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
@JsonAutoDetect
@Entity
@Table(name = SpulwBaseTableConfig.TableName.USER_PERMISSION)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.USER_PERMISSION, comment = "用户权限表")
class SpulwUserPermissionTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "my", strategy = "uuid")  //声明一个主键生成策略,并设置一个引用名称,这里采用的是hibernate提供的预定策略
    @GeneratedValue(generator = "my")  //配置主键的生成策略为自己声明的那个生成策略
    @Column(name = SpulwBaseTableConfig.UserPermissionColumn.ID,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment 'id'")
    var id: String? = null

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
