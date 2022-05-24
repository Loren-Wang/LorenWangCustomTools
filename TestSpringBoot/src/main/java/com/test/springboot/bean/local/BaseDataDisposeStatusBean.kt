package com.test.springboot.bean.local

import com.test.springboot.enums.NetRepStatusEnum
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean

class BaseDataDisposeStatusBean : SpblwBaseDataDisposeStatusBean {
    private constructor(status: Boolean, repStatusEnum: NetRepStatusEnum, body: Any) : super(status, repStatusEnum.code, repStatusEnum.messageKey,
        body)

    constructor(status: Boolean) : super(status)
    constructor(status: Boolean, body: String?) : super(status, body)
    constructor(status: Boolean, code: String?, msg: String?, body: Any?) : super(status, code, msg, body)
    constructor(status: Boolean, code: String?, msg: String?, pageIndex: Int?, pageSize: Int?, sumCount: Int?, list: List<Any?>?) : super(status,
        code, msg, pageIndex, pageSize, sumCount, list)

    constructor(status: Boolean, code: String?, msg: String?, body: Any?, pageIndex: Int?, pageSize: Int?, sumCount: Int?, list: List<Any?>?) : super(
        status, code, msg, body, pageIndex, pageSize, sumCount, list)
}