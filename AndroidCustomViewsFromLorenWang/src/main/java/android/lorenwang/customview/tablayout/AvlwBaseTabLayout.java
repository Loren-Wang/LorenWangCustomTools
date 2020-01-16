package android.lorenwang.customview.tablayout;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 功能作用：基础tablayout布局接口
 * 创建时间：2020-01-15 15:20
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AvlwBaseTabLayout {

    /**
     * 获取要绘制的宽度
     *
     * @param avlwTabLayout    绘制view
     * @param widthMeasureSpec 绘制view的MeasureSpec
     * @param tabTextListSize  tab文本列表大小
     * @return 要绘制的宽度
     */
    int getMeasureWidth(AvlwTabLayout avlwTabLayout, int widthMeasureSpec, int tabTextListSize);

    /**
     * 获取绘制高度
     *
     * @param avlwTabLayout     绘制view
     * @param heightMeasureSpec 绘制view的MeasureSpec
     * @param tabTextListSize   tab文本列表大小
     * @return 要绘制的高度
     */
    int getMeasureHeight(AvlwTabLayout avlwTabLayout, int heightMeasureSpec, int tabTextListSize);

    /**
     * 子部分绘制
     *
     * @param canvas           画板
     * @param currentPosition  当前位置
     * @param scrollToPosition 目标位置
     * @param lineSlipPercent  移动百分比
     * @param startX           起始点x坐标
     * @param stopX            结束点x坐标
     * @param currentWidth     当前位置宽度
     * @param scrollToWidth    滑动到目标的宽度
     */
    void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition, float lineSlipPercent, float startX, float stopX, float currentWidth, float scrollToWidth);

    /**
     * item数据绘制
     *
     * @param canvas            画板
     * @param textPaint         文本画笔
     * @param drawTextX         文本x坐标
     * @param drawTextY         文本y坐标
     * @param scrollToTextX     滑动目标的文本x坐标
     * @param scrollToTextWidth 滑动到目标的文本宽度
     * @param lineScrollPercent 滑动到目标的滑动百分比
     * @param isCurrent         当前是否是选中的
     * @param textWidth         文本宽度
     * @param textHeight        文本高度
     */
    void drawTypeItem(Canvas canvas, Paint textPaint, float drawTextX, float drawTextY, float scrollToTextX, float scrollToTextWidth, float lineScrollPercent, boolean isCurrent, float textWidth, float textHeight);

}
