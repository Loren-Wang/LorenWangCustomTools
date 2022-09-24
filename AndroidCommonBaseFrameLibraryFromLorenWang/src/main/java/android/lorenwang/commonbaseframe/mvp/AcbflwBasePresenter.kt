package android.lorenwang.commonbaseframe.mvp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseType
import android.lorenwang.commonbaseframe.bean.AcbflwPageBaseRepBean
import android.lorenwang.commonbaseframe.bean.AcbflwPageShowViewDataBean
import android.lorenwang.commonbaseframe.network.callback.AcbflwFileDownLoadCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import androidx.annotation.LayoutRes
import javabase.lorenwang.tools.common.JtlwClassUtil
import kotlinbase.lorenwang.tools.KttlwConfig
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
     * 文件下载
     */
    fun downLoadFile(appCompileType: Int, context: Context, downLoadFile: AcbflwFileDownLoadBean, callback: AcbflwFileDownLoadCallback) {
        AtlwActivityUtil.getInstance()
            .goToRequestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                (Math.random() * 1000).toInt(), object : AtlwPermissionRequestCallback {
                    override fun permissionRequestSuccessCallback(p0: MutableList<String>?) {
                        //权限申请通过，开始下载文件
                        getModel(AcbflwBaseModel::class.java).downloadFile(appCompileType, "", downLoadFile, callback)
                    }

                    override fun permissionRequestFailCallback(p0: MutableList<String>?) {
                        callback.downloadFail(downLoadFile)
                    }
                })
    }

    /**
     * 格式化列表展示数据
     */
    fun <T, D : AcbflwPageBaseRepBean<T>> paramsListData(page: Int?, count: Int?, body: D): AcbflwPageShowViewDataBean<T> {
        val pageIndex = getPageIndex(body.getCurrentPageIndex().kttlwGetNotEmptyData(page.kttlwGetNotEmptyData(defaultFirstPageIndex)))
        val pageCount = getPageCount(body.getCurrentPageSize().kttlwGetNotEmptyData(count.kttlwGetNotEmptyData(defaultPageCount)))
        val entity = AcbflwPageShowViewDataBean<T>()
        entity.isLastPageData =
            judgeLastPage(pageIndex, body.getSumPageCount().kttlwGetNotEmptyData(0), body.getSumDataCount().kttlwGetNotEmptyData(0))
        entity.isFirstPageData = judgeFirstPage(pageIndex)
        entity.currentPageIndex = pageIndex
        entity.currentPageCount = pageCount
        entity.total = body.getSumDataCount().kttlwGetNotEmptyData(0)
        entity.totalPage = body.getSumPageCount().kttlwGetNotEmptyData(0)
        entity.list = body.getList()
        return entity
    }

    /**
     * 格式化列表展示数据(基础部分数据，不包含列表数)
     */
    fun <T, D : AcbflwPageBaseRepBean<T>> paramsListBaseData(page: Int?, count: Int?, body: D): AcbflwPageShowViewDataBean<T> {
        val pageIndex = getPageIndex(body.getCurrentPageIndex().kttlwGetNotEmptyData(page.kttlwGetNotEmptyData(defaultFirstPageIndex)))
        val pageCount = getPageCount(body.getCurrentPageSize().kttlwGetNotEmptyData(count.kttlwGetNotEmptyData(defaultPageCount)))
        val entity = AcbflwPageShowViewDataBean<T>()
        entity.isLastPageData =
            judgeLastPage(pageIndex, body.getSumPageCount().kttlwGetNotEmptyData(0), body.getSumDataCount().kttlwGetNotEmptyData(0))
        entity.isFirstPageData = judgeFirstPage(pageIndex)
        entity.currentPageIndex = pageIndex
        entity.currentPageCount = pageCount
        entity.total = body.getSumDataCount().kttlwGetNotEmptyData(0)
        entity.totalPage = body.getSumPageCount().kttlwGetNotEmptyData(0)
        entity.list = arrayListOf()
        return entity
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
     * 获取请求model记录
     */
    fun <T> getModel(cls: Class<T>): T {
        val model = JtlwClassUtil.getInstance().getClassEntity(cls)
        //新增model初始化记录
        AcbflwBaseApplication.application?.addPresenter(activity, this)
        return model
    }

}
