package springbase.lorenwang.base.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：数据库表版本
 * 创建时间：2019-10-02 下午 15:45:1
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
open class SpblwBaseDatabaseTableVersionTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * 版本id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SpblwBaseTableConfig.DatabaseTableVersionColumn.VERSION_ID)
    var id: Long? = null

    /**
     * 版本名称
     */
    @Column(name = SpblwBaseTableConfig.DatabaseTableVersionColumn.VERSION_NAME, nullable = false, columnDefinition = "varchar(50) comment '数据库版本名称'")
    var versionName: String? = null

    /**
     * 版本码
     */
    @Column(name = SpblwBaseTableConfig.DatabaseTableVersionColumn.VERSION_CODE, nullable = false, columnDefinition = "bigint comment '数据库版本码'")
    var versionCode: Long? = null
}
