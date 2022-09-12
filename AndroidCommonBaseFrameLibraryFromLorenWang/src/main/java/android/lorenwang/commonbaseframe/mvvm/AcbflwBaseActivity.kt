package android.lorenwang.commonbaseframe.mvvm

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.lorenwang.commonbaseframe.AcbflwMenuItemModel
import android.lorenwang.commonbaseframe.AcbflwViewInitInterface
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.extension.setViewToGone
import android.lorenwang.commonbaseframe.extension.setViewToInvisible
import android.lorenwang.commonbaseframe.extension.setViewToVisible
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.share.WbShareCallback
import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * 功能作用：mvvm基类activity
 * 初始注释时间： 2022/9/12 13:00
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
abstract class AcbflwBaseActivity<VM : AcbflwVModel, VB : ViewBinding> : AppCompatActivity(), AcbflwViewInitInterface {
    protected lateinit var mViewModel: VM
    protected lateinit var mViewBinding: VB
    private var mTitlebar: View? = null

    /**
     * 空视图view
     */
    private var mEmptyView: View? = null

    /**
     * 根目录view
     */
    private var mRootView: ViewGroup? = null

    /**
     * 上一个显示的fragment
     */
    protected var mLastShowFragment: WeakReference<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initCreateSuperBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        initViewBinding()
        initViewModel()
        initView(savedInstanceState)
        initListener(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun setTitle(title: CharSequence?) {
        mTitlebar?.findViewById<AppCompatTextView>(R.id.base_toolbar_title)?.text = title
    }

    override fun getTitleBarView(): View? {
        return (layoutInflater.inflate(R.layout.acbflw_layout_titlebar, null) as ViewGroup).apply {
            if (this is Toolbar) {
                //设置titlebar 返回按钮，取值navigationIcon
                setNavigationIcon(R.drawable.picture_icon_back_arrow)
                setNavigationOnClickListener { onBackPressed() }
            }
        }
    }

    override fun initCreateSuperBefore(savedInstanceState: Bundle?) {
        //解决部分机型 内容区域被底部导航栏遮挡的问题，注意activity和fragment 都设置的情况，暂未发现异常
        if (fitsSystemWindows()) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
    override fun showEmptyView(view: View?) {
        if (view != null) {
            mEmptyView = view
        }
        if (mEmptyView != null) {
            mViewBinding.root.setViewToInvisible()
            if (mRootView?.contains(mEmptyView!!).kttlwGetNotEmptyData { false }) {
                mEmptyView.setViewToVisible()
            } else {
                mRootView?.addView(mEmptyView, ConstraintLayout.LayoutParams(0, 0).apply {
                    leftToLeft = mViewBinding.root.id
                    topToTop = mViewBinding.root.id
                    rightToRight = mViewBinding.root.id
                    bottomToBottom = mViewBinding.root.id
                })
            }
        }
    }

    /**
     * 显示内容视图
     */
    override fun showContentView() {
        mViewBinding.root.setViewToVisible()
        mEmptyView?.setViewToGone()
    }

    /**
     * 获取标题栏
     */
    fun getTitleBar() = mTitlebar

    /**
     * viewModel是否初始化
     */
    fun isViewModelInitialized() = this::mViewModel.isInitialized

    /**
     * viewBinding是否初始化
     */
    fun isViewBindingInitialized() = this::mViewBinding.isInitialized

    private fun initViewBinding() {
        mViewBinding = getViewBinding()
        //随机生成一个id使用，如果没有的话，为了当需要id时无法使用的问题
        if (mViewBinding.root.id == -1) {
            mViewBinding.root.id = (Math.random() * -100000).toInt()
        }
        mRootView = (layoutInflater.inflate(R.layout.acbflw_page_mvp, null) as ViewGroup).apply {
            mTitlebar = getTitleBarView()?.also {
                if (it is Toolbar) {
                    it.findViewById<TextView>(R.id.base_toolbar_title)?.also { textView ->
                        if (textView.text.toString().isEmpty()) {
                            textView.text = (context as? Activity)?.title
                        }
                    }
                    if (it.parent == null) {
                        this.addView(it, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AtlwScreenUtil.getInstance().actionBarSize))
                    } else {
                        it.layoutParams.height = AtlwScreenUtil.getInstance().actionBarSize
                    }
                    //设置titlebar背景色，取值colorPrimary
                    val typedValue = TypedValue()
                    if (theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)) {
                        it.setBackgroundColor(ContextCompat.getColor(context, typedValue.resourceId))
                    }
                } else {
                    if (it.parent == null) {
                        addView(it, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                    } else {
                        it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
                mTitlebar = it
            }
            //添加内容区域view
            addView(mViewBinding.root, 0, ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0).apply {
                if (mTitlebar != null) topToBottom = mTitlebar!!.id
                else topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            })
        }
        if (fitsSystemWindows()) {
            window.statusBarColor = Color.TRANSPARENT
            //当设置为内容延伸至状态栏时，titleBar默认不会延伸至状态栏，并且titleBar背景透明。
            //如需titleBar也延伸至状态栏，可自定义titleBar[generateTitlebar]。
            (mViewBinding.root.layoutParams as ConstraintLayout.LayoutParams).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            }
            mTitlebar?.let {
                it.setBackgroundColor(Color.TRANSPARENT)
                if (it.layoutParams is ConstraintLayout.LayoutParams) {
                    (it.layoutParams as ConstraintLayout.LayoutParams).topMargin = AtlwScreenUtil.getInstance().statusBarHeight
                }
            }
        }
        setContentView(mRootView)
    }

    private fun initViewModel() {
        val vbClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[0] as Class<VM>
        mViewModel = ViewModelProvider(this)[viewModel]
    }

    /**
     * 获取ViewBinding，框架自动处理
     */
    private fun getViewBinding(): VB {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }


    override fun onDestroy() {
        AtlwActivityUtil.getInstance().setInputMethodVisibility(this, mRootView, View.GONE)
        super.onDestroy()
    }

    /**
     * 为了新老框架兼容使用
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AtlwActivityUtil.getInstance().receivePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AtlwActivityUtil.getInstance().receiveActivityResult(requestCode, resultCode, data)
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

    override fun onPause() {
        super.onPause()
        AtlwImageLoadingFactory.getInstance().pauseLoading()
    }

    override fun onResume() {
        super.onResume()
        AtlwImageLoadingFactory.getInstance().resumeLoading()
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