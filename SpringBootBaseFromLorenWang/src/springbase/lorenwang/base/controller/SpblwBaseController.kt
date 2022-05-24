package springbase.lorenwang.base.controller

import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.common.bean.KttlwNetPageResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.base.spblwConfig
import java.util.concurrent.ConcurrentHashMap

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
     * 响应内容处理
     */
    open fun responseContent(request: R?, data: SpblwBaseDataDisposeStatusBean): String {
        //响应数据实体
        val res = if (data.isRepDataList) {
            KttlwBaseNetResponseBean(KttlwNetPageResponseBean(data.pageIndex, data.pageSize, data.sumCount, if (data.sumCount % data.pageSize > 0) {
                data.sumCount / data.pageSize + 1
            } else {
                data.sumCount / data.pageSize
            }, data.dataList as ArrayList<Any>))
        } else {
            KttlwBaseNetResponseBean(data.body)
        }.let {
            it.stateCode = data.statusCode.kttlwGetNotEmptyData("")
            it.stateMessage = data.statusMsg.kttlwGetNotEmptyData("")
            it
        }
        //响应体json
        val resData = JdplwJsonUtil.toJson(res)
        //日志打印
        val logMap = ConcurrentHashMap<String, Any?>()
        if (request != null) {
            logMap["requestPath"] = request.servletPath
            logMap["requestPort"] = request.serverPort
            logMap["requestData"] = request.queryString
        }
        logMap["responseData"] = resData
        spblwConfig.getLogUtil().logI(javaClass, logMap.kttlwToJsonData(), true)
        return spblwConfig.encryptRequestContent(resData)
    }

}
