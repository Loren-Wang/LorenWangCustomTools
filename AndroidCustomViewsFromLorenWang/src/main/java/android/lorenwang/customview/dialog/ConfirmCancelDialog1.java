package android.lorenwang.customview.dialog;

import android.content.Context;
import android.lorenwang.customview.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ConfirmCancelDialog1 extends BaseDialog {
    private TextView tvContent;
    private Button btnLeft;
    private View viewLine;
    private Button btnRight;
    private LinearLayout lnOptions;

    public ConfirmCancelDialog1(Context context) {
        super(context, R.layout.dialog_confirm_cancel_1, R.style.dialog_confirm_cancel_1, R.style.dialog_anim_for_center, false);
        tvContent = view.findViewById(R.id.tvContent);
        btnLeft = view.findViewById(R.id.btnLeft);
        viewLine = view.findViewById(R.id.viewLine);
        btnRight = view.findViewById(R.id.btnRight);
        lnOptions = view.findViewById(R.id.lnOptions);
    }

    /**
     * 设置内容
     *
     * @param text
     */
    public void setContent(String text, Integer size, Integer color) {
        if (text != null) {
            tvContent.setText(text);
        }
        if (size != null) {
            tvContent.setTextSize(size);
        }
        if (color != null) {
            tvContent.setTextColor(color);
        }
    }

    /**
     * 设置左侧按钮
     *
     * @param text
     * @param onClickListener
     */
    public void setBtnLeft(String text, Integer size, Integer color, View.OnClickListener onClickListener) {
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
    }

    /**
     * 设置右侧按钮
     *
     * @param text
     * @param onClickListener
     */
    public void setBtnRight(String text, Integer size, Integer color, View.OnClickListener onClickListener) {
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
    }

    /**
     * 设置按钮显示状态
     *
     * @param isShowBtnLeft
     * @param isShowBtnRight
     */
    public void setOptionsState(boolean isShowBtnLeft, boolean isShowBtnRight,Integer optionsHeight) {
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
        if(optionsHeight != null){
            ViewGroup.LayoutParams layoutParams = lnOptions.getLayoutParams();
            if(layoutParams != null){
                layoutParams.height = optionsHeight;
            }else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,optionsHeight);
            }
            lnOptions.setLayoutParams(layoutParams);
        }
    }
}
