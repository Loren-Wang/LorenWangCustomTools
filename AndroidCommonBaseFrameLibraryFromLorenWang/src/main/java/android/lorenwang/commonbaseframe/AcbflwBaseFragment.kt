package android.lorenwang.commonbaseframe

import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import android.lorenwang.tools.app.AtlwActivityUtils
import android.lorenwang.tools.app.AtlwViewUtils
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

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
    protected var titleBarHeadViewHeight = AcbflwBaseConfig.titleBarHeadViewHeight
    protected var baseBottomViewHeight = AcbflwBaseConfig.baseBottomViewHeight
    /**
     * 默认的网络请求code，多个不同请求是子类需要新增请求吗
     */
    protected val DEFAULT_NET_REQUEST_CODE = -1

    /**
     * fragment的view视图
     */
    protected var fragmentView: View? = null

    /**
     * 内容视图
     */
    protected var contentView: View? = null

    /**
     * 空视图
     */
    protected var emptyView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.acbflw_activity_base, null)
        initView(savedInstanceState)
        initListener(savedInstanceState)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId 视图资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int) {
        addContentView(resId, null, null)
    }

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     * @param titleBarHeadViewLayoutResId 标题栏视图资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int,
                                      @LayoutRes titleBarHeadViewLayoutResId: Int?) {
        addContentView(resId, titleBarHeadViewLayoutResId, null)
    }

    /**
     * 添加内容视图，其内部设置了contentview，然后通过baselayout当中的viewstub设置布局并进行绘制显示
     *
     * @param resId                       视图资源id
     * @param titleBarHeadViewLayoutResId 标题栏视图资源id
     * @param bottomViewResId             底部操作栏资源id
     */
    protected open fun addContentView(@LayoutRes resId: Int,
                                      @LayoutRes titleBarHeadViewLayoutResId: Int?,
                                      @LayoutRes bottomViewResId: Int?) {
        //内容视图
        val vsbContent = fragmentView!!.findViewById<ViewStub>(R.id.vsbContent)
        vsbContent.layoutResource = resId
        contentView = vsbContent.inflate()
        //标题栏视图
        if (titleBarHeadViewLayoutResId != null) {
            val vsbTitleBarHeadView = fragmentView!!.findViewById<ViewStub>(R.id.vsbTitleBarHeadView)
            vsbTitleBarHeadView.layoutResource = titleBarHeadViewLayoutResId
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbTitleBarHeadView,
                    ViewGroup.LayoutParams.MATCH_PARENT, titleBarHeadViewHeight)
            vsbTitleBarHeadView.inflate()
            fragmentView!!.findViewById<View>(R.id.viewHeadViewShadow).visibility = View.VISIBLE
        }
        //底部栏视图
        if (bottomViewResId != null) {
            val vsbBottomView = fragmentView!!.findViewById<ViewStub>(R.id.vsbBottomView)
            vsbBottomView.layoutResource = bottomViewResId
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbBottomView,
                    ViewGroup.LayoutParams.MATCH_PARENT, baseBottomViewHeight)
            vsbBottomView.inflate()
        }
    }

    /**
     * 显示内容数据，隐藏空数据
     */
    protected open fun showContentData() {
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE
        }
        if (contentView != null) {
            contentView!!.visibility = View.VISIBLE
        }
    }

    /**
     * 显示空数据视图
     *
     * @param emptyResId 空数据视图资源id
     */
    protected open fun showEmptyData(@LayoutRes emptyResId: Int) {
        if (contentView != null) {
            contentView!!.visibility = View.GONE
        }
        if (emptyView == null) {
            val vsbEmpty = fragmentView!!.findViewById<ViewStub>(R.id.vsbEmpty)
            vsbEmpty.layoutResource = emptyResId
            emptyView = vsbEmpty.inflate()
            initEmptyView(emptyView, emptyResId)
        } else {
            emptyView!!.visibility = View.VISIBLE
        }
    }

    /**
     * 初始化视图
     *
     * @param savedInstanceState 页面切换等操作是手动存储的值
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化监听
     *
     * @param savedInstanceState 页面切换等操作是手动存储的值
     */
    protected open fun initListener(savedInstanceState: Bundle?) {}

    /**
     * 初始化数据
     *
     * @param savedInstanceState 页面切换等操作是手动存储的值
     */
    protected open fun initData(savedInstanceState: Bundle?) {}

    /**
     * 初始化空数据视图
     */
    protected open fun initEmptyView(view: View?, emptyResId: Int) {}

    override fun onPause() {
        super.onPause()
        AtlwImageLoadingFactory.getInstance().pauseLoading()
    }

    override fun onResume() {
        super.onResume()
        AtlwImageLoadingFactory.getInstance().resumeLoading()
    }

    /**
     * 权限接收回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AtlwActivityUtils.getInstance().receivePermissionsResult(requestCode, permissions,
                grantResults)
    }
}
