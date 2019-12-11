package android.lorenwang.common_base_frame

import android.app.Application
import android.content.Context
import android.lorenwang.tools.AtlwSetting

/**
 * 初始注释时间： 2019/8/14 0014 下午 17:24:25
 * 注释创建人：LorenWang（王亮）
 * 功能作用：基础application类，App单独的
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        //初始化第三方配置
        AtlwSetting.isDebug = BuildConfig.DEBUG
        AtlwSetting.registActivityLifecycleCallbacks(this)
    }

    /**
     * 设置状态
     * @param pro 是否是正式环境
     */
    fun setStatus(pro: Boolean) {
        //初始化第三方配置
        AtlwSetting.isDebug = !pro
    }

    companion object {
        /**
         * App级别的context上下文
         */
        @JvmField
        var appContext: Context? = null
    }
}
