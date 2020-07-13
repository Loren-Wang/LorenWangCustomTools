package com.test.springboot.controller

import com.qtoolsbaby.servicemmxs.base.BaseController
import com.test.springboot.enums.UserPermissionType
import com.test.springboot.kotlinExtend.controllerCheckAndOptions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.tools.file.JtlwFileOptionUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

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
class CommonController : BaseController() {
    @PostMapping("test")
    @ApiOperation(value = "test", httpMethod = "POST")
    fun submit(request: SbcbflwBaseHttpServletRequestWrapper, @RequestBody reqBean: Object):
            String {
        super.base(request, reqBean)
        UserPermissionType.s
        return request.controllerCheckAndOptions(arrayOf(reqBean), this) {
            return@controllerCheckAndOptions responseSuccess(null)
        }
    }
}
