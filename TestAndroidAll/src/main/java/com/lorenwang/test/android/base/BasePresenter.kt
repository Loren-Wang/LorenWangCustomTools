package com.lorenwang.test.android.base

import android.lorenwang.commonbaseframe.bean.AcbflwBaseRepBean
import android.lorenwang.commonbaseframe.mvp.AcbflwBasePresenter
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwRepOptionsByPresenterCallback

/**
 * 功能作用：基础接口处理
 * 初始注释时间： 2021/10/14 17:17
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
class BasePresenter(view: AcbflwBaseView) : AcbflwBasePresenter<AcbflwBaseView>(view) {
    override fun releasePresenterChild() {
    }

    override fun <DATA, REP : AcbflwBaseRepBean<DATA>, CALL : AcbflwRepOptionsByPresenterCallback<REP>, MCALL : AcbflwNetOptionsByModelCallback<DATA, REP>> getRepOptionsByPresenterCallback(
        repOptionsCallback: CALL): MCALL {
        return AcbflwNetOptionsByModelCallback<DATA, REP>() as MCALL
    }
}
