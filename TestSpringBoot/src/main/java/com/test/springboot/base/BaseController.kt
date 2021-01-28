package com.test.springboot.base

import com.test.springboot.enums.NetRepStatusEnum
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData

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
open class BaseController : SbcbflwBaseController<BaseHttpServletRequestWrapper>() {
    /**
     * 数据删除失败
     */
    override fun responseDeleteFail(request : BaseHttpServletRequestWrapper?) : String {
        return getResponseData(NetRepStatusEnum.DATA_DELETE_FAIL)
    }

    /**
     * 参数异常响应
     */
    override fun responseErrorForParams(request : BaseHttpServletRequestWrapper?) : String {
        return getResponseData(NetRepStatusEnum.PARAMS_ERROR)
    }

    /**
     * 无权限异常
     */
    override fun responseErrorNotPermission(request : BaseHttpServletRequestWrapper?) : String {
        return getResponseData(NetRepStatusEnum.USER_HAVE_NOT_PERMISSION)
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     */
    override fun responseErrorUserLoginEmptyOrTokenNoneffective(request : BaseHttpServletRequestWrapper?) : String {
        return getResponseData(NetRepStatusEnum.USER_NOT_LOGIN_OR_TOKEN_FAILURE)
    }

    /**
     * 未知错误失败
     */
    override fun responseFailForUnKnow(request : BaseHttpServletRequestWrapper?) : String {
        return getResponseData(NetRepStatusEnum.FAIL_UN_KNOW)
    }

    /**
     * 数据响应成功
     */
    override fun responseSuccess(request : BaseHttpServletRequestWrapper?, data : Any?) : String {
        return getResponseData(NetRepStatusEnum.SUCCESS, data)
    }

    /**
     * 获取响应信息
     * @param repStatusEnum 状态枚举
     * @param data data信息
     */
    private fun getResponseData(repStatusEnum : NetRepStatusEnum, data : Any? = null) : String {
        val repBean = KttlwBaseNetResponseBean(data)
        repBean.stateMessage = getMessage(repStatusEnum.messageKey)
        repBean.stateCode = repStatusEnum.code
        return repBean.kttlwToJsonData()
    }
}
