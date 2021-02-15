package android.lorenwang.customview.recycleview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：通用垂直分隔线
 * 创建时间：2020-09-20 4:22 下午
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
public class AvlwCommonVerticalItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 画笔
     */
    private final Paint paint = new Paint();
    /**
     * 分隔线高度
     */
    private final Float divideHeight;

    /**
     * 分隔线左间距
     */
    private final Float divideLeftDistance;
    /**
     * 分隔线右间距
     */
    private final Float divideRightDistance;
    /**
     * 顶部行上间距
     */
    private final Float rowTopDistance;
    /**
     * 底部行下间距
     */
    private final Float rowBottomDistance;
    /**
     * 顶部底部行上下左间距
     */
    private final Float rowTopBottomLeftDistance;
    /**
     * 顶部底部行上下右间距
     */
    private final Float rowTopBottomRightDistance;

    /**
     * 顶部颜色
     */
    private final int rowTopBgColor;

    /**
     * 底部颜色
     */
    private final int rowBottomBgColor;

    /**
     * 分隔线颜色
     */
    private final int divideColor;

    /**
     * 构造函数
     *
     * @param color        颜色
     * @param divideHeight 高度
     */
    public AvlwCommonVerticalItemDecoration(Integer color, Float divideHeight) {
        this(color, divideHeight, 0F, 0F);
    }


    /**
     * 构造函数
     *
     * @param color               颜色
     * @param divideHeight        高度
     * @param divideLeftDistance  分隔线左间距
     * @param divideRightDistance 分隔线右间距
     */
    public AvlwCommonVerticalItemDecoration(Integer color, Float divideHeight,
                                            Float divideLeftDistance, Float divideRightDistance) {
        this(color, divideHeight, divideLeftDistance, divideRightDistance, 0F, 0F, 0F, 0F, null, null);
    }

    /**
     * 构造函数
     *
     * @param color                     颜色
     * @param divideHeight              高度
     * @param divideLeftDistance        分隔线左间距
     * @param divideRightDistance       分隔线右间距
     * @param rowTopDistance            顶部行上间距
     * @param rowBottomDistance         底部行下间距
     * @param rowTopBottomLeftDistance  顶部底部行上下左间距
     * @param rowTopBottomRightDistance 顶部底部行上下右间距
     */
    public AvlwCommonVerticalItemDecoration(Integer color, Float divideHeight,
                                            Float divideLeftDistance, Float divideRightDistance,
                                            Float rowTopDistance, Float rowBottomDistance,
                                            Float rowTopBottomLeftDistance,
                                            Float rowTopBottomRightDistance,
                                            Integer rowTopBgColor,
                                            Integer rowBottomBgColor) {
        paint.setAntiAlias(true);
        if (color != null) {
            divideColor = color;
        } else {
            divideColor = Color.TRANSPARENT;
        }
        this.divideHeight = divideHeight == null ? AtlwScreenUtil.getInstance().dip2px(16F) : divideHeight;
        this.divideLeftDistance = divideLeftDistance != null ? divideLeftDistance : 0;
        this.divideRightDistance = divideRightDistance != null ? divideRightDistance : 0;
        this.rowTopDistance = rowTopDistance != null ? rowTopDistance : 0;
        this.rowBottomDistance = rowBottomDistance != null ? rowBottomDistance : 0;
        this.rowTopBottomLeftDistance = rowTopBottomLeftDistance != null ?
                rowTopBottomLeftDistance : 0;
        this.rowTopBottomRightDistance = rowTopBottomRightDistance != null ?
                rowTopBottomRightDistance : 0;
        this.rowTopBgColor = rowTopBgColor != null ?
                rowTopBgColor : divideColor;
        this.rowBottomBgColor = rowBottomBgColor != null ?
                rowBottomBgColor : divideColor;
    }


    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view,
                               @NonNull @NotNull RecyclerView parent,
                               @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = divideHeight.intValue();
        if (((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition() == 0) {
            outRect.top = rowTopDistance.intValue();
        } else if (((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition() ==
                parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = rowBottomDistance.intValue();
        }
    }

    @Override
    public void onDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent,
                       @NonNull @NotNull RecyclerView.State state) {
        int childCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        if (childCount > 0) {
            //绘制最顶部的top
            View view = parent.getChildAt(0);
            if (view != null) {
                //画顶部
                paint.setColor(rowTopBgColor);
                c.drawRect(left + rowTopBottomLeftDistance, view.getTop() - rowTopDistance,
                        right - rowTopBottomRightDistance,
                        view.getTop(), paint);
            }
            //绘制其他
            paint.setColor(divideColor);
            for (int i = 0; i < childCount - 1; i++) {
                view = parent.getChildAt(i);
                if (view == null) {
                    continue;
                }
                c.drawRect(left + divideLeftDistance, view.getBottom(),
                        right - divideRightDistance, view.getBottom() + divideHeight, paint);
            }
            //绘制最底部的top
            paint.setColor(rowBottomBgColor);
            view = parent.getChildAt(childCount - 1);
            if (view != null) {
                //画顶部
                c.drawRect(left + rowTopBottomLeftDistance, view.getBottom(),
                        right - rowTopBottomRightDistance,
                        view.getBottom() + rowBottomDistance, paint);
            }
        }
    }

    public Float getDivideHeight() {
        return divideHeight;
    }
}
