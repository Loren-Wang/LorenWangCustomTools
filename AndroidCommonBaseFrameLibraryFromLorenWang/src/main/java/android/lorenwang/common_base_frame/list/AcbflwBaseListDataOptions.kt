package android.lorenwang.common_base_frame.list

import android.app.Activity
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.common_base_frame.adapter.AcbflwBaseType
import android.lorenwang.common_base_frame.refresh.AcbflwBaseRefreshDataOptions
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils

/**
 * 功能作用：列表数据操作
 * 创建时间：2020-01-07 17:56
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwBaseListDataOptions<T>(activity: Activity?,
                                   /**
                                    * 装饰器接口
                                    */
                                   private val decorator: AcbflwBaseListDataOptionsDecorator<T>?,
                                   /**
                                    * 数据刷新操作
                                    */
                                   private val refreshDataOptions: AcbflwBaseRefreshDataOptions?,
                                   /**
                                    * 列表控件群
                                    */
                                   private val recyclerView: RecyclerView) : AcbflwBaseListDataOptionsDecorator<T> {
    /**
     * 列表布局管理器
     */
    private var layoutManager: RecyclerView.LayoutManager?
    /**
     * 适配器
     */
    private val adapter: AcbflwBaseRecyclerAdapter<T>?
    /**
     * 数据列表
     */
    private var list: ArrayList<AcbflwBaseType<T>> = arrayListOf();

    override fun getListViewHolder(viewType: Int, itemView: View?): AcbflwBaseRecyclerViewHolder<T>? {
        return decorator?.getListViewHolder(viewType, itemView)
    }

    override fun clear() {
        if (adapter != null) {
            adapter.clear()
            list = adapter.adapterDataList
        } else {
            list.clear()
        }
        refreshDataOptions?.setAllowLoadMore(false)
    }

    override fun singleTypeLoad(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        list?.let {
            if (adapter != null) {
                adapter.singleTypeLoad(list, layoutId, haveMoreData)
                this.list = adapter.adapterDataList
            } else {
                if (!JtlwCheckVariateUtils.getInstance().isEmpty(list)) {
                    for (t in list) {
                        if (t != null) {
                            this.list.add(AcbflwBaseType(layoutId, t))
                        } else {
                            return
                        }
                    }
                } else {
                    this.list.clear()
                }
            }
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
    }

    override fun multiTypeLoad(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        list?.let {
            if (adapter != null) {
                adapter.multiTypeLoad(list, haveMoreData)
                this.list = adapter.adapterDataList
            } else {
                if (!JtlwCheckVariateUtils.getInstance().isEmpty(list)) {
                    this.list.addAll(list)
                } else {
                    this.list.clear()
                }
            }
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
    }

    override fun singleTypeRefresh(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        list?.let {
            if (adapter != null) {
                adapter.singleTypeRefresh(list, layoutId, haveMoreData)
                this.list = adapter.adapterDataList
            } else {
                this.list.clear()
                if (!JtlwCheckVariateUtils.getInstance().isEmpty(list)) {
                    for (t in list) {
                        if (t != null) {
                            this.list.add(AcbflwBaseType(layoutId, t))
                        } else {
                            return
                        }
                    }
                }
            }
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
    }

    override fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        list?.let {
            if (adapter != null) {
                adapter.multiTypeRefresh(list, haveMoreData)
                this.list = adapter.adapterDataList
            } else {
                this.list.clear()
                if (!JtlwCheckVariateUtils.getInstance().isEmpty(list)) {
                    this.list.addAll(list)
                }
            }
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
    }

    override fun showEmptyView(layoutId: Int, desc: T, haveMoreData: Boolean) {
        if (adapter != null) {
            adapter.showEmptyView(layoutId, desc, haveMoreData)
            list = adapter.adapterDataList
        } else {
            list.clear()
        }
        refreshDataOptions?.setAllowLoadMore(haveMoreData)
    }


    /**
     * 设置列表控件
     *
     * @param recyclerView  列表控件
     * @param layoutManager 布局管理器
     */
    fun setRecycle(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager?) {
        if (recyclerView != null) {
            this.layoutManager = layoutManager
                    ?: if (this.layoutManager != null) this.layoutManager else LinearLayoutManager(recyclerView.context)
            recyclerView.layoutManager = this.layoutManager
        }
    }

    init {
        layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        adapter = object : AcbflwBaseRecyclerAdapter<T>(activity!!) {
            override fun getListViewHolder(viewType: Int, itemView: View?): AcbflwBaseRecyclerViewHolder<T>? {
                return this@AcbflwBaseListDataOptions.getListViewHolder(viewType, itemView)
            }
        }
        recyclerView.adapter = adapter
    }

    override val adapterDataList: ArrayList<AcbflwBaseType<T>>
        get() = adapter!!.dataList
}
