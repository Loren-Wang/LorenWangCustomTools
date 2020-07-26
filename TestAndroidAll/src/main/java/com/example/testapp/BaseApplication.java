package com.example.testapp;

import android.app.Application;
import android.lorenwang.tools.AtlwConfig;

/**
 * 创建时间：2019-04-13 下午 14:30:36
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AtlwConfig.isDebug = true;
        AtlwConfig.nowApplication = this;
        AtlwConfig.registActivityLifecycleCallbacks(this);
    }
}
