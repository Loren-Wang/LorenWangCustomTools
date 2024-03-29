package springbase.lorenwang.base.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * 功能作用：基础表，所有表都要继承，所有表都要有的字段，部分表不用继承
 * 创建时间：2019-09-19 下午 14:53:59
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
@EntityListeners(AuditingEntityListener::class)
open class SpblwBaseTb : Serializable, Cloneable {
    /**
     * 创建时间
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.CREATE_TIME, updatable = false, columnDefinition = "DATETIME comment '创建时间'")
    @CreationTimestamp
    var createTime: Date? = null

    /**
     * 更新时间时间
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.UPDATE_TIME, columnDefinition = "DATETIME comment '更新时间'")
    @UpdateTimestamp
    var updateTime: Date? = null
}
