package android.lorenwang.customview.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 功能作用：高度可变的轮播控件
 * 创建时间：2021-02-08 8:08 下午
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
public class AvlwHeightChangeViewPager extends AvlwScrollStatusChangeViewPager {
    public AvlwHeightChangeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvlwHeightChangeViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (getCurrentItem() == i) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
                break;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
