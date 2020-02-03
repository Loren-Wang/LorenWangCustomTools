package javabase.lorenwang.common_base_frame.controller

import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiParam
import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils

/**
 * 功能作用：基础请求实体
 * 创建时间：2019-09-12 下午 16:33:22
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1：是否返回完整图片地址---returnFullImagePath
 * 配置参数2：分页页码---pageIndex
 * 配置参数3：每页请求数量---pageSize
 */
open class SbcbflwBaseRequestBean {
    /**
     * 是否返回完整图片地址
     */
    @ApiModelProperty(value = "请求中返回的图片相关是否返回全地址链接", example = "true")
    @ApiParam(value = "请求中返回的图片相关是否返回全地址链接", defaultValue = true.toString())
    var returnFullImagePath = true;
    /**
     * 分页页码
     */
    @ApiModelProperty(value = "进行分页列表请求是传递的分页页码")
    @ApiParam(value = "请求中返回的图片相关是否返回全地址链接")
    var pageIndex = SbcbflwCommonUtils.instance.propertiesConfig.defaultRequestPageIndex;
    /**
     * 每页请求数量
     */
    @ApiModelProperty(value = "进行分页列表请求是传递的每页请求数量")
    @ApiParam(value = "进行分页列表请求是传递的每页请求数量")
    var pageSize = SbcbflwCommonUtils.instance.propertiesConfig.defaultRequestPageSize;
}
