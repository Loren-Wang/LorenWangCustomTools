package android.lorenwang.commonbaseframe.mvp

import android.app.Activity
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseType
import android.lorenwang.commonbaseframe.bean.AcbflwPageShowViewDataBean
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwRepOptionsByPresenterCallback
import android.lorenwang.tools.AtlwConfig
import androidx.annotation.LayoutRes
import javabase.lorenwang.tools.common.JtlwClassUtils
import kotlinbase.lorenwang.tools.KttlwConfig
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.common.bean.KttlwNetPageResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：基础功能present
 * 初始注释时间： 2021/1/17 8:53 下午
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
abstract class AcbflwBasePresenter<V : AcbflwBaseView>(var baseView: V) {

    var activity: Activity? = null
        private set

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
     * 获取默认页面数据
     */
    fun getPageCount(pageCount: Int?): Int {
        return pageCount ?: defaultPageCount
    }

    /**
     * 是否是最后一页
     */
    fun judgeLastPage(currentPage: Int, totalPage: Int, total: Int): Boolean {
        var page = currentPage
        return if (total == 0) {
            true
        } else {
            if (defaultFirstPageIndex == 0) {
                ++page
            }
            page.compareTo(totalPage) == 0
        }
    }

    /**
     * 是否是第一页
     */
    fun judgeFirstPage(currentPage: Int?): Boolean {
        return currentPage == null || Integer.compare(currentPage, defaultFirstPageIndex) == 0
    }


    /**
     * 格式化列表展示数据
     */
    fun <T, R : KttlwNetPageResponseBean<T>, PD : AcbflwPageShowViewDataBean<T>, DATA : KttlwBaseNetResponseBean<R>> paramsListData(page: Int?,
        count: Int?, body: DATA): PD {
        val pageIndex = getPageIndex(body.data?.pageIndex.kttlwGetNotEmptyData(page.kttlwGetNotEmptyData(defaultFirstPageIndex)))
        val pageCount = getPageCount(body.data?.pageSize.kttlwGetNotEmptyData(count.kttlwGetNotEmptyData(defaultPageCount)))
        val entity = getPageShowDataBean<T, PD>()
        entity.isLastPageData =
            judgeLastPage(pageIndex, body.data?.sumPageCount.kttlwGetNotEmptyData(0), body.data?.sumDataCount.kttlwGetNotEmptyData(0))
        entity.isFirstPageData = judgeFirstPage(pageIndex)
        entity.currentPageIndex = pageIndex
        entity.currentPageCount = pageCount
        entity.total = body.data?.sumDataCount.kttlwGetNotEmptyData(0)
        entity.totalPage = body.data?.sumPageCount.kttlwGetNotEmptyData(0)
        entity.list = body.data?.dataList
        return entity
    }

    /**
     * 格式化列表展示数据(基础部分数据，不包含列表数)
     */
    fun <T, R, P : KttlwNetPageResponseBean<T>, PD : AcbflwPageShowViewDataBean<R>, DATA : KttlwBaseNetResponseBean<P>> paramsListBaseData(page: Int?,
        count: Int?, body: DATA): PD {
        val pageIndex = getPageIndex(body.data?.pageIndex.kttlwGetNotEmptyData(page.kttlwGetNotEmptyData(defaultFirstPageIndex)))
        val pageCount = getPageCount(body.data?.pageSize.kttlwGetNotEmptyData(count.kttlwGetNotEmptyData(defaultPageCount)))
        val entity = getPageShowDataBean<R, PD>()
        entity.isLastPageData =
            judgeLastPage(pageIndex, body.data?.sumPageCount.kttlwGetNotEmptyData(0), body.data?.sumDataCount.kttlwGetNotEmptyData(0))
        entity.isFirstPageData = judgeFirstPage(pageIndex)
        entity.currentPageIndex = pageIndex
        entity.currentPageCount = pageCount
        entity.total = body.data?.sumDataCount.kttlwGetNotEmptyData(0)
        entity.totalPage = body.data?.sumPageCount.kttlwGetNotEmptyData(0)
        entity.list = arrayListOf()
        return entity
    }


    /**
     * 列表转换为basetype类型列表
     */
    fun <T, BT : AcbflwBaseType<T>> listToBaseTypeList(@LayoutRes showLayout: Int, oldList: ArrayList<T>): ArrayList<BT> {
        val listBaseType = ArrayList<BT>()
        val iterator = oldList.iterator()
        while (iterator.hasNext()) {
            listBaseType.add(getBaseType(showLayout, iterator.next()))
        }
        return listBaseType
    }

    /**
     * 获取响应数据回调
     */
    fun <DATA, REP : KttlwBaseNetResponseBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getNetOptionsCallback(
        requestCode: Int, dataIsNull: Boolean?, repOptionsCallback: CALL): MCALL {
        return getNetOptionsCallback(requestCode = requestCode, dataIsNull = dataIsNull, showLoading = true, successHideLoading = true,
            errorHideLoading = true, allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     * @param successHideLoading 成功是否隐藏加载中
     */
    fun <DATA, REP : KttlwBaseNetResponseBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getNetOptionsCallback(
        requestCode: Int, dataIsNull: Boolean?, successHideLoading: Boolean, repOptionsCallback: CALL): MCALL {
        return getNetOptionsCallback(requestCode = requestCode, dataIsNull = dataIsNull, showLoading = true, successHideLoading = successHideLoading,
            errorHideLoading = true, allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
    }

    /**
     * 获取响应数据回调
     */
    fun <DATA, REP : KttlwBaseNetResponseBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getNetOptionsCallback(
        requestCode: Int, repOptionsCallback: CALL): MCALL {
        return getNetOptionsCallback(requestCode = requestCode, dataIsNull = false, showLoading = true, successHideLoading = true,
            errorHideLoading = true, allowLoadingBackFinishPage = false, repOptionsCallback = repOptionsCallback)
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
    fun <DATA, REP : KttlwBaseNetResponseBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getNetOptionsCallback(
        requestCode: Int, dataIsNull: Boolean?, showLoading: Boolean, successHideLoading: Boolean, errorHideLoading: Boolean,
        allowLoadingBackFinishPage: Boolean, repOptionsCallback: CALL): MCALL {
        //新增presenter记录
        AcbflwBaseApplication.application?.addPresenter(activity, this)
        val entity = getRepOptionsByPresenterCallback<DATA, REP, CALL, MCALL>(repOptionsCallback)
        entity.setAllFun({ dto ->
            if (!activity?.isFinishing.kttlwGetNotEmptyData(false)) {
                if (successHideLoading) {
                    baseView.hideBaseLoading()
                }
                if (dataIsNull == null || !dataIsNull) {
                    if (dto.data != null) {
                        repOptionsCallback.viewOptionsData(dto)
                    } else {
                        baseView.netReqFail(requestCode, AtlwConfig.nowApplication.getString(R.string.empty_data_default))
                        repOptionsCallback.repDataError(null, AtlwConfig.nowApplication.getString(R.string.empty_data_default))
                    }
                } else {
                    repOptionsCallback.viewOptionsData(dto)
                }
            }
        }, { e ->
            if (!activity?.isFinishing.kttlwGetNotEmptyData(false)) {
                if (errorHideLoading) {
                    baseView.hideBaseLoading()
                }
                baseView.netReqFail(requestCode, e.message)
                repOptionsCallback.repDataError(null, e.message)
            }
        }, {

        }, {
            if (showLoading) {
                baseView.showBaseLoading(allowLoadingBackFinishPage)
            }
        }, { code, message ->
            baseView.userLoginStatusError(code, message)
        }, { bean, total, nowUpload, process ->
            repOptionsCallback.fileUpLoadProcess(bean, total, nowUpload, process)
        })
        return entity
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

    /**
     * 获取默认实例
     */
    abstract fun <DATA, REP : KttlwBaseNetResponseBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getRepOptionsByPresenterCallback(
        repOptionsCallback: CALL): MCALL

    /**
     * 返回列表BaseType数据
     */
    abstract fun <T, BT : AcbflwBaseType<T>> getBaseType(@LayoutRes showLayout: Int, data: T): BT

    /**
     * 获取分页显示数据
     */
    abstract fun <R, PD : AcbflwPageShowViewDataBean<R>> getPageShowDataBean(): PD
}
