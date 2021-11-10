package com.lorenwang.test.android.activity.customView.dialog;

import android.app.Activity;
import android.lorenwang.customview.dialog.AvlwTextListSelectDialog;
import android.lorenwang.customview.radiogroup.AvlwTextListSelectRadioGroup;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.lorenwang.test.android.R;
import com.lorenwang.test.android.bean.dialog.TextSelectBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 功能作用：文本列表选择弹窗
 * 创建时间：2020-07-24 10:56 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
public class TextSelectDialog extends AvlwTextListSelectDialog<TextSelectBean> {
    /**
     * 标题
     */
    private final AppCompatTextView tvTitle;

    /**
     * 提示
     */
    private final AppCompatTextView tvHint;

    public TextSelectDialog(Activity context) {
        super(context, R.layout.avlw_dialog_text_select, android.lorenwang.customview.R.style.AvlwLayoutDialogBottom,
                android.lorenwang.customview.R.style.AvlwAnimDialogBottom, false, ViewGroup.LayoutParams.MATCH_PARENT, null, Gravity.BOTTOM);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvHint = view.findViewById(R.id.tvHint);
        //关闭按钮
        AppCompatImageButton imgBtnClose = view.findViewById(R.id.imgBtnClose);
        imgBtnClose.setOnClickListener(v -> dismiss());
        //确定按钮
        AppCompatButton btnConfirm = view.findViewById(R.id.btnConfirm);
        //选择内容
        setRgSelect(view.findViewById(R.id.rgSelect));
        //确定回调
        btnConfirm.setOnClickListener(v -> {
            v.setEnabled(false);
            callbackSelect();
            dismiss();
            clear();
            v.setEnabled(true);
        });
    }

    /**
     * 显示文本选择
     *
     * @param title            标题
     * @param type             文本列表类型
     * @param textList         文本选择列表
     * @param changeListener   改变监听
     * @param defaultSelected  默认选中的文本
     * @param radioButtonStyle 选择radioButton样式
     */
    public void showTextListSelect(@NonNull String title, String hint, int type, List<TextSelectBean> textList,
            AvlwTextListSelectRadioGroup.OnSelectChangeListener<TextSelectBean> changeListener, String defaultSelected,
            @StyleRes Integer radioButtonStyle) {
        if (hint != null) {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText(hint);
        } else {
            tvHint.setVisibility(View.GONE);
        }
        tvTitle.setText(title);
        super.showTextListSelect(type, textList, changeListener, defaultSelected, radioButtonStyle);
        super.show();
    }
}
