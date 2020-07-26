package com.test.androidcommonframe;

import android.lorenwang.commonbaseframe.AcbflwBaseApplication;

/**
 * 功能作用：项目application
 * 创建时间：2020-07-24 11:00 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
public class MyApplication extends AcbflwBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //设置编译模式
        setCompileType(BuildConfig.APP_COMPILE_TYPE);
    }
}
