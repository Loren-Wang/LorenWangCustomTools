package springbase.lorenwang.user.database

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
object SpulwBaseTableConfig {
    /*********************************表名部分*********************************/

    object TableName {
        /**
         * 用户角色权限中间表
         */
        const val INTERMEDIATE_USER_ROLE_PERMISSION = "intermediate_role_permission"

        /**
         * 用户角色表
         */
        const val USER_ROLE = "user_role"

        /**
         * 用户角色权限表
         */
        const val USER_PERMISSION = "user_permission"

        /**
         * 用户信息表
         */
        const val USER_INFO = "user_info"

        /**
         * 平台token
         */
        const val PLATFORM_TOKEN = "platform_token"
    }

    /*********************************用户信息部分*****************************/
    object UserInfoColumn {
        /**
         * id
         */
        const val USER_CHILD_ID = "user_child_id"

        /**
         * 用户组id
         */
        const val USER_GROUP_ID = "user_group_id"

        /**
         * 名称
         */
        const val ACCOUNT = "account"

        /**
         * 用户写别
         */
        const val SEX = "sex"

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
         * 用户角色
         */
        const val USER_ROLE = "user_role"

        /**
         * 头像
         */
        const val HEAD_IMAGE = "head_image"

        /**
         * 微信开放id
         */
        const val WX_ID = "wx_id"

        /**
         * qqID
         */
        const val QQ_ID = "qq_id"

        /**
         * 新浪微博id
         */
        const val SINA_ID = "sina_id"
    }

    /*******************************第三方平台token部分*****************************/
    object PlatformTokenColumn {
        /**
         * id
         */
        const val ID = "table_id"

        /**
         * 平台返回的id
         */
        const val PLATFORM_ID = "platform_id"

        /**
         * 使用的token
         */
        const val ACCESS_TOKEN = "access_token"

        /**
         * 刷新的token
         */
        const val REFRESH_TOKEN = "refresh_token"

        /**
         * 失效时间
         */
        const val FAILURE_TIME = "failure_time"

    }

    /*******************************用户角色权限中间表部分*****************************/
    object IntermediateUserRolePermissionColumn {
        /**
         * 角色
         */
        const val ROLE = "role"

        /**
         * 权限
         */
        const val PERMISSION = "permission"
    }
}
