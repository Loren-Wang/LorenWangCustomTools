package com.test.springboot.bean

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import java.util.*

/**
 * 功能作用：接口功能之间处理结果传输使用类
 * 创建时间：2020-07-02 11:53 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
internal class DataDisposeStatusBean : SbcbflwBaseDataDisposeStatusBean {
    constructor(statusResult: Boolean, statusCode: String?,
                statusMsgCode: String?, statusMsg: String?, body: Any?) : super(statusResult, statusCode, statusMsgCode, statusMsg, body) {
    }

    constructor(statusResult: Boolean, body: Any?) : super(statusResult, body) {}
    constructor(statusResult: Boolean) : super(statusResult) {}
    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String?) : super(statusResult, statusCode, statusMsgCode) {}
    constructor(statusResult: Boolean, statusCode: String,
                statusMsgCode: String?, statusMsg: String?) : super(statusResult, statusCode, statusMsgCode, statusMsg) {
    }

    constructor(statusResult: Boolean, statusCode: String,
                statusMsgCode: String, statusMsg: String?, pageIndex: Int, pageSize: Int, sumCount: Long, dataList: ArrayList<Any?>) : super(statusResult, statusCode, statusMsgCode, statusMsg, pageIndex, pageSize, sumCount,
            dataList) {
    }

    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String, pageIndex: Int, pageSize: Int, sumCount: Long, dataList: ArrayList<Any?>) : super(statusResult, statusCode, statusMsgCode, pageIndex, pageSize, sumCount, dataList) {}
}
