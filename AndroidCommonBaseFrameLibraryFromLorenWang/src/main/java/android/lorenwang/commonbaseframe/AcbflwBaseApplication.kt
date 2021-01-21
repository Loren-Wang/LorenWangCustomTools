package android.lorenwang.commonbaseframe

import android.app.Activity
import android.app.Application
import android.content.Context
import android.lorenwang.commonbaseframe.image.AcbflwImageSelectUtil
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseModel
import android.lorenwang.commonbaseframe.mvp.AcbflwBasePresenter
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView
import android.lorenwang.customview.video.AvlwVideoPlayManager
import android.lorenwang.tools.AtlwConfig
import android.os.Bundle
import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion

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
abstract class AcbflwBaseApplication : Application() {
    /**
     * model的创建集合
     */
    private val modelMap: HashMap<Activity, ArrayList<AcbflwBaseModel?>> = hashMapOf()

    /**
     * presenter的创建集合
     */
    private val presenterMap: HashMap<Activity, ArrayList<AcbflwBasePresenter<AcbflwBaseView>?>> = hashMapOf()

    override fun onCreate() {
        super.onCreate()
        application = this
        appContext = applicationContext

        //注册监听供当前库使用
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                currentShowActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                currentShowActivity = null
                //暂停视频播放
                if (isUseVideoPlayLibrary()) {
                    AvlwVideoPlayManager.getInstance().pausePlayVideoViews(activity, null)
                }
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) { //取消activity页面的model、presenter相关
                //释放mvp缓存
                releaseModelsAndPresenters(activity)
                //清除图片选择缓存
                if (isUsePictureSelectLibrary()) {
                    AcbflwImageSelectUtil.getInstance().clearCache(activity)
                }
                //移除相应页面适配
                if (isUseVideoPlayLibrary()) {
                    AvlwVideoPlayManager.getInstance().removePlayVideoViews(activity)
                }
            }
        })

        //基础包初始化配置
        AcbflwBaseConfig.titleBarHeadViewHeight = resources.getDimensionPixelOffset(R.dimen.avlw_base_title_bar_head_view_height)
        AcbflwBaseConfig.baseBottomViewHeight = resources.getDimensionPixelOffset(R.dimen.avlw_base_bottom_view_height)

        //初始化安卓自定义工具类相关
        initAndroidCustomTools()
    }

    /**
     * 初始化安卓自定义工具类相关
     */
    private fun initAndroidCustomTools() { //设置项目application实例
        AtlwConfig.nowApplication = this //注册监听供工具库使用
        AtlwConfig.registActivityLifecycleCallbacks(this)
        //设置新页面进入动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM = R.anim.aalw_anim_from_right
        //设置旧业面退出动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM = R.anim.aalw_anim_to_left
        //设置后退时新页面进入动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_ENTER_ANIM = R.anim.aalw_anim_from_left
        //设置后退时就业面退出动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_EXIT_ANIM = R.anim.aalw_anim_to_right
    }

    /**
     * 添加model记录
     *
     * @param activity  activity实例
     * @param baseModel model实例
     */
    open fun <BM : AcbflwBaseModel> addModel(activity: Activity?, baseModel: BM?) {
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
    open fun <BV : AcbflwBaseView, BP : AcbflwBasePresenter<BV>> addPresenter(activity: Activity?, basePresenter: BP?) {
        if (activity != null && basePresenter != null) {
            val list = presenterMap[activity] ?: arrayListOf()
            list.add(basePresenter.kttlwFormatConversion())
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
        @JvmStatic
        var appContext: Context? = null

        /**
         * application实例
         */
        @JvmStatic
        var application: AcbflwBaseApplication? = null

        /**
         * 当前正在展示的Activity页面
         */
        @JvmStatic
        var currentShowActivity: Activity? = null
    }

    /**
     * 是否使用了Picture图片选择库
     */
    abstract fun isUsePictureSelectLibrary(): Boolean

    /**
     * 是否使用了视图播放库
     */
    abstract fun isUseVideoPlayLibrary(): Boolean
}
