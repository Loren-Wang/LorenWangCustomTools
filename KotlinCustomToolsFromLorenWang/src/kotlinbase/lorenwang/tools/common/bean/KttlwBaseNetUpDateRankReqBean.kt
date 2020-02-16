package kotlinbase.lorenwang.tools.common.bean

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
class KttlwBaseNetUpDateRankReqBean<T> : KttlwBaseNetRequestBean() {
    var ids: Array<T>? = null
}
