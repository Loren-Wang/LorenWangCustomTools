package android.lorenwang.common_base_frame.mvp

import android.lorenwang.common_base_frame.adapter.AcbflwBaseType
import androidx.annotation.LayoutRes
import android.lorenwang.common_base_frame.AcbflwBaseActivity
import android.lorenwang.common_base_frame.AcbflwBaseConfig
import android.lorenwang.common_base_frame.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.common_base_frame.network.callback.AcbflwRepOptionsByPresenterCallback
import javabase.lorenwang.tools.common.JtlwClassUtils
import kotlinbase.lorenwang.tools.KttlwConfig
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 创建时间：2019-07-15 上午 11:11:22
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

abstract class AcbflwBasePresenter(var activity: AcbflwBaseActivity) {
    protected var TAG: String? = javaClass.name;
    /**
     * 默认首页页码
     */
    protected val defaultFirstPageIndex = KttlwConfig.DEFAULT_NET_PAGE_INDEX
    /**
     * 默认每页数量
     */
    protected val defaultPageCount = KttlwConfig.DEFAULT_NET_PAGE_SIZE;

    /**
     * 释放所有
     */
    fun releasePresenter() {
        TAG = null
        releasePresenterChild();
    }

    /**
     * 释放所有的继承该presenter的
     */
    abstract fun releasePresenterChild();

    /**
     * 获取页码
     */
    fun getPageIndex(pageIndex: Int?): Int {
        return pageIndex ?: defaultFirstPageIndex
    }

    /**
     * 列表转换为basetype类型列表
     */
    fun <T> listToBaseTypeList(@LayoutRes showLayout: Int, oldList: ArrayList<T>): ArrayList<AcbflwBaseType<T>> {
        val listBaseType = ArrayList<AcbflwBaseType<T>>()
        val iterator = oldList.iterator()
        while (iterator.hasNext()) {
            listBaseType.add(AcbflwBaseType(showLayout, iterator.next()))
        }
        return listBaseType
    }

    /**
     * 获取响应数据回调
     */
    open fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>, RESULT : AcbflwNetOptionsByModelCallback<D, T>>
            getNetOptionsCallback(repOptionsCallback: CALL): RESULT {
        return getNetOptionsCallback(showLoading = true, successHideLoading = true, errorHideLoading = true, allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     * @param successHideLoading 成功是否隐藏加载中
     */
    open fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>, RESULT : AcbflwNetOptionsByModelCallback<D, T>>
            getNetOptionsCallback(successHideLoading: Boolean, repOptionsCallback: CALL):RESULT {
        return getNetOptionsCallback(showLoading = true, successHideLoading = successHideLoading, errorHideLoading = true, allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     * @param showLoading 是否显示加载中
     * @param successHideLoading 成功是否隐藏加载中
     * @param errorHideLoading 异常是否隐藏加载中
     * @param allowLoadingBackFinishPage 显示加载中是是否允许后退结束当前页面
     * @param repOptionsCallback 数据操作后的回调
     * @return 网络请求回调
     */
    open fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>, RESULT : AcbflwNetOptionsByModelCallback<D, T>>
            getNetOptionsCallback(showLoading: Boolean, successHideLoading: Boolean, errorHideLoading: Boolean,
                                  allowLoadingBackFinishPage: Boolean, repOptionsCallback: CALL): RESULT {
        return object : RESULT() {
            override fun success(dto: T) {
                if (activity.isFinishing) {
                    return
                }
                if (successHideLoading) {
                    activity.hideBaseLoading()
                }
                //执行了成功回调，代表着和自定义的成功码一致，就是接口请求必须成功才会执行该方法
                repOptionsCallback.viewOptionsData(dto)
            }

            override fun error(e: Throwable) {
                if (activity.isFinishing) {
                    return
                }
                if (errorHideLoading) {
                    activity.hideBaseLoading()
                }
                if (e.message.isNullOrEmpty()) {
                    repOptionsCallback.repDataError(null, "")
                } else {
                    e.message!!.split("-")
                            .also { list ->
                                if (list.size == 2) {
                                    repOptionsCallback.repDataError(list[0], list[1])
                                }
                            }
                }
            }

            override fun onCompleteFinish() {
            }

            override fun onSubscribeStart() {
                if (showLoading) {
                    activity.showBaseLoading(allowLoadingBackFinishPage)
                }
            }

            override fun userLoginStatusError(code: Any?, message: String?) {
                activity.userLoginStatusError(code, message)
            }

        }
    }

}
