package android.lorenwang.customview.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 功能作用：RecycleView仿ViewPager
 * 初始注释时间： 2021/2/15 10:45 下午
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
public class AvlwRecycleViewViewpager extends RecyclerView {
    /**
     * 是否允许触摸滑动
     */
    private boolean isCanTouchScroll = true;
    /**
     * 当前位置
     */
    private int nowPosi = 0;
    /**
     * 布局方向
     */
    private int mOrientation = LinearLayoutManager.HORIZONTAL;
    /**
     * 监听
     */
    private AvlwRecycleviewViewPageOnPageChangeListener recycleviewViewPageOnPageChangeListener;

    public AvlwRecycleViewViewpager(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwRecycleViewViewpager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwRecycleViewViewpager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        setmOrientation(mOrientation);
    }

    public void setmOrientation(int mOrientation) {
        if (mOrientation != HORIZONTAL && mOrientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + mOrientation);
        }
        this.mOrientation = mOrientation;
        super.setLayoutManager(new LinearLayoutManager(getContext(), mOrientation, false) {
            @Override
            public boolean canScrollHorizontally() {
                return isCanTouchScroll && mOrientation == HORIZONTAL;
            }

            @Override
            public boolean canScrollVertically() {
                return isCanTouchScroll && mOrientation == VERTICAL;
            }
        });
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        super.setLayoutManager(null);
    }

    /**
     * 按下位置
     */
    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanTouchScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    return super.dispatchTouchEvent(ev);
                case MotionEvent.ACTION_HOVER_MOVE:
                    return false;
                case MotionEvent.ACTION_UP:
                    if (mOrientation == HORIZONTAL) {
                        if (downX < ev.getX()) {
                            moveToLastItem();
                            return true;
                        } else if (downX > ev.getX()) {
                            moveToNextItem();
                            return true;
                        } else {
                            return super.dispatchTouchEvent(ev);
                        }
                    } else if (mOrientation == VERTICAL) {
                        if (downY < ev.getY()) {
                            moveToLastItem();
                            return true;
                        } else if (downY > ev.getY()) {
                            moveToNextItem();
                            return true;
                        } else {
                            return super.dispatchTouchEvent(ev);
                        }
                    }
                default:
                    return super.dispatchTouchEvent(ev);
            }
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 设置改变监听
     *
     * @param recycleviewViewPageOnPageChangeListener 监听
     * @return 当前实例
     */
    public AvlwRecycleViewViewpager setRecycleviewViewPageOnPageChangeListener(
            AvlwRecycleviewViewPageOnPageChangeListener recycleviewViewPageOnPageChangeListener) {
        this.recycleviewViewPageOnPageChangeListener = recycleviewViewPageOnPageChangeListener;
        return this;
    }

    /**
     * 移动等到上一个位置
     */
    private void moveToLastItem() {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            int firstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            if (firstVisibleItemPosition >= 0 && getAdapter() != null && firstVisibleItemPosition < getAdapter().getItemCount()) {
                smoothScrollToPosition(firstVisibleItemPosition);
            }
        }
    }

    /**
     * 移动到下一个位置
     */
    private void moveToNextItem() {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            int lastVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            if (lastVisibleItemPosition >= 0 && getAdapter() != null && lastVisibleItemPosition < getAdapter().getItemCount()) {
                smoothScrollToPosition(lastVisibleItemPosition);
            }
        }
    }

    /**
     * 设置是否允许触摸滑动
     *
     * @param canTouchScroll 是否允许触摸滑动
     * @return 当前实例
     */
    public AvlwRecycleViewViewpager setCanTouchScroll(boolean canTouchScroll) {
        isCanTouchScroll = canTouchScroll;
        return this;
    }

    /**
     * 获取当前位置
     *
     * @return 当前位置
     */
    public int getNowPosi() {
        return nowPosi;
    }

    @Override
    public void scrollToPosition(int position) {
        if (recycleviewViewPageOnPageChangeListener != null) {
            recycleviewViewPageOnPageChangeListener.onPageChange(position);
        }
        nowPosi = position;
        super.scrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (recycleviewViewPageOnPageChangeListener != null) {
            recycleviewViewPageOnPageChangeListener.onPageChange(position);
        }
        nowPosi = position;
        super.smoothScrollToPosition(position);
    }
}
