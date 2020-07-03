package android.lorenwang.customview.dialog;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.os.Build;
import android.view.Gravity;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;


/**
 * 功能作用：加载中弹窗
 * 创建时间：2018-11-16 下午 15:10:8
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwLoadingDialogType1 extends AvlwBaseDialog {
    /**
     * 是否允许加载中后退退出当前页面
     */
    private boolean allowLoadingBackFinishPage = false;
    private final AppCompatActivity activity;

    public AvlwLoadingDialogType1(AppCompatActivity activity) {
        super(activity, R.layout.avlw_dialog_loading_type_1,
                R.style.avlw_layout_dialog_loading,
                R.style.avlw_anim_dialog_center, false, null, null, Gravity.CENTER);
        this.activity = activity;
    }

    public AvlwLoadingDialogType1(AppCompatActivity activity, @StyleRes int styleRes) {
        super(activity, R.layout.avlw_dialog_loading_type_1, styleRes,
                R.style.avlw_anim_dialog_center, false, null, null, Gravity.CENTER);
        this.activity = activity;
    }

    /**
     * 设置进度条颜色
     *
     * @param color 进度条颜色
     */
    public void setProgressBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ProgressBar) view.findViewById(R.id.progressBar)).setIndeterminateTintList(ColorStateList.valueOf(color));
        } else {
            Drawable drawable = new ColorDrawable(color);
            ((ProgressBar) view.findViewById(R.id.progressBar)).setProgressDrawable(drawable);
        }
    }

    /**
     * 设置进度条颜色
     *
     * @param drawable 进度条颜色
     */
    public void setProgressBarDrawable(Drawable drawable) {
        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgressDrawable(drawable);
    }

    /**
     * 带参数显示弹窗
     *
     * @param allowLoadingBackFinishPage 是否允许加载中后退销毁当前页面
     */
    public void show(boolean allowLoadingBackFinishPage) {
        this.allowLoadingBackFinishPage = allowLoadingBackFinishPage;
        super.show();
    }

    /**
     * 禁用原有的显示方法
     */
    @Override
    public void show() {
    }

    @Override
    public void onBackPressed() {
        if (allowLoadingBackFinishPage && activity != null) {
            activity.finish();
        }
    }

}
