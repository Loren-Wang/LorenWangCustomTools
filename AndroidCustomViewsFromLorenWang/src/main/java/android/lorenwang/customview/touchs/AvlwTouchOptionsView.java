package android.lorenwang.customview.touchs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 功能作用：触摸操作控件，和热区功能差不多，点击某个位置手指抬起触发操作
 * 创建时间：2021-03-11 18:22
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
public class AvlwTouchOptionsView extends View {
    /**
     * 实际使用数据
     */
    private final List<TouchRangBean> realUseDataList = new ArrayList<>();

    /**
     * 原始数据列表
     */
    private final List<TouchRangBean> originDataList = new ArrayList<>();

    /**
     * 原始宽度
     */
    private int originWidth = 0;

    /**
     * 原始高度
     */
    private int originHeight = 0;

    /**
     * 触摸监听
     */
    private TouchListener touchListener;

    /**
     * 绘制触摸操作区域
     */
    private boolean drawTouchOptions;
    /**
     * 绘制触摸区域颜色
     */
    private int drawTouchColor = Color.RED;
    /**
     * 绘制区域画笔
     */
    private final Paint drawTouchPaint = new Paint();

    public AvlwTouchOptionsView(Context context) {
        super(context);
        drawTouchPaint.setColor(drawTouchColor);
        drawTouchPaint.setAntiAlias(true);
    }

    public AvlwTouchOptionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawTouchPaint.setColor(drawTouchColor);
        drawTouchPaint.setAntiAlias(true);
    }

    public AvlwTouchOptionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawTouchPaint.setColor(drawTouchColor);
        drawTouchPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initRealUserData();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnabled() && touchListener != null && !realUseDataList.isEmpty()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                for (TouchRangBean bean : realUseDataList) {
                    if (bean.startX < event.getX() && bean.startX + bean.width > event.getX() && bean.startY < event.getY() &&
                            bean.startY + bean.height > event.getY()) {
                        touchListener.touchRang(bean);
                        return true;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawTouchOptions && !realUseDataList.isEmpty()) {
            for (TouchRangBean bean : realUseDataList) {
                canvas.drawRect(bean.startX, bean.startY, bean.startX + bean.width, bean.startY + bean.height, drawTouchPaint);
            }
        }
    }

    /**
     * 设置原始使用数据,会按照当前控件的宽高缩放
     *
     * @param originWidth    原始宽度
     * @param originHeight   原始高度
     * @param originDataList 原始数据列表
     */
    public void setOriginUserData(int originWidth, int originHeight, List<TouchRangBean> originDataList) {
        this.originWidth = originWidth;
        this.originHeight = originHeight;
        this.originDataList.clear();
        this.originDataList.addAll(originDataList);
        initRealUserData();
    }

    /**
     * 设置触摸监听
     *
     * @param touchListener 触摸监听
     */
    public void setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    /**
     * 设置绘制触摸区域
     *
     * @param drawTouch      是否绘制
     * @param drawTouchColor 绘制区域颜色
     */
    public void setDrawTouch(boolean drawTouch, Integer drawTouchColor) {
        this.drawTouchOptions = drawTouch;
        if (drawTouchColor != null) {
            this.drawTouchColor = drawTouchColor;
            drawTouchPaint.setColor(drawTouchColor);
        }
        invalidate();
    }

    /**
     * 初始化实际使用数据
     */
    private void initRealUserData() {
        if (originWidth > 0 && originHeight > 0) {
            int width = getWidth();
            int height = getHeight();
            float widthScale = width * 1.0F / originWidth;
            float heightScale = height * 1.0F / originHeight;
            this.realUseDataList.clear();
            for (TouchRangBean bean : this.originDataList) {
                this.realUseDataList.add(
                        new TouchRangBean((int) (bean.width * widthScale), (int) (bean.height * heightScale), (int) (bean.startX * widthScale),
                                (int) (bean.startY * heightScale), bean.tag));
            }
        }
    }

    /**
     * 触摸范围使用实体
     */
    public static class TouchRangBean {
        /**
         * 范围宽度
         */
        private final float width;
        /**
         * 范围高度
         */
        private final float height;
        /**
         * 起始点x坐标
         */
        private final float startX;
        /**
         * 起始点y坐标
         */
        private final float startY;
        /**
         * 额外数据
         */
        private final Object tag;

        public TouchRangBean(float width, float height, float startX, float startY, Object tag) {
            this.width = width;
            this.height = height;
            this.startX = startX;
            this.startY = startY;
            this.tag = tag;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public Object getTag() {
            return tag;
        }
    }

    /**
     * 触摸监听
     */
    public interface TouchListener {
        /**
         * 触摸区域生效
         *
         * @param bean 触摸区域的实体
         */
        void touchRang(TouchRangBean bean);
    }
}
