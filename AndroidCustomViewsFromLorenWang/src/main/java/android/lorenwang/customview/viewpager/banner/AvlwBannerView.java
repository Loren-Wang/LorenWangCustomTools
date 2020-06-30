package android.lorenwang.customview.viewpager.banner;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
 *
 * @author wangliang
 */

public class AvlwBannerView extends ConstraintLayout {
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
     * 轮播控件
     */
    private ViewPager2 vpgBaseList;
    /**
     * 指示器
     */
    private View indicatorView;
    /**
     * 指示器
     */
    private AvlwBaseBannerIndicator bannerIndicator;
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
    private final Runnable autoplayRunnable = new Runnable() {
        @Override
        public void run() {
            if (context == null || ((context instanceof Activity) && ((Activity) context).isFinishing())) {
                //移除线程
                AtlwThreadUtils.getInstance().removeRunnable(autoplayRunnable);
                return;
            }
            //切换位置
            if (vpgBaseList != null && vpgBaseList.getAdapter() != null) {
                int next = vpgBaseList.getCurrentItem() + 1;
                if (next >= vpgBaseList.getAdapter().getItemCount()) {
                    next = 0;
                }
                vpgBaseList.setCurrentItem(next, true);
            }
        }
    };


    public AvlwBannerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwBannerView(@NonNull Context context, @Nullable AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager, this);
        vpgBaseList = view.findViewById(R.id.vpgBaseList);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwBannerView);
        //设置指示器类型
        switch (indicatorType = attributes.getInt(R.styleable.AvlwBannerView_avlwBvIndicatorType,
                0)) {
            case INDICATOR_TYPE_TEXT:
                //文本样式
                bannerIndicator = new AvlwBannerTextIndicator(attributes, indicatorView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager_indicator_text, this).findViewById(R.id.tvBaseText));
                break;
            case INDICATOR_TYPE_POINT:
                //点样式
                bannerIndicator = new AvlwBannerDotIndicator(attributes, indicatorView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_custom_viewpager_indicator_point, this).findViewById(R.id.lnBaseIndicator));
                break;
            case INDICATOR_TYPE_NONE:
            default:
                break;
        }
        if (indicatorView != null) {
            ConstraintLayout.LayoutParams viewLayoutParams =
                    AtlwViewUtils.getInstance().getViewLayoutParams(ConstraintLayout.LayoutParams.class, indicatorView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置边距
            int distance = (int) AtlwScreenUtils.getInstance().dip2px(10F);
            viewLayoutParams.setMargins(
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorMarginLeft, distance),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorMarginTop, distance),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorMarginRight, distance),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorMarginBottom, distance));
            indicatorView.setLayoutParams(viewLayoutParams);
            //设置内边距
            indicatorView.setPadding(
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorPaddingLeft, distance),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorPaddingTop, 0),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorPaddingRight, distance),
                    attributes.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorPaddingBottom, 0));
            //设置背景
            indicatorView.setBackgroundResource(attributes.getResourceId(R.styleable.AvlwBannerView_avlwBvIndicatorBackground,
                    R.drawable.avlw_solid_radius_8));
            //设置背景渲染色
            AtlwViewUtils.getInstance().setBackgroundTint(indicatorView,
                    ColorStateList.valueOf(attributes.getColor(R.styleable.AvlwBannerView_avlwBvIndicatorBackgroundTintColor,
                            Color.TRANSPARENT)));

            //设置指示器位置
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(this);
            if (attributes.getBoolean(R.styleable.AvlwBannerView_avlwBvIndicatorTheLeft,
                    bannerIndicator.getDefaultTheLeft())) {
                constraintSet.connect(indicatorView.getId(), ConstraintSet.LEFT,
                        R.id.vpgBaseList, ConstraintSet.LEFT);
            }
            if (attributes.getBoolean(R.styleable.AvlwBannerView_avlwBvIndicatorTheTop,
                    bannerIndicator.getDefaultTheTop())) {
                constraintSet.connect(indicatorView.getId(), ConstraintSet.TOP,
                        R.id.vpgBaseList, ConstraintSet.TOP);
            }
            if (attributes.getBoolean(R.styleable.AvlwBannerView_avlwBvIndicatorTheRight,
                    bannerIndicator.getDefaultTheRight())) {
                constraintSet.connect(indicatorView.getId(), ConstraintSet.RIGHT,
                        R.id.vpgBaseList, ConstraintSet.RIGHT);
            }
            if (attributes.getBoolean(R.styleable.AvlwBannerView_avlwBvIndicatorTheBottom,
                    bannerIndicator.getDefaultTheBottom())) {
                constraintSet.connect(indicatorView.getId(), ConstraintSet.BOTTOM,
                        R.id.vpgBaseList, ConstraintSet.BOTTOM);
            }
            constraintSet.applyTo(this);
        }
        //设置指示器时间
        setAutoplayTime((long) attributes.getInt(R.styleable.AvlwBannerView_avlwBvAutoplayTime,
                autoplayTime.intValue()));
        attributes.recycle();
    }


    /**
     * 设置自动播放时间，如果时间为空或者为0则停止自动播放
     *
     * @param autoplayTime 自动播放时间
     */
    public void setAutoplayTime(Long autoplayTime) {
        this.autoplayTime = autoplayTime;
        //重置线程
        resetChangePageTask();
    }

    /**
     * 设置数据视图适配
     *
     * @param dataListSize 数据实际大小
     * @param adapter      适配器
     * @param <T>          itemHolder适配器
     */
    public <T extends RecyclerView.ViewHolder> void setViewData(
            final int dataListSize, RecyclerView.Adapter<T> adapter) {
        if (adapter != null) {
            this.dataListSize = dataListSize;
            if (this.dataListSize > 0) {
                vpgBaseList.setAdapter(adapter);
                vpgBaseList.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    /**
                     * This method will be invoked when a new page becomes selected. Animation is
                     * not
                     * necessarily complete.
                     *
                     * @param position Position index of the new selected page.
                     */
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        //切换指示器显示
                        if (bannerIndicator != null) {
                            bannerIndicator.changeShowIndicator(position % dataListSize);
                        }
                        //重置自动切换任务
                        resetChangePageTask();
                    }
                });
                //设置预加载数量
                vpgBaseList.setOffscreenPageLimit(dataListSize);
                //获取总倍数
                int sum = adapter.getItemCount() / dataListSize;
                if (sum > 1) {
                    vpgBaseList.setCurrentItem(dataListSize * (sum / 2), false);
                }
                //初始化指示器
                if (bannerIndicator != null) {
                    bannerIndicator.initIndicator(context, dataListSize, 0);
                }
            }
        }
    }

    /**
     * 重置切换任务
     */
    public void resetChangePageTask() {
        //关闭自动切换线程
        AtlwThreadUtils.getInstance().removeRunnable(autoplayRunnable);
        //开启新的线程
        if (autoplayTime != null && autoplayTime > 0 && dataListSize > 0) {
            AtlwThreadUtils.getInstance().postOnUiThreadDelayed(autoplayRunnable, autoplayTime);
        }
    }

    public ViewPager2 getVpgBaseList() {
        return vpgBaseList;
    }

    public View getIndicatorView() {
        return indicatorView;
    }

}
