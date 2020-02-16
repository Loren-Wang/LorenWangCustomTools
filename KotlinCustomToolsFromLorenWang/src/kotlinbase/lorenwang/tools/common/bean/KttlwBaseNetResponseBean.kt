package kotlinbase.lorenwang.tools.common.bean

import io.swagger.annotations.ApiModelProperty

/**
 * 功能作用：基础响应实例
 * 创建时间：2019-12-16 14:05
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1：响应的状态码---stateCode
 * 配置参数2：响应的状态信息---stateMessage
 * 配置参数3：响应数据---data
 */
open class KttlwBaseNetResponseBean<T>(var data: T? = null) {
    @ApiModelProperty(value = "响应的状态码，和前端会是约定好的值列表", required = true)
    var stateCode: String = "0"
    @ApiModelProperty(value = "响应的状态信息，例如错误信息等", required = true)
    var stateMessage: String? = null
}
