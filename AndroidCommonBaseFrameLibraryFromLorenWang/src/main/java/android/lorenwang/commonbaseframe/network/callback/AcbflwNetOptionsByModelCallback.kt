package android.lorenwang.commonbaseframe.network.callback

import android.lorenwang.commonbaseframe.network.file.AcbflwFileUpLoadBean
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 功能作用：网络接口操作回调(主要对model层的基础处理回调，处理后会回调到presenter层)
 * 创建时间：2019-08-05 17:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：该类主要放到model层中，主要是对接口返回的数据的处理，例如转换成本地使用的数据结构类型等
 */
open class AcbflwNetOptionsByModelCallback<D, T : KttlwBaseNetResponseBean<D>> {


    /**
     * 页码
     */
    var pageIndex: Int? = null

    /**
     * 每页请求数量
     */
    var pageCount: Int? = null

    private var successFun: ((T) -> Unit?)? = null
    private var errorFun: ((Throwable) -> Unit?)? = null
    private var onCompleteFinishFun: (() -> Unit?)? = null
    private var onSubscribeStartFun: (() -> Unit?)? = null
    private var userLoginStatusErrorFun: ((code: Any?, message: String?) -> Unit?)? = null
    private var fileUpLoadProcessFun: ((bean: AcbflwFileUpLoadBean, total: Long, nowUpload: Long, process: Double) -> Unit?)? = null

    /**
     * 设置所有函数回调
     */
    fun setAllFun(successFun: (dto: T) -> Unit, errorFun: (e: Throwable) -> Unit, onCompleteFinishFun: () -> Unit, onSubscribeStartFun: () -> Unit,
        userLoginStatusErrorFun: (code: Any?, message: String?) -> Unit,
        fileUpLoadProcessFun: (bean: AcbflwFileUpLoadBean, total: Long, nowUpload: Long, process: Double) -> Unit) {
        this.successFun = successFun
        this.errorFun = errorFun
        this.onCompleteFinishFun = onCompleteFinishFun
        this.onSubscribeStartFun = onSubscribeStartFun
        this.userLoginStatusErrorFun = userLoginStatusErrorFun
        this.fileUpLoadProcessFun = fileUpLoadProcessFun
    }

    /**
     * 请求成功
     */
    open fun success(dto: T) {
        successFun?.let { it(dto) }
    }

    /**
     * 请求发生异常
     */
    open fun error(e: Throwable) {
        errorFun?.let { it(e) }
    }

    /**
     * 请求结束
     */
    open fun onCompleteFinish() {
        onCompleteFinishFun?.let { it() }
    }

    /**
     * 请求开始
     */
    open fun onSubscribeStart() {
        onSubscribeStartFun?.let { it() }
    }

    /**
     * 用户登陆状态异常
     */
    open fun userLoginStatusError(code: Any?, message: String?) {
        userLoginStatusErrorFun?.let { it(code, message) }
    }

    /**
     * 文件上传进度
     *
     * @param bean      文件实例
     * @param total     文件上传总容量
     * @param nowUpload 当前已上传数据
     * @param process   上传进度，0-1之间
     */
    open fun fileUpLoadProcess(bean: AcbflwFileUpLoadBean, total: Long, nowUpload: Long, process: Double) {
        fileUpLoadProcessFun?.let { it(bean, total, nowUpload, process) }
    }
}
