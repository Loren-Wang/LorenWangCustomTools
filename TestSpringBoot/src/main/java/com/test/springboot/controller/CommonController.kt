package com.test.springboot.controller

import com.test.springboot.base.BaseController
import com.test.springboot.base.BaseHttpServletRequestWrapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springbase.lorenwang.base.kotlinExtend.spblwControllerCheckAndOptions

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

    @GetMapping("test")
    @ApiOperation(value = "test", httpMethod = "GET")
    fun submit(request: BaseHttpServletRequestWrapper, reqBean: String): String {
        super.base(request, reqBean)
        return spblwControllerCheckAndOptions(request, arrayOf(reqBean), this) {
            responseSuccess(request, null)
        }
    }
}
