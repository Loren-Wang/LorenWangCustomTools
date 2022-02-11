package com.lorenwang.test.android.activity.splash

import android.content.Intent
import android.lorenwang.commonbaseframe.AcbflwBaseConfig.initBaseConfig
import android.lorenwang.commonbaseframe.network.AcbflwNetworkManager.Companion.instance
import android.lorenwang.tools.app.AtlwThreadUtil
import android.os.Bundle
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.MainActivity
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：启动页面
 * 初始注释时间： 2020/7/24 12:03 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
class StartActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        //初始化网络请求
        instance.initRetrofit(BuildConfig.APP_COMPILE_TYPE, "https://www.baidu.com", "", "", null, null, null)
        //        //初始化插件参数
        //        AcbflwPluginUtils.getInstance().initWeChatConfigInfo(
        //                new AcbflwWeChatConfigInfoBean.Build()
        //                        .setAppid("BuildConfig.WEIXIN_ID")
        //                        .setWeChatApplyId("BuildConfig.WEIXIN_MINI")
        //                        .setWeiChatSecret("BuildConfig.WEIXIN_SECRET")
        //                        .setWeChatId("BuildConfig.WEIXIN_ID")
        //                        .setCheckSignature(true)
        //                        .build());
        //配置全局参数
        initBaseConfig(BuildConfig.APPLICATION_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher)

        //自动页面跳转
        AtlwThreadUtil.getInstance().postOnUiThreadDelayed({ startActivity(Intent(this@StartActivity, MainActivity::class.java)) }, 1500L)
    }
}