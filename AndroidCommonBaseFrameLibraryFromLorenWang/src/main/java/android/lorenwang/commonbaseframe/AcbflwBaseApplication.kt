package android.lorenwang.commonbaseframe

import android.app.Activity
import android.app.Application
import android.content.Context
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseModel
import android.lorenwang.commonbaseframe.mvp.AcbflwBasePresenter
import android.lorenwang.tools.AtlwSetting
import android.os.Bundle

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
    /**
     * model的创建集合
     */
    private val modelMap: HashMap<Activity, ArrayList<AcbflwBaseModel?>> = hashMapOf()

    /**
     * presenter的创建集合
     */
    private val presenterMap: HashMap<Activity, ArrayList<AcbflwBasePresenter?>> = hashMapOf()

    override fun onCreate() {
        super.onCreate()
        application = this
        appContext = applicationContext
        //初始化第三方配置
        AtlwSetting.isDebug = AcbflwBaseConfig.appCompileTypeIsDebug(BuildConfig.APP_COMPILE_TYPE)
        AtlwSetting.nowApplication = this
        //注册监听供工具库使用
        AtlwSetting.registActivityLifecycleCallbacks(this)

        //注册监听供当前库使用
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {
                //取消activity页面的model、presenter相关
                releaseModelsAndPresenters(activity)
            }
        })

        //基础包初始化配置
        AcbflwBaseConfig.titleBarHeadViewHeight = resources.getDimensionPixelOffset(R.dimen.avlw_base_title_bar_head_view_height)
        AcbflwBaseConfig.baseBottomViewHeight = resources.getDimensionPixelOffset(R.dimen.avlw_base_bottom_view_height)
    }

    /**
     * 添加model记录
     *
     * @param activity  activity实例
     * @param baseModel model实例
     */
    open fun addModel(activity: Activity?, baseModel: AcbflwBaseModel?) {
        if (activity != null && baseModel != null) {
            val modelList = modelMap[activity] ?: arrayListOf()
            modelList.add(baseModel)
            modelMap[activity] = modelList
        }
    }

    /**
     * 添加Presenter记录
     *
     * @param activity      activity实例
     * @param basePresenter presenter实例
     */
    open fun addPresenter(activity: Activity?, basePresenter: AcbflwBasePresenter?) {
        if (activity != null && basePresenter != null) {
            val list = presenterMap[activity] ?: arrayListOf()
            list.add(basePresenter)
            presenterMap[activity] = list
        }
    }

    /**
     * 释放指定Activity的model数据以及presenter数据
     *
     * @param activity activity实例
     */
    fun releaseModelsAndPresenters(activity: Activity?) {
        if (activity != null) {
            val modelList = modelMap[activity]
            if (modelList != null) {
                for (model in modelList) {
                    model?.releaseModel()
                }
                modelMap.remove(activity)
            }
            val presenterList = presenterMap[activity]
            if (presenterList != null) {
                for (presenter in presenterList) {
                    presenter?.releasePresenter()
                }
                presenterMap.remove(activity)
            }
        }
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
        var application: AcbflwBaseApplication? = null
    }
}
