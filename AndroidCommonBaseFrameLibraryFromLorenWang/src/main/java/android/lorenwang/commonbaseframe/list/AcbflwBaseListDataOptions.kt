package android.lorenwang.commonbaseframe.list

import android.app.Activity
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseType
import android.lorenwang.commonbaseframe.adapter.AcbflwEmptyViewHolder
import android.lorenwang.commonbaseframe.bean.AcbflwPageShowViewDataBean
import android.lorenwang.commonbaseframe.refresh.AcbflwBaseRefreshDataOptions
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

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
 * @param activity 页面实例
 * @param decorator 装饰器接口
 * @param refreshDataOptions 数据刷新操作
 * @param recyclerView 列表控件群
 */
open class AcbflwBaseListDataOptions<T>(val activity: Activity?, private val decorator: AcbflwBaseListDataOptionsDecorator<T>?,
    private val refreshDataOptions: AcbflwBaseRefreshDataOptions?, private var recyclerView: RecyclerView?) : AcbflwBaseListDataOptionsDecorator<T> {
    /**
     * 列表布局管理器
     */
    lateinit var layoutManager: RecyclerView.LayoutManager

    /**
     * 适配器
     */
    lateinit var adapter: AcbflwBaseRecyclerAdapter<T>

    /**
     * 数据列表
     */
    private var list: ArrayList<AcbflwBaseType<T>> = arrayListOf()


    /**
     * 适配器数据
     */
    override val adapterDataList: ArrayList<AcbflwBaseType<T>>
        get() = adapter.dataList

    init {
        activity?.let {
            setRecycle(recyclerView, LinearLayoutManager(activity))
        }
    }

    override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<T> {
        return decorator?.getListViewHolder(viewType, itemView).kttlwGetNotEmptyData { AcbflwEmptyViewHolder(itemView) }
    }

    override fun clear() {
        adapter.clear()
        list = adapter.adapterDataList
        refreshDataOptions?.setAllowLoadMore(false)
    }

    override fun singleTypeLoad(list: List<T>?, layoutId: Int, haveMoreData: Boolean, showScrollEnd: Boolean) {
        list?.let {
            adapter.singleTypeLoad(list, layoutId, haveMoreData, showScrollEnd)
            this.list = adapter.adapterDataList
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
        refreshDataOptions?.finishAll()
    }

    override fun multiTypeLoad(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean, showScrollEnd: Boolean) {
        list?.let {
            adapter.multiTypeLoad(list, haveMoreData, showScrollEnd)
            this.list = adapter.adapterDataList
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
        refreshDataOptions?.finishAll()
    }

    override fun singleTypeRefresh(list: List<T>?, layoutId: Int, haveMoreData: Boolean, showScrollEnd: Boolean): Boolean {
        var status = false
        list?.let {
            status = adapter.singleTypeRefresh(list, layoutId, haveMoreData, showScrollEnd)
            this.list = adapter.adapterDataList
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
        refreshDataOptions?.finishAll()
        return status
    }

    override fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean, showScrollEnd: Boolean): Boolean {
        var status = false
        list?.let {
            status = adapter.multiTypeRefresh(list, haveMoreData, showScrollEnd)
            this.list = adapter.adapterDataList
            refreshDataOptions?.setAllowLoadMore(haveMoreData)
        }
        refreshDataOptions?.finishAll()
        return status
    }

    /**
     * 设置列表显示数据
     * @param data 列表通用数据加载
     * @param itemLayoutRes 列表item布局资源
     * @param showScrollEnd 是否显示滑动结束
     */
    override fun setListShowData(data: AcbflwPageShowViewDataBean<T>?, @LayoutRes itemLayoutRes: Int, showScrollEnd: Boolean): Boolean {
        val status = adapter.setListShowData(data, itemLayoutRes, showScrollEnd)
        this.list = adapter.adapterDataList
        refreshDataOptions?.setAllowLoadMore(!data?.isLastPageData.kttlwGetNotEmptyData(true))
        return status
    }

    /**
     * 设置列表显示数据
     * @param data 列表通用数据加载
     * @param showScrollEnd 是否显示滑动结束
     */
    override fun setListShowData(data: AcbflwPageShowViewDataBean<AcbflwBaseType<T>>?, showScrollEnd: Boolean): Boolean {
        val status = adapter.setListShowData(data, showScrollEnd)
        this.list = adapter.adapterDataList
        refreshDataOptions?.setAllowLoadMore(!data?.isLastPageData.kttlwGetNotEmptyData(true))
        return status
    }

    override fun showEmptyView(layoutId: Int, desc: T?, haveMoreData: Boolean): Boolean {
        adapter.showEmptyView(layoutId, desc, haveMoreData)
        list = adapter.adapterDataList
        refreshDataOptions?.setAllowLoadMore(haveMoreData)
        return true
    }

    override fun showContentData(): Boolean {
        adapter.showContentData()
        this.list = adapter.adapterDataList
        refreshDataOptions?.setAllowLoadMore(false)
        return true
    }

    /**
     * 设置列表控件
     *
     * @param recyclerView  列表控件
     * @param layoutManager 布局管理器
     */
    fun setRecycle(recyclerView: RecyclerView?, layoutManager: RecyclerView.LayoutManager?, useAdapter: AcbflwBaseRecyclerAdapter<T>? = null) {
        if (recyclerView != null && activity != null) {
            this.recyclerView = recyclerView
            this.layoutManager = layoutManager ?: this.layoutManager
            this.recyclerView?.layoutManager = this.layoutManager
            this.recyclerView?.layoutManager = layoutManager
            adapter = useAdapter.kttlwGetNotEmptyData {
                object : AcbflwBaseRecyclerAdapter<T>(activity) {
                    override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<T> {
                        return this@AcbflwBaseListDataOptions.getListViewHolder(viewType, itemView)
                    }
                }
            }
            this.recyclerView?.adapter = adapter
        }
    }


}
