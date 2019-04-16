package android.lorenwang.customview.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.widget.ProgressBar;


/**
 * 创建时间：2018-11-16 下午 15:10:8
 * 创建人：王亮（Loren wang）
 * 功能作用：加载中弹窗
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class LoadingDialogType1 extends BaseDialog {

    public LoadingDialogType1(Context context) {
        super(context, R.layout.dialog_loading_type_1, R.style.loading_dialog_type1, R.style.dialog_anim_for_center, false, false, false);
    }

    public LoadingDialogType1(Context context, @StyleRes int styleRes) {
        super(context, R.layout.dialog_loading_type_1, styleRes, R.style.dialog_anim_for_center, false, false, false);
    }

    /**
     * 设置进度条颜色
     *
     * @param color
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
     * @param drawable
     */
    public void setProgressBarDrawable(Drawable drawable) {
        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgressDrawable(drawable);
    }

    @Override
    public void onBackPressed() {

    }

}
