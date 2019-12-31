package android.lorenwang.customview.dialog;

import android.content.Context;
import android.lorenwang.customview.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 创建时间：2019-02-27 下午 16:04:33
 * 创建人：王亮（Loren wang）
 * 功能作用：确定取消弹窗
 * 思路：
 * 方法：1、设置显示文本
 * 2、设置确定取消显示状态
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwConfirmCancelDialog1 extends AvlwBaseDialog {
    private AppCompatTextView tvContent;
    private AppCompatButton btnLeft;
    private View viewLine;
    private AppCompatButton btnRight;
    private LinearLayout lnOptions;

    public AvlwConfirmCancelDialog1(Context context) {
        super(context, R.layout.avlw_dialog_confirm_cancel_1, R.style.avlw_dialog_confirm_cancel_1
                , R.style.avlw_dialog_anim_for_center, false, false, false);
        tvContent = view.findViewById(R.id.tvContent);
        btnLeft = view.findViewById(R.id.btnLeft);
        viewLine = view.findViewById(R.id.viewLine);
        btnRight = view.findViewById(R.id.btnRight);
        lnOptions = view.findViewById(R.id.lnOptions);
    }

    /**
     * 设置内容
     *
     * @param text 内容文本
     * @return 当前实例
     */
    public AvlwConfirmCancelDialog1 setContent(String text, Integer size, Integer color) {
        if (text != null) {
            tvContent.setText(text);
        }
        if (size != null) {
            tvContent.setTextSize(size);
        }
        if (color != null) {
            tvContent.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置左侧按钮
     *
     * @param text            左侧按钮文本
     * @param onClickListener 点击监听
     * @return 当前实例
     */
    public AvlwConfirmCancelDialog1 setBtnLeft(String text, Integer size, Integer color, View.OnClickListener onClickListener) {
        if (text != null) {
            btnLeft.setText(text);
        }
        if (size != null) {
            btnLeft.setTextSize(size);
        }
        if (color != null) {
            btnLeft.setTextColor(color);
        }
        btnLeft.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置右侧按钮
     *
     * @param text            右侧按钮文本
     * @param onClickListener 点击监听
     * @return 当前实例
     */
    public AvlwConfirmCancelDialog1 setBtnRight(String text, Integer size, Integer color, View.OnClickListener onClickListener) {
        if (text != null) {
            btnRight.setText(text);
        }
        if (size != null) {
            btnRight.setTextSize(size);
        }
        if (color != null) {
            btnRight.setTextColor(color);
        }
        btnRight.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置按钮显示状态
     *
     * @param isShowBtnLeft  是否显示左侧按钮
     * @param isShowBtnRight 是否显示右侧按钮
     * @return 当前实例
     */
    public AvlwConfirmCancelDialog1 setOptionsState(boolean isShowBtnLeft, boolean isShowBtnRight, Integer optionsHeight) {
        if (!isShowBtnLeft || !isShowBtnRight) {
            viewLine.setVisibility(View.GONE);
        } else {
            viewLine.setVisibility(View.VISIBLE);
        }
        if (isShowBtnLeft) {
            btnLeft.setVisibility(View.VISIBLE);
        } else {
            btnLeft.setVisibility(View.GONE);
        }
        if (isShowBtnRight) {
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.GONE);
        }
        if (optionsHeight != null) {
            ViewGroup.LayoutParams layoutParams = lnOptions.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = optionsHeight;
            } else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, optionsHeight);
            }
            lnOptions.setLayoutParams(layoutParams);
        }
        return this;
    }
}
