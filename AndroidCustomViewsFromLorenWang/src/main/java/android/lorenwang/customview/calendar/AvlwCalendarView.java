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
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

        @Override
        public View getWeekDayView() {
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setGravity(Gravity.CENTER);
            return textView;
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
        init(context, null, -1);
    }

    public AvlwCalendarView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwCalendarView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwCalendarView);
        caledarWeekFirst = attributes.getInt(R.styleable.AvlwCalendarView_avlwCaledarWeekFirst, caledarWeekFirst);
        showOnlyMonth = attributes.getBoolean(R.styleable.AvlwCalendarView_avlwCaledarShowOnlyMonth, showOnlyMonth);
        selecOne = attributes.getBoolean(R.styleable.AvlwCalendarView_avlwCaledarSelecOne, selecOne);
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
        addView(contentShowContainer);

        //标题组件新增标题元素
        View view;
        LayoutParams layoutParams;
        for (int i = 0; i < 7; i++) {
            view = calendarViewGetChild.getWeekTitleView((caledarWeekFirst + i) % 7);
            view.measure(0, 0);
            layoutParams = AtlwViewUtil.getInstance().getViewLayoutParams(LayoutParams.class, view, view.getMeasuredWidth(),
                    view.getMeasuredHeight());
            layoutParams.weight = 1;
            view.setLayoutParams(layoutParams);
            weekTitleContainer.addView(view);
        }

        //初始化数据
        setShowMonthCount(showMonthLeftCount, showMonthRightCount);
    }

    /**
     * 设置显示月份数量
     *
     * @param showMonthLeftCount  左侧数量
     * @param showMonthRightCount 右侧数量
     */
    public void setShowMonthCount(int showMonthLeftCount, int showMonthRightCount) {
        this.showMonthLeftCount = showMonthLeftCount;
        this.showMonthRightCount = showMonthRightCount;
        initMonthData();
    }

    /**
     * 设置字视图获取
     *
     * @param calendarViewGetChild 字视图获取接口
     */
    public void setCalendarViewGetChild(AvlwCalendarViewGetChild calendarViewGetChild) {
        if (calendarViewGetChild != null) {
            this.calendarViewGetChild = calendarViewGetChild;
        }
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
     * 设置月份数据
     */
    private void initMonthData() {
        vpgViewList.clear();
        //最大高度
        int maxHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
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

            //高度处理
            recyclerView.measure(0, 0);
            view = calendarViewGetChild.getWeekDayView();
            view.measure(0, 0);
            maxHeight = Math.max(recyclerView.getHeight(),
                    Math.max(maxHeight, getViewPagerItemViewHeight(monthTimeList.size(), view.getMeasuredHeight())));
        }
        //添加本月
        time = System.currentTimeMillis();
        monthTimeList = JtlwDateTimeUtils.getInstance().getMonthTimeList(time, caledarWeekFirst, showOnlyMonth);
        recyclerView = getViewPagerItemView(monthTimeList, vpgViewList.size());
        vpgViewList.add(recyclerView);
        //高度处理
        recyclerView.measure(0, 0);
        view = calendarViewGetChild.getWeekDayView();
        view.measure(0, 0);
        maxHeight = Math.max(recyclerView.getHeight(),
                Math.max(maxHeight, getViewPagerItemViewHeight(monthTimeList.size(), view.getMeasuredHeight())));


        //添加后面的的
        for (int i = 0; i < showMonthRightCount; i++) {
            time = JtlwDateTimeUtils.getInstance().getNextMonthStartDayTime(time);
            monthTimeList = JtlwDateTimeUtils.getInstance().getMonthTimeList(time, caledarWeekFirst, showOnlyMonth);
            recyclerView = getViewPagerItemView(monthTimeList, vpgViewList.size());
            vpgViewList.add(recyclerView);

            //高度处理
            recyclerView.measure(0, 0);
            view = calendarViewGetChild.getWeekDayView();
            view.measure(0, 0);
            maxHeight = Math.max(recyclerView.getHeight(),
                    Math.max(maxHeight, getViewPagerItemViewHeight(monthTimeList.size(), view.getMeasuredHeight())));
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
        //定位到当前月
        contentShowContainer.setCurrentItem(showMonthLeftCount, false);
        contentShowContainer.setOffscreenPageLimit(vpgViewList.size());
        //重新修改高度
        contentShowContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight));
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
                                        selectPageList.remove(String.valueOf(vpgPosition));
                                        notifyItemChanged(position);
                                    } else {
                                        selectTimeOne = time;
                                        selectPageList.clear();
                                        selectPageList.add(String.valueOf(vpgPosition));
                                        //更新所有轮播页面
                                        updateViewPager();
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    selectTimeOne = time;
                                    selectPageList.clear();
                                    selectPageList.add(String.valueOf(vpgPosition));
                                    notifyItemChanged(position);
                                }
                            } else {
                                //起始为空,赋值起始
                                if (selectTimeOne == null) {
                                    selectTimeOne = time;
                                    selectPageList.add(String.valueOf(vpgPosition));
                                    notifyItemChanged(position);
                                    return;
                                }
                                //起始和当前选中是一个
                                if (time.compareTo(selectTimeOne) == 0) {
                                    selectTimeOne = null;
                                    selectPageList.remove(String.valueOf(vpgPosition));

                                    //判断结束是否为空,不为空则要进行数据转换
                                    if (selectTimeTwo != null) {
                                        selectTimeOne = selectTimeTwo;
                                        selectTimeTwo = null;
                                        //最后一位
                                        String last = selectPageList.get(selectPageList.size() - 1);
                                        selectPageList.clear();
                                        selectPageList.add(last);
                                    }
                                    notifyItemChanged(position);
                                    updateViewPager();
                                    return;
                                }
                                //起始不为空，但是结束为空
                                if (selectTimeTwo == null) {
                                    selectTimeTwo = time;
                                    //第一个元素为起始位置
                                    int start = Integer.parseInt(selectPageList.get(0));
                                    if (start < vpgPosition) {
                                        for (int i = start + 1; i < vpgPosition; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                    } else {
                                        for (int i = vpgPosition + 1; i < start; i++) {
                                            selectPageList.add(String.valueOf(i));
                                        }
                                    }
                                    //放到后面
                                    selectPageList.add(String.valueOf(vpgPosition));
                                    //更新所有轮播页面
                                    updateViewPager();
                                    return;
                                }
                                //结束也不为空，但是和当前选择是否是一个
                                if (time.compareTo(selectTimeTwo) == 0) {
                                    selectTimeTwo = null;
                                    notifyItemChanged(position);
                                    int size = selectPageList.size();
                                    for (int i = 1; i < size; i++) {
                                        selectPageList.remove(selectPageList.get(i));
                                    }
                                    //更新所有轮播页面
                                    updateViewPager();
                                    return;
                                }
                                //点击的是非起始以及结束区域，但是此时已经有起始和结束了
                                if (time < selectTimeOne) {
                                    selectTimeOne = time;
                                    //第一个元素为起始位置
                                    int start = Integer.parseInt(selectPageList.get(0));
                                    for (int i = start - 1; i >= vpgPosition; i--) {
                                        selectPageList.add(0, String.valueOf(i));
                                    }
                                    //更新所有轮播页面
                                    updateViewPager();
                                    return;
                                }
                                //点击大于第二时间
                                if (time > selectTimeTwo) {
                                    selectTimeTwo = time;
                                    //第一个元素为起始位置
                                    int start = Integer.parseInt(selectPageList.get(0));
                                    for (int i = start + 1; i <= vpgPosition; i++) {
                                        selectPageList.add(String.valueOf(i));
                                    }
                                    //更新所有轮播页面
                                    updateViewPager();
                                    return;
                                }
                                //在中间则修改第二时间
                                selectTimeTwo = time;
                                int start = Integer.parseInt(selectPageList.get(0));
                                selectPageList.clear();
                                for (int i = start; i <= vpgPosition; i++) {
                                    selectPageList.add(String.valueOf(i));
                                }
                                //更新所有轮播页面
                                updateViewPager();
                            }
                        }
                        updateViewPager();
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
     * 获取轮播item高度
     *
     * @return 高度
     */
    private int getViewPagerItemViewHeight(int dataListSize, int childItemHeight) {
        if (dataListSize % 7 == 0) {
            return childItemHeight * dataListSize / 7;
        } else {
            return childItemHeight * (dataListSize / 7 + 1);
        }
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
