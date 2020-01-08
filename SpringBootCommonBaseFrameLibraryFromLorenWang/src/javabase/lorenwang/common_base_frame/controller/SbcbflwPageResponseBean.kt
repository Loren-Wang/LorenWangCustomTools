package javabase.lorenwang.common_base_frame.controller

import io.swagger.annotations.ApiModelProperty

/**
 * 功能作用：列表分页数据请求响应实体
 * 创建时间：2019-10-22 上午 11:45:27
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1：分页的页码---pageIndex
 * 配置参数2：分页的每页请求数量---pageSize
 * 配置参数3：当前条件下的取到的数据总数---sumCount
 * 配置参数4：列表数据实体，不能为空，但是可以为空数组---dataList
 */
open class SbcbflwPageResponseBean<T>(@ApiModelProperty(value = "分页的页码，由传递进来的数据决定", required = true) var pageIndex: Int,
                                      @ApiModelProperty(value = "分页的每页请求数量，为实际数量而不是请求数量", required = true) var pageSize: Int,
                                      @ApiModelProperty(value = "当前条件下的取到的数据总数", required = true) var sumCount: Long?,
                                      @ApiModelProperty(value = "列表数据实体，不能为空，但是可以为空数组", required = true) var dataList: T) : SbcbflwBaseResponseBean<T>() {

}
