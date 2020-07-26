package com.test.androidcommonframe.activity.splash;

import android.lorenwang.commonbaseframe.mvp.AcbflwNetRepCode;
import android.lorenwang.commonbaseframe.network.AcbflwNetworkManager;
import android.lorenwang.tools.AtlwConfig;
import android.os.Bundle;

import com.test.androidcommonframe.BuildConfig;
import com.test.androidcommonframe.R;
import com.test.androidcommonframe.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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
public class StartActivity extends BaseActivity {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        addContentView(R.layout.activity_start, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        //初始化网络请求
        AcbflwNetworkManager.getInstance().initRetrofit(
                BuildConfig.APP_COMPILE_TYPE,
                "https://qtoolsapp.qtoolsbaby.cn/",
                "http://qtoolsapp-hd.qtoolsbaby.cn/",
                "http://qtoolsapp-hd.qtoolsbaby.cn/",
                null, null, null);
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
        AtlwConfig.imageLoadingFailResId = R.mipmap.ic_launcher;
        AtlwConfig.imageLoadingLoadResId = R.mipmap.ic_launcher;
        AcbflwNetRepCode.INSTANCE.setRepCodeSuccess(200);
        ArrayList<Object> loginStatusError = new ArrayList<>();
        AcbflwNetRepCode.INSTANCE.setRepCodeLoginStatusError(loginStatusError);
    }
}
