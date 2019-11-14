package android.lorenwang.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * Created by wangliang on 0029/2017/6/29.
 * 创建时间： 0029/2017/6/29 11:41
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwRecycleViewViewpager extends RecyclerView {
    private Context context;
    private boolean isCanTouchScroll = true;//是否允许触摸滑动
    private int nowPosi = 0;//当前位置
    private AvlwRecycleviewViewPageOnPageChangeListener recycleviewViewPageOnPageChangeListener;

    public AvlwRecycleViewViewpager(Context context) {
        super(context);
        init(context);
    }

    public AvlwRecycleViewViewpager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvlwRecycleViewViewpager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context){
        this.context = context;
        setLayoutManager(null);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return isCanTouchScroll;
            }
        });
    }

    private float downX;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isCanTouchScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    return super.dispatchTouchEvent(ev);
                case MotionEvent.ACTION_HOVER_MOVE:
                    return super.dispatchTouchEvent(ev);
                case MotionEvent.ACTION_UP:
                    if (downX < ev.getX()) {
                        moveToLastItem();
                        return true;
                    } else if (downX > ev.getX()) {
                        moveToNextItem();
                        return true;
                    } else {
                        return super.dispatchTouchEvent(ev);
                    }
                default:
                    return super.dispatchTouchEvent(ev);
            }
        }else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public AvlwRecycleViewViewpager setRecycleviewViewPageOnPageChangeListener(AvlwRecycleviewViewPageOnPageChangeListener recycleviewViewPageOnPageChangeListener) {
        this.recycleviewViewPageOnPageChangeListener = recycleviewViewPageOnPageChangeListener;
        return this;
    }

    /**
     * 移动等到上一个位置
     */
    private void moveToLastItem(){
        if(getLayoutManager() instanceof LinearLayoutManager){
            int firstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            if(firstVisibleItemPosition >= 0 && getAdapter() != null && firstVisibleItemPosition < getAdapter().getItemCount()) {
                smoothScrollToPosition(firstVisibleItemPosition);
            }
        }
    }

    /**
     * 移动到下一个位置
     */
    private void moveToNextItem(){
        if(getLayoutManager() instanceof LinearLayoutManager){
            int lastVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            if(lastVisibleItemPosition >= 0 && getAdapter() != null && lastVisibleItemPosition < getAdapter().getItemCount()) {
                smoothScrollToPosition(lastVisibleItemPosition);
            }
        }
    }

    public AvlwRecycleViewViewpager setCanTouchScroll(boolean canTouchScroll) {
        isCanTouchScroll = canTouchScroll;
        return this;
    }

    public int getNowPosi() {
        return nowPosi;
    }

    @Override
    public void scrollToPosition(int position) {
        if(recycleviewViewPageOnPageChangeListener != null){
            recycleviewViewPageOnPageChangeListener.onPageChange(position);
        }
        nowPosi = position;
        super.scrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if(recycleviewViewPageOnPageChangeListener != null){
            recycleviewViewPageOnPageChangeListener.onPageChange(position);
        }
        nowPosi = position;
        super.smoothScrollToPosition(position);
    }
}
