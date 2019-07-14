package android.lorenwang.customview.sidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创建时间：2019-04-16 下午 18:28:48
 * 创建人：王亮（Loren wang）
 * 功能作用：排序序号显示类型
 * 思路：定义以下参数处理
 * sbar_text_width 文本显示宽度
 * sbar_text_height 文本显示高度
 * sbar_text_color_y 文本选中颜色
 * sbar_text_color_n 文本未选中颜色
 * sbar_text_size 文本大小
 * sbar_text_bg_drawable_y 文本选中背景
 * sbar_text_bg_drawable_n 文本未选中背景
 * sbar_text_bg_width 背景宽度
 * sbar_text_bg_height  背景高度
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwSideBar1 extends View {
    private final String TAG = "AvlwSideBar1";
    /**
     * 文本宽度
     */
    private int textWidth = 0;
    /**
     * 文本高度
     */
    private int textHeight = 0;
    /**
     * 文本选中颜色
     */
    private int textColorY = Color.WHITE;
    /**
     * 文本未选中颜色
     */
    private int textColorN = Color.BLACK;
    /**
     * 文本大小
     */
    private int textSize = 0;
    /**
     * 文本选中背景图片
     */
    private Bitmap textBgBitmapY = null;
    /**
     * 文本未选中背景图片
     */
    private Bitmap textBgBitmapN = null;
    /**
     * 背景宽度
     */
    private int textBgWidth = 0;
    /**
     * 背景高度
     */
    private int textBgHeight = 0;


    /******************************************绘制参数*********************************************/

    /**
     * 文本选中画笔
     */
    private Paint textPaintY = new Paint();
    /**
     * 文本未选中画笔
     */
    private Paint textPaintN = new Paint();


    /*********************************************操作的参数****************************************/

    /**
     * 当前位置
     */
    private int nowPosi = 0;
    /**
     * 文本列表
     */
    private List<String> textList = new ArrayList<>();
    /**
     * 触摸监听
     */
    private AvlwOnSideBarTounchListener avlwOnSideBarTounchListener;


    public AvlwSideBar1(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwSideBar1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwSideBar1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.avlw_sidebar);
        textWidth = attributes.getDimensionPixelOffset(R.styleable.avlw_sidebar_avlw_sbar_text_width, textWidth);
        textHeight = attributes.getDimensionPixelOffset(R.styleable.avlw_sidebar_avlw_sbar_text_height, textHeight);
        textColorY = attributes.getColor(R.styleable.avlw_sidebar_avlw_sbar_text_color_y, textColorY);
        textColorN = attributes.getColor(R.styleable.avlw_sidebar_avlw_sbar_text_color_n, textColorN);
        textSize = attributes.getDimensionPixelOffset(R.styleable.avlw_sidebar_avlw_sbar_text_size, textSize);
        Drawable textBgDrawableY = attributes.getDrawable(R.styleable.avlw_sidebar_avlw_sbar_text_bg_drawable_y);
        Drawable textBgDrawableN = attributes.getDrawable(R.styleable.avlw_sidebar_avlw_sbar_text_bg_drawable_n);
        textBgWidth = attributes.getDimensionPixelOffset(R.styleable.avlw_sidebar_avlw_sbar_text_bg_width, textBgWidth);
        textBgHeight = attributes.getDimensionPixelOffset(R.styleable.avlw_sidebar_avlw_sbar_text_bg_height, textBgHeight);

        //初始化绘制参数
        textPaintY.reset();
        textPaintY.setTextSize(textSize);
        textPaintY.setColor(textColorY);
        textPaintY.setAntiAlias(true);

        textPaintN.reset();
        textPaintN.setTextSize(textSize);
        textPaintN.setColor(textColorN);
        textPaintN.setAntiAlias(true);

        if (textBgDrawableY != null) {
            textBgBitmapY = AtlwImageCommonUtils.getInstance().drawableToBitmap(textBgDrawableY, textBgWidth, textBgHeight);
        }
        if (textBgDrawableN != null) {
            textBgBitmapN = AtlwImageCommonUtils.getInstance().drawableToBitmap(textBgDrawableN, textBgWidth, textBgHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getWidth() < textWidth) {
            setMinimumWidth(textWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (nowPosi < textList.size()) {
            Iterator<String> iterator = textList.iterator();
            int index = 0;
            String text;
            while (iterator.hasNext()) {
                text = iterator.next();
                //绘制背景
                if (index != nowPosi) {
                    if (textBgBitmapN != null) {
                        canvas.drawBitmap(textBgBitmapN, getTextContainerCoordinateX(text, textPaintN), getTextContainerCoordinateY(index, textBgBitmapN), null);
                    }
                } else {
                    if (textBgBitmapY != null) {
                        canvas.drawBitmap(textBgBitmapY, getTextContainerCoordinateX(text, textPaintN), getTextContainerCoordinateY(index, textBgBitmapY), null);
                    }
                }
                //绘制文本
                if (index != nowPosi) {
                    canvas.drawText(text, getTextCoordinateX(text, textPaintN), getTextCoordinateY(index, textPaintN), textPaintN);
                } else {
                    canvas.drawText(text, getTextCoordinateX(text, textPaintY), getTextCoordinateY(index, textPaintY), textPaintY);
                }
                index++;
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchCallback(event.getY(), true, false, false);
                return true;
            case MotionEvent.ACTION_MOVE:
                onTouchCallback(event.getY(), false, true, false);
                return true;
            case MotionEvent.ACTION_UP:
                onTouchCallback(event.getY(), false, false, true);
                return true;
            default:
                return true;
        }
    }

    /**
     * 获取文本显示y坐标
     *
     * @param index
     * @param textPaint
     * @return
     */
    private Float getTextCoordinateY(int index, Paint textPaint) {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        return (textHeight - fm.bottom - fm.top) / 2 + getPaddingTop() + index * textHeight;
    }

    /**
     * 获取文本显示x坐标
     *
     * @param text
     * @param textPaint
     * @return
     */
    private Float getTextCoordinateX(String text, Paint textPaint) {
        float width = textPaint.measureText(text);
        if (width > textWidth) {
            width = textWidth;
        }
        return getPaddingLeft() + (textWidth - width) / 2;
    }

    /**
     * 获取文本容器显示y坐标
     *
     * @param index
     * @param bitmap
     * @return
     */
    private int getTextContainerCoordinateY(int index, Bitmap bitmap) {
        return getPaddingTop() + index * textHeight + (textHeight - bitmap.getHeight()) / 2;
    }

    /**
     * 获取文本容器显示x坐标
     *
     * @param text
     * @param textPaint
     * @return
     */
    private int getTextContainerCoordinateX(String text, Paint textPaint) {
        return getPaddingLeft();
    }

    /**
     * 触摸回调
     *
     * @param eventY 触摸y轴位置
     * @param isDown 是否按下
     * @param isMove 是否移动
     * @param isUp   是否抬起
     */
    private void onTouchCallback(float eventY, boolean isDown, boolean isMove, boolean isUp) {
        if (textList.isEmpty()) {
            return;
        }
        int posi = Float.valueOf((eventY - getPaddingTop()) / textHeight).intValue();
        if (posi >= textList.size()) {
            posi = textList.size() - 1;
        }

        //判断位置和上一次是否一致,一致则不返回
        if (posi == nowPosi) {
            return;
        } else {
            nowPosi = posi;
        }
        //发起重绘
        postInvalidate();

        if (isDown) {
            AtlwLogUtils.logI(TAG, "按下数组位置：" + posi);
        } else if (isMove) {
            AtlwLogUtils.logI(TAG, "滑动数组位置：" + posi);
        } else if (isUp) {
            AtlwLogUtils.logI(TAG, "抬起数组位置：" + posi);
        }

        if (avlwOnSideBarTounchListener != null) {
            avlwOnSideBarTounchListener.onTouchPosiForList(posi, textList.get(posi), isDown, isMove, isUp);
        }
    }

    /**
     * 设置文本显示列表
     *
     * @param textList 文本显示列表
     */
    public void setTextList(List<String> textList) {
        if (textList != null && !textList.isEmpty()) {
            this.textList = textList;
            postInvalidate();
        }
    }

    /**
     * 设置触摸监听
     *
     * @param avlwOnSideBarTounchListener 触摸监听
     */
    public void setAvlwOnSideBarTounchListener(AvlwOnSideBarTounchListener avlwOnSideBarTounchListener) {
        this.avlwOnSideBarTounchListener = avlwOnSideBarTounchListener;
    }

    /**
     * 获取当前位置
     *
     * @return 当前位置
     */
    public int getNowPosi() {
        return nowPosi;
    }

    /**
     * 设置当前位置
     *
     * @param nowPosi 当前位置
     */
    public void setNowPosi(int nowPosi) {
        if (nowPosi >= textList.size()) {
            return;
        }
        this.nowPosi = nowPosi;
        postInvalidate();
    }
}
