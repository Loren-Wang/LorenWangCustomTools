package android.lorenwang.customview.recycleview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：宫格列表通用分隔线
 * 创建时间：2020-09-20 3:03 下午
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
public class AvlwCommonGridItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 画笔
     */
    private final Paint paint = new Paint();
    /**
     * 宫格总列数
     */
    private final int gridSpanCount;
    /**
     * 左间距
     */
    private final Float leftDistance;
    /**
     * 上间距
     */
    private final Float topDistance;
    /**
     * 右间距
     */
    private final Float rightDistance;
    /**
     * 下间距
     */
    private final Float bottomDistance;
    /**
     * 顶部行上间距
     */
    private final Float rowTopDistance;
    /**
     * 底部行下间距
     */
    private final Float rowBottomDistance;

    /**
     * 构造函数
     *
     * @param color          分隔线颜色
     * @param gridSpanCount  宫格列数
     * @param leftDistance   左间距
     * @param topDistance    上间距
     * @param rightDistance  右间距
     * @param bottomDistance 下间距
     */
    public AvlwCommonGridItemDecoration(Integer color, int gridSpanCount,
                                        float leftDistance, float topDistance,
                                        float rightDistance, float bottomDistance,
                                        float rowTopDistance, float rowBottomDistance) {
        paint.setAntiAlias(true);
        if (color != null) {
            paint.setColor(color);
        } else {
            paint.setColor(Color.TRANSPARENT);
        }
        this.gridSpanCount = gridSpanCount;
        this.leftDistance = leftDistance;
        this.topDistance = topDistance;
        this.rightDistance = rightDistance;
        this.bottomDistance = bottomDistance;
        this.rowTopDistance = rowTopDistance;
        this.rowBottomDistance = rowBottomDistance;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view,
                               @NonNull @NotNull RecyclerView parent,
                               @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int count = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        //总行数
        int sumRows = count % gridSpanCount == 0 ? count / gridSpanCount :
                (count / gridSpanCount) + 1;
        //当前位置
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int column = position % gridSpanCount;
        int row = position / gridSpanCount;

        if (row == 0 && column == 0) {
            outRect.top = rowTopDistance.intValue();
            outRect.bottom = bottomDistance.intValue();
            outRect.right = rightDistance.intValue();
        } else if (row == 0 && column == gridSpanCount - 1) {
            outRect.top = rowTopDistance.intValue();
            outRect.bottom = bottomDistance.intValue();
            outRect.left = leftDistance.intValue();
        } else if (row == 0) {
            outRect.top = rowTopDistance.intValue();
            outRect.bottom = bottomDistance.intValue();
            outRect.right = rightDistance.intValue();
            outRect.left = leftDistance.intValue();
        } else if (row == sumRows - 1 && column == 0) {
            outRect.bottom = rowBottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.right = rightDistance.intValue();
        } else if (row == sumRows - 1 && column == gridSpanCount - 1) {
            outRect.bottom = rowBottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.left = leftDistance.intValue();
        } else if (row == sumRows - 1) {
            outRect.bottom = rowBottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.right = rightDistance.intValue();
            outRect.left = leftDistance.intValue();
        } else if (column == 0) {
            outRect.bottom = bottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.right = rightDistance.intValue();
        } else if (column == gridSpanCount - 1) {
            outRect.bottom = bottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.left = leftDistance.intValue();
        } else {
            outRect.bottom = bottomDistance.intValue();
            outRect.top = topDistance.intValue();
            outRect.right = rightDistance.intValue();
            outRect.left = leftDistance.intValue();
        }
    }

    @Override
    public void onDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent,
                       @NonNull @NotNull RecyclerView.State state) {

        int childCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        //总行数
        int sumRows = childCount % gridSpanCount == 0 ? childCount / gridSpanCount :
                (childCount / gridSpanCount) + 1;

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view == null) {
                continue;
            }
            int column = i % gridSpanCount;
            int row = i / gridSpanCount;
            if (row == 0 && column == 0) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, bottomDistance);
                drawRight(c, view, true, false, rowTopDistance, bottomDistance);
            } else if (row == 0 && column == gridSpanCount - 1) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, bottomDistance);
                drawLeft(c, view, true, false, rowTopDistance, bottomDistance);
            } else if (row == 0) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, bottomDistance);
                drawRight(c, view, true, false, rowTopDistance, bottomDistance);
                drawLeft(c, view, true, false, rowTopDistance, bottomDistance);
            } else if (row == sumRows - 1 && column == 0) {
                drawTop(c, view, topDistance);
                drawBottom(c, view, rowBottomDistance);
                drawRight(c, view, false, true, topDistance, rowBottomDistance);
            } else if (row == sumRows - 1 && column == gridSpanCount - 1) {
                drawTop(c, view, topDistance);
                drawBottom(c, view, rowBottomDistance);
                drawLeft(c, view, false, true, topDistance, rowBottomDistance);
            } else if (row == sumRows - 1) {
                drawTop(c, view, topDistance);
                drawBottom(c, view, rowBottomDistance);
                drawLeft(c, view, false, true, topDistance, rowBottomDistance);
                drawRight(c, view, false, true, topDistance, rowBottomDistance);
            } else if (column == 0) {
                drawTop(c, view, topDistance);
                drawBottom(c, view, bottomDistance);
                drawRight(c, view, true, true, topDistance, bottomDistance);
            } else if (column == gridSpanCount - 1) {
                drawTop(c, view, topDistance);
                drawBottom(c, view, bottomDistance);
                drawLeft(c, view, true, true, topDistance, bottomDistance);
            } else {
                drawTop(c, view, topDistance);
                drawBottom(c, view, bottomDistance);
                drawLeft(c, view, true, true, topDistance, bottomDistance);
                drawRight(c, view, true, true, topDistance, bottomDistance);
            }
        }
    }

    /**
     * 画右侧
     *
     * @param c                  画板
     * @param view               视图控件
     * @param showBottomDistance 是否要显示底部，要的话要绘制右下角
     * @param showTopDistance    是否要显示顶部，要的话要绘制右上角
     */
    private void drawRight(@NotNull @NonNull Canvas c, View view,
                           boolean showBottomDistance, boolean showTopDistance,
                           float topDistance, float bottomDistance) {
        c.drawRect(view.getRight(),
                showTopDistance ? view.getTop() - view.getPaddingTop() - topDistance :
                        view.getTop() - view.getPaddingTop(),
                view.getRight() + rightDistance,
                showBottomDistance ? view.getBottom() - view.getPaddingBottom() + bottomDistance :
                        view.getBottom() - view.getPaddingBottom(),
                paint);
    }

    /**
     * 画左侧
     *
     * @param c                  画板
     * @param view               视图控件
     * @param showBottomDistance 是否要显示底部，要的话要绘制左下角
     * @param showTopDistance    是否要显示顶部，要的话要绘制左上角
     */
    private void drawLeft(@NotNull @NonNull Canvas c, View view,
                          boolean showBottomDistance, boolean showTopDistance,
                          float topDistance, float bottomDistance) {
        c.drawRect(view.getLeft() - leftDistance,
                showTopDistance ? view.getTop() - view.getPaddingTop() - topDistance :
                        view.getTop() - view.getPaddingTop(),
                view.getLeft(),
                showBottomDistance ? view.getBottom() - view.getPaddingBottom() + bottomDistance :
                        view.getBottom() - view.getPaddingBottom(),
                paint);
    }

    /**
     * 绘制底部
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawBottom(@NotNull @NonNull Canvas c, View view, float distance) {
        c.drawRect(view.getLeft() + view.getPaddingLeft(),
                view.getBottom(),
                view.getRight() - view.getPaddingRight(),
                view.getBottom() + distance, paint);
    }

    /**
     * 绘制顶部
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawTop(@NotNull @NonNull Canvas c, View view, float distance) {
        c.drawRect(view.getLeft() + view.getPaddingLeft(),
                view.getTop() - distance,
                view.getRight() - view.getPaddingRight(),
                view.getTop(), paint);
    }

    public Float getLeftDistance() {
        return leftDistance;
    }

    public Float getTopDistance() {
        return topDistance;
    }

    public Float getRightDistance() {
        return rightDistance;
    }

    public Float getBottomDistance() {
        return bottomDistance;
    }
}
