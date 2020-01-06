package javabase.lorenwang.common_base_frame.controller

import io.swagger.annotations.ApiModel

/**
 * 功能作用：更新排行请求实体
 * 创建时间：2019-10-29 下午 17:36:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@ApiModel("更新排行请求实体")
class SbcbflwBaseUpDateRankReqBean : SbcbflwBaseRequestBean() {
    var ids: Array<Long>? = null
}
