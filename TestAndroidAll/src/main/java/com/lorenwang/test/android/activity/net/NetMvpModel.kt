package com.lorenwang.test.android.activity.net

import android.lorenwang.commonbaseframe.network.AcbflwNetManager
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.base.BaseModel
import javabase.lorenwang.dataparse.JdplwJsonUtil

class NetMvpModel : BaseModel() {
    fun<T> sendGetEmptyReuquest(callback: AcbflwNetCallback<T>) {
        val localKey = "local"
        AcbflwNetManager.resetNetwork(BuildConfig.APP_COMPILE_TYPE, localKey, "http://192.168.3.28")
        AcbflwNetManager.sendGetUrlRequest(localKey, ":8003.config.unit", "")?.execute(callback)
    }
    fun<T> sendPostUrlRequest(callback: AcbflwNetCallback<T>) {
        val localKey = "local"
        AcbflwNetManager.resetNetwork(BuildConfig.APP_COMPILE_TYPE, localKey, "http://192.168.3.28")
        AcbflwNetManager.sendPostUrlRequest(localKey, ":8003.code.getQrCode",
            JdplwJsonUtil.toJson(mapOf(Pair("content", "asdfj阿达发生；就发；了十点多开始减肥撒打飞机"))))?.execute(callback)
    }
}