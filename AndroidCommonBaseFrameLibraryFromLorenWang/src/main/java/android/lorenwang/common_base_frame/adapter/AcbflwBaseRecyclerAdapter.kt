package android.lorenwang.common_base_frame.adapter

import android.app.Activity
import android.lorenwang.common_base_frame.list.AcbflwBaseListDataOptionsDecorator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Machenike on 2018/4/4.
 *
 * 1、添加数据：添加数据，同时传递进入该数据要加载的布局id，以布局id作为type类型使用
 * 2、添加数据：使用baseType列表作为参数，此时不用再adapter中做转换
 * 3、清除并添加：清除所有数据，按照1注释的方法添加数据
 * 4、清除并添加：清除所有数据，按照2注释的方法添加数据
 * 5、显示空数据布局
 * 6、显示空数据布局以及提示
 * 7、清除所有数据
 * 8、添加所有数据
 */
/**
 * 功能作用：基础recycleview适配器，用来简化部分逻辑代码处理，子类只需要实现获取viewholder方法即可
 * 初始注释时间： 2019/12/11 16:18
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 * 1、添加数据：添加数据，同时传递进入该数据要加载的布局id，以布局id作为type类型使用
 * 2、添加数据：使用baseType列表作为参数，此时不用再adapter中做转换
 * 3、清除并添加：清除所有数据，按照1注释的方法添加数据
 * 4、清除并添加：清除所有数据，按照2注释的方法添加数据
 * 5、显示空数据布局
 * 6、清空数据列表
 */
abstract class AcbflwBaseRecyclerAdapter<T> : RecyclerView.Adapter<AcbflwBaseRecyclerViewHolder<T>>, AcbflwBaseListDataOptionsDecorator<T> {
    var dataList: ArrayList<AcbflwBaseType<T>> = arrayListOf()
        private set
    var activity: Activity

    /**
     * 是否显示最大数量
     */
    private var showWhetherTheCycle = false

    constructor(activity: Activity) : this(activity, false)

    constructor(activity: Activity, showWhetherTheCycle: Boolean) {
        this.activity = activity
        this.showWhetherTheCycle = showWhetherTheCycle
    }


    override fun getItemViewType(position: Int): Int {
        return dataList[position % dataList.size].layoutResId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcbflwBaseRecyclerViewHolder<T> {
        val itemView = LayoutInflater.from(activity)
                .inflate(viewType, parent, false)
        return getListViewHolder(viewType, itemView)!!
    }

    override fun onBindViewHolder(holder: AcbflwBaseRecyclerViewHolder<T>, position: Int) {
        holder.adapter = this
        holder.setViewData(activity, dataList[position % dataList.size].bean, position)
    }

    override fun getItemCount(): Int {
        //显示循环并且数据数量大于1时循环，否则只返回当前数量
        return if (showWhetherTheCycle && dataList.size > 1) {
            Int.MAX_VALUE
        } else {
            dataList.size
        }
    }

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    override val adapterDataList: ArrayList<AcbflwBaseType<T>>
        get() = dataList

    /**
     * 清空数据列表
     */
    override fun clear() {
        dataList.clear()
    }

    /**
     * 添加数据和布局id
     *
     * @param list     数据列表
     * @param layoutId 布局id
     */
    override fun singleTypeLoad(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        list?.let {
            for (t in list) {
                if (t != null) {
                    dataList.add(AcbflwBaseType(layoutId, t))
                } else {
                    return
                }
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 添加转换后的basetype数据
     *
     * @param list basetype数据列表
     */
    override fun multiTypeLoad(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        list?.let {
            this.dataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 清除旧数据并添加新数据和布局id
     *
     * @param list     数据列表
     * @param layoutId 布局id
     */
    override fun singleTypeRefresh(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        list?.let {
            dataList.clear()
            for (t in list) {
                dataList.add(AcbflwBaseType(layoutId, t))
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 清除旧数据并添加转换后的basetype数据
     *
     * @param list basetype数据列表
     */
    override fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        if (list != null && list.isNotEmpty()) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 显示空视图
     *
     * @param layoutId 布局资源id
     * @param desc     空视图实例
     */
    override fun showEmptyView(layoutId: Int, desc: T, haveMoreData: Boolean) {
        dataList.clear()
        dataList.add(AcbflwBaseType(layoutId, desc))
        notifyDataSetChanged()
    }
}
