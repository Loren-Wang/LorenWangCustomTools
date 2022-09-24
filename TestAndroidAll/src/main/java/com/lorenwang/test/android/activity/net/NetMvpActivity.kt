package com.lorenwang.test.android.activity.net

import android.lorenwang.commonbaseframe.extension.setOnNoDoubleClick
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityNetMvpBinding

class NetMvpActivity : BaseActivity() {

    private val mPresent by lazy { NetMvpPresent(this) }
    private lateinit var mViewBinding: ActivityNetMvpBinding

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        title = "Mvp网络相关请求"
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        mViewBinding.btnMvpRequestGetEmpty.setOnNoDoubleClick { mPresent.sendGetEmptyReuquest(-100) }
        mViewBinding.btnMvpRequestPost.setOnNoDoubleClick { mPresent.sendPostUrlRequest(-200) }
    }

    override fun setContentViewConfig(resId: Int?) {
        super.setContentViewConfig(R.layout.activity_net_mvp)
        mViewBinding = ActivityNetMvpBinding.bind(mContentView!!)
    }

    override fun <T> netReqSuccess(netOptionReqCode: Int, data: T, result: String?) {
        super.netReqSuccess(netOptionReqCode, data, result)
        mViewBinding.tvShow.text = result
    }
}