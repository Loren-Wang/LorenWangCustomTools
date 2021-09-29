package android.lorenwang.customview.recycleview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
     * 水平方向距离
     */
    private final Float horizontalDistance;
    /**
     * 垂直方向距离
     */
    private final Float verticalDistance;
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
    public AvlwCommonGridItemDecoration(Integer color, int gridSpanCount, float leftDistance, float topDistance, float rightDistance,
            float bottomDistance, float rowTopDistance, float rowBottomDistance) {
        this(color, gridSpanCount, leftDistance + rightDistance, topDistance + bottomDistance, rowTopDistance, rowBottomDistance);
    }

    public AvlwCommonGridItemDecoration(Integer color, int gridSpanCount, float horizontalDistance, float verticalDistance, float rowTopDistance,
            float rowBottomDistance) {

        paint.setAntiAlias(true);
        if (color != null) {
            paint.setColor(color);
        } else {
            paint.setColor(Color.TRANSPARENT);
        }
        this.gridSpanCount = gridSpanCount;
        this.horizontalDistance = horizontalDistance;
        this.verticalDistance = verticalDistance;
        this.rowTopDistance = rowTopDistance;
        this.rowBottomDistance = rowBottomDistance;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent,
            @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int count = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        //总行数
        int sumRows = count % gridSpanCount == 0 ? count / gridSpanCount : (count / gridSpanCount) + 1;
        //当前位置
        int position;
        int column;
        int row;
        if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            position = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            column = position;
            row = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getViewLayoutPosition() / gridSpanCount;
        } else {
            position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            column = position % gridSpanCount;
            row = position / gridSpanCount;
        }

        float distance = horizontalDistance * (gridSpanCount - 1) / gridSpanCount;
        outRect.top = (int) (verticalDistance / 2);
        outRect.bottom = (int) (verticalDistance / 2);
        if (row == 0 && column == 0) {
            outRect.top = (int) (verticalDistance / 2 + rowTopDistance);
            outRect.right = (int) distance;
        } else if (row == 0 && column == gridSpanCount - 1) {
            outRect.top = (int) (verticalDistance / 2 + rowTopDistance);
            outRect.left = (int) distance;
        } else if (row == 0) {
            outRect.top = (int) (verticalDistance / 2 + rowTopDistance);
            outRect.right = (int) (distance / 2);
            outRect.left = (int) (distance / 2);
        } else if (row == sumRows - 1 && column == 0) {
            outRect.bottom = (int) (verticalDistance / 2 + rowBottomDistance);
            outRect.right = (int) distance;
        } else if (row == sumRows - 1 && column == gridSpanCount - 1) {
            outRect.bottom = (int) (verticalDistance / 2 + rowBottomDistance);
            outRect.left = (int) distance;
        } else if (row == sumRows - 1) {
            outRect.bottom = (int) (verticalDistance / 2 + rowBottomDistance);
            outRect.right = (int) (distance / 2);
            outRect.left = (int) (distance / 2);
        } else if (column == 0) {
            outRect.right = (int) distance;
        } else if (column == gridSpanCount - 1) {
            outRect.left = (int) distance;
        } else {
            outRect.right = (int) (distance / 2);
            outRect.left = (int) (distance / 2);
        }
    }

    @Override
    public void onDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {

        int childCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        //总行数
        int sumRows = childCount % gridSpanCount == 0 ? childCount / gridSpanCount : (childCount / gridSpanCount) + 1;

        //当前位置
        int position;
        int column;
        int row;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view == null) {
                continue;
            }
            if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                position = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
                column = position;
                row = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getViewLayoutPosition() / gridSpanCount;
            } else {
                position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                column = position % gridSpanCount;
                row = position / gridSpanCount;
            }
            if (row == 0 && column == 0) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, 0);
                drawRight(c, view);
            } else if (row == 0 && column == gridSpanCount - 1) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, 0);
                drawLeft(c, view);
            } else if (row == 0) {
                drawTop(c, view, rowTopDistance);
                drawBottom(c, view, 0);
                drawRight(c, view);
                drawLeft(c, view);
            } else if (row == sumRows - 1 && column == 0) {
                drawTop(c, view, 0);
                drawBottom(c, view, rowBottomDistance);
                drawRight(c, view);
            } else if (row == sumRows - 1 && column == gridSpanCount - 1) {
                drawTop(c, view, 0);
                drawBottom(c, view, rowBottomDistance);
                drawLeft(c, view);
            } else if (row == sumRows - 1) {
                drawTop(c, view, 0);
                drawBottom(c, view, rowBottomDistance);
                drawLeft(c, view);
                drawRight(c, view);
            } else if (column == 0) {
                drawTop(c, view, 0);
                drawBottom(c, view, 0);
                drawRight(c, view);
            } else if (column == gridSpanCount - 1) {
                drawTop(c, view, 0);
                drawBottom(c, view, 0);
                drawLeft(c, view);
            } else {
                drawTop(c, view, 0);
                drawBottom(c, view, 0);
                drawLeft(c, view);
                drawRight(c, view);
            }
        }
    }

    /**
     * 画右侧
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawRight(@NotNull @NonNull Canvas c, View view) {
        c.drawRect(view.getRight(), view.getTop() - view.getPaddingTop(), view.getRight(), view.getBottom() - view.getPaddingBottom(), paint);
    }

    /**
     * 画左侧
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawLeft(@NotNull @NonNull Canvas c, View view) {
        c.drawRect(view.getLeft(), view.getTop() - view.getPaddingTop(), view.getLeft(), view.getBottom() - view.getPaddingBottom(), paint);
    }

    /**
     * 绘制底部
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawBottom(@NotNull @NonNull Canvas c, View view, float distance) {
        c.drawRect(view.getLeft() + view.getPaddingLeft(), view.getTop(), view.getRight() - view.getPaddingRight(), view.getBottom() + distance,
                paint);
    }

    /**
     * 绘制顶部
     *
     * @param c    画板
     * @param view 视图控件
     */
    private void drawTop(@NotNull @NonNull Canvas c, View view, float distance) {
        c.drawRect(view.getLeft() + view.getPaddingLeft(), view.getTop() - distance, view.getRight() - view.getPaddingRight(), view.getTop(), paint);
    }
}
