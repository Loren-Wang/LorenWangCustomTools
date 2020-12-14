package com.test.springboot.base

import com.test.springboot.base.BaseHttpServletRequestWrapper
import com.test.springboot.service.UserService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseControllerFilter
import javabase.lorenwang.common_base_frame.service.SbcbflwUserService
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.extend.toJsonData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest

/**
 * 功能作用：
 * 创建时间：2020-06-12 12:11 下午
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
@Service
internal class BaseControllerFilter : SbcbflwBaseControllerFilter<BaseHttpServletRequestWrapper>() {
    /**
     * 格式化
     *
     * @param request 默认进入的请求
     * @return 格式化的请求
     */
    override fun paramsRequest(request : ServletRequest?) : BaseHttpServletRequestWrapper {
        return BaseHttpServletRequestWrapper(request as HttpServletRequest)
    }

    /**
     * 获取header中accessToken使用的关键字
     *
     * @return key值关键字
     */
    override fun getHeaderAccessTokenKey() : String {
        return Setting.ACCESS_TOKEN_KEY
    }

}
