package android.lorenwang.commonbaseframe

import android.content.Intent
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwViewUtil
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.share.WbShareCallback
import javabase.lorenwang.dataparse.JdplwJsonUtils
import kotlinbase.lorenwang.tools.extend.kttlwToVisible

/**
 * 功能作用：基础activity
 * 创建时间：2019-12-11 10:11
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseActivity : AppCompatActivity(), AcbflwBaseView {
    protected var titleBarHeadViewHeight = AcbflwBaseConfig.titleBarHeadViewHeight
    protected var baseBottomViewHeight = AcbflwBaseConfig.baseBottomViewHeight

    /**
     * 默认的网络请求code，多个不同请求是子类需要新增请求吗
     */
    protected val DEFAULT_NET_REQUEST_CODE = -1

    /**
     * 内容视图
     */
    protected var showContentView: View? = null

    /**
     * 标题栏视图
     */
    protected var showTitleBarView: View? = null

    /**
     * 底部操作栏视图
     */
    protected var showBottomOptionsView: View? = null

    /**
     * 空视图
     */
    protected var emptyView: View? = null

    /**
     * 基类刷新控件
     */
    protected var swipeAcbflwRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initCreateSuperBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
        initListener(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     * 初始化create父级方法super之前调用
     */
    open fun initCreateSuperBefore(savedInstanceState: Bundle?) {}

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
     * 初始化空数据视图
     */
    open fun <T> initEmptyView(view: View?, emptyResId: Int, data: T) {}

    /**
     * 执行刷新数据
     */
    open fun onRefreshData() {}

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int) {
        addContentView(resId, null)
    }

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     * @param titleBarHeadViewLayoutResId 标题栏视图资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int, @LayoutRes titleBarHeadViewLayoutResId: Int?) {
        addContentView(resId, titleBarHeadViewLayoutResId, null)
    }

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     * @param titleBarHeadViewLayoutResId 标题栏视图资源id
     * @param bottomViewResId 底部操作栏资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int, @LayoutRes titleBarHeadViewLayoutResId: Int?, @LayoutRes bottomViewResId: Int?) {
        //使用基础布局
        useBaseLayout()

        //内容视图
        val vsbContent = findViewById<ViewStub>(R.id.vsbAcbflwContent)
        vsbContent.layoutResource = resId
        showContentView = vsbContent.inflate()

        //标题栏视图
        titleBarHeadViewLayoutResId?.let {
            val vsbTitleBarHeadView = findViewById<ViewStub>(R.id.vsbTitleBarHeadView)
            vsbTitleBarHeadView.layoutResource = it
            AtlwViewUtil.getInstance().setViewWidthHeight(vsbTitleBarHeadView, ViewGroup.LayoutParams.MATCH_PARENT, titleBarHeadViewHeight)
            showTitleBarView = vsbTitleBarHeadView.inflate()
            findViewById<View>(R.id.viewAcbflwHeadViewShadow).visibility = View.VISIBLE
        }

        //底部栏视图
        bottomViewResId?.let {
            val vsbBottomView = findViewById<ViewStub>(R.id.vsbAcbflwBottomView)
            vsbBottomView.layoutResource = bottomViewResId
            AtlwViewUtil.getInstance().setViewWidthHeight(vsbBottomView, ViewGroup.LayoutParams.MATCH_PARENT, baseBottomViewHeight)
            showBottomOptionsView = vsbBottomView.inflate()
        }
    }

    /**
     * 添加显示的内容视图绑定
     */
    protected open fun <T : ViewDataBinding> addShowContentView(addBaseLayout: Boolean, binding: T?): T? {
        if (addBaseLayout && findViewById<View>(R.id.lnAcbflwBase) == null) {
            useBaseLayout()
        }
        if (binding == null) {
            return null
        }
        val vsbAcbflwContent = findViewById<ViewStub>(R.id.vsbAcbflwContent)
        val flAcbflwContent = findViewById<FrameLayout>(R.id.flAcbflwContent)
        //内容视图
        if (vsbAcbflwContent != null && flAcbflwContent != null) {
            setViewStub(vsbAcbflwContent, flAcbflwContent, binding)
            showContentView = binding.root
        }
        return binding
    }

    /**
     * 添加显示的标题视图绑定
     */
    protected open fun <T : ViewDataBinding> addShowTitleView(addBaseLayout: Boolean, binding: T?): T? {
        if (addBaseLayout && findViewById<View>(R.id.lnAcbflwBase) == null) {
            useBaseLayout()
        }
        if (binding == null) {
            return null
        }
        val vsbTitleBarHeadView = findViewById<ViewStub>(R.id.vsbTitleBarHeadView)
        val lnAcbflwBase = findViewById<LinearLayoutCompat>(R.id.lnAcbflwBase)
        //内容视图
        if (vsbTitleBarHeadView != null && lnAcbflwBase != null) {
            setViewStub(vsbTitleBarHeadView, lnAcbflwBase, binding)
            showTitleBarView = binding.root
        }
        return binding
    }

    /**
     * 添加显示的底部操作视图绑定
     */
    protected open fun <T : ViewDataBinding> addShowBottomOptionsView(addBaseLayout: Boolean, binding: T?): T? {
        if (addBaseLayout && findViewById<View>(R.id.lnAcbflwBase) == null) {
            useBaseLayout()
        }
        if (binding == null) {
            return null
        }
        val vsbAcbflwBottomView = findViewById<ViewStub>(R.id.vsbAcbflwBottomView)
        val lnAcbflwBase = findViewById<LinearLayoutCompat>(R.id.lnAcbflwBase)
        //内容视图
        if (vsbAcbflwBottomView != null && lnAcbflwBase != null) {
            setViewStub(vsbAcbflwBottomView, lnAcbflwBase, binding)
            showBottomOptionsView = binding.root
        }
        return binding
    }

    /**
     * 使用基础布局
     */
    protected open fun useBaseLayout() {
        setContentView(R.layout.acbflw_page_base)
        //初始化刷新控件
        swipeAcbflwRefresh = findViewById(R.id.swipeAcbflwRefresh) //初始化刷新控件监听
        swipeAcbflwRefresh?.setOnRefreshListener { onRefreshData() }
        swipeAcbflwRefresh?.isEnabled = false
    }

    /**
     * 设置viewStub
     */
    protected fun setViewStub(view: ViewStub, parent: ViewGroup, binding: ViewDataBinding) {
        val index: Int = parent.indexOfChild(view)
        parent.removeViewInLayout(view)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        parent.addView(binding.root, index, layoutParams)
    }

    /**
     * 显示内容数据，隐藏空数据
     */
    protected open fun showContentData(): Boolean {
        emptyView?.visibility = View.GONE
        showContentView?.visibility = View.VISIBLE
        return true
    }

    protected open fun <T> showEmptyData(@LayoutRes emptyResId: Int, data: T): Boolean {
        showContentView?.visibility = View.GONE
        if (emptyView == null) {
            val vsbQtEmpty = findViewById<ViewStub>(R.id.vsbAcbflwEmpty)
            vsbQtEmpty.layoutResource = emptyResId
            emptyView = vsbQtEmpty.inflate()
            emptyView?.setOnClickListener { _: View? -> onRefreshData() }
            initEmptyView(emptyView, emptyResId, data)
        } else {
            emptyView.kttlwToVisible()
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AtlwActivityUtil.getInstance().receivePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //新浪微博相关回调
        if (AcbflwPluginUtil.getInstance().getSinaApi(this) != null) {
            AcbflwPluginUtil.getInstance().getSinaApi(this).doResultIntent(data, object : WbShareCallback {
                override fun onComplete() {
                    AcbflwPluginUtil.getInstance().callBackInfo(AcbflwPluginUtil.getInstance().sinaKey(this@AcbflwBaseActivity))
                }

                override fun onError(p0: UiError?) {
                    AtlwLogUtil.logUtils.logI("shareToSina", JdplwJsonUtils.toJson(p0))
                    AcbflwPluginUtil.getInstance()
                        .callBackError(AcbflwPluginUtil.getInstance().sinaKey(this@AcbflwBaseActivity), AcbflwPluginErrorTypeEnum.SHARE_FAIL)
                }

                override fun onCancel() {
                    AcbflwPluginUtil.getInstance()
                        .callBackError(AcbflwPluginUtil.getInstance().sinaKey(this@AcbflwBaseActivity), AcbflwPluginErrorTypeEnum.SHARE_CANCEL)
                }
            })
            AcbflwPluginUtil.getInstance().getSinaApi(this).authorizeCallback(requestCode, resultCode, data);
        }
    }

}
