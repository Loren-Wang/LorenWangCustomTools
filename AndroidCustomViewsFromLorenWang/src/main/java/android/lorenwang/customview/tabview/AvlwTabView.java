package android.lorenwang.customview.tabview;

import android.content.Context;
import android.content.res.TypedArray;
import android.lorenwang.customview.R;
import android.lorenwang.customview.tablayout.AvlwBaseTabLayoutChangeListener;
import android.lorenwang.customview.tablayout.AvlwTabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 功能作用：tab自定义控件
 * 创建时间：2020-01-15 11:57
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * @author wangliang
 */

public class AvlwTabView extends FrameLayout {
    /**
     * tab列表，可能为空
     */
    private AvlwTabLayout hstlTabList;
    /**
     * vpg列表，可能为空
     */
    private ViewPager vpgList;

    public AvlwTabView(Context context) {
        super(context);
        init(context, null);
    }

    public AvlwTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.avlw_tab_layout, this);
        //滑动tab顶部区域
        ViewStub vsbTabTop = view.findViewById(R.id.vsbTabTop);
        //tab区域占位
        ViewStub vsbTab = view.findViewById(R.id.vsbTab);
        //tab底部占位区域
        ViewStub vsbTabBottom = view.findViewById(R.id.vsbTabBottom);

        //格式化三个view
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwTabView);
        int tabTopViewLayout = attributes.getResourceId(R.styleable.AvlwTabView_avlw_tbv_tabTopViewLayout, -1);
        if (tabTopViewLayout > 0) {
            vsbTabTop.setLayoutResource(tabTopViewLayout);
            vsbTabTop.inflate();
        }
        int tabTopLayout = attributes.getResourceId(R.styleable.AvlwTabView_avlw_tbv_tabTopLayout, -1);
        vsbTab.setLayoutResource(tabTopLayout > 0 ? tabTopLayout : R.layout.avlw_tab_layout_tab);
        vsbTab.inflate();
        int tabBottomLayout = attributes.getResourceId(R.styleable.AvlwTabView_avlw_tbv_tabBottomLayout, -1);
        vsbTabBottom.setLayoutResource(tabBottomLayout > 0 ? tabBottomLayout : R.layout.avlw_tab_layout_vpg);
        vsbTabBottom.inflate();
        attributes.recycle();
        hstlTabList = findViewById(R.id.hstlTabList);
        vpgList = findViewById(R.id.vpgList);
    }

    /**
     * tab文本列表
     *
     * @param tabList 文本列表
     */
    public void setTabList(List<String> tabList) {
        if (hstlTabList != null && tabList != null && !tabList.isEmpty()) {
            hstlTabList.setTabTextList(tabList).setTabWidth(getResources().getDisplayMetrics().widthPixels * 1.0f / tabList.size());
        }
    }

    /**
     * 设置viewpager适配器
     */
    public void setVpgAdapter(final AvlwTabVpgAdapter<Fragment> adapter) {
        if (vpgList != null && adapter != null) {
            vpgList.setAdapter(new FragmentPagerAdapter(adapter.getActivity().getSupportFragmentManager(), 0) {
                @NonNull
                @Override
                public Fragment getItem(int i) {
                    return adapter.getItem(i);
                }

                @Override
                public int getCount() {
                    return adapter.getItemCount();
                }
            });
            if (hstlTabList != null) {
                vpgList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (position == hstlTabList.getCurrentPosition() && positionOffset > 0) {
                            hstlTabList.setCurrentPosition(position + 1, positionOffset);
                        } else if (position < hstlTabList.getCurrentPosition() && positionOffset > 0) {
                            hstlTabList.setCurrentPosition(position, 1 - positionOffset);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        hstlTabList.setCurrentPosition(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                hstlTabList.setChangeListener(new AvlwBaseTabLayoutChangeListener() {

                    /**
                     * 位置切换
                     *
                     * @param isOnTouchChange 是否是触摸切换的位置
                     * @param position        切换后的位置
                     */
                    @Override
                    public void onChangePosition(boolean isOnTouchChange, int position) {

                    }

                    /**
                     * 切换进度
                     *
                     * @param isOnTouchChange 是否是触摸切换的位置
                     * @param percent         进度
                     */
                    @Override
                    public void onChangePercent(boolean isOnTouchChange, float percent) {

                    }

                });
            }
        }
    }


}
