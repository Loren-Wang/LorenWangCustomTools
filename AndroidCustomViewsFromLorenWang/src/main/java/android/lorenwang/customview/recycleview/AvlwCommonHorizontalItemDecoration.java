package android.lorenwang.customview.recycleview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：通用水平列表分隔线
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
public class AvlwCommonHorizontalItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 画笔
     */
    private final Paint paint = new Paint();
    /**
     * 分隔线宽度
     */
    private final Float divideWidth;

    /**
     * 第一个左侧宽度
     */
    private final Float firstLeftDivideWidth;

    /**
     * 最后一个右侧宽度
     */
    private final Float endRightDivideWidth;

    /**
     * 构造函数
     *
     * @param color       颜色
     * @param divideWidth 高度
     */
    public AvlwCommonHorizontalItemDecoration(Integer color, Float divideWidth, Float firstLeftDivideWidth, Float endRightDivideWidth) {
        paint.setAntiAlias(true);
        if (color != null) {
            paint.setColor(color);
        } else {
            paint.setColor(Color.TRANSPARENT);
        }
        this.divideWidth = divideWidth == null ? AtlwScreenUtil.getInstance().dip2px(16F) : divideWidth;
        this.firstLeftDivideWidth = firstLeftDivideWidth == null ? 0F : firstLeftDivideWidth;
        this.endRightDivideWidth = endRightDivideWidth == null ? 0F : endRightDivideWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getAdapter() == null) {
            return;
        }
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position == 0) {
            outRect.left = firstLeftDivideWidth.intValue();
            outRect.right = divideWidth.intValue();
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.right = endRightDivideWidth.intValue();
        } else {
            outRect.right = divideWidth.intValue();
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int childCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        //总数量
        int itemCount = parent.getAdapter() == null ? childCount : parent.getAdapter().getItemCount();


        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view == null) {
                continue;
            }
            if (i == 0) {
                c.drawRect(left, view.getTop(), left + firstLeftDivideWidth, view.getBottom(), paint);
                c.drawRect(right, view.getTop(), right + divideWidth, view.getBottom(), paint);
            } else if (i == itemCount - 1) {
                c.drawRect(right, view.getTop(), right + endRightDivideWidth, view.getBottom(), paint);
            } else {
                c.drawRect(right, view.getTop(), right + divideWidth, view.getBottom(), paint);
            }
        }
    }

    public Float getFirstLeftDivideWidth() {
        return firstLeftDivideWidth;
    }

    public Float getEndRightDivideWidth() {
        return endRightDivideWidth;
    }

    public Float getDivideWidth() {
        return divideWidth;
    }
}
