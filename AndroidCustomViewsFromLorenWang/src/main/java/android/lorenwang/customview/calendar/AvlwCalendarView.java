package android.lorenwang.customview.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;

/**
 * 功能作用：日历控件
 * 创建时间：2021-02-08 8:11 下午
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
public class AvlwCalendarView extends LinearLayoutCompat {
    /**
     * 显示月份数量，默认12个月
     */
    private int showMonthLeftCount = 6;
    private int showMonthRightCount = 6;
    /**
     * 每周的第一天是周几，默认周日，0代表周日，1代表周一，后续依次累加
     */
    private int caledarWeekFirst = 0;
    /**
     * 是否只显示一个月
     */
    private boolean showOnlyMonth = true;
    /**
     * 是否是单选
     */
    private boolean selecOne = true;
    /**
     * 是否使用自动高度切换
     */
    private boolean useAutoHeight = true;
    /**
     * 轮播列表页面
     */
    private final List<RecyclerView> vpgViewList = new ArrayList<>();
    /**
     * 标题容器
     */
    private LinearLayoutCompat weekTitleContainer;
    /**
     * 内容显示轮播
     */
    private ViewPager2 contentShowContainer;
    /**
     * 日历子视图获取
     */
    private AvlwCalendarViewGetChild calendarViewGetChild = new AvlwCalendarViewGetChild() {
        @Override
        public View getWeekTitleView(int weekDay) {
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setText(String.valueOf(weekDay));
            textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            textView.setBackgroundColor(Color.BLUE);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

        @Override
        public int getWeekTitleViewHeight() {
            return 100;
        }

        @Override
        public View getWeekDayView() {
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            return textView;
        }

        @Override
        public int getWeekDayViewHeight() {
            return 200;
        }

        @Override
        public void setWeekDayView(RecyclerView.ViewHolder holder, Long time, boolean select) {
            if (time != null) {
                ((AppCompatTextView) holder.itemView).setText(JtlwDateTimeUtils.getInstance().getFormatDateTime("MM-dd", time));
            } else {
                ((AppCompatTextView) holder.itemView).setText("");
            }
            if (select) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    };
    /**
     * 控件回调
     */
    private AvlwCalendarViewCallback calendarViewCallback = new AvlwCalendarViewCallback() {
        @Override
        public void selectChange(Long selectTimeOne, Long selectTimeTwo) {

        }
    };
    /**
     * 第一个选择的时间
     */
    private Long selectTimeOne = null;
    /**
     * 第二个选择的时间
     */
    private Long selectTimeTwo = null;
    /**
     * 已选择的页面下标
     */
    private final List<String> selectPageList = new ArrayList<>();

    public AvlwCalendarView(@NonNull @NotNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwCalendarView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwCalendarView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwCalendarView);
        caledarWeekFirst = attributes.getInt(R.styleable.AvlwCalendarView_avlwCaledarWeekFirst, caledarWeekFirst);
        showOnlyMonth = attributes.getBoolean(R.styleable.AvlwCalendarView_avlwCaledarShowOnlyMonth, showOnlyMonth);
        selecOne = attributes.getBoolean(R.styleable.AvlwCalendarView_avlwCaledarSelecOne, selecOne);
        useAutoHeight = attributes.getBoolean(R.styleable.AvlwCalendarView_avlwCaledarUseAutoHeight, useAutoHeight);
        attributes.recycle();

        //设置当前组件元素
        setOrientation(VERTICAL);
        //添加标题组件元素
        weekTitleContainer = new LinearLayoutCompat(context);
        weekTitleContainer.setOrientation(LinearLayoutCompat.HORIZONTAL);
        weekTitleContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(weekTitleContainer);
        //添加内容组件
        contentShowContainer = new ViewPager2(context);
        contentShowContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentShowContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (useAutoHeight) {
                    RecyclerView view = vpgViewList.get(position);
                    if (view != null && view.getAdapter() != null) {
                        //重新修改高度
                        contentShowContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                calendarViewGetChild.getWeekDayViewHeight() * getDayRowCount()));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        addView(contentShowContainer);


        //先初始一套默认数据
        initWeekTitleData();
        //初始化数据
        setShowMonthCount(showMonthLeftCount, showMonthRightCount);
    }

    /**
     * 设置显示月份数量
     *
     * @param showMonthLeftCount  左侧数量
     * @param showMonthRightCount 右侧数量
     * @return 当前实例
     */
    public AvlwCalendarView setShowMonthCount(int showMonthLeftCount, int showMonthRightCount) {
        this.showMonthLeftCount = showMonthLeftCount;
        this.showMonthRightCount = showMonthRightCount;
        initMonthData();
        return this;
    }

    /**
     * 切换到当前月
     *
     * @return 当前实例
     */
    public AvlwCalendarView changeToNowMonth() {
        //定位到当前月
        contentShowContainer.setCurrentItem(showMonthLeftCount, false);
        return this;
    }

    /**
     * 设置字视图获取
     *
     * @param calendarViewGetChild 字视图获取接口
     * @return 当前实例
     */
    public AvlwCalendarView setCalendarViewGetChild(AvlwCalendarViewGetChild calendarViewGetChild) {
        if (calendarViewGetChild != null) {
            this.calendarViewGetChild = calendarViewGetChild;
            initWeekTitleData();
        }
        return this;
    }

    /**
     * 设置回调
     *
     * @param calendarViewCallback 回调
     * @return 当前实例
     */
    public AvlwCalendarView setCalendarViewCallback(AvlwCalendarViewCallback calendarViewCallback) {
        if (calendarViewCallback != null) {
            this.calendarViewCallback = calendarViewCallback;
        }
        return this;
    }

    /**
     * 获取内容轮播控件
     *
     * @return 内容轮播控件
     */
    public ViewPager2 getContentShowContainer() {
        return contentShowContainer;
    }

    /**
     * 获取选中的时间区间开始
     *
     * @return 时间，可能为空
     */
    public Long getSelectTimeOne() {
        return selectTimeOne;
    }

    /**
     * 获取选中的时间区间结束
     *
     * @return 时间，可能为空
     */
    public Long getSelectTimeTwo() {
        return selectTimeTwo;
    }

    /**
     * 获取每周的第一天
     *
     * @return 每周的第一天是周几，默认周日，0代表周日，1代表周一，后续依次累加
     */
    public int getCaledarWeekFirst() {
        return caledarWeekFirst;
    }

    /**
     * 获取当前显示的数据列表
     *
     * @return 当前显示的数据列表控件
     */
    public RecyclerView getCurrentShowRecycleView() {
        return vpgViewList.get(contentShowContainer.getCurrentItem());
    }

    /**
     * 获取日期显示的行数
     *
     * @return 日期显示的行数
     */
    public int getDayRowCount() {
        RecyclerView view = getCurrentShowRecycleView();
        if (view != null && view.getAdapter() != null) {
            int count = view.getAdapter().getItemCount();
            int rows;
            if (count % 7 == 0) {
                rows = count / 7;
            } else {
                rows = count / 7 + 1;
            }
            return rows;
        }
        return 0;
    }

    /**
     * 初始化标题数据
     */
    private void initWeekTitleData() {
        //标题组件新增标题元素
        View view;
        weekTitleContainer.removeAllViews();
        LayoutParams layoutParams;
        for (int i = 0; i < 7; i++) {
            view = calendarViewGetChild.getWeekTitleView((caledarWeekFirst + i) % 7);
            layoutParams = AtlwViewUtil.getInstance().getViewLayoutParams(LayoutParams.class, view, ViewGroup.LayoutParams.MATCH_PARENT,
                    calendarViewGetChild.getWeekTitleViewHeight());
            layoutParams.weight = 1;
            view.setLayoutParams(layoutParams);
            weekTitleContainer.addView(view);
        }
    }

    /**
     * 设置月份数据
     */
    private synchronized void initMonthData() {
        vpgViewList.clear();
        //起始时间
        long time = System.currentTimeMillis();
        List<Long> monthTimeList;
        RecyclerView recyclerView;
        View view;
        //添加之前的
        for (int i = showMonthLeftCount - 1; i >= 0; i--) {
            time = JtlwDateTimeUtils.getInstance().getLastMonthStartDayTime(time);
            monthTimeList = JtlwDateTimeUtils.getInstance().getMonthTimeList(time, caledarWeekFirst, showOnlyMonth);
            recyclerView = getViewPagerItemView(monthTimeList, i);
            vpgViewList.add(0, recyclerView);
        }

        //添加本月
        time = System.currentTimeMillis();
        monthTimeList = JtlwDateTimeUtils.getInstance().getMonthTimeList(time, caledarWeekFirst, showOnlyMonth);
        recyclerView = getViewPagerItemView(monthTimeList, vpgViewList.size());
        vpgViewList.add(recyclerView);

        //添加后面的的
        for (int i = 0; i < showMonthRightCount; i++) {
            time = JtlwDateTimeUtils.getInstance().getNextMonthStartDayTime(time);
            monthTimeList = JtlwDateTimeUtils.getInstance().getMonthTimeList(time, caledarWeekFirst, showOnlyMonth);
            recyclerView = getViewPagerItemView(monthTimeList, vpgViewList.size());
            vpgViewList.add(recyclerView);
        }
        contentShowContainer.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(vpgViewList.get(viewType)) {};
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return vpgViewList.size();
            }
        });

        //容器高度处理
        if (!useAutoHeight) {
            //重新修改高度
            contentShowContainer.setLayoutParams(
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calendarViewGetChild.getWeekDayViewHeight() * 7));
        }
    }

    /**
     * 获取轮播显示数据
     *
     * @param dataList    数据列表
     * @param vpgPosition 下标位置
     * @return view
     */
    private RecyclerView getViewPagerItemView(List<Long> dataList, int vpgPosition) {
        RecyclerView recycleView = new RecyclerView(getContext());
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setLayoutParams(new ViewPager2.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recycleView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(calendarViewGetChild.getWeekDayView()) {};
            }

            @Override
            public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
                Long time = dataList.get(position);
                if (selecOne) {
                    calendarViewGetChild.setWeekDayView(holder, dataList.get(position),
                            time != null && selectTimeOne != null && time.compareTo(selectTimeOne) == 0);
                } else {
                    boolean select = false;
                    if (time != null && selectTimeOne != null && time.compareTo(selectTimeOne) == 0) {
                        select = true;
                    } else if (time != null && selectTimeTwo != null && time.compareTo(selectTimeTwo) == 0) {
                        select = true;
                    } else if (time != null && selectTimeOne != null && time.compareTo(selectTimeOne) > 0 && selectTimeTwo != null && time.compareTo(
                            selectTimeTwo) < 0) {
                        select = true;
                    }
                    calendarViewGetChild.setWeekDayView(holder, dataList.get(position), select);
                }
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (time != null) {
                            if (selecOne) {
                                if (selectTimeOne != null) {
                                    if (time.compareTo(selectTimeOne) == 0) {
                                        selectTimeOne = null;
                                        selectPageList.clear();
                                        notifyItemChanged(position);
                                    } else {
                                        selectTimeOne = time;
                                        //先更新旧的
                                        updateViewPager();
                                        //替换新的
                                        selectPageList.clear();
                                        selectPageList.add(String.valueOf(vpgPosition));
                                        //更新所有轮播页面
                                        updateViewPager();
                                    }
                                } else {
                                    selectTimeOne = time;
                                    selectPageList.clear();
                                    selectPageList.add(String.valueOf(vpgPosition));
                                    notifyItemChanged(position);
                                }
                            } else {
                                if (selectTimeOne == null && selectTimeTwo == null) {
                                    selectTimeOne = time;
                                    selectPageList.add(String.valueOf(vpgPosition));
                                    notifyItemChanged(position);
                                } else if (selectTimeOne != null && selectTimeTwo == null) {
                                    //判断是否和起始一样
                                    if (time.compareTo(selectTimeOne) == 0) {
                                        selectTimeOne = null;
                                        selectPageList.clear();
                                        notifyItemChanged(position);
                                    } else if (time.compareTo(selectTimeOne) < 0) {
                                        //设置个起始，起始设置给结束
                                        selectTimeTwo = selectTimeOne;
                                        selectTimeOne = time;
                                        //先进行更新清除
                                        updateViewPager();
                                        //再进行新数据替换
                                        int start = Integer.parseInt(selectPageList.get(0));
                                        selectPageList.clear();
                                        for (int i = vpgPosition; i <= start; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                        updateViewPager();
                                    } else {
                                        //设置给结束
                                        selectTimeTwo = time;
                                        int start = Integer.parseInt(selectPageList.get(0));
                                        //先进行更新清除
                                        updateViewPager();
                                        //再进行新数据替换
                                        selectPageList.clear();
                                        for (int i = start; i <= vpgPosition; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                        updateViewPager();
                                    }
                                } else if (selectTimeOne != null && selectTimeTwo != null) {
                                    //判断是否和起始一样
                                    if (time.compareTo(selectTimeOne) == 0) {
                                        selectTimeOne = selectTimeTwo;
                                        selectTimeTwo = null;
                                        //先进行更新清除
                                        updateViewPager();
                                        //再进行新数据替换
                                        String last = selectPageList.get(selectPageList.size() - 1);
                                        selectPageList.clear();
                                        selectPageList.add(last);
                                        updateViewPager();
                                    } else if (time.compareTo(selectTimeOne) < 0) {
                                        //比开始小
                                        selectTimeOne = time;
                                        //第一个元素为起始位置
                                        int start = Integer.parseInt(selectPageList.get(0));
                                        for (int i = start - 1; i >= vpgPosition; i--) {
                                            selectPageList.add(0, String.valueOf(i));
                                        }
                                        //更新所有轮播页面
                                        updateViewPager();
                                    } else if (time.compareTo(selectTimeTwo) < 0) {
                                        //比结束小，修改结束
                                        selectTimeTwo = time;
                                        //先进行更新清除
                                        updateViewPager();
                                        //再进行新数据替换
                                        int start = Integer.parseInt(selectPageList.get(0));
                                        selectPageList.clear();
                                        for (int i = start; i <= vpgPosition; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                        //更新所有轮播页面
                                        updateViewPager();
                                    } else if (time.compareTo(selectTimeTwo) == 0) {
                                        //就是结束位置
                                        selectTimeTwo = null;
                                        //先进行更新清除
                                        updateViewPager();
                                        //再进行新数据替换
                                        String start = selectPageList.get(0);
                                        selectPageList.clear();
                                        selectPageList.add(start);
                                        //更新所有轮播页面
                                        updateViewPager();
                                    } else {
                                        //比结束大
                                        selectTimeTwo = time;
                                        //第一个元素为起始位置
                                        int start = Integer.parseInt(selectPageList.get(0));
                                        for (int i = start + 1; i <= vpgPosition; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                        //更新所有轮播页面
                                        updateViewPager();
                                    }
                                }
                            }
                            //回调数据
                            calendarViewCallback.selectChange(selectTimeOne, selectTimeTwo);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        });
        return recycleView;
    }

    /**
     * 更新列表所有页面
     */
    private void updateViewPager() {
        Map<String, String> map = new HashMap<>();
        for (String s : selectPageList) {
            map.put(s, s);
        }
        for (String s : map.keySet()) {
            vpgViewList.get(Integer.parseInt(s)).getAdapter().notifyDataSetChanged();
        }
        map.clear();
    }
}
