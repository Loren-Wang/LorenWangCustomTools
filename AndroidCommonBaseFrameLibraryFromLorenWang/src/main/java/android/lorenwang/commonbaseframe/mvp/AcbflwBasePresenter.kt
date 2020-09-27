package android.lorenwang.commonbaseframe.mvp

import android.app.Activity
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseType
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwRepOptionsByPresenterCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileUpLoadBean
import android.lorenwang.tools.AtlwConfig
import androidx.annotation.LayoutRes
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

abstract class AcbflwBasePresenter(var baseView: AcbflwBaseView) {
    protected var TAG: String? = javaClass.name

    protected var activity: Activity? = null

    init {
        if (baseView is Activity) {
            activity = baseView as Activity
        }
    }

    /**
     * 默认首页页码
     */
    protected val defaultFirstPageIndex = KttlwConfig.DEFAULT_NET_PAGE_INDEX

    /**
     * 默认每页数量
     */
    protected val defaultPageCount = KttlwConfig.DEFAULT_NET_PAGE_SIZE

    /**
     * 释放所有
     */
    fun releasePresenter() {
        TAG = null
        releasePresenterChild()
    }

    /**
     * 释放所有的继承该presenter的
     */
    abstract fun releasePresenterChild()

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
    fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>> getNetOptionsCallback(
            requestCode: Int, dataIsNull: Boolean?,
            repOptionsCallback: CALL): AcbflwNetOptionsByModelCallback<D, T> {
        return getNetOptionsCallback(
                requestCode = requestCode, dataIsNull = dataIsNull,
                showLoading = true, successHideLoading = true, errorHideLoading = true,
                allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     * @param successHideLoading 成功是否隐藏加载中
     */
    fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>> getNetOptionsCallback(
            requestCode: Int, dataIsNull: Boolean?,
            successHideLoading: Boolean, repOptionsCallback: CALL): AcbflwNetOptionsByModelCallback<D, T> {
        return getNetOptionsCallback(
                requestCode = requestCode, dataIsNull = dataIsNull,
                showLoading = true, successHideLoading = successHideLoading, errorHideLoading = true,
                allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     */
    fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>> getNetOptionsCallback(requestCode: Int, repOptionsCallback: CALL): AcbflwNetOptionsByModelCallback<D, T> {
        return getNetOptionsCallback(
                requestCode = requestCode, dataIsNull = false,
                showLoading = true, successHideLoading = true, errorHideLoading = true,
                allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
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
    fun <D, T : KttlwBaseNetResponseBean<D>, CALL : AcbflwRepOptionsByPresenterCallback<T>> getNetOptionsCallback(
            requestCode: Int, dataIsNull: Boolean?,
            showLoading: Boolean, successHideLoading: Boolean, errorHideLoading: Boolean,
            allowLoadingBackFinishPage: Boolean, repOptionsCallback: CALL): AcbflwNetOptionsByModelCallback<D, T> {
        //新增presenter记录
        AcbflwBaseApplication.application?.addPresenter(activity, this)
        //回传callback
        return object : AcbflwNetOptionsByModelCallback<D, T>() {
            override fun fileUpLoadProcess(bean: AcbflwFileUpLoadBean, total: Long, nowUpload: Long,
                                           process: Double) {
                super.fileUpLoadProcess(bean, total, nowUpload, process)
                repOptionsCallback.fileUpLoadProcess(bean, total, nowUpload, process)
            }

            override fun success(dto: T) {
                if (activity == null || activity!!.isFinishing) {
                    return
                }
                if (successHideLoading) {
                    baseView.hideBaseLoading()
                }
                if (dataIsNull == null || !dataIsNull) {
                    if (dto.data != null) {
                        repOptionsCallback.viewOptionsData(dto)
                    } else {
                        baseView.netReqFail(requestCode, AtlwConfig.nowApplication.getString(R.string.empty_data_default))
                        repOptionsCallback.repDataError(null,
                                AtlwConfig.nowApplication.getString(R.string.empty_data_default))
                    }
                } else {
                    repOptionsCallback.viewOptionsData(dto)
                }
            }

            override fun error(e: Throwable) {
                if (activity == null || activity!!.isFinishing) {
                    return
                }
                if (errorHideLoading) {
                    baseView.hideBaseLoading()
                }
                baseView.netReqFail(requestCode, e.message)
                repOptionsCallback.repDataError(null, e.message)
            }

            override fun onCompleteFinish() {
            }

            override fun onSubscribeStart() {
                if (showLoading) {
                    baseView.showBaseLoading(allowLoadingBackFinishPage)
                }
            }

            override fun userLoginStatusError(code: Any?, message: String?) {
                baseView.userLoginStatusError(code, message)
            }

        }
    }

    /**
     * 获取请求model记录
     */
    fun <T> getModel(cls: Class<T>): T {
        val model = JtlwClassUtils.getInstance().getClassEntity(cls)
        //新增model初始化记录
        AcbflwBaseApplication.application?.addPresenter(activity, this)
        return model
    }

}
