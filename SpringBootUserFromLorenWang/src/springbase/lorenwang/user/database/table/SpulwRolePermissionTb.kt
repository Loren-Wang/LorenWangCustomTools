package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.GenericGenerator
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

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
    @GenericGenerator(name = "my", strategy = "uuid")  //声明一个主键生成策略,并设置一个引用名称,这里采用的是hibernate提供的预定策略
    @GeneratedValue(generator = "my")  //配置主键的生成策略为自己声明的那个生成策略
    @Column(name = SpblwBaseTableConfig.CommonColumn.PARENT_ID,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY} comment 'id'")
    var id: String? = null

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
