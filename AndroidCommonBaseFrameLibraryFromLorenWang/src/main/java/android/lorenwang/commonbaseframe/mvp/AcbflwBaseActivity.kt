package android.lorenwang.commonbaseframe.mvp

import android.content.Intent
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.share.WbShareCallback
import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.extend.kttlwToGone
import kotlinbase.lorenwang.tools.extend.kttlwToVisible
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
    protected val TAG = javaClass.simpleName

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


    /**
     * 上一个显示的fragment
     */
    protected var mLastShowFragment: WeakReference<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initCreateSuperBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acbflw_page_mvp)
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
        initTitleBarView(View.inflate(this, resId, null))
    }

    /**
     * 初始化标题栏控件
     */
    open fun initTitleBarView(view: View) {
        mTitleBarView = view
        val baseView = findViewById<ViewGroup>(R.id.lnAcbflwBase)
        val stub = findViewById<View>(R.id.vsbTitleBarHeadView)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mTitleBarView, index)
    }

    /**
     * 初始化内容视图
     */
    open fun initContentView(@LayoutRes resId: Int) {
        initContentView(View.inflate(this, resId, null))
    }

    /**
     * 初始化内容视图
     */
    open fun initContentView(view: View) {
        mContentView = view
        val baseView = findViewById<ViewGroup>(R.id.flAcbflwContent)
        val stub = findViewById<View>(R.id.vsbAcbflwContent)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mContentView, index)
    }

    /**
     * 初始化底部操作视图
     */
    open fun initBottomOptionsView(@LayoutRes resId: Int) {
        initBottomOptionsView(View.inflate(this, resId, null))
    }

    /**
     * 初始化底部操作视图
     */
    open fun initBottomOptionsView(view: View) {
        mBottomOptionsView = view
        val baseView = findViewById<ViewGroup>(R.id.lnAcbflwBase)
        val stub = findViewById<View>(R.id.vsbAcbflwBottomView)
        val index = baseView.indexOfChild(stub)
        baseView.removeViewAt(index)
        baseView.addView(mBottomOptionsView, index)
    }

    /**
     * 初始化空view视图
     */
    open fun initEmptyView(@LayoutRes resId: Int) {
        initEmptyView(View.inflate(this, resId, null))
    }

    /**
     * 初始化空view视图
     */
    open fun initEmptyView(view: View) {
        mEmptyView = view
        val baseView = findViewById<ViewGroup>(R.id.flAcbflwContent)
        val stub = findViewById<View>(R.id.vsbAcbflwEmpty)
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
