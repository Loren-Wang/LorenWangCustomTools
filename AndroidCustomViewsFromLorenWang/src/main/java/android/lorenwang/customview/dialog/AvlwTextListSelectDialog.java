package android.lorenwang.customview.dialog;

import android.app.Activity;
import android.lorenwang.customview.radiogroup.AvlwTextListSelectRadioGroup;

import java.util.List;

import androidx.annotation.StyleRes;

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
public abstract class AvlwTextListSelectDialog<T extends AvlwTextListSelectRadioGroup.SelectItemBean> extends AvlwBaseDialog {

    /**
     * 选择列表
     */
    private AvlwTextListSelectRadioGroup rgSelect;

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

    protected AvlwTextListSelectDialog(Activity context) {
        super(context);
    }

    protected AvlwTextListSelectDialog(Activity context, int style) {
        super(context, style);
    }

    public AvlwTextListSelectDialog(Activity context, int dialogViewLayoutResId, int modelStyleResId, int dialogAnim, boolean isOutSideCancel,
            Integer showDialogWidth, Integer showDialogHeight, Integer windowGravity) {
        super(context, dialogViewLayoutResId, modelStyleResId, dialogAnim, isOutSideCancel, showDialogWidth, showDialogHeight, windowGravity);
    }

    @Override
    public void show() {
    }

    /**
     * 设置选择控件
     *
     * @param rgSelect 选择控件
     */
    public void setRgSelect(AvlwTextListSelectRadioGroup rgSelect) {
        this.rgSelect = rgSelect;
        //选择回调
        rgSelect.setOnSelectChangeListener((type, item) -> {
            nowSelect = (T) item;
            nowType = type;
        });
    }

    /**
     * 显示文本选择
     *
     * @param type             文本列表类型(回传的时候自己判断的)
     * @param textList         文本选择列表
     * @param changeListener   改变监听
     * @param defaultSelected  默认选中的文本
     * @param radioButtonStyle 选择radioButton样式
     */
    public void showTextListSelect(int type, List<T> textList, AvlwTextListSelectRadioGroup.OnSelectChangeListener<T> changeListener,
            String defaultSelected, @StyleRes Integer radioButtonStyle) {
        this.useSelectListener = changeListener;
        if (textList != null) {
            rgSelect.setList(type, textList, defaultSelected, radioButtonStyle);
        }
        super.show();
    }


    /**
     * 清除配置信息
     */
    public void clear() {
        useSelectListener = null;
        nowSelect = null;
        nowType = null;
    }

    /**
     * 回调选择
     */
    public void callbackSelect() {
        if (nowType != null && nowSelect != null && useSelectListener != null) {
            useSelectListener.selectChanged(nowType, nowSelect);
        }
    }
}
