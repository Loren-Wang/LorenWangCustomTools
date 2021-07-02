package android.lorenwang.commonbaseframe.refresh

import android.content.Context
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader

/**
 * 功能作用：基础列表数据类
 * 创建时间：2020-01-06 18:31
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwBaseRefreshDataOptions(context: Context?, baseView: AcbflwBaseView?, private val refreshLayout: AcbflwRefreshView?,
    private val qtBaseRefreshDataOptionsDecorator: AcbflwBaseRefreshDataOptionsDecorator) : AcbflwBaseRefreshDataOptionsDecorator {
    /**
     * 刷新头
     */
    private var refreshHeader: RefreshHeader? = null

    /**
     * 刷新底
     */
    private var refreshFooter: RefreshFooter? = null

    /**
     * 是否是首次刷新
     *
     * @return 是返回true
     */
    var isFirstRefresh = true
        private set

    /**
     * 设置刷新顶部
     *
     * @param refreshHeader 刷新顶部
     */
    fun setRefreshHeader(refreshHeader: RefreshHeader?) {
        if (refreshHeader != null) {
            this.refreshHeader = refreshHeader
            refreshLayout!!.setRefreshHeader(refreshHeader)
        }
    }

    /**
     * 设置刷新底部
     *
     * @param refreshFooter 刷新底部
     */
    fun setRefreshFooter(refreshFooter: RefreshFooter?) {
        if (refreshFooter != null) {
            this.refreshFooter = refreshFooter
            refreshLayout!!.setRefreshFooter(refreshFooter)
        }
    }

    /**
     * 结束刷新或者结束加载更多
     */
    fun finishAll() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
        }
    }

    /**
     * 设置是否允许加载更多
     *
     * @param allow 允许为true
     */
    fun setAllowLoadMore(allow: Boolean) {
        refreshLayout?.setEnableLoadMore(allow)
    }

    /**
     * 设置是否允许加载更多
     *
     * @param allow 允许为true
     */
    fun setAllowRefresh(allow: Boolean) {
        refreshLayout?.setEnableRefresh(allow)
    }

    override fun startRefreshing() {
        isFirstRefresh = false
        qtBaseRefreshDataOptionsDecorator.startRefreshing()
    }

    override fun startLoadingMore() {
        isFirstRefresh = false
        qtBaseRefreshDataOptionsDecorator.startLoadingMore()
    }

    init {
        if (refreshHeader != null) {
            refreshLayout?.setRefreshHeader(refreshHeader!!)
        }
        if (refreshFooter != null) {
            refreshLayout?.setRefreshFooter(refreshFooter!!)
        }
        //头部高度
        refreshLayout?.setHeaderHeight(60f)
        //底部高度
        refreshLayout?.setFooterHeight(40f)
        //设置允许刷新
        refreshLayout?.setEnableRefresh(true)
        //设置允许加载更多
        refreshLayout?.setEnableLoadMore(true)
        //设置底部越界时是否自动加载，在项目中关闭自动加载更多
        refreshLayout?.setEnableAutoLoadMore(false)
        //刷新监听
        refreshLayout?.setOnRefreshListener { startRefreshing() }
        //加载更多监听
        refreshLayout?.setOnLoadMoreListener { startLoadingMore() }
    }
}
