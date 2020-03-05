package android.lorenwang.customview.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.app.AtlwThreadUtils;
import android.lorenwang.tools.app.AtlwViewUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 功能作用：文本指示器viewpager
 * 创建时间：2020-01-14 11:40
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwCustomViewPager extends FrameLayout {
    private Context context;
    /**
     * 无指示器
     */
    private final int INDICATOR_TYPE_NONE = 0;
    /**
     * 文本指示器
     */
    private final int INDICATOR_TYPE_TEXT = 1;
    /**
     * 点指示器
     */
    private final int INDICATOR_TYPE_POINT = 2;
    /**
     * 林波控件
     */
    private ViewPager2 vpgBaseList;
    /**
     * 指示器
     */
    private View indicatorView;
    /**
     * 选中指示器颜色
     */
    private ColorStateList selectIndicatorColor = ColorStateList.valueOf(Color.parseColor("#ffffff"));
    /**
     * 未选中指示器颜色
     */
    private ColorStateList normalIndicatorColor = ColorStateList.valueOf(Color.parseColor("#55ffffff"));
    /**
     * 指示器类型
     */
    private int indicatorType = INDICATOR_TYPE_NONE;
    /**
     * 数据列表大小
     */
    private int dataListSize = 0;


    /**
     * 自定播放时间,默认不进行轮播
     */
    private Long autoplayTime = 0L;
    /**
     * 自动播放线程
     */
    private Runnable autoplayRunnable = new Runnable() {
        @Override
        public void run() {
            if (context == null || ((context instanceof Activity) && ((Activity) context).isFinishing())) {
                //移除线程
                AtlwThreadUtils.getInstance().removeRunnable(autoplayRunnable);
                return;
            }
            if (autoplayTime != null && autoplayTime > 0) {
                if (vpgBaseList != null && vpgBaseList.getAdapter() != null) {
                    vpgBaseList.setCurrentItem(vpgBaseList.getCurrentItem() + 1, true);
                }
                AtlwThreadUtils.getInstance().postOnUiThreadDelayed(this, autoplayTime);
            }
        }
    };


    public AvlwCustomViewPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwCustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwCustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager, this);
        vpgBaseList = view.findViewById(R.id.vpgBaseList);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwCustomViewPager);
        //设置指示器类型
        switch (indicatorType = attributes.getInt(R.styleable.AvlwCustomViewPager_avlwCVPIndicatorType, 0)) {
            case INDICATOR_TYPE_TEXT://文本样式
                indicatorView = LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager_indicator_text, this).findViewById(R.id.tvBaseText);
                break;
            case INDICATOR_TYPE_POINT:
                indicatorView = LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager_indicator_point, this).findViewById(R.id.lnBaseIndicator);
                break;
            case INDICATOR_TYPE_NONE:
            default:
                break;
        }
        //设置指示器时间
        setAutoplayTime((long) attributes.getInt(R.styleable.AvlwCustomViewPager_avlwCVPAutoplayTime, autoplayTime.intValue()));
        attributes.recycle();
    }

    public View getVpgBaseList() {
        return vpgBaseList;
    }

    /**
     * 设置自动播放时间，如果时间为空或者为0则停止自动播放
     *
     * @param autoplayTime 自动播放时间
     */
    public void setAutoplayTime(Long autoplayTime) {
        this.autoplayTime = autoplayTime;
        //关闭自动切换线程
        AtlwThreadUtils.getInstance().removeRunnable(autoplayRunnable);
        if (autoplayTime != null && autoplayTime > 0) {
            AtlwThreadUtils.getInstance().postOnUiThreadDelayed(autoplayRunnable, autoplayTime);
            if (dataListSize > 0) {
                //设置当前显示到中间位置，以达到左右皆可滑动的效果,初始位置为100左右
                vpgBaseList.setCurrentItem(dataListSize * (10000 / dataListSize));
            }
        }
    }

    /**
     * 设置数据适配器
     *
     * @param adapter 适配器
     */
    public void setListDataAdapter(@Nullable @SuppressWarnings("rawtypes") final RecyclerView.Adapter adapter) {
        setAdapter(adapter);
    }

    /**
     * 设置数据适配器
     *
     * @param adapter 适配器
     * @param <T>     数据泛型
     */
    private <T> void setAdapter(@Nullable @SuppressWarnings("rawtypes") final RecyclerView.Adapter adapter) {
        if (adapter != null) {
            vpgBaseList.setAdapter(adapter);
            //注册页面改变监听
            vpgBaseList.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (indicatorView != null) {
                        if (indicatorType == INDICATOR_TYPE_TEXT) {
                            if (dataListSize > 0) {
                                ((AppCompatTextView) indicatorView).setText((position % dataListSize + 1) + "/" + dataListSize);
                            } else {
                                ((AppCompatTextView) indicatorView).setText("0/0");
                            }
                        } else if (indicatorType == INDICATOR_TYPE_POINT) {
                            changeIndicatorTypePoint();
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
            //关闭自动切换线程
            AtlwThreadUtils.getInstance().removeRunnable(autoplayRunnable);
            //判断是否是point类型,如果是该类型则需要初始化数据
            initIndicatorTypePoint();
            //开启自动切换线程
            setAutoplayTime(autoplayTime);
        }
    }

    /**
     * 初始化point类型指示器
     */
    private void initIndicatorTypePoint() {
        if (indicatorView == null || indicatorType != INDICATOR_TYPE_POINT
                || vpgBaseList.getAdapter() == null) {
            return;
        }
        LinearLayout mIndicatorContainer = (LinearLayout) this.indicatorView;
        mIndicatorContainer.removeAllViews();
        if (dataListSize > 0) {
            int current = vpgBaseList.getCurrentItem() % dataListSize;
            int widthHeight = (int) AtlwScreenUtils.getInstance().dip2px(5);
            for (int i = 0; i < dataListSize; i++) {
                AppCompatImageView imageView = new AppCompatImageView(getContext());
                AtlwViewUtils.getInstance().setViewMarginParams(imageView,
                        new LinearLayout.LayoutParams(widthHeight, widthHeight),
                        6, 0, 6, 0);
                imageView.setImageResource(R.drawable.avlw_solid_radius_8);
                if (i == current) {
                    AtlwViewUtils.getInstance().setImageSrcTint(imageView, selectIndicatorColor);
                } else {
                    AtlwViewUtils.getInstance().setImageSrcTint(imageView, normalIndicatorColor);
                }
                mIndicatorContainer.addView(imageView);
            }
        }
    }

    /**
     * 修改point类型指示器状态
     */
    private void changeIndicatorTypePoint() {
        if (indicatorView == null || indicatorType != INDICATOR_TYPE_POINT
                || vpgBaseList.getAdapter() == null) {
            return;
        }
        LinearLayout mIndicatorContainer = (LinearLayout) this.indicatorView;
        if (dataListSize > 0) {
            int current = vpgBaseList.getCurrentItem() % dataListSize;
            for (int i = 0; i < dataListSize; i++) {
                if (i == current) {
                    AtlwViewUtils.getInstance().setImageSrcTint(((AppCompatImageView) mIndicatorContainer.getChildAt(i)), selectIndicatorColor);
                } else {
                    AtlwViewUtils.getInstance().setImageSrcTint(((AppCompatImageView) mIndicatorContainer.getChildAt(i)), normalIndicatorColor);
                }
            }
        }
    }

    /**
     * 设置数据列表大小
     *
     * @param dataListSize 数据列表大小
     */
    public void setDataListSize(int dataListSize) {
        this.dataListSize = dataListSize;
        //重新初始化指示器
        initIndicatorTypePoint();
    }
}
