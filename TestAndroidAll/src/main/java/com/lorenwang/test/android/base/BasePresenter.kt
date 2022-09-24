package com.lorenwang.test.android.base

import android.lorenwang.commonbaseframe.mvp.AcbflwBasePresenter
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView

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
open class BasePresenter(view: AcbflwBaseView) : AcbflwBasePresenter<AcbflwBaseView>(view) {
    override fun releasePresenterChild() {
    }
}
