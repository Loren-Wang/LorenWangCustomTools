package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.GenericGenerator
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：第三方平台token表
 * 初始注释时间： 2022/4/16 12:44
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
@JsonAutoDetect
@Entity
@Table(name = SpulwBaseTableConfig.TableName.PLATFORM_TOKEN)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.PLATFORM_TOKEN, comment = "平台token表")
class SpulwPlatformTokenTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "my", strategy = "uuid")  //声明一个主键生成策略,并设置一个引用名称,这里采用的是hibernate提供的预定策略
    @GeneratedValue(generator = "my")  //配置主键的生成策略为自己声明的那个生成策略
    @Column(name = SpulwBaseTableConfig.PlatformTokenColumn.ID, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment '主键id'")
    var tbId: String? = null

    /**
     * 平台id
     */
    @Column(name = SpulwBaseTableConfig.PlatformTokenColumn.PLATFORM_ID, nullable = false, columnDefinition = "varchar(100) comment '平台id'")
    var platformId: String? = null

    /**
     * 主token
     */
    @Column(name = SpulwBaseTableConfig.PlatformTokenColumn.ACCESS_TOKEN, nullable = false, columnDefinition = "varchar(100) comment '认证token'")
    var accessToken: String? = null

    /**
     * 刷新token
     */
    @Column(name = SpulwBaseTableConfig.PlatformTokenColumn.REFRESH_TOKEN, columnDefinition = "varchar(100) comment '刷新token'")
    var refreshToken: String? = null

    /**
     * 失效时间,也是token有效时间
     */
    @Column(name = SpulwBaseTableConfig.PlatformTokenColumn.FAILURE_TIME, columnDefinition = "long comment '失效时间'")
    var failureTime: Long? = null
}