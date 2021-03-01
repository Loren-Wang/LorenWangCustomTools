package android.lorenwang.tools.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.text.style.ReplacementSpan;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

/**
 * 功能作用：SpannableTag标签相关数据实体
 * 初始注释时间： 2020/8/16 10:01 上午
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
public class AvlwSpannableTagBean extends ReplacementSpan {
    /**
     * 要改变颜色的数据
     */
    private final String paramsMsg;
    /**
     * 背景颜色
     */
    private final Integer bgColor;
    /**
     * 文本颜色
     */
    private final Integer textColor;
    /**
     * 边框颜色
     */
    private final Integer borderColor;
    /**
     * 边框宽度
     */
    private int borderWidth = 1;
    /**
     * 文本大小
     */
    private Float textSize;
    /**
     * tag和文本左侧间距
     */
    private float tagTextDistanceLeft = AtlwScreenUtil.getInstance().dip2px(2);
    /**
     * tag和文本右侧间距
     */
    private float tagTextDistanceRight = AtlwScreenUtil.getInstance().dip2px(2);
    /**
     * tag内部左右边距
     */
    private float tagPaddingDistanceLeftRight = AtlwScreenUtil.getInstance().dip2px(6);
    /**
     * tag内容步上边距
     */
    private float tagPaddingDistanceTop = AtlwScreenUtil.getInstance().dip2px(1.8F);
    /**
     * tag内容步下边距
     */
    private float tagPaddingDistanceBottom = AtlwScreenUtil.getInstance().dip2px(1.8F);
    /**
     * 标签圆角度数
     */
    private float tagRadio = AtlwScreenUtil.getInstance().dip2px(4);

    /**
     * 构造初始化
     *
     * @param paramsMsg   标签文本
     * @param bgColor     背景颜色
     * @param textColor   文本颜色
     * @param borderColor 边框颜色
     * @param tagRadio    标签圆角度数
     */
    public AvlwSpannableTagBean(String paramsMsg, int bgColor, int textColor, int borderColor, int borderWidth, Float tagRadio, Float textSize) {
        this(paramsMsg, bgColor, textColor, borderColor, borderWidth, false, textSize, null, null, null, null, null, tagRadio);
    }


    /**
     * 构造初始化
     *
     * @param paramsMsg                   标签文本
     * @param bgColor                     背景颜色
     * @param textColor                   文本颜色
     * @param borderColor                 边框颜色
     * @param isSquare                    是否是正方形，是的话跳转内部左右边距，当前不一定会是绝对的正方形,当为正方形时取内边距的最小值
     * @param textSize                    文本大小
     * @param tagTextDistanceLeft         文本标签外部左边距
     * @param tagTextDistanceRight        文本标签外部有边距
     * @param tagPaddingDistanceLeftRight 文本标签内部左右边距
     * @param tagPaddingDistanceTop       文本标签内部上边距
     * @param tagPaddingDistanceBottom    文本标签内容步下边距
     * @param tagRadio                    标签圆角度数
     */
    public AvlwSpannableTagBean(@NotNull String paramsMsg, int bgColor, int textColor, int borderColor, Integer borderWidth, boolean isSquare,
            Float textSize, Float tagTextDistanceLeft, Float tagTextDistanceRight, Float tagPaddingDistanceLeftRight, Float tagPaddingDistanceTop,
            Float tagPaddingDistanceBottom, Float tagRadio) {
        this.paramsMsg = paramsMsg;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth != null ? borderWidth : this.borderWidth;
        this.textSize = textSize;
        this.tagTextDistanceLeft = tagTextDistanceLeft == null ? this.tagTextDistanceLeft : tagTextDistanceLeft;
        this.tagTextDistanceRight = tagTextDistanceRight == null ? this.tagTextDistanceRight : tagTextDistanceRight;
        this.tagPaddingDistanceLeftRight = tagPaddingDistanceLeftRight == null ? this.tagPaddingDistanceLeftRight : tagPaddingDistanceLeftRight;
        this.tagPaddingDistanceTop = tagPaddingDistanceTop == null ? this.tagPaddingDistanceTop : tagPaddingDistanceTop;
        this.tagPaddingDistanceBottom = tagPaddingDistanceBottom == null ? this.tagPaddingDistanceBottom : tagPaddingDistanceBottom;
        this.tagRadio = tagRadio == null ? this.tagRadio : tagRadio;

        //如果是正方形则去内边距最小值
        if (isSquare) {
            this.tagPaddingDistanceLeftRight = this.tagPaddingDistanceBottom = this.tagPaddingDistanceTop = Math.min(
                    Math.min(this.tagPaddingDistanceBottom, this.tagPaddingDistanceTop), this.tagPaddingDistanceLeftRight);
            //方形情况下文本上下有一定的小边距，所以要乘以1.5尽量保证是方形内边距相同
            this.tagPaddingDistanceLeftRight *= 1.5;
        }
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        textSize = textSize == null ? paint.getTextSize() - AtlwScreenUtil.getInstance().sp2px(2) : textSize;
        paint.setTextSize(textSize);
        return (int) (paint.measureText(text, start, end) + tagPaddingDistanceLeftRight * 2 + tagTextDistanceLeft + tagTextDistanceRight);
    }

    public String getParamsMsg() {
        return paramsMsg;
    }

    public Integer getBgColor() {
        return bgColor;
    }

    public Integer getTextColor() {
        return textColor;
    }


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        //文本原始颜色
        int originalColor = paint.getColor();
        //文本原始大小
        float originalTextSize = paint.getTextSize();
        //开始绘制背景，设置背景颜色
        paint.setColor(this.bgColor);
        //抗锯齿
        paint.setAntiAlias(true);
        //设置文本大小
        paint.setTextSize(textSize);
        //绘制背景
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(x + tagTextDistanceLeft + borderWidth, y + paint.getFontMetrics().top - tagPaddingDistanceTop - borderWidth,
                x + paint.measureText(text, start, end) + tagPaddingDistanceLeftRight * 2 + tagTextDistanceLeft - borderWidth,
                y + paint.getFontMetrics().descent + tagPaddingDistanceBottom - borderWidth), tagRadio, tagRadio, paint);
        //绘制边框
        paint.setColor(this.borderColor);
        //设置为边框
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(new RectF(x + tagTextDistanceLeft, y + paint.getFontMetrics().top - tagPaddingDistanceTop,
                x + paint.measureText(text, start, end) + tagPaddingDistanceLeftRight * 2 + tagTextDistanceLeft,
                y + paint.getFontMetrics().descent + tagPaddingDistanceBottom), tagRadio, tagRadio, paint);
        //开始设置文本颜色
        paint.setColor(this.textColor);
        //不使用加粗
        paint.setFakeBoldText(false);
        //画文字,两边各增加8dp
        paint.setStyle(Paint.Style.FILL);
        //绘制文本
        canvas.drawText(text, start, end, x + tagPaddingDistanceLeftRight + tagTextDistanceLeft, y, paint);
        //将paint复原
        paint.setColor(originalColor);
        paint.setTextSize(originalTextSize);
    }
}
