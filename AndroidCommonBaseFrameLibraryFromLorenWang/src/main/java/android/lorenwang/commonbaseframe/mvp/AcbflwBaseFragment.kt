package android.lorenwang.commonbaseframe.mvp

import android.lorenwang.commonbaseframe.R
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinbase.lorenwang.tools.extend.kttlwToGone
import kotlinbase.lorenwang.tools.extend.kttlwToVisible

/**
 * 功能作用：基础通用fragment
 * 创建时间：2020-03-01 17:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseFragment : Fragment(), AcbflwBaseView {
    /**
     * 当前页面view
     */
    protected lateinit var mFragmentView: View

    /**
     * 内容视图
     */
    protected var mContentView: View? = null

    /**
     * 标题栏控件
     */
    protected var mTitleBarView: View? = null

    /**
     * 底部操作栏控件
     */
    protected var mBottomOptionsView: View? = null

    /**
     * 空视图view
     */
    protected var mEmptyView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.acbflw_page_mvp, null)
        return mFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initListener(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化监听
     */
    open fun initListener(savedInstanceState: Bundle?) {}

    /**
     * 初始化数据
     */
    open fun initData(savedInstanceState: Bundle?) {}

    /**
     * 执行刷新数据
     */
    open fun onRefreshData() {}

    /**
     * 加载更多数据
     */
    open fun onLoadMoreData() {}

    /**
     * 初始化标题栏控件
     */
    open fun initTitleBarView(@LayoutRes resId: Int) {
        initTitleBarView(View.inflate(requireContext(), resId, null))
    }

    /**
     * 初始化标题栏控件
     */
    open fun initTitleBarView(view: View) {
        mTitleBarView = view
        val baseView = mFragmentView.findViewById<ViewGroup>(R.id.lnAcbflwBase)
        val stub = mFragmentView.findViewById<View>(R.id.vsbTitleBarHeadView)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mTitleBarView, index)
    }

    /**
     * 初始化内容视图
     */
    open fun initContentView(@LayoutRes resId: Int) {
        initContentView(View.inflate(requireContext(), resId, null))
    }

    /**
     * 初始化内容视图
     */
    open fun initContentView(view: View) {
        mContentView = view
        val baseView = mFragmentView.findViewById<ViewGroup>(R.id.flAcbflwContent)
        val stub = mFragmentView.findViewById<View>(R.id.vsbAcbflwContent)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mContentView, index)
    }

    /**
     * 初始化底部操作视图
     */
    open fun initBottomOptionsView(@LayoutRes resId: Int) {
        initBottomOptionsView(View.inflate(requireContext(), resId, null))
    }

    /**
     * 初始化底部操作视图
     */
    open fun initBottomOptionsView(view: View) {
        mBottomOptionsView = view
        val baseView = mFragmentView.findViewById<ViewGroup>(R.id.lnAcbflwBase)
        val stub = mFragmentView.findViewById<View>(R.id.vsbAcbflwBottomView)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mBottomOptionsView, index)
    }

    /**
     * 初始化空view视图
     */
    open fun initEmptyView(@LayoutRes resId: Int) {
        initEmptyView(View.inflate(requireContext(), resId, null))
    }

    /**
     * 初始化空view视图
     */
    open fun initEmptyView(view: View) {
        mEmptyView = view
        val baseView = mFragmentView.findViewById<ViewGroup>(R.id.flAcbflwContent)
        val stub = mFragmentView.findViewById<View>(R.id.vsbAcbflwEmpty)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mEmptyView, index)
    }

    /**
     * 显示内容数据
     */
    open fun showContentData(): Boolean {
        mEmptyView.kttlwToGone()
        mContentView.kttlwToVisible()
        return true
    }

    /**
     * 显示空视图
     */
    open fun <T> showEmptyData(data: T): Boolean {
        mEmptyView.kttlwToVisible()
        mContentView.kttlwToGone()
        return true
    }

    override fun onPause() {
        super.onPause()
        AtlwImageLoadingFactory.getInstance().pauseLoading()
    }

    override fun onResume() {
        super.onResume()
        AtlwImageLoadingFactory.getInstance().resumeLoading()
    }

}
