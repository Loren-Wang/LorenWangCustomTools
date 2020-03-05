package android.lorenwang.customview.viewpager;

import android.content.Context;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 功能作用：重写viewpager2容器
 * 创建时间：2019-12-23 14:47
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、设置一级ViewPager2---setFirstViewPage（vpg）
 * 2、设置二级viewpager2---setSecondViewPageMap（position,vpg）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwViewPagerConstraintLayout extends ConstraintLayout {
    private final String TAG = "QtViewPager";
    private ViewPager2 firstViewPage;
    private Map<Integer, ViewPager2> secondViewPageMap = new HashMap<>();
    private ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            AtlwLogUtils.logI(TAG, String.valueOf(positionOffset));
            if (positionOffset > 0 && positionOffset < 1) {
                isScroll = true;
            } else if (position == 1 || positionOffset == 0) {
                isScroll = false;
            }
        }
    };

    public AvlwViewPagerConstraintLayout(@NonNull Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwViewPagerConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwViewPagerConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    }

    /**
     * 设置一级ViewPager
     *
     * @param firstViewPage 一级viewpager
     */
    public void setFirstViewPage(ViewPager2 firstViewPage) {
        this.firstViewPage = firstViewPage;
        this.firstViewPage.registerOnPageChangeCallback(callback);
    }

    /**
     * 设置二级viewpager
     *
     * @param position  位置
     * @param viewPager viewpager
     */
    public void setSecondViewPageMap(int position, ViewPager2 viewPager) {
        viewPager.registerOnPageChangeCallback(callback);
        secondViewPageMap.put(position, viewPager);
    }

    /**
     * 按下位置
     */
    private float downX;
    /**
     * 按下位置viewpager
     */
    private ViewPager2 downViewPage;
    /**
     * 是否在滑动执行当中
     */
    private boolean isScroll = false;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                if (firstViewPage != null) {
                    //判断按下位置是否是二级viewpager
                    int currentItem = firstViewPage.getCurrentItem();
                    ViewPager2 secondPage = secondViewPageMap.get(currentItem);
                    if (secondPage != null) {
                        int[] location = new int[2];
                        secondPage.getLocationInWindow(location);
                        //判断是否在二级viewpager中滑动
                        if (ev.getRawX() >= location[0] && ev.getRawX() <= location[0] + secondPage.getWidth()
                                && ev.getRawY() >= location[1] && ev.getRawY() <= location[1] + secondPage.getHeight()) {
                            //在二级中滑动
                            downViewPage = secondPage;
                            secondPage.setUserInputEnabled(true);
                            firstViewPage.setUserInputEnabled(false);
                            AtlwLogUtils.logI(this.TAG, "点击的是二级ViewPager");
                        } else {
                            downViewPage = firstViewPage;
                            secondPage.setUserInputEnabled(false);
                            firstViewPage.setUserInputEnabled(true);
                            AtlwLogUtils.logI(this.TAG, "点击的是二级ViewPager");
                        }
                    } else {
                        firstViewPage.setUserInputEnabled(true);
                        downViewPage = firstViewPage;
                        AtlwLogUtils.logI(this.TAG, "点击的是一级ViewPager");
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //边界判断
                if (downViewPage != null && !isScroll) {
                    if (downX > ev.getRawX() && downViewPage.getCurrentItem() == 0) {
                        //判断是否到达左边界手势向左滑
                        firstViewPage.setUserInputEnabled(false);
                        downViewPage.setUserInputEnabled(true);
                        AtlwLogUtils.logI(this.TAG, "左边界手势向左滑");
                    } else if (downX < ev.getRawX() && downViewPage.getCurrentItem() == 0) {
                        //判断是否左边界手势向右滑
                        downViewPage.setUserInputEnabled(false);
                        firstViewPage.setUserInputEnabled(true);
                        AtlwLogUtils.logI(this.TAG, "左边界手势向右滑");
                    } else if (downX > ev.getRawX() && downViewPage.getAdapter() != null
                            && downViewPage.getCurrentItem() == downViewPage.getAdapter().getItemCount() - 1) {
                        //判断是否是右边界手势向左滑
                        downViewPage.setUserInputEnabled(false);
                        firstViewPage.setUserInputEnabled(true);
                        AtlwLogUtils.logI(this.TAG, "右边界手势向左滑");
                    } else if (downX < ev.getRawX() && downViewPage.getAdapter() != null
                            && downViewPage.getCurrentItem() == downViewPage.getAdapter().getItemCount() - 1) {
                        //判断是否是右边界手势向右滑
                        firstViewPage.setUserInputEnabled(false);
                        downViewPage.setUserInputEnabled(true);
                        AtlwLogUtils.logI(this.TAG, "右边界手势向右滑");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                downViewPage = null;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
