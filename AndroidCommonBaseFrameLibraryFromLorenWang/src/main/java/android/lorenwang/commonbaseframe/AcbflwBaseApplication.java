package android.lorenwang.commonbaseframe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.lorenwang.commonbaseframe.image.AcbflwImageSelectUtil;
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseModel;
import android.lorenwang.commonbaseframe.mvp.AcbflwBasePresenter;
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.customview.video.AvlwVideoPlayManager;
import android.lorenwang.tools.AtlwConfig;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 功能作用：基础application类，App单独的
 * 创建时间：2021-03-01 11:50
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
public abstract class AcbflwBaseApplication extends Application {
    /**
     * model的创建集合
     */
    private final HashMap<Activity, ArrayList<AcbflwBaseModel>> modelMap = new HashMap<>();

    /**
     * presenter的创建集合
     */
    private final HashMap<Activity, ArrayList<AcbflwBasePresenter<AcbflwBaseView>>> presenterMap = new HashMap<>();
    /**
     * App级别的context上下文
     */
    public static Context appContext;

    /**
     * application实例
     */
    public static AcbflwBaseApplication application;

    /**
     * 当前正在展示的Activity页面
     */
    public static Activity currentShowActivity;

    /**
     * App上下文
     *
     * @return App上下文
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * 当前正在显示的页面
     *
     * @return 当前正在显示的页面
     */
    public static Activity getCurrentShowActivity() {
        return currentShowActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        appContext = getApplicationContext();

        //注册监听供当前库使用
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull @NotNull Activity activity,
                    @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull @NotNull Activity activity) {
                currentShowActivity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull @NotNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull @NotNull Activity activity) {
                currentShowActivity = null;
                //暂停视频播放
                if (isUseVideoPlayLibrary()) {
                    AvlwVideoPlayManager.getInstance().pausePlayVideoViews(activity, null);
                }
            }

            @Override
            public void onActivityStopped(@NonNull @NotNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull @NotNull Activity activity, @NonNull @NotNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull @NotNull Activity activity) {
                //释放mvp缓存
                releaseModelsAndPresenters(activity);
                //清除图片选择缓存
                if (isUsePictureSelectLibrary()) {
                    AcbflwImageSelectUtil.getInstance().clearCache(activity);
                }
                //移除相应页面适配
                if (isUseVideoPlayLibrary()) {
                    AvlwVideoPlayManager.getInstance().removePlayVideoViews(activity);
                }
                //移除相关微博实例
                if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA) != null) {
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA).removeSinaApi(activity);
                }
            }
        });
        //基础包初始化配置
        AcbflwBaseConfig.INSTANCE.setTitleBarHeadViewHeight(getResources().getDimensionPixelOffset(R.dimen.avlw_base_title_bar_head_view_height));
        AcbflwBaseConfig.INSTANCE.setBaseBottomViewHeight(getResources().getDimensionPixelOffset(R.dimen.avlw_base_bottom_view_height));

        //初始化安卓自定义工具类相关
        initAndroidCustomTools();
    }

    /**
     * 初始化安卓自定义工具类相关
     */
    private void initAndroidCustomTools() { //设置项目application实例
        AtlwConfig.nowApplication = this; //注册监听供工具库使用
        AtlwConfig.registerActivityLifecycleCallbacks(this);
        //设置新页面进入动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM = R.anim.aalw_anim_from_right;
        //设置旧业面退出动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM = R.anim.aalw_anim_to_left;
        //设置后退时新页面进入动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_ENTER_ANIM = R.anim.aalw_anim_from_left;
        //设置后退时就业面退出动画
        AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_EXIT_ANIM = R.anim.aalw_anim_to_right;
    }

    /**
     * 添加model记录
     *
     * @param activity  activity实例
     * @param baseModel model实例
     */
    public <BM extends AcbflwBaseModel> void addModel(Activity activity, BM baseModel) {
        if (activity != null && baseModel != null) {
            ArrayList<AcbflwBaseModel> list = modelMap.get(activity);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(baseModel);
            modelMap.put(activity, list);
        }
    }

    /**
     * 添加Presenter记录
     *
     * @param activity      activity实例
     * @param basePresenter presenter实例
     */
    public <BV extends AcbflwBaseView, BP extends AcbflwBasePresenter<BV>> void addPresenter(Activity activity, BP basePresenter) {
        if (activity != null && basePresenter != null) {
            ArrayList<AcbflwBasePresenter<AcbflwBaseView>> list = presenterMap.get(activity);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add((AcbflwBasePresenter<AcbflwBaseView>) basePresenter);
            presenterMap.put(activity, list);
        }
    }

    /**
     * 释放指定Activity的model数据以及presenter数据
     *
     * @param activity activity实例
     */
    private void releaseModelsAndPresenters(Activity activity) {
        if (activity != null) {
            ArrayList<AcbflwBaseModel> modelList = modelMap.get(activity);
            if (modelList != null) {
                for (AcbflwBaseModel mode : modelList) {
                    if (mode != null) {
                        mode.releaseModel();
                    }
                }
                modelMap.remove(activity);
            }
            ArrayList<AcbflwBasePresenter<AcbflwBaseView>> list = presenterMap.get(activity);
            if (list != null) {
                for (AcbflwBasePresenter<AcbflwBaseView> item : list) {
                    if (item != null) {
                        item.releasePresenter();
                    }
                }
                presenterMap.remove(activity);
            }
        }
    }

    /**
     * 是否使用了Picture图片选择库
     */
    public abstract boolean isUsePictureSelectLibrary();

    /**
     * 是否使用了视图播放库
     */
    public abstract boolean isUseVideoPlayLibrary();
}
