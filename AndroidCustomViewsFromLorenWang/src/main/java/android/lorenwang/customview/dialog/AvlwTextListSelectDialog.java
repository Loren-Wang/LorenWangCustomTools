package android.lorenwang.customview.dialog;

import android.app.Activity;
import android.lorenwang.customview.R;
import android.lorenwang.customview.radiogroup.AvlwTextListSelectRadioGroup;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 功能作用：文本列表选择弹窗
 * 创建时间：2020-07-22 2:26 下午
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

class AvlwTextListSelectDialog<T extends AvlwTextListSelectRadioGroup.SelectItemBean> extends AvlwBaseBottomDialog {
    /**
     * 标题
     */
    private final AppCompatTextView tvTitle;

    /**
     * 提示
     */
    private final AppCompatTextView tvHint;

    /**
     * 选择列表
     */
    private final AvlwTextListSelectRadioGroup<T> rgSelect;

    /**
     * 当前选择
     */
    private T nowSelect;

    /**
     * 当前类
     */
    private Integer nowType;

    /**
     * 当前回调
     */
    private AvlwTextListSelectRadioGroup.OnSelectChangeListener<T> useSelectListener;

    public AvlwTextListSelectDialog(Activity context) {
        super(context, R.layout.avlw_dialog_text_select, false,
                ViewGroup.LayoutParams.MATCH_PARENT, null);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvHint = view.findViewById(R.id.tvHint);
        //关闭按钮
        AppCompatImageButton imgBtnClose = view.findViewById(R.id.imgBtnClose);
        //确定按钮
        AppCompatButton btnConfirm = view.findViewById(R.id.btnConfirm);
        rgSelect = view.findViewById(R.id.rgSelect);

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //选择回调
        rgSelect.setOnSelectChangeListener(new AvlwTextListSelectRadioGroup.OnSelectChangeListener<T>() {
            @Override
            public void selectChanged(int type, T item) {
                nowSelect = item;
                nowType = type;
            }
        });
        //确定回调
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                if (nowType != null && nowSelect != null && useSelectListener != null) {
                    useSelectListener.selectChanged(nowType, nowSelect);
                }
                dismiss();
                useSelectListener = null;
                nowSelect = null;
                nowType = null;
                v.setEnabled(true);
            }
        });
    }

    @Override
    public void show() {
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
    public void showTextListSelect(@NonNull String title, String hint, int type, List<T> textList,
                                   AvlwTextListSelectRadioGroup.OnSelectChangeListener<T> changeListener,
                                   String defaultSelected, @StyleRes Integer radioButtonStyle) {
        if (hint != null) {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText(hint);
        } else {
            tvHint.setVisibility(View.GONE);
        }
        tvTitle.setText(title);
        this.useSelectListener = changeListener;
        if (textList != null) {
            rgSelect.setList(type, textList, defaultSelected, radioButtonStyle);
        }
        super.show();
    }
}
