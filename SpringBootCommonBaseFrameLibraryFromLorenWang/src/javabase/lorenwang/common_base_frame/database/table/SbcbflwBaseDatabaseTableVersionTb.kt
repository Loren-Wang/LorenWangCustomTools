package javabase.lorenwang.common_base_frame.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig
import java.io.Serializable
import javax.persistence.*

/**
 * 功能作用：数据库表版本
 * 创建时间：2019-10-02 下午 15:45:1
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
open class SbcbflwBaseDatabaseTableVersionTb : SbcbflwBaseTb(), Serializable, Cloneable {
    /**
     * 版本id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SbcbflwBaseTableConfig.DatabaseTableVersionColumn.VERSION_ID)
    var id: Long? = null
    /**
     * 版本名称
     */
    @Column(name = SbcbflwBaseTableConfig.DatabaseTableVersionColumn.VERSION_NAME, nullable = false, columnDefinition = "varchar(50) comment '数据库版本名称'")
    var versionName: String? = null
    /**
     * 版本码
     */
    @Column(name = SbcbflwBaseTableConfig.DatabaseTableVersionColumn.VERSION_CODE, nullable = false, columnDefinition = "bigint comment '数据库版本码'")
    var versionCode: Long? = null
}
