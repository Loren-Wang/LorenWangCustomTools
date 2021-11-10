package com.lorenwang.test.android.activity.customToolsAndroid

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：消息传递样式
 * 初始注释时间： 2021/10/14 15:18
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
class FlyMessageActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_android_fly_message)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_register -> {
                    AtlwFlyMessageUtil.getInstance().registerMsgCallback(this, 1, object : AtlwFlyMessageUtil.FlyMessgeCallback {
                        override fun msg(msgType: Int, vararg msgs: Any?) {
                            AtlwToastHintUtil.getInstance().toastMsg(msgs[0]?.toString())
                        }
                    }, true, true)
                }
                R.id.btn_un_register -> {
                    AtlwFlyMessageUtil.getInstance().unRegisterMsgCallback(this)
                }
                R.id.btn_send -> {
                    AtlwFlyMessageUtil.getInstance().sendMsg(1, false, Math.random().toString())
                }
                else -> {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AtlwFlyMessageUtil.getInstance().unRegisterMsgCallback(this)
    }
}
