package android.lorenwang.customview.radiogroup;

import android.content.Context;
import android.lorenwang.customview.R;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import java.util.List;

import androidx.annotation.StyleRes;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatRadioButton;

/**
 * 功能作用：文本列表单选控件
 * 创建时间：2020-07-22 2:09 下午
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

public class AvlwTextListSelectRadioGroup<T extends AvlwTextListSelectRadioGroup.SelectItemBean> extends RadioGroup {
    private int type = 0;

    /**
     * 选择回调
     */
    private OnSelectChangeListener<T> onSelectChangeListener;

    /**
     * {@inheritDoc}
     *
     * @param context
     */
    public AvlwTextListSelectRadioGroup(Context context) {
        super(context);
        init();
    }

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param attrs
     */
    public AvlwTextListSelectRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化相关
     */
    private void init() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (onSelectChangeListener != null && group.getChildAt(checkedId) != null
                        && group.getChildAt(checkedId).getTag() != null) {
                    onSelectChangeListener.selectChanged(type,
                            (T) group.getChildAt(checkedId).getTag());
                }
            }
        });
    }

    /**
     * 设置文本列表
     *
     * @param type             文本列表类型
     * @param textList         文本列表
     * @param defaultSelected  默认选中文本
     * @param radioButtonStyle 选择radioButton样式
     */
    public void setList(int type, List<T> textList, String defaultSelected,
                        @StyleRes Integer radioButtonStyle) {
        if (radioButtonStyle == null) {
            radioButtonStyle = R.style.AvlwRadioButtonTextListSelect;
        }
        this.type = type;
        removeAllViews();
        AppCompatRadioButton rbSelect;
        T item;
        for (int i = 0; i < textList.size(); i++) {
            item = textList.get(i);
            if (item.getText() == null || item.getText().trim().length() == 0) {
                return;
            }
            rbSelect = new AppCompatRadioButton(new ContextThemeWrapper(getContext(),
                    radioButtonStyle));
            rbSelect.setText(item.getText());
            if (defaultSelected != null && TextUtils.equals(defaultSelected, item.getText())) {
                rbSelect.setChecked(true);
                if (onSelectChangeListener != null) {
                    onSelectChangeListener.selectChanged(type, item);
                }
            } else {
                rbSelect.setChecked(false);
            }
            rbSelect.setTag(item);
            rbSelect.setId(i);
            addView(rbSelect);
        }
    }

    public void setOnSelectChangeListener(OnSelectChangeListener<T> onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    /**
     * 功能作用：选中改变监听
     * 初始注释时间： 2020/4/1 4:13 下午
     * 注释创建人：LorenWang（王亮）
     * 方法介绍：
     * 思路：
     * 修改人：
     * 修改时间：
     * 备注：
     */
    public interface OnSelectChangeListener<T extends SelectItemBean> {
        /**
         * 选中改变回调
         *
         * @param type 当前列表类型
         * @param item 当前选中文本
         */
        void selectChanged(int type, T item);
    }

    /**
     * 功能作用：选择item的Bean
     * 初始注释时间： 2020/4/21 4:53 下午
     * 注释创建人：LorenWang（王亮）
     * 方法介绍：
     * 思路：
     * 修改人：
     * 修改时间：
     * 备注：
     *
     * @author LorenWang（王亮）
     */
    public static class SelectItemBean {
        /**
         * 文本
         */
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

}
