package com.test.springboot.controller

import com.test.springboot.bean.LoginWxReq
import com.test.springboot.bean.local.BaseDataDisposeStatusBean
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springbase.lorenwang.base.controller.SpblwBaseController
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.user.database.table.SpulwUserInfoTb
import springbase.lorenwang.user.enums.SpulwUserLoginFromEnum
import springbase.lorenwang.user.enums.SpulwUserLoginTypeEnum
import springbase.lorenwang.user.interfaces.SpulwLoginUserCallback
import springbase.lorenwang.user.service.SpulwUserService

/**
 * 功能作用：通用接口请求
 * 创建时间：2020-06-12 12:14 下午
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
@RestController
@RequestMapping("/")
@Api(tags = ["test"], description = "test")
class CommonController : SpblwBaseController<SpblwBaseHttpServletRequestWrapper>() {
    @Autowired
    private lateinit var service: SpulwUserService

    @PostMapping("test")
    @ApiOperation(value = "test", httpMethod = "POST")
    fun submit(request: SpblwBaseHttpServletRequestWrapper, reqBean: LoginWxReq): String {
        super.base(request, reqBean)
        return responseContent(request, BaseDataDisposeStatusBean(true,
            service.loginUser(reqBean.code!!, reqBean.info, SpulwUserLoginFromEnum.WEB, SpulwUserLoginTypeEnum.PHONE_CODE,
                object : SpulwLoginUserCallback {
                    override fun loginUserSuccess(token: String, info: SpulwUserInfoTb): String {
                        return info.kttlwToJsonData()
                    }

                    override fun loginUserFailUnKnow(msg: String?): String {
                        return ""
                    }

                    override fun userIsEmpty(): String {
                        return "用户不存在"
                    }
                })))
    }
}
