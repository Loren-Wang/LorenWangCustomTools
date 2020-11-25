package javabase.lorenwang.common_base_frame.database

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
object SbcbflwBaseTableConfig {
    /*********************************表名部分*********************************/

    object TableName {
        /**
         * 用户角色权限中间表
         */
        const val INTERMEDIATE_USER_ROLE_PERMISSION = "intermediate_role_permission"
    }

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

    /********************************数据库表版本表部分*****************************/
    object DatabaseTableVersionColumn {
        /**
         * 版本id
         */
        const val VERSION_ID = "v_id"

        /**
         * 版本名称
         */
        const val VERSION_NAME = "v_name"

        /**
         * 版本码
         */
        const val VERSION_CODE = "v_code"
    }

    /*********************************用户信息部分*****************************/
    object UserInfoColumn {
        /**
         * id
         */
        const val USER_ID = "user_id"

        /**
         * 名称
         */
        const val ACCOUNT = "account"

        /**
         *密码
         */
        const val PASSWORD = "password"

        /**
         * 昵称
         */
        const val NICK_NAME = "nick_name"

        /**
         * security加密的密码种子，和md5加密的密码类似
         */
        const val SECURITY_SALT = "security_salt"

        /**
         * security用户角色，还没有搞明白
         */
        const val SECURITY_ROLE = "security_role"

        /**
         * 用户角色
         */
        const val USER_ROLE = "user_role"

        /**
         * 用户token
         */
        const val ACCESS_TOKEN = "access_token"
    }

    /*******************************用户角色部分*****************************/
    object UserRoleColumn {
        /**
         * id
         */
        const val ID = "role_id"

        /**
         * 角色类型
         */
        const val ROLE_TYPE = "role_type"

        /**
         * 角色名称
         */
        const val ROLE_NAME = "role_name"

        /**
         * 角色权限
         */
        const val ROLE_PERMISSION = "permission"
    }

    /*******************************用户权限部分*****************************/
    object UserPermissionColumn {
        /**
         * id
         */
        const val ID = "permission_id"

        /**
         * 权限名称
         */
        const val PERMISSION_NAME = "permission_name"

        /**
         * 权限角色
         */
        const val PERMISSION_ROLE = "permission_role"
    }

    /*******************************用户角色权限中间表部分*****************************/
    object IntermediateUserRolePermissionColumn {
        /**
         * 角色id
         */
        const val ROLE_ID = "r_id"

        /**
         * 权限id
         */
        const val PERMISSION_ID = "p_id"
    }
}
