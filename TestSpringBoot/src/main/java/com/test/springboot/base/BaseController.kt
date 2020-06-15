package com.qtoolsbaby.servicemmxs.base

import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.dataparse.JdplwJsonUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 功能作用：基础接口
 * 创建时间：2020-06-12 12:16 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
open class BaseController :SbcbflwBaseController() {
    /**
     * 数据删除失败
     */
    override fun responseDeleteFail(): String {
        return ""
    }

    /**
     * 参数异常响应
     */
    override fun responseErrorForParams(): String {
        return ""
    }

    /**
     * 无权限异常
     */
    override fun responseErrorNotPermission(): String {
        return ""
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     */
    override fun responseErrorUserLoginEmptyOrTokenNoneffective(): String {
        return ""
    }

    /**
     * 未知错误失败
     */
    override fun responseFailForUnKnow(): String {
        return ""
    }

    /**
     * 数据响应成功
     */
    override fun responseSuccess(data: Any?): String {
        return ""
    }
}
