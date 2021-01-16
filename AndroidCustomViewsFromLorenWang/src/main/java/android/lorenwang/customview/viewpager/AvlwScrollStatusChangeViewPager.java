package android.lorenwang.customview.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 功能作用：左右滑动状态改变的viewPager
 * 创建时间：2020-12-17 5:34 下午
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
public class AvlwScrollStatusChangeViewPager extends ViewPager {
    /**
     * 是否允许滑动切换
     */
    private boolean allowScrollChange = true;

    public AvlwScrollStatusChangeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvlwScrollStatusChangeViewPager(Context context) {
        super(context);
    }

    /**
     * 设置滑动改变
     *
     * @param allowScrollChange 滑动改变
     */
    public void setAllowScrollChange(boolean allowScrollChange) {
        this.allowScrollChange = allowScrollChange;
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!allowScrollChange) {
            return false;
        } else {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!allowScrollChange) {
            return false;
        } else {
            return super.onInterceptTouchEvent(arg0);
        }
    }

}
