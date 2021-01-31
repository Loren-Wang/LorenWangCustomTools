package android.lorenwang.customview.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AvlwWheelRecyclerView extends RecyclerView {

    //默认参数
    private final int DEFAULT_WIDTH = (int) AtlwScreenUtil.getInstance().dip2px(160);

    private final int DEFAULT_ITEM_HEIGHT = (int) AtlwScreenUtil.getInstance().dip2px(50);

    private final int DEFAULT_SELECT_TEXT_COLOR = Color.BLACK;

    private final int DEFAULT_UNSELECT_TEXT_COLOR = Color.parseColor("#999999");

    private final int DEFAULT_SELECT_TEXT_SIZE = (int) AtlwScreenUtil.getInstance().sp2px(14);

    private final int DEFAULT_UNSELECT_TEXT_SIZE = (int) AtlwScreenUtil.getInstance().sp2px(14);

    private final int DEFAULT_OFFSET = 1;

    private final int DEFAULT_DIVIDER_WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;

    private final int DEFAULT_DIVIDER_HEIGHT = (int) AtlwScreenUtil.getInstance().dip2px(1);

    private final int DEFAULT_DIVIVER_COLOR = Color.parseColor("#666666");


    private WheelAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    /**
     * 数据列表
     */
    private final List<Object> mDatas;

    /**
     * 当前数据大小
     */
    private Integer mDatasSize = 0;

    /**
     * 选项高度
     */
    private int mItemHeight;

    /**
     * 处于中间的item为选中，在头尾需补充 offset个空白view，可显示的item数量=2*offset+1
     */
    private final int mOffset;

    /**
     * 选中item的文本颜色
     */
    private final int mSelectTextColor;

    /**
     * 非选中item的文本颜色
     */
    private final int mUnselectTextColor;

    /**
     * 选中item的文本大小
     */
    private final float mSelectTextSize;

    /**
     * 非选中item的文本大小
     */
    private final float mUnselectTextSize;

    /**
     * 选中的文字是否加粗
     */
    private final boolean selectTextIsBold;

    /**
     * 非选中的文字是否加粗
     */
    private final boolean unSelectTextIsBold;

    /**
     * 分割线的宽度
     */
    private float mDividerWidth;

    /**
     * 分割线高度
     */
    private final float mDividerHeight;

    /**
     * 分割线颜色
     */
    private final int mDividerColor;

    /**
     * 绘制分割线的paint
     */
    private final Paint mPaint;
    /**
     * 文本选择监听
     */
    private OnSelectListener mOnSelectListener;

    private int mSelected = 0;

    /**
     * 文本显示类型
     */
    private Typeface typeface;

    public AvlwWheelRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AvlwWheelRecyclerView);

        mItemHeight = (int) ta.getDimension(R.styleable.AvlwWheelRecyclerView_avlwItemHeight, DEFAULT_ITEM_HEIGHT);
        mSelectTextColor = ta.getColor(R.styleable.AvlwWheelRecyclerView_avlwSelectTextColor, DEFAULT_SELECT_TEXT_COLOR);
        mUnselectTextColor = ta.getColor(R.styleable.AvlwWheelRecyclerView_avlwUnSelectTextColor, DEFAULT_UNSELECT_TEXT_COLOR);
        mSelectTextSize = ta.getDimension(R.styleable.AvlwWheelRecyclerView_avlwSelectTextSize, DEFAULT_SELECT_TEXT_SIZE);
        mUnselectTextSize = ta.getDimension(R.styleable.AvlwWheelRecyclerView_avlwUnSelectTextSize, DEFAULT_UNSELECT_TEXT_SIZE);
        selectTextIsBold = ta.getBoolean(R.styleable.AvlwWheelRecyclerView_avlwSelectTextIsBold, false);
        unSelectTextIsBold = ta.getBoolean(R.styleable.AvlwWheelRecyclerView_avlwUnSelectTextIsBold, false);
        mOffset = ta.getInteger(R.styleable.AvlwWheelRecyclerView_avlwWheelOffset, DEFAULT_OFFSET);
        mDividerWidth = ta.getDimension(R.styleable.AvlwWheelRecyclerView_avlwDividerWidth, DEFAULT_DIVIDER_WIDTH);
        mDividerHeight = ta.getDimension(R.styleable.AvlwWheelRecyclerView_avlwDividerHeight, DEFAULT_DIVIDER_HEIGHT);
        mDividerColor = ta.getColor(R.styleable.AvlwWheelRecyclerView_avlwDividerColor, DEFAULT_DIVIVER_COLOR);

        ta.recycle();

        mDatas = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerHeight);

        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(mLayoutManager);
        if (mDividerColor != Color.TRANSPARENT && mDividerHeight != 0 && mDividerWidth != 0) {
            addItemDecoration(new DividerItemDecoration());
        }
        mAdapter = new WheelAdapter();
        setAdapter(mAdapter);
        addOnScrollListener(new OnWheelScrollListener());
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width;
        int height;
        int heightSpecSize = View.MeasureSpec.getSize(heightSpec);
        int heightSpecMode = View.MeasureSpec.getMode(heightSpec);
        //当指定了控件高度时，每个item平分整个高度，控件无指定高度时，默认高度为可视item的累加高度
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            height = heightSpecSize;
            mItemHeight = height / (mOffset * 2 + 1);
        } else {
            height = (mOffset * 2 + 1) * mItemHeight;
        }
        width = getDefaultSize(DEFAULT_WIDTH, widthSpec);
        if (mDividerWidth == DEFAULT_DIVIDER_WIDTH) {
            mDividerWidth = width;
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 设置数据
     *
     * @param datas 数据列表
     */
    public void setData(List<Object> datas) {
        if (datas == null) {
            return;
        }
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
        mDatasSize = mDatas.size();
        setSelect(mSelected);

        //回调，为了做联动使用
        if (mOnSelectListener != null && mDatasSize > 0) {
            mOnSelectListener.onScrollCenterJudge(mSelected, mDatas.get(mSelected), mDatasSize == 1, false);
        }
    }

    /**
     * 设置文本显示typeFace
     *
     * @param typeface 文本显示使用
     * @return 当前实例
     */
    public AvlwWheelRecyclerView setTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    /**
     * 设置选中监听
     *
     * @param listener 选中监听
     */
    public void setOnSelectListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    /**
     * 设置选中位置
     *
     * @param position 选中位置
     */
    public void setSelect(int position) {
        if (position >= mDatasSize) {
            mSelected = mDatasSize - 1;
        } else {
            mSelected = position;
        }
        if (mSelected < 0) {
            mSelected = 0;
        }
        mLayoutManager.scrollToPosition(mSelected);
        mAdapter.notifyItemChanged(mSelected);
    }

    public int getSelected() {
        return mSelected;
    }

    /**
     * 获取选中数据
     *
     * @return 选中数据
     */
    public Object getSelectedData() {
        return mDatas.get(mSelected);
    }

    /**
     * 滚筒适配器
     */
    private class WheelAdapter extends Adapter<WheelAdapter.WheelHolder> {
        @Override
        public @NotNull WheelHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            WheelHolder holder = new WheelHolder(LayoutInflater.from(getContext()).inflate(android.R.layout.test_list_item, parent, false));
            holder.name.getLayoutParams().height = mItemHeight;
            return holder;
        }

        @Override
        public int getItemCount() {
            return mDatas.size() == 0 ? 0 : mDatas.size() + mOffset * 2;
        }

        @Override
        public void onBindViewHolder(@NotNull WheelHolder holder, int position) {
            //头尾填充offset个空白view以使数据能处于中间选中状态
            if (position < mOffset || position > mDatas.size() + mOffset - 1) {
                holder.name.setText("");
            } else {
                setText(holder.name, position);
            }
            setTextShowType((mSelected + 1) == position, holder.name);
        }

        private void setText(TextView textView, int position) {
            Object o = mDatas.get(position - mOffset);
            if (o instanceof String) {
                textView.setText((String) o);
            } else {
                if (mOnSelectListener != null) {
                    textView.setText(mOnSelectListener.getShowItemText(position - mOffset));
                } else {
                    textView.setText("");
                }
            }
        }


        class WheelHolder extends ViewHolder {
            TextView name;

            public WheelHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(android.R.id.text1);
                name.setPadding(0, 0, 0, 0);
                name.setGravity(Gravity.CENTER);
                name.setMaxLines(1);
                name.setEllipsize(TextUtils.TruncateAt.END);

                if (typeface != null) {
                    name.setTypeface(typeface);
                }

            }
        }
    }

    /**
     * 设置选中
     */
    private void setSelectedItem() {
        //获取可视范围的第一个控件的位置
        int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePos == RecyclerView.NO_POSITION) {
            return;
        }
        Rect rect = new Rect();
        if (mLayoutManager != null && mLayoutManager.findViewByPosition(firstVisiblePos) != null) {
            Objects.requireNonNull(mLayoutManager.findViewByPosition(firstVisiblePos)).getHitRect(rect);
        }
        //被选中item是否已经滑动超出中间区域
        boolean overScroll = Math.abs(rect.top) > mItemHeight / 2.0f;
        //更新可视范围内所有item的样式
        TextView item;
        for (int i = 0; i < 1 + mOffset * 2; i++) {
            if (overScroll) {
                item = (TextView) mLayoutManager.findViewByPosition(firstVisiblePos + i + 1);
            } else {
                item = (TextView) mLayoutManager.findViewByPosition(firstVisiblePos + i);
            }
            if (item != null) {
                setTextShowType(i == mOffset, item);
            }
        }

        if (mOnSelectListener != null) {
            if (overScroll) {
                mSelected = firstVisiblePos + 1;//必须要先更新位置，否则可能会导致获取内容失败
                mOnSelectListener.onScrollCenterJudge(firstVisiblePos + 1, mDatas.get(firstVisiblePos + 1),
                        mDatasSize.compareTo(firstVisiblePos + mOffset + 1) == 0, true);
            } else {
                mSelected = firstVisiblePos;
                mOnSelectListener.onScrollCenterJudge(firstVisiblePos, mDatas.get(firstVisiblePos),
                        mDatasSize.compareTo(firstVisiblePos + mOffset) == 0, true);
            }
        }
    }

    /**
     * 设置文字显示样式
     *
     * @param isSelect 是否是选中的
     * @param textView 文本控件
     */
    private void setTextShowType(boolean isSelect, @NotNull TextView textView) {
        if (isSelect) {
            textView.setTextColor(mSelectTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize);
            textView.getPaint().setFakeBoldText(selectTextIsBold);
        } else {
            textView.setTextColor(mUnselectTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectTextSize);
            textView.getPaint().setFakeBoldText(unSelectTextIsBold);
        }
    }

    /**
     * 分隔线
     */
    private class DividerItemDecoration extends ItemDecoration {
        @Override
        public void onDrawOver(Canvas c, @NotNull RecyclerView parent, @NotNull State state) {
            //绘制分割线
            float startX = getMeasuredWidth() / 2.0f - mDividerWidth / 2 + getPaddingLeft();
            float topY = mItemHeight * mOffset + getPaddingTop();
            float endX = getMeasuredWidth() / 2.0f + mDividerWidth / 2 - getPaddingRight();
            float bottomY = mItemHeight * (mOffset + 1) - getPaddingBottom();

            c.drawLine(startX, topY, endX, topY, mPaint);
            c.drawLine(startX, bottomY, endX, bottomY, mPaint);
        }
    }

    /**
     * 滑动监听
     */
    private class OnWheelScrollListener extends OnScrollListener {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //当控件停止滚动时，获取可视范围第一个item的位置，滚动调整控件以使选中的item刚好处于正中间
                int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePos == RecyclerView.NO_POSITION) {
                    return;
                }
                Rect rect = new Rect();
                if (mLayoutManager != null && mLayoutManager.findViewByPosition(firstVisiblePos) != null) {
                    Objects.requireNonNull(mLayoutManager.findViewByPosition(firstVisiblePos)).getHitRect(rect);
                }
                if (Math.abs(rect.top) > mItemHeight / 2) {
                    smoothScrollBy(0, rect.bottom);
                    mSelected = firstVisiblePos + 1;

                } else {
                    smoothScrollBy(0, rect.top);
                    mSelected = firstVisiblePos;
                }

                setSelectedItem();

            }
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            setSelectedItem();
        }
    }

    /**
     * 选择监听
     */
    public abstract static class OnSelectListener {
        /**
         * 上一次记录的滑动位置
         */
        private Integer lastScrollPosition = -1;

        /**
         * 当前滑动到的中间的选项，做逻辑判定，如果和上一次相同就不要返回了，这个方法不能重载或重写
         *
         * @param position    位置
         * @param data        位置内容
         * @param isLast      是否是最后一个选项
         * @param isCheckLast 时候需要和上一个暂存内容做比较
         */
        public void onScrollCenterJudge(int position, Object data, boolean isLast, boolean isCheckLast) {
            if (isCheckLast) {
                if (lastScrollPosition.compareTo(position) != 0) {
                    lastScrollPosition = position;
                    onScrollCenter(position, data, isLast);
                }
            } else {
                lastScrollPosition = position;
                onScrollCenter(position, data, isLast);
            }
        }

        /**
         * 获取要显示的item的内容（可以不重写）
         *
         * @param position 位置
         * @return 显示文本
         */
        public String getShowItemText(int position) {return "";}

        /**
         * 当前滑动到的中间的选项,isLast,是否是最后一个
         *
         * @param position 位置
         * @param data     数据
         * @param isLast   是否是最后一个
         */
        protected abstract void onScrollCenter(int position, Object data, boolean isLast);
    }
}

