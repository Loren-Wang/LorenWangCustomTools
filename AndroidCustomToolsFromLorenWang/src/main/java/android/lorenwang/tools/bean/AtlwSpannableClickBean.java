package android.lorenwang.tools.bean;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

/**
 * 功能作用：Spannable点击相关数据实体
 * 创建时间：2021-03-01 15:22
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwSpannableClickBean extends ClickableSpan {
    /**
     * 要改变颜色的数据
     */
    private final String paramsMsg;

    /**
     * 要改变后的颜色
     */
    private final Integer color;

    /**
     * 点击事件
     */
    private final View.OnClickListener onClickListener;

    /**
     * 文本大小
     */
    private final Integer textSize;

    /**
     * 实例化
     *
     * @param paramsMsg       要改变颜色的数据
     * @param color           要改变后的颜色
     * @param onClickListener 点击事件
     */
    public AtlwSpannableClickBean(@NonNull String paramsMsg, Integer color, View.OnClickListener onClickListener) {
        this(paramsMsg, color, null, onClickListener);
    }

    /**
     * 实例化
     *
     * @param paramsMsg       要改变颜色的数据
     * @param color           要改变后的颜色
     * @param onClickListener 点击事件
     */
    public AtlwSpannableClickBean(@NonNull String paramsMsg, Integer color, Integer textSize, View.OnClickListener onClickListener) {
        this.paramsMsg = paramsMsg;
        this.textSize = textSize;
        this.color = color;
        this.onClickListener = onClickListener;
    }

    /**
     * 获取要改变颜色的数据
     *
     * @return 要改变颜色的数据
     */
    public String getParamsMsg() {
        return paramsMsg;
    }

    /**
     * 获取颜色，颜色为空则跳过当前
     *
     * @return 颜色
     */
    public Integer getColor() {
        return color;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(@NotNull TextPaint ds) {
        super.updateDrawState(ds);
        if (textSize != null) {
            ds.setTextSize(textSize);
        }
        if (color != null) {
            ds.setColor(color);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }

    }
}
