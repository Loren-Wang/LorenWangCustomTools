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
