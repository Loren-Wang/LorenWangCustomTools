package android.lorenwang.customview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

/**
 * 创建时间：2018-11-16 下午 15:11:28
 * 创建人：王亮（Loren wang）
 * 功能作用：${DESCRIPTION}
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwBaseDialog extends AlertDialog {
    protected final String TAG = getClass().getName();
    protected View view;
    protected boolean isFullWidthShow = false;//是否宽度全屏显示
    protected boolean isFullHeightShow = false;//是否高度全屏显示

    protected AvlwBaseDialog(Context context) {
        super(context);
    }

    protected AvlwBaseDialog(Context context, int style) {
        super(context, style);
    }

    public AvlwBaseDialog(Context context, @LayoutRes int dialogViewLayoutResId
            , @StyleRes int modelStyleResId, @StyleRes int dialogAnimo
            , boolean isOutSideCancel, boolean isFullWidthShow, boolean isFullHeightShow) {
        super(context, modelStyleResId);
        view = LayoutInflater.from(context).inflate(dialogViewLayoutResId, null);
        new Builder(context, modelStyleResId).create();
        setView(view);
        setCanceledOnTouchOutside(isOutSideCancel);
        getWindow().setWindowAnimations(dialogAnimo);
        this.isFullWidthShow = isFullWidthShow;
        this.isFullHeightShow = isFullHeightShow;
    }


    @Override
    public void show() {
        super.show();
        if (isFullWidthShow || isFullHeightShow) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = isFullWidthShow ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = isFullHeightShow ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
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
     * 释放内存
     */
    public void release() {

    }
}
