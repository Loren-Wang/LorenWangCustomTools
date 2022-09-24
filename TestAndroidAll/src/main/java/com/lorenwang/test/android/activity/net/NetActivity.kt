package com.lorenwang.test.android.activity.net

import android.lorenwang.commonbaseframe.extension.setOnNoDoubleClick
import android.lorenwang.tools.app.AtlwActivityJumpUtil
import android.os.Bundle
import com.lorenwang.test.android.base.BaseMvvmActivity
import com.lorenwang.test.android.databinding.ActivityNetBinding

class NetActivity : BaseMvvmActivity<NetVModel, ActivityNetBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        title = "网络主页"
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        mViewBinding.btnMvvmRequest.setOnNoDoubleClick {
            AtlwActivityJumpUtil.getInstance().jump(this@NetActivity, NetMvvmActivity::class.java)
        }
        mViewBinding.btnMvpRequest.setOnNoDoubleClick {
            AtlwActivityJumpUtil.getInstance().jump(this@NetActivity, NetMvpActivity::class.java)
        }
    }
}