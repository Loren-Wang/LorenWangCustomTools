package android.lorenwang.common_base_frame

import android.lorenwang.common_base_frame.adapter.AcbflwBaseType
import android.lorenwang.common_base_frame.list.AcbflwBaseListDataOptions
import android.lorenwang.common_base_frame.list.AcbflwBaseListDataOptionsDecorator
import android.lorenwang.common_base_frame.mvp.AcbflwBaseView
import android.lorenwang.common_base_frame.refresh.AcbflwBaseRefreshDataOptions
import android.lorenwang.common_base_frame.refresh.AcbflwBaseRefreshDataOptionsDecorator
import android.lorenwang.common_base_frame.refresh.AcbflwRefreshView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * 功能作用：列表fragment
 * 创建时间：2020-03-05 12:19
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseListFragment<T> : Fragment(), AcbflwBaseRefreshDataOptionsDecorator, AcbflwBaseListDataOptionsDecorator<T> {
    private var refreshDataOptions: AcbflwBaseRefreshDataOptions? = null
    private var listDataOptions: AcbflwBaseListDataOptions<T>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        refreshDataOptions = AcbflwBaseRefreshDataOptions(context, activity as AcbflwBaseView?, getRefreshView(), this)
        listDataOptions = AcbflwBaseListDataOptions<T>(activity, this, refreshDataOptions, getRecycleView())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected abstract fun getRefreshView(): AcbflwRefreshView?
    protected abstract fun getRecycleView(): RecyclerView
    override fun clear() {
        listDataOptions?.clear()
        refreshDataOptions?.finishAll()
    }

    override fun singleTypeLoad(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        listDataOptions?.singleTypeLoad(list, layoutId, haveMoreData)
        refreshDataOptions?.finishAll()
    }

    override fun singleTypeRefresh(list: List<T>?, layoutId: Int, haveMoreData: Boolean) {
        listDataOptions?.singleTypeRefresh(list, layoutId, haveMoreData)
        refreshDataOptions?.finishAll()
    }

    override fun multiTypeLoad(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        listDataOptions?.multiTypeLoad(list, haveMoreData)
        refreshDataOptions?.finishAll()
    }

    override fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean) {
        listDataOptions?.multiTypeRefresh(list, haveMoreData)
        refreshDataOptions?.finishAll()
    }

    override fun showEmptyView(layoutId: Int, desc: T, haveMoreData: Boolean) {
        listDataOptions?.showEmptyView(layoutId, desc, haveMoreData)
        refreshDataOptions?.finishAll()
    }

    fun getAdapterDataList(): List<AcbflwBaseType<T>> {
        return if (listDataOptions == null) {
            arrayListOf()
        } else {
            listDataOptions!!.adapterDataList
        }
    }
}

