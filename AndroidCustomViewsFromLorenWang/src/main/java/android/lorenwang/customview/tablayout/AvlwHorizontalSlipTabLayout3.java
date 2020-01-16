package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 创建时间：2019-03-15 下午 13:55:20
 * 创建人：王亮（Loren wang）
 * 功能作用：水平滑动tab布局
 * 思路：首先每个tab标签的宽高都是固定的,下划线的宽高也是,同时tab标签以及下划线的高度以及各个间距的高度不能超过总高度
 * 超过总高度会对所有的高度做等比缩小,当前通过布局文件可设置的参数如下
 * tabWidth--tab宽度;
 * tabHeight--tab高度；
 * tabTextSize--tab文本大小；
 * tabTextColorY--tab文本选中颜色；
 * tabTextColorN--tab文本未选中颜色；
 * lineWidth--下划线宽度；
 * lineHeight--下划线高度；
 * lineColor--下划线颜色；
 * lineTextSpace--文本下划线之间间距；
 * <p>
 * 在设置文本资源时对所有文本的坐标位置进行计算并存储，此时ondraw就可以直接根据坐标进行绘制
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AvlwHorizontalSlipTabLayout3 extends FrameLayout implements AvlwBaseHorizontalSlipTabLayout {
    private AvlwHorizontalSlipTabLayout tabView;
    /**
     * 下划线容器画笔
     */
    private Paint lineContainerPaint;
    private Float lineContainerWidth;

    public AvlwHorizontalSlipTabLayout3(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwHorizontalSlipTabLayout3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwHorizontalSlipTabLayout3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        tabView = new AvlwHorizontalSlipTabLayout(context, attrs, defStyleAttr);
        addView(tabView);
        ViewGroup.LayoutParams params = tabView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        if (params instanceof LayoutParams) {
            ((LayoutParams) params).gravity = Gravity.CENTER;
        }
        tabView.setLayoutParams(params);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AvlwHorizontalSlipTabLayout, defStyleAttr, 0);
        lineContainerPaint = new Paint();
        lineContainerPaint.setAntiAlias(true);
        lineContainerPaint.setColor(attr.getColor(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineContainerColor, Color.TRANSPARENT));
        lineContainerPaint.setStrokeWidth(attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineContainerHeight, 0f));
        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float lineContainerWidthPercent = attr.getFloat(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineContainerWidthPercent, -1f);
        if (lineContainerWidthPercent > 0) {
            if (lineContainerWidthPercent > 1) {
                lineContainerWidthPercent = 1f;
            }
            lineContainerWidth = getResources().getDisplayMetrics().widthPixels * lineContainerWidthPercent;
        }
        attr.recycle();
        //设置需要绘制，否则的话底部线条会无法绘制
        setWillNotDraw(false);
    }

    private float startX = 0f;
    private float stopX = 0f;
    private float lingContainerY = 0f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lingContainerY = tabView.getLineCoordinateY() + (getHeight() - tabView.getHeight()) / 2;
        if (lineContainerWidth == null || lineContainerWidth > getHeight()) {
            startX = getPaddingLeft();
            stopX = getWidth() - getPaddingRight();
        } else {
            startX = (getPaddingLeft() + (getWidth() - lineContainerWidth) / 2);
            stopX = (startX - lineContainerWidth);
        }
        canvas.drawLine(startX, lingContainerY, stopX, lingContainerY, lineContainerPaint);
    }

    /**
     * 设置tab列表
     */
    @Override
    public void setTabList(@Nullable List<String> tabList, @Nullable Integer selectPosi) {
        tabView.setTabList(tabList, selectPosi);
    }

    /**
     * 跳转到指定位置
     */
    @Override
    public void skipToPosi(int posi) {
        tabView.skipToPosi(posi);
    }

    /**
     * 滑动到指定位置，带百分比滑动
     */
    @Override
    public void slipToPosi(int slipToPosi, float percent) {
        tabView.slipToPosi(slipToPosi, percent);
    }

    /**
     * 滑动跳转到指定位置
     */
    @Override
    public void slipSkipToPosi(int slipToPosi) {
        tabView.slipSkipToPosi(slipToPosi);
    }
};
