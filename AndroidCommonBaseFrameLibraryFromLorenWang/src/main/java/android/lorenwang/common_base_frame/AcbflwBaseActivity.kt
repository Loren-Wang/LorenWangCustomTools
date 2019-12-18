package android.lorenwang.common_base_frame

import androidx.appcompat.app.AppCompatActivity
import android.lorenwang.common_base_frame.mvp.AcbflwBaseView
import android.lorenwang.tools.app.AtlwViewUtils
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes

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
     * 用户登陆状态异常
     */
    abstract fun userLoginStatusError(code: Any?, message: String?)

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
        vsbContent.inflate()
        //标题栏视图
        titleBarHeadViewLayoutResId?.let {
            val vsbTitleBarHeadView = findViewById<ViewStub>(R.id.vsbTitleBarHeadView)
            vsbTitleBarHeadView.layoutResource = it
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbTitleBarHeadView, ViewGroup.LayoutParams.MATCH_PARENT, titleBarHeadViewHeight)
            vsbTitleBarHeadView.inflate()
        }
        //底部栏视图
        bottomViewResId?.let {
            val vsbBottomView = findViewById<ViewStub>(R.id.vsbBottomView)
            vsbBottomView.layoutResource = bottomViewResId
            AtlwViewUtils.getInstance().setViewWidthHeight(vsbBottomView, ViewGroup.LayoutParams.MATCH_PARENT, baseBottomViewHeight)
            vsbBottomView.inflate()
        }
    }
}
