package android.lorenwang.commonbaseframe.mvp

import android.content.Intent
import android.graphics.Color
import android.lorenwang.commonbaseframe.AcbflwMenuItemModel
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.extension.setViewToGone
import android.lorenwang.commonbaseframe.extension.setViewToInvisible
import android.lorenwang.commonbaseframe.extension.setViewToVisible
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.share.WbShareCallback
import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import java.lang.ref.WeakReference

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
    /**
     * 标题栏控件
     */
    protected var mTitlebar: View? = null

    /**
     * 内容控件
     */
    protected var mContentView: View? = null

    /**
     * 根目录view
     */
    protected lateinit var mRootView: ConstraintLayout

    /**
     * 刷新控件
     */
    private lateinit var mRefreshView: SwipeRefreshLayout

    /**
     * 内容父控件
     */
    private lateinit var mFlContentView: FrameLayout

    /**
     * 标题栏父控件
     */
    private lateinit var mFlTitleBarView: FrameLayout

    /**
     * 空视图view
     */
    private var mEmptyView: View? = null

    /**
     * 上一个显示的fragment
     */
    protected var mLastShowFragment: WeakReference<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initCreateSuperBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acbflw_page_base)
        mRootView = findViewById(R.id.cl_acbflw_base_continer)
        mRefreshView = findViewById(R.id.sf_Acbflw_refresh)
        mFlContentView = findViewById(R.id.fl_acbflw_content)
        mFlTitleBarView = findViewById(R.id.fl_title_bar)
        mRefreshView.setOnRefreshListener { onRefreshData() }
        //是否全屏逻辑处理
        if (fitsSystemWindows()) {
            window.statusBarColor = Color.TRANSPARENT
            if (mFlTitleBarView.layoutParams is ConstraintLayout.LayoutParams) {
                (mFlTitleBarView.layoutParams as ConstraintLayout.LayoutParams).topMargin = AtlwScreenUtil.getInstance().statusBarHeight
            }
            mRefreshView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            if (mFlTitleBarView.layoutParams is ConstraintLayout.LayoutParams) {
                (mFlTitleBarView.layoutParams as ConstraintLayout.LayoutParams).topMargin = 0
            }
            mRefreshView.layoutParams.kttlwFormatConversion<ConstraintLayout.LayoutParams>()?.let { params ->
                params.height = 0
                params.topToBottom = mFlTitleBarView.id
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                mRefreshView.layoutParams = params
            }
        }
        setTitleBarViewConfig()
        setContentViewConfig()
        setEmptyViewConfig()
        initView(savedInstanceState)
        initListener(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun setTitle(title: CharSequence?) {
        mTitlebar?.findViewById<AppCompatTextView>(R.id.base_toolbar_title)?.text = title
    }

    override fun initCreateSuperBefore(savedInstanceState: Bundle?) {
        //解决部分机型 内容区域被底部导航栏遮挡的问题，注意activity和fragment 都设置的情况，暂未发现异常
        if (fitsSystemWindows()) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 设置标题栏控件配置
     */
    override fun setTitleBarViewConfig(@LayoutRes resId: Int?) {
        if (resId != null) {
            mRootView.findViewById<ViewStub>(R.id.vsb_title_bar_head_view).let {
                it.layoutResource = resId
                mTitlebar = it.inflate()
            }
        }
        mTitlebar?.let {
            //标题栏控件设置
            if (it is Toolbar) {
                //设置titlebar 返回按钮，取值navigationIcon
                it.setNavigationIcon(R.drawable.picture_icon_back_arrow)
                it.setNavigationOnClickListener { onBackPressed() }
                it.findViewById<TextView>(R.id.base_toolbar_title)?.also { textView ->
                    if (textView.text.toString().isEmpty()) {
                        textView.text = title
                    }
                }
                //设置titlebar背景色，取值colorPrimary
                val typedValue = TypedValue()
                if (theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)) {
                    it.setBackgroundColor(ContextCompat.getColor(AtlwConfig.nowApplication, typedValue.resourceId))
                } else {
                    it.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            if (resId == null && mTitlebar != null) {
                mFlTitleBarView.removeAllViews()
                mFlTitleBarView.addView(mTitlebar, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            if (fitsSystemWindows()) {
                it.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        if (mTitlebar == null) {
            mFlTitleBarView.setViewToGone()
        } else {
            mFlTitleBarView.setViewToVisible()
            mFlTitleBarView.layoutParams.height = getTitleBarHeight()
        }
    }

    /**
     * 设置内容控件配置
     */
    override fun setContentViewConfig(@LayoutRes resId: Int?) {
        if (resId != null) {
            mRootView.findViewById<ViewStub>(R.id.vsb_acbflw_content)?.let {
                it.layoutResource = resId
                mContentView = it.inflate()
            }
        } else {
            if (mContentView != null) {
                mFlContentView.addView(mContentView, 0)
            }
        }
    }

    /**
     * 设置空显示控件配置
     */
    override fun setEmptyViewConfig(@LayoutRes resId: Int?) {
        if (resId != null) {
            mRootView.findViewById<ViewStub>(R.id.vsb_acbflw_empty).let {
                it.layoutResource = resId
                mEmptyView = it.inflate()
                mEmptyView.setViewToGone()
            }
        }
    }

    override fun setTitleBarMenus(menus: List<AcbflwMenuItemModel>?, overflow: Boolean) {
        clearTitleBarMenus()
        val toolbar = mTitlebar as Toolbar?
        menus?.let {
            toolbar?.inflateMenu(R.menu.acbflw_layout_menu)
            menus.forEachIndexed { index, menuItemModel ->
                (mTitlebar as Toolbar?)?.menu?.add(menuItemModel.title ?: index.toString()).apply {
                    if (!overflow) this?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                    if (menuItemModel.showIcon) this?.setIcon(menuItemModel.icon)
                }
            }
            toolbar?.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    menus.forEachIndexed { index, menuItemModel ->
                        if (menuItemModel.title == item?.title) {
                            menus[index].itemOnClickListener.invoke(index)
                            return@forEachIndexed
                        }
                    }
                    return true
                }

            })
        }
    }

    override fun clearTitleBarMenus() {
        (mTitlebar as Toolbar?)?.menu?.clear()
    }

    /**
     * 显示空视图
     */
    override fun <T> showEmptyView(view: View?, data: T?) {
        if (view != null) {
            mEmptyView = view
        }
        if (mEmptyView != null) {
            mContentView.setViewToInvisible()
            if (mFlContentView.contains(mEmptyView!!).kttlwGetNotEmptyData { false }) {
                mEmptyView.setViewToVisible()
            } else {
                mFlContentView.addView(mEmptyView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                mEmptyView.setViewToVisible()
            }
        }
    }

    /**
     * 显示内容视图
     */
    override fun showContentView() {
        mContentView.setViewToVisible()
        mEmptyView?.setViewToGone()
    }

    override fun onPause() {
        super.onPause()
        AtlwImageLoadingFactory.getInstance().pauseLoading()
    }

    override fun onResume() {
        super.onResume()
        AtlwImageLoadingFactory.getInstance().resumeLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //新浪微博相关回调
        AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA)?.getSinaApi(this)?.let {
            it.doResultIntent(data, object : WbShareCallback {
                override fun onComplete() {
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT)
                        ?.callBackInfo(AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA)?.sinaKey(this@AcbflwBaseActivity))
                }

                override fun onError(p0: UiError?) {
                    AtlwLogUtil.logUtils.logI("shareToSina", JdplwJsonUtil.toJson(p0))
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT)
                        ?.callBackInfo(AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA)?.sinaKey(this@AcbflwBaseActivity),
                            AcbflwPluginErrorTypeEnum.SHARE_FAIL)
                }

                override fun onCancel() {
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT)
                        ?.callBackInfo(AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA)?.sinaKey(this@AcbflwBaseActivity),
                            AcbflwPluginErrorTypeEnum.SHARE_CANCEL)
                }
            })
            it.authorizeCallback(requestCode, resultCode, data)
        }
    }

    /**
     * 修改显示页面
     */
    @Synchronized
    protected fun changeFragment(fragment: Fragment, id: Int) {
        if (mLastShowFragment == null || mLastShowFragment!!.get() != fragment) {
            supportFragmentManager.apply {
                if (findFragmentByTag(fragment.hashCode().toString()) == null) {
                    if (mLastShowFragment != null) {
                        beginTransaction().hide(mLastShowFragment!!.get()!!).add(id, fragment, fragment.hashCode().toString())
                            .commitAllowingStateLoss()
                    } else {
                        beginTransaction().add(id, fragment, fragment.hashCode().toString()).commitAllowingStateLoss()
                    }
                } else {
                    if (mLastShowFragment != null) {
                        beginTransaction().hide(mLastShowFragment!!.get()!!).show(fragment).commitAllowingStateLoss()
                    } else {
                        beginTransaction().show(fragment).commitAllowingStateLoss()
                    }
                }
                mLastShowFragment = WeakReference(fragment)
            }
        }
    }
}
