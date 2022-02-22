package springbase.lorenwang.base.kotlinExtend

import kotlinbase.lorenwang.tools.extend.kttlwHaveEmptyCheck
import springbase.lorenwang.base.controller.SpblwBaseController
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper

/**
 * 功能作用：接口请求处理扩展
 * 创建时间：2020-11-21 10:49 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */

/**
 * 接口检测并操作扩展
 * @param emptyCheckArray 空数据检测
 * @param baseController 基础接口控制器
 * @param optionsFun 验证通过操作调用函数
 */
fun <R : SpblwBaseHttpServletRequestWrapper, BC : SpblwBaseController<R>> spblwControllerCheckAndOptions(request: R, emptyCheckArray: Array<*>,
    baseController: BC, optionsFun: (() -> Any?)): String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams(request)
    }, {
        baseController.responseData(request, optionsFun())
    }, emptyCheckArray)
}

