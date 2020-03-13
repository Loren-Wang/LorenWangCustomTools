package android.lorenwang.customview.dialog;

import android.content.Context;
import android.lorenwang.customview.R;

/**
 * 功能作用：基础底部弹窗dialog
 * 创建时间：2020-01-13 15:51
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwBaseBottomDialog extends AvlwBaseDialog {
    public AvlwBaseBottomDialog(Context context, int dialogViewLayoutResId, boolean isOutSideCancel) {
        super(context, dialogViewLayoutResId, R.style.avlw_layout_dialog_bottom,
                R.style.avlw_anim_dialog_bottom, isOutSideCancel, true, false);
    }
}
