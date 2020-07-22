package android.lorenwang.customview.dialog;


import android.app.Activity;
import android.lorenwang.customview.R;
import android.view.Gravity;

/**
 * 功能作用：基础中心弹窗dialog
 * 初始注释时间： 2020/7/3 11:02 上午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwBaseCenterDialog extends AvlwBaseDialog {
    public AvlwBaseCenterDialog(Activity context, int dialogViewLayoutResId,
                                boolean isOutSideCancel) {
        super(context, dialogViewLayoutResId, R.style.AvlwLayoutDialogCenter,
                R.style.AvlwAnimDialogCenter, isOutSideCancel, null, null, Gravity.CENTER);
    }

    public AvlwBaseCenterDialog(Activity context, int dialogViewLayoutResId,
                                boolean isOutSideCancel, Integer showDialogWidth,
                                Integer showDialogHeight) {
        super(context, dialogViewLayoutResId, R.style.AvlwLayoutDialogCenter,
                R.style.AvlwLayoutDialogCenter, isOutSideCancel, showDialogWidth,
                showDialogHeight, Gravity.CENTER);
    }
}
