package com.moments_of_life.task.enums

/**
 * 功能作用：网络响应状态枚举
 * 创建时间：2020-11-26 2:10 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 * @param code 响应code
 * @param messageKey 响应消息的文本读取key
 */
enum class NetRepStatusEnum(val code : String, val messageKey : String) {
    /**
     * 成功
     */
    SUCCESS("200", "net_response_statue_success"),

    /**
     * 参数异常
     */
    PARAMS_ERROR("1000", "net_response_statue_params_error"),

    /**
     * 未知异常失败
     */
    FAIL_UN_KNOW("1001", "net_response_statue_fail_un_know"),

    /**
     * 用户没有权限
     */
    USER_HAVE_NOT_PERMISSION("3000", "net_response_status_user_have_not_permission"),

    /**
     * 用户没有登录或者token失效
     */
    USER_NOT_LOGIN_OR_TOKEN_FAILURE("3001", "net_response_status_user_empty_or_token_failure"),

    /**
     * 数据删除失败
     */
    DATA_DELETE_FAIL("2000", "net_response_status_data_delete_fail"),
}
