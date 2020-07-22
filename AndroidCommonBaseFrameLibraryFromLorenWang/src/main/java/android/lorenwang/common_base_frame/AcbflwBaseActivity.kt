package android.lorenwang.common_base_frame

import android.lorenwang.common_base_frame.mvp.AcbflwBaseView
import android.lorenwang.tools.app.AtlwActivityUtils
import android.lorenwang.tools.app.AtlwViewUtils
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

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
     * 空视图
     */
    protected var emptyView: View? = null

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
    open fun initEmptyView(view: View?, emptyResId: Int) {}

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     * @param titleBarHeadViewLayoutResId 标题栏视图资源id
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
        //设置view
        setContentView(R.layout.acbflw_activity_base)
        //内容视图
        val vsbContent = findViewById<ViewStub>(R.id.vsbContent)
        vsbContent.layoutResource = resId
        showContentView = vsbContent.inflate()
        //标题栏视图
        titleBarHeadViewLayoutResId?.let {
            val vsbTitleBarHeadView = findViewById<ViewStub>(R.id.vsbTitleBarHeadView)
            vsbTitleBarHeadView.layoutResource = it
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbTitleBarHeadView, ViewGroup.LayoutParams.MATCH_PARENT, titleBarHeadViewHeight)
            vsbTitleBarHeadView.inflate()
            findViewById<View>(R.id.viewHeadViewShadow).visibility = View.VISIBLE
        }
        //底部栏视图
        bottomViewResId?.let {
            val vsbBottomView = findViewById<ViewStub>(R.id.vsbBottomView)
            vsbBottomView.layoutResource = bottomViewResId
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbBottomView, ViewGroup.LayoutParams.MATCH_PARENT, baseBottomViewHeight)
            vsbBottomView.inflate()
        }
    }

    /**
     * 显示内容数据，隐藏空数据
     */
    protected open fun showContentData() {
        emptyView?.visibility = View.GONE
        showContentView?.visibility = View.VISIBLE
    }

    /**
     * 显示空数据视图
     *
     * @param emptyResId 空数据视图资源id
     */
    protected open fun showEmptyData(@LayoutRes emptyResId: Int) {
        showContentView?.visibility = View.GONE
        if (emptyView == null) {
            val vsbQtEmpty = findViewById<ViewStub>(R.id.vsbEmpty)
            vsbQtEmpty.layoutResource = emptyResId
            emptyView = vsbQtEmpty.inflate()
            initEmptyView(emptyView, emptyResId)
        } else {
            emptyView!!.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        AtlwImageLoadingFactory.getInstance().pauseLoading()
    }

    override fun onResume() {
        super.onResume()
        AtlwImageLoadingFactory.getInstance().resumeLoading()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AtlwActivityUtils.getInstance().receivePermissionsResult(requestCode, permissions,
                grantResults)
    }

}
