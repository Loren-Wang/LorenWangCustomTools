package springbase.lorenwang.user.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.hibernate.annotations.GenericGenerator
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.base.database.table.SpblwBaseTb
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * 功能作用：验证码数据库
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
@Table(name = SpulwBaseTableConfig.TableName.VERIFICATION_CODE)
@org.hibernate.annotations.Table(appliesTo = SpulwBaseTableConfig.TableName.VERIFICATION_CODE, comment = "验证码表")
class SpulwVerificationCodeTb : SpblwBaseTb(), Serializable, Cloneable {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "my", strategy = "uuid")  //声明一个主键生成策略,并设置一个引用名称,这里采用的是hibernate提供的预定策略
    @GeneratedValue(generator = "my")  //配置主键的生成策略为自己声明的那个生成策略
    @Column(name = SpulwBaseTableConfig.VerificationCodeColumn.ID, nullable = false,
        columnDefinition = "${SpblwBaseTableConfig.ColumnType.COMMON_PRIMARY_KEY}  comment '主键id'")
    var vsId: String? = null

    /**
     * 验证码
     */
    @Column(name = SpulwBaseTableConfig.VerificationCodeColumn.CODE, nullable = false, columnDefinition = "varchar(10) comment '验证码'")
    var code: String? = null

    /**
     * 账户
     */
    @Column(name = SpulwBaseTableConfig.VerificationCodeColumn.ACCOUNT, nullable = false, columnDefinition = "varchar(1000) comment '账户'")
    var account: String? = null

    /**
     * 结束时间
     */
    @Column(name = SpulwBaseTableConfig.VerificationCodeColumn.END_TIME, nullable = false, columnDefinition = "DATETIME comment '结束时间'")
    var endTime: Date? = null

    /**
     * 验证码类型
     */
    @Column(name = SpblwBaseTableConfig.CommonColumn.TYPE, columnDefinition = "varchar(100) comment '验证码类型'")
    var type: String? = null

}