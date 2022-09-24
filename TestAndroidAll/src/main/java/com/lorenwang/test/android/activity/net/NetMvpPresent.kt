package com.lorenwang.test.android.activity.net

import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import com.lorenwang.test.android.base.BasePresenter

class NetMvpPresent(view: AcbflwBaseView) : BasePresenter(view) {
    fun sendGetEmptyReuquest(requestCode: Int) {
        getModel(NetMvpModel::class.java).sendGetEmptyReuquest(object : AcbflwNetCallback<Any>() {
            override fun onSuccess(data: Any?, result: String?) {
                baseView.netReqSuccess(requestCode, data, result)
            }

            override fun onError(code: String, message: String?) {
                super.onError(code, message)
                baseView.netReqFail(requestCode, code, message)
            }
        })
    }
    fun sendPostUrlRequest(requestCode: Int) {
        getModel(NetMvpModel::class.java).sendPostUrlRequest(object : AcbflwNetCallback<Any>() {
            override fun onSuccess(data: Any?, result: String?) {
                baseView.netReqSuccess(requestCode, data, result)
            }

            override fun onError(code: String, message: String?) {
                super.onError(code, message)
                baseView.netReqFail(requestCode, code, message)
            }
        })
    }
}