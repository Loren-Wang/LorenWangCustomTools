package android.lorenwang.common_base_frame.adapter

import androidx.annotation.LayoutRes

/**
 * Created by Machenike on 2018/4/9.
 */
/**
 * 功能作用：recycleview适配器使用的实例，用来记录资源id以及数据的实例
 * 初始注释时间： 2019/12/11 16:23
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 * @param bean 数据实例
 * @param layoutResId 布局资源id
 */
open class AcbflwBaseType<T>( @LayoutRes var layoutResId: Int,var bean: T?)
