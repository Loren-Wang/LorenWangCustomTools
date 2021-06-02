package android.lorenwang.commonbaseframe.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 功能作用：基础recycleview的viewholder，主要抽象一个设置view数据的方法，同时也可以在holder当中获取适配器
 * 初始注释时间： 2019/12/11 16:16
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseBindingRecyclerViewHolder<T, VB : ViewDataBinding>(itemView: View) : AcbflwBaseRecyclerViewHolder<T>(itemView) {
    /**
     * binding显示
     */
    var binding: VB? = null

    init {
        binding = DataBindingUtil.bind(itemView)
    }
}
