package com.test.springboot.controller

import com.qtoolsbaby.servicemmxs.base.BaseController
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.kotlinExtend.controllerCheckAndOptions
import javabase.lorenwang.tools.JtlwMatchesRegularCommon
import javabase.lorenwang.tools.common.JtlwDateTimeUtils
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
        return request.controllerCheckAndOptions(emptyCheckArray = arrayOf(reqBean), baseController = this) {
            return@controllerCheckAndOptions responseSuccess(null)
        }
    }
}
