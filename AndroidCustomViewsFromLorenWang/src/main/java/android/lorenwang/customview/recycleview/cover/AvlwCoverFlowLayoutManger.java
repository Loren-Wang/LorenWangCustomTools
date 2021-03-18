package android.lorenwang.customview.recycleview.cover;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：层叠画廊LayoutManager
 * 创建时间：2021-03-18 15:52
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
class AvlwCoverFlowLayoutManger extends RecyclerView.LayoutManager {

    /**
     * item 向右移动
     */
    private final static int SCROLL_TO_RIGHT = 1;
    /**
     * item 向左移动
     */
    private final static int SCROLL_TO_LEFT = 2;


    /**
     * 保存所有的Item的上下左右的偏移量信息
     */
    private final SparseArray<Rect> mAllItemFrames = new SparseArray<>();
    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private final SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    /**
     * 滑动总偏移量
     */
    private int mOffsetAll = 0;
    /**
     * Item宽
     */
    private int mDecoratedChildWidth = 0;
    /**
     * Item高
     */
    private int mDecoratedChildHeight = 0;
    /**
     * 起始ItemX坐标
     */
    private int mStartX = 0;
    /**
     * 起始Item Y坐标
     */
    private int mStartY = 0;

    /**
     * RecyclerView的Item回收器
     */
    private RecyclerView.Recycler mRecycle;
    /**
     * RecyclerView的状态器
     */
    private RecyclerView.State mState;
    /**
     * 滚动动画
     */
    private ValueAnimator mAnimation;

    /**
     * 正显示在中间的Item
     */
    private int mSelectPosition = 0;
    /**
     * 前一个正显示在中间的Item
     */
    private int mLastSelectPosition = 0;

    /**
     * 选中监听
     */
    private AvlwCoverRecycleView.OnSelected mSelectedListener;

    /**
     * 是否为平面滚动，Item之间没有叠加，也没有缩放
     */
    private final boolean mIsFlatFlow;
    /**
     * 是否启动Item灰度值渐变
     */
    private final boolean mItemGradualGrey;
    /**
     * 是否启动Item半透渐变
     */
    private final boolean mItemGradualAlpha;
    /**
     * 是否无限循环
     */
    private final boolean mIsLoop;
    /**
     * 是否启动Item 3D 倾斜
     */
    private final boolean mItem3D;
    /**
     * 宽度偏移百分比
     */
    private final float widthOffsetPercent;
    /**
     * 透明百分比
     */
    private final float alphaPercent;
    /**
     * 缩放百分比
     */
    private final float scalePercent;

    public AvlwCoverFlowLayoutManger() {
        this(false, false, false, false, false, 0.5F, 0.5F, 0.5F);
    }

    public AvlwCoverFlowLayoutManger(boolean mIsFlatFlow, boolean mItemGradualGrey, boolean mItemGradualAlpha, boolean mIsLoop, boolean mItem3D) {
        this(mIsFlatFlow, mItemGradualGrey, mItemGradualAlpha, mIsLoop, mItem3D, 0.5F, 0.5F, 0.5F);
    }

    public AvlwCoverFlowLayoutManger(float widthOffsetPercent, float alphaPercent, float scalePercent) {
        this(scalePercent != 1, false, alphaPercent != 1, false, false, widthOffsetPercent, alphaPercent, scalePercent);
    }

    public AvlwCoverFlowLayoutManger(boolean mIsFlatFlow, boolean mItemGradualGrey, boolean mItemGradualAlpha, boolean mIsLoop, boolean mItem3D,
            float widthOffsetPercent, float alphaPercent, float scalePercent) {
        this.mIsFlatFlow = mIsFlatFlow;
        this.mItemGradualGrey = mItemGradualGrey;
        this.mItemGradualAlpha = mItemGradualAlpha;
        this.mIsLoop = mIsLoop;
        this.mItem3D = mItem3D;
        this.widthOffsetPercent = widthOffsetPercent;
        this.alphaPercent = alphaPercent;
        this.scalePercent = scalePercent;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item，直接返回
        //跳过preLayout，preLayout主要用于支持动画
        if (getItemCount() <= 0 || state.isPreLayout()) {
            mOffsetAll = 0;
            return;
        }
        mAllItemFrames.clear();
        mHasAttachedItems.clear();

        //得到子view的宽和高，这边的item的宽高都是一样的，所以只需要进行一次测量
        View scrap = recycler.getViewForPosition(0);
        addView(scrap);
        measureChildWithMargins(scrap, 0, 0);
        //计算测量布局的宽高
        mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
        mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);
        mStartX = Math.round((getHorizontalSpace() - mDecoratedChildWidth) * 1.0f / 2);
        mStartY = Math.round((getVerticalSpace() - mDecoratedChildHeight) * 1.0f / 2);

        float offset = mStartX;

        /*只存{@link MAX_RECT_COUNT}个item具体位置*/
        for (int i = 0; i < getItemCount(); i++) {
            Rect frame = mAllItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth), mStartY + mDecoratedChildHeight);
            mAllItemFrames.put(i, frame);
            mHasAttachedItems.put(i, false);
            //原始位置累加，否则越后面误差越大
            offset = offset + getIntervalDistance();
        }
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        //在为初始化前调用smoothScrollToPosition 或者 scrollToPosition,只会记录位置
        if ((mRecycle == null || mState == null) && mSelectPosition != 0) {
            //所以初始化时需要滚动到对应位置
            mOffsetAll = calculateOffsetForPosition(mSelectPosition);
            onSelectedCallBack();
        }

        layoutItems(recycler, state, SCROLL_TO_LEFT);

        mRecycle = recycler;
        mState = state;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
        }
        //限制滑动速度
        int travel = (int) (dx * 0.9);
        if (!mIsLoop) { //非循环模式，限制滚动位置
            if (dx + mOffsetAll < 0) {
                travel = -mOffsetAll;
            } else if (dx + mOffsetAll > getMaxOffset()) {
                travel = (int) (getMaxOffset() - mOffsetAll);
            }
        }
        //累计偏移量
        mOffsetAll += travel;
        layoutItems(recycler, state, dx > 0 ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT);
        return travel;
    }

    /**
     * 布局Item
     *
     * <p>1，先清除已经超出屏幕的item
     * <p>2，再绘制可以显示在屏幕里面的item
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void layoutItems(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDirection) {
        if (state == null || state.isPreLayout()) {
            return;
        }

        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());

        int position = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null) {
                if (child.getTag() != null) {
                    position = (int) child.getTag();
                } else {
                    position = getPosition(child);
                }
                Rect rect = getFrame(position);
                if (!Rect.intersects(displayFrame, rect)) {//Item没有在显示区域，就说明需要回收
                    removeAndRecycleView(child, recycler); //回收滑出屏幕的View
                    mHasAttachedItems.delete(position);
                } else { //Item还在显示区域内，更新滑动后Item的位置
                    layoutItem(child, rect); //更新Item位置
                    mHasAttachedItems.put(position, true);
                }
            }
        }

        if (position == 0) {
            position = getCenterPosition();
        }

        // 检查前后 20 个 item 是否需要绘制
        int min = position - 20;
        int max = position + 20;

        if (!mIsLoop) {
            if (min < 0) {
                min = 0;
            }
            if (max > getItemCount()) {
                max = getItemCount();
            }
        }

        for (int i = min; i < max; i++) {
            Rect rect = getFrame(i);
            if (Rect.intersects(displayFrame, rect) && !mHasAttachedItems.get(i)) { //重新加载可见范围内的Item
                // 循环滚动时，计算实际的 item 位置
                int actualPos = i % getItemCount();
                // 循环滚动时，位置可能是负值，需要将其转换为对应的 item 的值
                if (actualPos < 0) {
                    actualPos = getItemCount() + actualPos;
                }

                try {
                    View scrap = recycler.getViewForPosition(actualPos);
                    scrap.setTag(i);

                    measureChildWithMargins(scrap, 0, 0);
                    if (scrollDirection == SCROLL_TO_RIGHT) { //item 向右滚动，新增的Item需要添加在最前面
                        addView(scrap, 0);
                    } else { //item 向左滚动，新增的item要添加在最后面
                        addView(scrap);
                    }
                    layoutItem(scrap, rect); //将这个Item布局出来
                    mHasAttachedItems.put(i, true);
                } catch (Exception ignored) {
                }

            }
        }
    }

    /**
     * 布局Item位置
     *
     * @param child 要布局的Item
     * @param frame 位置信息
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void layoutItem(View child, Rect frame) {
        layoutDecorated(child, frame.left - mOffsetAll, frame.top, frame.right - mOffsetAll, frame.bottom);
        //不是平面普通滚动的情况下才进行缩放
        if (mIsFlatFlow) {
            Log.e("SCALE_ITEM", computeScale(frame.left - mOffsetAll) + "");
            Log.e("SCALE_X", (frame.left - mOffsetAll) + "");
            child.setScaleX(computeScale(frame.left - mOffsetAll)); //缩放
            child.setScaleY(computeScale(frame.left - mOffsetAll)); //缩放
        }

        if (mItemGradualAlpha) {
            child.setAlpha(computeAlpha(frame.left - mOffsetAll));
        }

        if (mItemGradualGrey) {
            greyItem(child, frame);
        }

        if (mItem3D) {
            item3D(child, frame);
        }
    }

    /**
     * 动态获取Item的位置信息
     *
     * @param index item位置
     * @return item的Rect信息
     */
    private Rect getFrame(int index) {
        Rect frame = mAllItemFrames.get(index);
        if (frame == null) {
            frame = new Rect();
            float offset = mStartX + getIntervalDistance() * index; //原始位置累加（即累计间隔距离）
            frame.set(Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth), mStartY + mDecoratedChildHeight);
        }

        return frame;
    }

    /**
     * 变化Item的灰度值
     *
     * @param child 需要设置灰度值的Item
     * @param frame 位置信息
     */
    private void greyItem(View child, Rect frame) {
        float value = computeGreyScale(frame.left - mOffsetAll);
        ColorMatrix cm = new ColorMatrix(
                new float[]{value, 0, 0, 0, 120 * (1 - value), 0, value, 0, 0, 120 * (1 - value), 0, 0, value, 0, 120 * (1 - value), 0, 0, 0, 1,
                        250 * (1 - value),});
        //        cm.setSaturation(0.9f);

        // Create a paint object with color matrix
        Paint greyPaint = new Paint();
        greyPaint.setColorFilter(new ColorMatrixColorFilter(cm));

        // Create a hardware layer with the grey paint
        child.setLayerType(View.LAYER_TYPE_HARDWARE, greyPaint);
        if (value >= 1) {
            // Remove the hardware layer
            child.setLayerType(View.LAYER_TYPE_NONE, null);
        }

    }

    private void item3D(View child, Rect frame) {
        float center = (frame.left + frame.right - 2 * mOffsetAll) / 2f;
        float value = (center - (mStartX + mDecoratedChildWidth / 2f)) * 1f / (getItemCount() * getIntervalDistance());
        value = (float) Math.sqrt(Math.abs(value));
        float symbol = center > (mStartX + mDecoratedChildWidth / 2f) ? -1 : 1;
        child.setRotationY(symbol * 50 * value);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                //滚动停止时
                fixOffsetWhenFinishScroll();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //拖拽滚动时
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                //动画滚动时
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount() - 1) {
            return;
        }
        mOffsetAll = calculateOffsetForPosition(position);
        if (mRecycle == null || mState == null) {//如果RecyclerView还没初始化完，先记录下要滚动的位置
            mSelectPosition = position;
        } else {
            layoutItems(mRecycle, mState, position > mSelectPosition ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT);
            onSelectedCallBack();
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        // TODO 循环模式暂不支持平滑滚动
        if (mIsLoop) {
            return;
        }
        int finalOffset = calculateOffsetForPosition(position);
        if (mRecycle == null || mState == null) {//如果RecyclerView还没初始化完，先记录下要滚动的位置
            mSelectPosition = position;
        } else {
            startScroll(mOffsetAll, finalOffset);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        removeAllViews();
        mRecycle = null;
        mState = null;
        mOffsetAll = 0;
        mSelectPosition = 0;
        mLastSelectPosition = 0;
        mHasAttachedItems.clear();
        mAllItemFrames.clear();
    }

    /**
     * 获取第一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的第一个Item，有可能被第二个Item遮挡
     */
    public int getFirstVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur - 1; ; i--) {
            Rect rect = getFrame(i);
            if (rect.left <= displayFrame.left) {
                return Math.abs(i) % getItemCount();
            }
        }
    }

    /**
     * 获取最后一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的最后一个Item，有可能被倒数第二个Item遮挡
     */
    public int getLastVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur + 1; ; i++) {
            Rect rect = getFrame(i);
            if (rect.right >= displayFrame.right) {
                return Math.abs(i) % getItemCount();
            }
        }
    }

    /**
     * 获取可见范围内最大的显示Item个数
     */
    public int getMaxVisibleCount() {
        int oneSide = (getHorizontalSpace() - mStartX) / (getIntervalDistance());
        return oneSide * 2 + 1;
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnSelectedListener(AvlwCoverRecycleView.OnSelected l) {
        mSelectedListener = l;
    }

    /**
     * 获取被选中Item位置
     */
    public int getSelectedPos() {
        return mSelectPosition;
    }

    /**
     * 获取中间位置
     * <p>Note:该方法主要用于{RecyclerCoverFlowgetChildDrawingOrder(int, int)}判断中间位置
     * <p>如果需要获取被选中的Item位置，调用{@link #getSelectedPos()}
     */
    public int getCenterPosition() {
        int pos = mOffsetAll / getIntervalDistance();
        int more = mOffsetAll % getIntervalDistance();
        if (Math.abs(more) >= getIntervalDistance() * 0.5f) {
            if (more >= 0) {
                pos++;
            } else {
                pos--;
            }
        }
        return pos;
    }

    /**
     * 该方法主要用于{RecyclerCoverFlow#getChildDrawingOrder(int, int)}判断中间位置
     *
     * @param index child 在 RecyclerCoverFlow 中的位置
     * @return child 的实际位置，如果 {@link #mIsLoop} 为 true ，返回结果可能为负值
     */
    public int getChildActualPos(int index) {
        View child = getChildAt(index);
        if (child != null) {
            if (child.getTag() != null) {
                return (int) child.getTag();
            } else {
                return getPosition(child);
            }
        } else {
            return -1;
        }
    }

    /**
     * 获取整个布局的水平空间大小
     */
    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    /**
     * 获取整个布局的垂直空间大小
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    /**
     * 获取最大偏移量
     */
    private float getMaxOffset() {
        return (getItemCount() - 1) * getIntervalDistance();
    }

    /**
     * 计算Item缩放系数
     *
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private float computeScale(int x) {
        Log.e("Start_X", mStartX + "");
        float scale = 1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / scalePercent);
        if (scale < 0) {
            scale = 0;
        }
        if (scale > 1) {
            scale = 1;
        }
        return scale;
    }

    /**
     * 计算Item的灰度值
     *
     * @param x Item的偏移量
     * @return 灰度系数
     */
    private float computeGreyScale(int x) {
        //item中点x坐标
        float itemMidPos = x + mDecoratedChildWidth / 2.0F;
        //item中点距离控件中点距离
        float itemDx2Mid = Math.abs(itemMidPos - getHorizontalSpace() / 2f);
        float value = 1 - itemDx2Mid * 1.0f / (getHorizontalSpace() / 2.0F);
        if (value < 0.1) {
            value = 0.1f;
        }
        if (value > 1) {
            value = 1;
        }
        value = (float) Math.pow(value, .8);
        return value;
    }

    /**
     * 计算Item半透值
     *
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private float computeAlpha(int x) {
        float alpha = 1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / alphaPercent / 2);
        if (alpha < 0f) {
            alpha = 0f;
        }
        if (alpha > 1) {
            alpha = 1.0f;
        }
        return alpha;
    }

    /**
     * 计算Item所在的位置偏移
     *
     * @param position 要计算Item位置
     */
    private int calculateOffsetForPosition(int position) {
        return Math.round(getIntervalDistance() * position);
    }

    /**
     * 修正停止滚动后，Item滚动到中间位置
     */
    private void fixOffsetWhenFinishScroll() {
        if (getIntervalDistance() != 0) { // 判断非 0 ，否则除 0 会导致异常
            int scrollN = (int) (mOffsetAll * 1.0f / getIntervalDistance());
            float moreDx = (mOffsetAll % getIntervalDistance());
            if (Math.abs(moreDx) > (getIntervalDistance() * 0.5)) {
                if (moreDx > 0) {
                    scrollN++;
                } else {
                    scrollN--;
                }
            }
            int finalOffset = scrollN * getIntervalDistance();
            startScroll(mOffsetAll, finalOffset);
            mSelectPosition = Math.abs(Math.round(finalOffset * 1.0f / getIntervalDistance())) % getItemCount();
        }
    }

    /**
     * 滚动到指定X轴位置
     *
     * @param from X轴方向起始点的偏移量
     * @param to   X轴方向终点的偏移量
     */
    private void startScroll(int from, int to) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
        }
        final int direction = from < to ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT;
        mAnimation = ValueAnimator.ofFloat(from, to);
        mAnimation.setDuration(500);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetAll = Math.round((float) animation.getAnimatedValue());
                layoutItems(mRecycle, mState, direction);
            }
        });
        mAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onSelectedCallBack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimation.start();
    }

    /**
     * 获取Item间隔
     */
    private int getIntervalDistance() {
        return Math.round(mDecoratedChildWidth * widthOffsetPercent);
    }

    /**
     * 计算当前选中位置，并回调
     */
    private void onSelectedCallBack() {
        mSelectPosition = Math.round(mOffsetAll * 1.0F / getIntervalDistance());
        mSelectPosition = Math.abs(mSelectPosition % getItemCount());
        if (mSelectedListener != null && mSelectPosition != mLastSelectPosition) {
            mSelectedListener.onItemSelected(mSelectPosition);
        }
        mLastSelectPosition = mSelectPosition;
    }
}
