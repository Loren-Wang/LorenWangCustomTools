package android.lorenwang.customview.dialog;

import android.app.Activity;
import android.lorenwang.customview.R;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Guideline;

/**
 * 功能作用：确定取消弹窗
 * 创建时间：2019-02-27 下午 16:04:33
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 1、设置显示文本
 * 2、设置确定取消显示状态
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwConfirmCancelDialog1 extends AvlwBaseDialog {
    /**
     * 内容文本
     */
    protected AppCompatTextView tvContent;
    /**
     * 左侧按钮
     */
    protected AppCompatButton btnLeft;
    /**
     * 右侧按钮
     */
    protected AppCompatButton btnRight;
    /**
     * 分割线
     */
    protected Guideline glCenter;
    /**
     * 按钮之间的分割线
     */
    protected View viewBtnLine;

    public AvlwConfirmCancelDialog1(Activity context) {
        super(context, R.layout.avlw_dialog_confirm_cancel_1,
                R.style.AvlwLayoutDialogCenter,
                R.style.AvlwAnimDialogCenter,
                false, null,
                context.getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.avlw_dialog_common_width),
                Gravity.CENTER);
        tvContent = view.findViewById(R.id.tvContent);
        btnLeft = view.findViewById(R.id.btnLeft);
        btnRight = view.findViewById(R.id.btnRight);
        glCenter = view.findViewById(R.id.glCenter);
        viewBtnLine = view.findViewById(R.id.viewBtnLine);
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
    public AvlwConfirmCancelDialog1 setBtnLeft(String text, Integer size, Integer color,
                                               View.OnClickListener onClickListener) {
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
    public AvlwConfirmCancelDialog1 setBtnRight(String text, Integer size, Integer color,
                                                View.OnClickListener onClickListener) {
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
    public AvlwConfirmCancelDialog1 setOptionsState(boolean isShowBtnLeft, boolean isShowBtnRight
            , Integer optionsHeight) {
        if (!isShowBtnLeft || !isShowBtnRight) {
            glCenter.setGuidelinePercent(1);
            viewBtnLine.setVisibility(View.GONE);
        } else {
            glCenter.setGuidelinePercent(0.5f);
            viewBtnLine.setVisibility(View.VISIBLE);
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
            //只要修改左高度就好，其他的数据以左高度为准
            btnLeft.setHeight(optionsHeight);
        }
        return this;
    }

    @Override
    public void onBackPressed() {
    }
}
