package springbase.lorenwang.base.database

/**
 * 功能作用：基础表配置类
 * 创建时间：2020-01-06 16:10
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
object SpblwBaseTableConfig {

    /**********************************通用部分*********************************/
    object CommonColumn {
        /**
         * 乐观锁字段
         */
        const val OPTIMISTIC_LOCKING = "locking_version"

        /**
         * 创建时间
         */
        const val CREATE_TIME = "create_time"

        /**
         * 更新时间
         */
        const val UPDATE_TIME = "update_time"

        /**
         * 父级id
         */
        const val PARENT_ID = "parent_id"

        /**
         * 名称
         */
        const val NAME = "name"

        /**
         * 名称id
         */
        const val NAME_ID = "name_id"

        /**
         * 图片
         */
        const val IMG_PATH = "img_path"

        /**
         * 图片
         */
        const val IMG_PATH_ID = "img_path_id"

        /**
         * 排行
         */
        const val RANK = "rank_list"

        /**
         * 手机号
         */
        const val PHONE_NUM = "phone_num"

        /**
         * 邮箱
         */
        const val EMAIL = "email"

        /**
         * 状态
         */
        const val STATUS = "status"

        /**
         * 备注
         */
        const val REMARKS = "remarks"

        /**
         * 类型
         */
        const val TYPE = "type"

        /**
         * 是否必选
         */
        const val IS_REQUIRED = "is_required"
    }

    /**********************************字段类型*********************************/
    object ColumnType {
        /**
         * 手机号
         */
        const val PHONE_NUMBER = "varchar(30)"

        /**
         * 邮箱
         */
        const val EMAIL = "varchar(100)"

        /**
         * 图片
         */
        const val IMAGE = "text"

        /**
         * 自定义的图片上传使用的
         */
        const val IMAGE_CUSTOM = "VARCHAR(1024)"

        /**
         * 人员名称
         */
        const val PERSON_NAME = "varchar(100)"

        /**
         * 状态类型
         */
        const val STATUS = "int"

        /**
         * 通用主键类型
         */
        const val COMMON_PRIMARY_KEY = "varchar(32)"

        /**
         * 类型字段类型
         */
        const val TYPE = "int"

        /**
         * 排行字段类型
         */
        const val RANK = "bigint"
    }

}
