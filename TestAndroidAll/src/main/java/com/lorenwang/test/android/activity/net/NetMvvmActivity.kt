package com.lorenwang.test.android.activity.net

import android.lorenwang.commonbaseframe.extension.setOnNoDoubleClick
import android.lorenwang.commonbaseframe.network.AcbflwNetManager
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import android.os.Bundle
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.base.BaseMvvmActivity
import com.lorenwang.test.android.databinding.ActivityNetMvvmBinding
import javabase.lorenwang.dataparse.JdplwJsonUtil

class NetMvvmActivity : BaseMvvmActivity<NetMvvmVModel, ActivityNetMvvmBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        title = "Mvvm网络请求"
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        val callback = object : AcbflwNetCallback<Any>() {
            override fun onSuccess(data: Any?, result: String?) {
                mViewBinding.tvShow.text = result
            }
        }
        val localKey = "local"
        AcbflwNetManager.resetNetwork(BuildConfig.APP_COMPILE_TYPE, localKey, "http://192.168.3.28")
        mViewBinding.btnMvvmRequestGetEmpty.setOnNoDoubleClick {
            AcbflwNetManager.sendGetUrlRequest(localKey, ":8003.config.unit", "")?.execute(callback)
        }
        mViewBinding.btnMvvmRequestPost.setOnNoDoubleClick {
            AcbflwNetManager.sendPostUrlRequest(localKey, ":8003.code.getQrCode",
                JdplwJsonUtil.toJson(mapOf(Pair("content", "asdfj阿达发生；就发；了十点多开始减肥撒打飞机"))))?.execute(callback)
        }
    }

    override fun setContentViewConfig(resId: Int?) {
        mViewBinding = ActivityNetMvvmBinding.inflate(layoutInflater)
        super.setContentViewConfig(resId)
    }
}