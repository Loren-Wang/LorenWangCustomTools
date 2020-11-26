package com.test.springboot.service.impl

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.service.SbcbflwUserService
import org.springframework.stereotype.Service

/**
 * 功能作用：用户服务实现
 * 创建时间：2020-07-02 9:50 上午
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
class UserServiceImpl :SbcbflwUserService() {
    /**
     * 检查token是否有效
     */
    override fun checkAccessTokenEffective(token: String?): SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 检测用户是否已经登录
     */
    override fun checkUserLogin(request: SbcbflwBaseHttpServletRequestWrapper): SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 通过请求头获取用户token
     */
    override fun getAccessTokenByReqHeader(request: SbcbflwBaseHttpServletRequestWrapper): String? {
        TODO("Not yet implemented")
    }

    /**
     * 根据用户token获取用户id
     */
    override fun getUserIdByAccessToken(token: String?): String? {
        TODO("Not yet implemented")
    }

    /**
     * 刷新用户token
     */
    override fun refreshAccessToken(token: String): String {
        TODO("Not yet implemented")
    }
}
