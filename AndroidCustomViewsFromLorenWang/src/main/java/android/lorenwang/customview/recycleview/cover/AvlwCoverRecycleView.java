package android.lorenwang.customview.recycleview.cover;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：层叠画廊recycleview
 * 创建时间：2021-03-18 16:33
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：来源链接：https://github.com/ChenLittlePing/RecyclerCoverFlow
 *
 * @author 王亮（Loren）
 */
public class AvlwCoverRecycleView extends RecyclerView {

    //速度因子
    public double sppedScale;

    /**
     * 按下的X轴坐标
     */
    private float mDownX;

    public AvlwCoverRecycleView(Context context) {
        super(context);
        init();
    }

    public AvlwCoverRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvlwCoverRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        //设置recyclerview的抛掷速度
        velocityX = (int) (velocityX * 0.5);
        return super.fling(velocityX, velocityY);
    }

    private void init() {
        //设置布局管理器
        setLayoutManagerConfig();
        //开启重新排序
        setChildrenDrawingOrderEnabled(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 设置布局管理器
     */
    public void setLayoutManagerConfig() {
        super.setLayoutManager(new AvlwCoverFlowLayoutManger());
    }

    /**
     * 设置布局管理器
     *
     * @param mIsFlatFlow       是否使用平滑滚动，也就是说是否使用缩放
     * @param mItemGradualGrey  是否使用灰度渐变
     * @param mItemGradualAlpha 是否使用透明渐变
     * @param mIsLoop           是否循环
     * @param mItem3D           是否使用3d样式
     */
    public void setLayoutManagerConfig(boolean mIsFlatFlow, boolean mItemGradualGrey, boolean mItemGradualAlpha, boolean mIsLoop, boolean mItem3D) {
        super.setLayoutManager(new AvlwCoverFlowLayoutManger(mIsFlatFlow, mItemGradualGrey, mItemGradualAlpha, mIsLoop, mItem3D));
    }

    /**
     * 设置布局管理器
     *
     * @param widthOffsetPercent 宽度偏移百分比
     * @param alphaPercent       透明百分比
     * @param scalePercent       缩放百分比
     */
    public void setLayoutManagerConfig(float widthOffsetPercent, float alphaPercent, float scalePercent) {
        super.setLayoutManager(new AvlwCoverFlowLayoutManger(widthOffsetPercent, alphaPercent, scalePercent));
    }

    /**
     * 设置布局管理器
     *
     * @param mIsFlatFlow        是否使用平滑滚动，也就是说是否使用缩放
     * @param mItemGradualGrey   是否使用灰度渐变
     * @param mItemGradualAlpha  是否使用透明渐变
     * @param mIsLoop            是否循环
     * @param mItem3D            是否使用3d样式
     * @param widthOffsetPercent 宽度偏移百分比
     * @param alphaPercent       透明百分比
     * @param scalePercent       缩放百分比
     */
    public void setLayoutManagerConfig(boolean mIsFlatFlow, boolean mItemGradualGrey, boolean mItemGradualAlpha, boolean mIsLoop, boolean mItem3D,
            float widthOffsetPercent, float alphaPercent, float scalePercent) {
        super.setLayoutManager(
                new AvlwCoverFlowLayoutManger(mIsFlatFlow, mItemGradualGrey, mItemGradualAlpha, mIsLoop, mItem3D, widthOffsetPercent, alphaPercent,
                        scalePercent));
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverLayoutManager().getCenterPosition();
        // 获取 RecyclerView 中第 i 个 子 view 的实际位置
        int actualPos = getCoverLayoutManager().getChildActualPos(i);

        // 距离中间item的间隔数
        int dist = actualPos - center;
        int order;
        if (dist < 0) { // [< 0] 说明 item 位于中间 item 左边，按循序绘制即可
            order = i;
        } else { // [>= 0] 说明 item 位于中间 item 右边，需要将顺序颠倒绘制
            order = childCount - 1 - dist;
        }

        if (order < 0) {
            order = 0;
        } else if (order > childCount - 1) {
            order = childCount - 1;
        }

        return order;
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        return getCoverLayoutManager().getSelectedPos();
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(OnSelected l) {
        getCoverLayoutManager().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE:
                //如果是滑动到了最前和最后，开放父类滑动事件拦截
                //滑动到中间，设置父类不拦截滑动事件
                getParent().requestDisallowInterceptTouchEvent((!(ev.getX() > mDownX) || getCoverLayoutManager().getCenterPosition() != 0) &&
                        (!(ev.getX() < mDownX) || getCoverLayoutManager().getCenterPosition() != getCoverLayoutManager().getItemCount() - 1));
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 选中监听接口
     */
    public interface OnSelected {
        /**
         * 监听选中回调
         *
         * @param position 显示在中间的Item的位置
         */
        void onItemSelected(int position);
    }

    /**
     * 获取当前layoutmanager
     */
    private AvlwCoverFlowLayoutManger getCoverLayoutManager() {
        return (AvlwCoverFlowLayoutManger) getLayoutManager();
    }
}

