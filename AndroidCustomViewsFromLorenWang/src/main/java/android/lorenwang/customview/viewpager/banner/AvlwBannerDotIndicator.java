package android.lorenwang.customview.viewpager.banner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.app.AtlwViewUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 功能作用：banner点指示器
 * 创建时间：2020-06-28 4:28 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */

class AvlwBannerDotIndicator extends AvlwBaseBannerIndicator {
    /**
     * 选中颜色
     */
    private final ColorStateList dotSelectColor;

    /**
     * 未选中颜色
     */
    private final ColorStateList dotUnSelectColor;

    /**
     * dot点的view
     */
    private int dotItemViewRes = R.drawable.avlw_solid_radio_max;

    /**
     * dot点显示的宽度
     */
    private int dotItemWidth = (int) AtlwScreenUtils.getInstance().dip2px(5.6F);

    /**
     * dot点显示的高度
     */
    private final int dotItemHeight;

    /**
     * dot点左右外边距
     */
    private int dotItemMarginLeftRight = (int) AtlwScreenUtils.getInstance().dip2px(4F);

    public AvlwBannerDotIndicator(TypedArray typedArray, View indicatorView) {
        super(typedArray,indicatorView);
        dotItemViewRes = typedArray.getResourceId(R.styleable.AvlwBannerView_avlwBvIndicatorDotItemViewRes
                , dotItemViewRes);
        dotItemWidth =
                typedArray.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorDotItemWidth,
                        dotItemWidth);
        dotItemHeight =
                typedArray.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorDotItemHeight
                        , dotItemWidth);
        dotItemMarginLeftRight =
                typedArray.getDimensionPixelOffset(R.styleable.AvlwBannerView_avlwBvIndicatorDotItemMarginLeftRight,
                        dotItemMarginLeftRight);
        dotSelectColor =
                ColorStateList.valueOf(typedArray.getColor(R.styleable.AvlwBannerView_avlwBvIndicatorDotSelectColor, Color.WHITE));
        dotUnSelectColor =
                ColorStateList.valueOf(typedArray.getColor(R.styleable.AvlwBannerView_avlwBvIndicatorDotUnSelectColor,
                        Color.parseColor("#88ffffff")));

    }

    /**
     * 初始化指示器
     *
     * @param context      上下文
     * @param dataSize     数据总数，非最大值，而是实际数据数量
     * @param showPosition 当前显示的位置
     */
    @Override
    void initIndicator(Context context, int dataSize, int showPosition) {
        LinearLayout mIndicatorContainer = (LinearLayout) this.indicatorView;
        mIndicatorContainer.removeAllViews();
        if (dataSize > 0) {
            int current = showPosition % dataSize;
            AppCompatImageView dotView;
            for (int i = 0; i < dataSize; i++) {
                dotView = new AppCompatImageView(context);
                //设置图片
                dotView.setImageResource(dotItemViewRes);
                //设置dot的宽高以及边距
                AtlwViewUtils.getInstance().setViewWidthHeightMargin(dotView,
                        LinearLayout.LayoutParams.class, dotItemWidth, dotItemHeight,
                        dotItemMarginLeftRight, 0, dotItemMarginLeftRight, 0);
                if (i == current) {
                    AtlwViewUtils.getInstance().setImageSrcTint(dotView, dotSelectColor);
                } else {
                    AtlwViewUtils.getInstance().setImageSrcTint(dotView, dotUnSelectColor);
                }
                mIndicatorContainer.addView(dotView);
            }
        }
    }

    /**
     * 切换指示器位置
     *
     * @param showPosition 切换后的指示器位置
     */
    @Override
    void changeShowIndicator(int showPosition) {
        LinearLayout mIndicatorContainer = (LinearLayout) this.indicatorView;
        int childCount = mIndicatorContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == showPosition) {
                AtlwViewUtils.getInstance().setImageSrcTint((ImageView) mIndicatorContainer.getChildAt(i),
                        dotSelectColor);
            } else {
                AtlwViewUtils.getInstance().setImageSrcTint((ImageView) mIndicatorContainer.getChildAt(i),
                        dotUnSelectColor);
            }
        }
    }

    @Override
    boolean getDefaultTheLeft() {
        return true;
    }

    @Override
    boolean getDefaultTheRight() {
        return true;
    }

    @Override
    boolean getDefaultTheBottom() {
        return true;
    }
}
