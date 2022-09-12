package android.lorenwang.commonbaseframe

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import javabase.lorenwang.tools.bean.A


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
     * 获取标题栏控件
     */
    fun getTitleBarView(): View?

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
    fun showEmptyView(view: View? = null)

    /**
     * 显示内容视图
     */
    fun showContentView()

    /**
     * 执行刷新数据
     */
    fun onRefreshData()

}