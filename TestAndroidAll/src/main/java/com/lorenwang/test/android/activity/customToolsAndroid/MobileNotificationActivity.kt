package com.lorenwang.test.android.activity.customToolsAndroid

import android.app.NotificationManager
import android.content.Intent
import android.lorenwang.tools.app.AtlwNotificationUtil
import android.lorenwang.tools.app.AtlwThreadUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.customView.titlebar.TitleBarHeadViewActivity
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_custom_tools_android_mobile_notification.*

/**
 * 功能作用：通知相关测试页面
 * 初始注释时间： 2021/9/18 13:13
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
class MobileNotificationActivity : BaseActivity() {

    private val progressChannelId = "12131231342"
    private val progressNotificationId = 1
    private var currentProgress = 0

    private val progressRunnable = object : Runnable {
        override fun run() {
            currentProgress++
            AtlwNotificationUtil.getInstance()
                .setProgress(progressChannelId, progressNotificationId, "通知进度测试", "当前通知进度:${currentProgress}/100", R.mipmap.ic_launcher, 100,
                    currentProgress, null)
            AtlwThreadUtil.getInstance().removeRunnable(this)
            if (currentProgress < 100) {
                AtlwThreadUtil.getInstance().postOnChildThreadDelayed(this, 100)
            } else {
                AtlwToastHintUtil.getInstance().toastMsg("通知进度完成")
                startActivity(Intent(this@MobileNotificationActivity, TitleBarHeadViewActivity::class.java))
                AtlwNotificationUtil.getInstance().removeNotification(progressNotificationId)
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_android_mobile_notification)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnSendProgress.kttlwThrottleClick {
            currentProgress = 0
            AtlwNotificationUtil.getInstance()
                .createNotificationChannel(progressChannelId, "进度更新通知", "用来在通知栏更新进度使用", NotificationManager.IMPORTANCE_DEFAULT)
            AtlwThreadUtil.getInstance().postOnChildThread(progressRunnable)
        }
        btnStopProgress.kttlwThrottleClick {
            AtlwThreadUtil.getInstance().removeRunnable(progressRunnable)
        }
        btnClearProgress.kttlwThrottleClick {
            AtlwNotificationUtil.getInstance().removeNotification(progressNotificationId)
        }
    }
}
