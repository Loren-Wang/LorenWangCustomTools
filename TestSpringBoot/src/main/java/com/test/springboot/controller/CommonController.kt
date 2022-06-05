package com.test.springboot.controller

import com.test.springboot.bean.LoginWxReq
import com.test.springboot.bean.local.BaseDataDisposeStatusBean
import com.test.springboot.configOptions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springbase.lorenwang.base.controller.SpblwBaseController
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.service.SpblwVerificationCodeService
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

    @Autowired
    private lateinit var codeService: SpblwVerificationCodeService

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

    @PostMapping("code")
    @ApiOperation(value = "code", httpMethod = "POST")
    fun code(request: SpblwBaseHttpServletRequestWrapper): String {
//        configOptions.getSmsUtil().sendSms("19145585201", mapOf(Pair("code", "1234")), "SMS_173340367")
//        configOptions.getSmsUtil().sendSms("19145585201", mapOf(Pair("title", "去超市"), Pair("time", Date()), Pair("remark", "记得买盐")), "SMS_180060517")
//        codeService.getVerificationCode("1234", "111")
        configOptions.getEmailUtil().sendEmailMessage("短信验证码：1234", "745342832@qq.com")
        return ""
    }
}
