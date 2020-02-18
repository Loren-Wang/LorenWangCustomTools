package android.lorenwang.common_base_frame.network.callback

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 功能作用：接口响应数据自定义结构中的数据处理
 * 创建时间：2020-02-18 下午 15:46:40
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * @param emptyData 是否是空数据体
 */
abstract class AcbflwRepDataOptionsCallback<D, T : KttlwBaseNetResponseBean<D>>(var emptyData: Boolean) : AcbflwRepOptionsByPresenterCallback<T> {
    override fun viewOptionsData(data: T) {
        if (emptyData) {
            //如果是空数据，则则构造数据返回
            repOptionsData()
        } else if (JtlwCheckVariateUtils.getInstance().isEmpty(data.data)) {
            repDataError(null, null)
        } else {
            repOptionsData(data = data.data!!)
        }
    }

    /**
     * 响应操作数据
     */
    abstract fun repOptionsData()

    /**
     * 响应操作数据
     */
    abstract fun repOptionsData(data: D)

}
