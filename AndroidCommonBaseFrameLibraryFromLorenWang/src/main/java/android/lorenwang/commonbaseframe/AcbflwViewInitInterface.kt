package android.lorenwang.commonbaseframe

import android.lorenwang.tools.app.AtlwScreenUtil
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes


/**
 * 功能作用：基础activity、fragment的需实现的函数
 * 初始注释时间： 2022/9/12 12:54
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
interface AcbflwViewInitInterface {
    /**
     * 初始化create父级方法super之前调用
     */
    fun initCreateSuperBefore(savedInstanceState: Bundle?)

    /**
     * 设置标题栏控件配置
     * @param resId 布局id，为空时使用默认的
     */
    fun setTitleBarViewConfig(@LayoutRes resId: Int? = R.layout.acbflw_layout_titlebar)

    /**
     * 获取标题栏高度
     */
    fun getTitleBarHeight():Int = AtlwScreenUtil.getInstance().actionBarSize.let { if (it == 0) AtlwScreenUtil.getInstance().dip2px(50F).toInt() else it }

    /**
     * 设置内容控件配置
     * @param resId 布局id，为空时使用默认的
     */
    fun setContentViewConfig(@LayoutRes resId: Int? = null)

    /**
     * 设置空显示控件配置
     * @param resId 布局id，为空时使用默认的
     */
    fun setEmptyViewConfig(@LayoutRes resId: Int? = null)

    /**
     * 初始化view
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化监听
     */
    fun initListener(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 内容区域是否延伸到状态栏，默认不延伸
     */
    fun fitsSystemWindows() = false

    /**
     * 设置页面标题栏 右边 menu，当自定义titlebar时需重写
     */
    fun setTitleBarMenus(menus: List<AcbflwMenuItemModel>?, overflow: Boolean = false)

    /**
     * 清除页面标题栏 右边 menu，当自定义titlebar时需重写
     */
    fun clearTitleBarMenus()

    /**
     * 显示空视图
     */
    fun <T> showEmptyView(view: View? = null, data: T? = null)

    /**
     * 显示内容视图
     */
    fun showContentView()

    /**
     * 执行刷新数据
     */
    fun onRefreshData()

    /**
     * 显示加载中
     * @param allowLoadingBackFinishPage 是否允许后退结束当前页面
     * @param data 加载中显示数据，可能为json字符串
     */
    fun showBaseLoading(allowLoadingBackFinishPage: Boolean, data: String? = null)

    /**
     * 隐藏加载中
     */
    fun hideBaseLoading()

}