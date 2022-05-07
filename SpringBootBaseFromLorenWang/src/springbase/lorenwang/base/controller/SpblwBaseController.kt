package springbase.lorenwang.base.controller

import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.common.bean.KttlwNetPageResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwIsNotNullOrEmpty
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.base.spblwConfig

/**
 * 功能作用：基础controller
 * 创建时间：2019-12-16 14:03
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 1、从资源文件中获取文本字符---getMessage(msgCode)
 * 2、从资源文件中获取文本字符---getMessage(msgCode,defaultMsg)
 * 3、接口处理之后的响应返回---responseContent(stateCode,stateMsg,data)
 * 4、接口处理之后的数据列表响应返回---responseDataListContent(stateCode,stateMsg,pageIndex,pageSize,sumCount,dataList)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SpblwBaseController<R : SpblwBaseHttpServletRequestWrapper> {

    /**
     * 保留方法（所有子方法都要继承这个方法）
     * @param t
     */
    protected fun <T> base(request: R, t: T?) {
        spblwConfig.getLogUtil().logI(javaClass, "当前请求地址：${request.servletPath}")
        t.let {
            spblwConfig.getLogUtil().logI(javaClass, "当前请求数据：${JdplwJsonUtil.toJson(it)}")
        }
    }

    /**
     * 接口处理之后的响应返回
     * @param stateCode 响应状态吗
     * @param stateMessage 响应状态消息
     * @param obj 响应数据
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    open fun <T> responseContent(request: R?, stateCode: String, stateMessage: String, obj: T?): String {
        val baseResponseBean = KttlwBaseNetResponseBean(obj)
        baseResponseBean.stateCode = stateCode
        baseResponseBean.stateMessage = stateMessage
        val toJson = JdplwJsonUtil.toJson(baseResponseBean)
        if (request != null) {
            spblwConfig.getLogUtil().logI(javaClass, "请求地址：${request.servletPath}，数据：${toJson}")
        }
        return toJson
    }

    /**
     * 接口处理之后的响应返回
     * @param stateCode 响应状态吗
     * @param stateMessageCode 响应状态消息
     * @param obj 响应数据
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    open fun <T> responseContentCode(request: R?, stateCode: String, stateMessageCode: String, obj: T?): String {
        return responseContent(request, stateCode, spblwConfig.getMessageResourceValue(stateMessageCode), obj)
    }

    /**
     * 响应数据状态处理
     */
    open fun responseDataDisposeStatus(request: R?, bean: SpblwBaseDataDisposeStatusBean): String {
        return if (bean.repDataList) {
            responseDataListContent(request, bean.statusCode!!, if (bean.statusMsg.kttlwIsNotNullOrEmpty()) {
                bean.statusMsg!!
            } else {
                spblwConfig.getMessageResourceValue(bean.statusMsgCode)
            }, bean.pageIndex!!, bean.pageSize!!, bean.sumCount!!, bean.dataList)
        } else {
            responseContent(request, bean.statusCode!!, spblwConfig.getMessageResourceValue(bean.statusMsgCode), bean.body)
        }
    }

    /**
     * 接口处理之后的数据列表响应返回
     * @param stateCode 响应状态吗
     * @param stateMessage 响应状态消息
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    open fun <T : ArrayList<*>> responseDataListContent(request: R?, stateCode: String, stateMessage: String, pageIndex: Int, pageSize: Int,
        sumCount: Long, dataList: T?): String {
        val sumPageCount = if (sumCount % pageSize > 0) {
            sumCount / pageSize + 1
        } else {
            sumCount / pageSize
        }
        return responseContent(request, stateCode, stateMessage, KttlwNetPageResponseBean(pageIndex, pageSize, sumCount.toInt(), sumPageCount.toInt(),
            dataList.kttlwGetNotEmptyData { arrayListOf<Any>() }))
    }

    /**
     * 接口处理之后的数据列表响应返回
     * @param stateCode 响应状态吗
     * @param stateMessageCode 响应状态消息
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    open fun <E, T : ArrayList<E>> responseDataListContentCode(request: R?, stateCode: String, stateMessageCode: String, pageIndex: Int,
        pageSize: Int, sumCount: Long, dataList: T): String {
        val sumPageCount = if (sumCount % pageSize > 0) {
            sumCount / pageSize + 1
        } else {
            sumCount / pageSize
        }
        return responseContent(request, stateCode, spblwConfig.getMessageResourceValue(stateMessageCode),
            KttlwNetPageResponseBean(pageIndex, pageSize, sumCount.toInt(), sumPageCount.toInt(), dataList))
    }

    /**
     * 响应数据
     */
    open fun responseData(request: R?, result: Any?): String {
        return when (result) {
            //如果是类型的话则按类型处理
            is SpblwBaseDataDisposeStatusBean -> {
                responseDataDisposeStatus(request, result)
            }
            //如果是字符串的话则直接返回
            is String -> {
                result
            }
            else -> {
                //如果是其他的则为未知错误
                responseFailForUnKnow(request)
            }
        }
    }

    /**
     * 参数异常响应
     */
    open fun responseErrorForParams(request: R?): String {
        return spblwConfig.responseErrorForParams(request)
    }

    /**
     * 数据响应成功
     */
    open fun responseSuccess(request: R?, data: Any?): String {
        return spblwConfig.responseSuccess(request, data)
    }

    /**
     * 数据删除失败
     */
    open fun responseDeleteFail(request: R?): String {
        return spblwConfig.responseDeleteFail(request)
    }

    /**
     * 未知错误失败
     */
    open fun responseFailForUnKnow(request: R?): String {
        return spblwConfig.responseFailForUnKnow(request)
    }

}
