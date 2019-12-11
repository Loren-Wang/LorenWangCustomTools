package android.lorenwang.common_base_frame.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
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
abstract class AcbflwBaseRecyclerAdapter<T> : RecyclerView.Adapter<AcbflwBaseRecyclerViewHolder<T>> {
    var dataList: ArrayList<AcbflwBaseType<T>>? = null
        private set
    var activity: Activity

    constructor(activity: Activity, dataList: ArrayList<AcbflwBaseType<T>>?) {
        this.dataList = dataList
        this.activity = activity
    }

    constructor(activity: Activity) {
        if (dataList == null) {
            dataList = ArrayList()
        }
        this.activity = activity
    }


    override fun getItemViewType(position: Int): Int {
        return dataList!![position].layoutResId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcbflwBaseRecyclerViewHolder<T> {
        val itemView = LayoutInflater.from(activity).inflate(viewType, parent, false)
        return getViewHolder(viewType, itemView)
    }

    override fun onBindViewHolder(holder: AcbflwBaseRecyclerViewHolder<T>, position: Int) {
        holder.adapter = this
        holder.setViewData(activity, dataList!![position].bean, position)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    /**
     * 获取viewholder
     * @param viewType 视图类型
     * @param itemView 每条item的view视图
     */
    abstract fun getViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<T>

    /**
     * 清空列表
     */
    fun clear() {
        dataList!!.clear()
    }

    /*
     * 添加
     * */
    fun singleTypeLoad(list: List<T>?, layoutId: Int) {
        if (list != null) {
            for (i in list.indices) {
                dataList!!.add(AcbflwBaseType(layoutId, list[i]))
            }
            notifyDataSetChanged()
        }
    }

    /*
     * 添加
     * */
    fun multiTypeLoad(list: List<AcbflwBaseType<T>>?) {
        if (list != null) {
            dataList!!.addAll(list)
            notifyDataSetChanged()
        }
    }

    /*
     * 清除 添加
     * */
    fun singleTypeRefresh(list: List<T>?, layoutId: Int) {
        if (list != null) {
            dataList!!.clear()
            for (i in list.indices) {
                dataList!!.add(AcbflwBaseType(layoutId, list[i]))
            }
            notifyDataSetChanged()
        }
    }

    /*
     * 清除 添加
     * */
    fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?) {
        if (list != null && list.isNotEmpty()) {
            dataList!!.clear()
            dataList!!.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 显示空视图
     */
    fun showEmptyView(layoutId: Int, desc: T) {
        dataList!!.clear()
        dataList!!.add(AcbflwBaseType(layoutId, desc))
        notifyDataSetChanged()
    }
}
