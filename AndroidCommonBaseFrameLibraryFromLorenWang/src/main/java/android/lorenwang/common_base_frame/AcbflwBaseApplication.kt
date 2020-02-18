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
open class AcbflwBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        appContext = applicationContext
        //初始化第三方配置
        AtlwSetting.isDebug = AcbflwBaseConfig.baseDebugStatus
        AtlwSetting.nowApplication = this
        AtlwSetting.registActivityLifecycleCallbacks(this)

        //基础包初始化配置
        AcbflwBaseConfig.titleBarHeadViewHeight = resources.getDimensionPixelOffset(R.dimen.base_title_bar_head_view_height)
        AcbflwBaseConfig.baseBottomViewHeight = resources.getDimensionPixelOffset(R.dimen.base_bottm_view_height)
    }

    /**
     * 设置状态
     * @param pro 是否是正式环境
     */
    fun setStatus(pro: Boolean) {
        //初始化第三方配置
        AtlwSetting.isDebug = !pro
        AcbflwBaseConfig.baseDebugStatus = !pro
    }

    companion object {
        /**
         * App级别的context上下文
         */
        @JvmField
        var appContext: Context? = null
        /**
         * application实例
         */
        @JvmField
        var application: Application? = null
    }
}
