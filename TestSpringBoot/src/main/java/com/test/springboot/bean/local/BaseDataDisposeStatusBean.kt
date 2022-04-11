package com.test.springboot.bean.local

import com.test.springboot.enums.NetRepStatusEnum
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean

/**
 * 功能作用：数据处理状态实体
 * 创建时间：2020-12-09 4:55 下午
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
class BaseDataDisposeStatusBean(statusResult: Boolean, val repStatusEnum: NetRepStatusEnum, body: String? = null) :
    SpblwBaseDataDisposeStatusBean(statusResult,repStatusEnum.code,repStatusEnum.messageKey, body)
