package com.example.testapp.base;

import android.app.Application;
import android.lorenwang.commonbaseframe.AcbflwBaseApplication;
import android.lorenwang.commonbaseframe.AcbflwBaseConfig;
import android.lorenwang.tools.AtlwConfig;

import com.example.testapp.BuildConfig;

import java.util.Objects;

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
 *
 * @author wangliang
 */

public class BaseApplication extends AcbflwBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AtlwConfig.initAndroidCustomTools(this,
                AcbflwBaseConfig.appCompileTypeIsDebug(BuildConfig.APP_COMPILE_TYPE),
                Objects.requireNonNull(this.getExternalFilesDir("logInfo")).getAbsolutePath());
    }
}
