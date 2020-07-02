package com.qtoolsbaby.servicemmxs.base

import com.test.springboot.service.UserService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseControllerFilter
import javabase.lorenwang.common_base_frame.service.SbcbflwUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
internal class BaseControllerFilter : SbcbflwBaseControllerFilter() {
//    @Autowired
    private lateinit var userService:UserService
    /**
     * 获取用户服务
     */
    override fun getUserService(): SbcbflwUserService {
        return userService
    }

    override fun responseErrorUser(errorInfo: SbcbflwBaseDataDisposeStatusBean?): String {
        return ""
    }
}
