package android.lorenwang.customview.dialog;

import android.app.Activity;
import android.app.Application;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

/**
 * 功能作用：基础弹窗
 * 创建时间：2018-11-16 下午 15:11:28
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwBaseDialog extends AlertDialog {
    protected final String TAG = getClass().getName();
    /**
     * 当前页面实例
     */
    protected Activity activity;
    /**
     * 当前页面view
     */
    protected View view;
    /**
     * 显示的弹窗宽度
     */
    protected int showDialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 显示的弹窗高度
     */
    protected int showDialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 窗口放置位置,默认底部
     */
    protected int windowGravity = Gravity.BOTTOM;

    protected AvlwBaseDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    protected AvlwBaseDialog(Activity context, int style) {
        super(context, style);
        this.activity = context;
    }

    public AvlwBaseDialog(final Activity context, @LayoutRes int dialogViewLayoutResId,
                          @StyleRes int modelStyleResId, @StyleRes int dialogAnim,
                          boolean isOutSideCancel, Integer showDialogWidth,
                          Integer showDialogHeight,Integer windowGravity) {
        super(context, modelStyleResId);
        this.activity = context;
        this.windowGravity = windowGravity != null ? windowGravity : this.windowGravity;
        view = LayoutInflater.from(context).inflate(dialogViewLayoutResId, null);
        new Builder(context, modelStyleResId).create();
        setView(view);
        setCanceledOnTouchOutside(isOutSideCancel);
        if (getWindow() != null) {
            getWindow().setWindowAnimations(dialogAnim);
        }
        this.showDialogWidth = showDialogWidth != null ? showDialogWidth : this.showDialogWidth;
        this.showDialogHeight = showDialogHeight != null ? showDialogHeight : this.showDialogHeight;
        if (ViewGroup.LayoutParams.MATCH_PARENT == this.showDialogHeight) {
            view.setMinimumHeight(AtlwScreenUtils.getInstance().getScreenHeight());
        }
        if (ViewGroup.LayoutParams.MATCH_PARENT == this.showDialogWidth) {
            view.setMinimumWidth(AtlwScreenUtils.getInstance().getScreenWidth());
        }
        if (context.getApplicationContext() != null && context.getApplicationContext() instanceof Application) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (activity == AvlwBaseDialog.this.activity) {
                        dismiss();
                        view = null;
                        ((Application) context.getApplicationContext()).unregisterActivityLifecycleCallbacks(this);
                    }
                }
            });
        }
    }


    @Override
    public void show() {
        super.show();
        showWidthHeightChange(windowGravity, showDialogWidth, showDialogHeight);
    }

    /**
     * 显示时的宽高修改
     *
     * @param gravity 布局位置
     * @param width   显示宽度
     * @param height  显示高度
     */
    public void showWidthHeightChange(int gravity, int width, int height) {
        if (getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.gravity = gravity;
            layoutParams.width = width;
            layoutParams.height = height;
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
            getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public void onBackPressed() {
        if (isShowing()) {
            dismiss();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 设置窗口放置位置
     *
     * @param windowGravity 窗口放置位置
     */
    public void setWindowGravity(int windowGravity) {
        this.windowGravity = windowGravity;
    }

    /**
     * 释放内存
     */
    public void release() {

    }
}
