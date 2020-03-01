package android.lorenwang.common_base_frame

import android.lorenwang.common_base_frame.mvp.AcbflwBaseView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    protected var fgView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fgView = inflater.inflate(getFgLayoutResId(), null)
        fgView?.let {
            initView(it)
            initListener(it)
            initData(it)
        }
        return fgView
    }

    /**
     * 初始化view
     */
    abstract fun initView(view: View);

    /**
     * 初始化监听
     */
    open fun initListener(view: View) {}

    /**
     * 初始化数据
     */
    abstract fun initData(view: View);

    /**
     * 获取fragment资源id
     */
    abstract fun getFgLayoutResId(): Int
}
