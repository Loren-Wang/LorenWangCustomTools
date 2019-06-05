package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.lorenwang.customview.R;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 创建时间：2019-03-15 下午 13:55:20
 * 创建人：王亮（Loren wang）
 * 功能作用：水平滑动tab布局类型2
 * 思路：在初始化时设置圆角角度，这个控件没有下划线，只有每个选中tab的切换背景
 *         <attr name="tabWidth" format="dimension"/>tab宽度
 *         <attr name="tabHeight" format="dimension"/>tab高度
 *         <attr name="tabTextSize" format="dimension"/>tab文本大小
 *         <attr name="tabTextColorY" format="color"/>tab文本选中颜色
 *         <attr name="tabTextColorN" format="color"/>tab文本未选中颜色
 *         <attr name="tabBgColorY" format="color"/>tab选中背景颜色
 *         <attr name="viewRadius" format="dimension"/>视图半径
 *         <attr name="viewBgColor" format="color"/>视图背景颜色
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class HorizontalSlipTabLayout2 extends View implements BaseHorizontalSlipTabLayout {


    /*******************************************绘制部分参数****************************************/
    /**
     * 文本画笔
     */
    private Paint tabPaint = new Paint();
    /**
     * 下划线画笔
     */
    private Paint tabBgPaint = new Paint();
    /**
     * 视图背景画笔
     */
    private Paint viewBgPaint = new Paint();
    /**
     * tab文本内容坐标，以两个为一组，分别代表着xy坐标,为文本左下角坐标
     */
    private ArrayList<Float> tabListCoordinate = new ArrayList<>();
    /**
     * tab选中颜色
     */
    private int tabTextColorY = Color.BLUE;
    /**
     * tab未选中颜色
     */
    private int tabTextColorN = Color.BLACK;
    /**
     * tab宽度
     */
    private float tabWidth = 300f;
    /**
     * tab高度
     */
    private float tabHeight = 100f;
    /**
     * 视图圆角半径
     */
    private float viewRadius = 0f;
    /**
     * tab背景顶部位置
     */
    private float tabBgTop = 0f;
    /**
     * tab背景底部位置
     */
    private float tabBgBottom = 0f;


    /*******************************************配置部分参数****************************************/

    /**
     * 背景滑动百分比
     */
    private float tabBgSlipPercent = 0f;
    /**
     * 是否允许触摸修改
     */
    private boolean isAllowTouchChange = true;
    /**
     * 是否允许修改位置
     */
    private boolean isAllowChangePosi = true;


    /*******************************************内容部分参数****************************************/
    /**
     * tab列表
     */
    private ArrayList<String> tabList = new ArrayList<>();
    /**
     * 选中位置
     */
    private int selectPosi = 0 ;
    /**
     * 滑动到指定位置
     */
    private int slipToPosi = 0;


    /*******************************************其他部分参数****************************************/
    /**
     * 滑动跳转到指定位置线程
     */
    private final Runnable slipSkipToPosiRunnable = new Runnable() {
        @Override
        public void run() {
            Float percent = 0f;
            while (true) {
                percent += 0.05f;
                /**
                 * 修改滑动进度
                 */
                changeSlipPercent(percent);
                if (percent.intValue() == 1) {
                    break;
                }
                try {
                    if (percent < 0.75) {
                        Thread.sleep(15);
                    } else {
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                }
            }
            isAllowChangePosi = true;
        }
    };
    /**
     * tab改变监听
     */
    private BaseHorizontalSlipTabLayoutChangeListener tabChangeListener;


    public HorizontalSlipTabLayout2(Context context) {
        super(context);
        init(context, null, -1);
    }

    public HorizontalSlipTabLayout2(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public HorizontalSlipTabLayout2(Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSlipTabLayout, defStyleAttr, 0);
        this.tabWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabWidth, this.tabWidth);
        this.tabHeight = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabHeight, this.tabHeight);
        this.tabTextColorY = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabTextColorY, this.tabTextColorY);
        this.tabTextColorN = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabTextColorN, this.tabTextColorN);
        this.viewRadius = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_viewRadius, this.viewRadius);

        this.measure(0, 0);

        //初始化tab文本画笔
        this.tabPaint.reset();
        this.tabPaint.setTextSize(attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabTextSize, 50.0F));
        this.tabPaint.setAntiAlias(true);

        //初始化tab背景画笔
        this.tabBgPaint.reset();
        this.tabBgPaint.setAntiAlias(true);
        this.tabBgPaint.setColor(attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabBgColorY, -1));
        this.tabBgPaint.setStyle(Paint.Style.FILL);

        //视图背景画笔
        this.viewBgPaint.reset();
        this.viewBgPaint.setAntiAlias(true);
        this.viewBgPaint.setColor(attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_viewBgColor, -16777216));
        this.viewBgPaint.setStyle(Paint.Style.FILL);
        attr.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 绘制整体背景
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight(), this.viewRadius, this.viewRadius, this.viewBgPaint);
        } else {
            canvas.drawRoundRect(new RectF(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight()), this.viewRadius, this.viewRadius, this.viewBgPaint);
        }

        /**
         * 绘制tab背景
         */
        this.tabBgTop = ((float)this.getHeight() - this.tabHeight) / (float)2;
        this.tabBgBottom = this.tabBgTop + this.tabHeight;
        if (this.tabBgSlipPercent == 0.0F) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect((float)this.selectPosi * this.tabWidth + (float)this.getPaddingLeft(), this.tabBgTop, (float)(this.selectPosi + 1) * this.tabWidth + (float)this.getPaddingLeft(), this.tabBgBottom, this.viewRadius, this.viewRadius, this.tabBgPaint);
            } else {
                canvas.drawRoundRect(new RectF((float)this.selectPosi * this.tabWidth + (float)this.getPaddingLeft(), this.tabBgTop, (float)(this.selectPosi + 1) * this.tabWidth + (float)this.getPaddingLeft(), this.tabBgBottom), this.viewRadius, this.viewRadius, this.tabBgPaint);
            }
        } else {
            /**
             * 根据百分比做移动绘制
             */
            /**
             * 滑动距离为目标x坐标减去当前x坐标乘以百分比
             */
            float slipSpace = this.tabWidth * this.tabBgSlipPercent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect((float)this.selectPosi * this.tabWidth + slipSpace + (float)this.getPaddingLeft(), this.tabBgTop, (float)(this.selectPosi + 1) * this.tabWidth + slipSpace + (float)this.getPaddingLeft(), this.tabBgBottom, this.viewRadius, this.viewRadius, this.tabBgPaint);
            } else {
                canvas.drawRoundRect(new RectF((float)this.selectPosi * this.tabWidth + slipSpace + (float)this.getPaddingLeft(), this.tabBgTop, (float)(this.selectPosi + 1) * this.tabWidth + slipSpace + (float)this.getPaddingLeft(), this.tabBgBottom), this.viewRadius, this.viewRadius, this.tabBgPaint);
            }
        }

        /**
         * 绘制文本
         */
        for (int i =0 ; i < tabList.size() ; i++) {
            if (i == this.selectPosi) {
                this.tabPaint.setColor(this.tabTextColorY);
            } else {
                this.tabPaint.setColor(this.tabTextColorN);
            }
            canvas.drawText(tabList.get(i), tabListCoordinate.get(2 * i)
                    , tabListCoordinate.get(2 * i + 1), tabPaint);
        }
    }

    private float lastX = 0f;
    private float lastY = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAllowChangePosi && isAllowTouchChange) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN : {
                    lastX = event.getX();
                    lastY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP : {
                    skipToPosi(Float.valueOf((event.getX() - getPaddingRight()) / tabWidth).intValue());
                    return true;
                }
                default:
                    return true;
            }
        } else {
            return super.onTouchEvent(event);
        }
    }
    /**
     * 设置tab列表
     */
    @Override
    public void setTabList( ArrayList<String> tabList,  Integer selectPosi) {
        /**
         * 计算坐标
         */
        if (tabList != null) {
            this.tabList.clear();
            this.tabListCoordinate.clear();
            Paint.FontMetrics fm = this.tabPaint.getFontMetrics();
            /**
             * 文本底部坐标
             */
            float textCoordinateY = (this.tabHeight - fm.bottom - fm.top) / (float)2 + (float)this.getPaddingTop();
            /**
             * 当前左侧宽度
             */
            float nowLeftWidth = (float)this.getPaddingLeft();
            /**
             * 文本宽度
             */
            float textWidth = 0.0F;
            Iterator<String> iterator = tabList.iterator();
            String text;
            while (iterator.hasNext()){
                text = iterator.next();
                if (text != null && !"".equals(text)) {
                    this.tabList.add(text);
                    textWidth = tabPaint.measureText(text);
                    if (textWidth > tabWidth) {
                        textWidth = tabWidth;
                    }
                    /**
                     * 添加文本坐标
                     */
                    this.tabListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2);
                    this.tabListCoordinate.add(textCoordinateY);
                    nowLeftWidth += tabWidth;
                }
            }

            /**
             * 重新设置宽高
             */
            if (this.getLayoutParams() != null) {
                this.getLayoutParams().width = (int)((float)(this.getPaddingLeft() + this.getPaddingRight()) + (float)tabList.size() * this.tabWidth);
                this.getLayoutParams().height = (int)((float)(this.getPaddingTop() + this.getPaddingBottom()) + this.tabHeight);
            } else {
                this.setLayoutParams(new ViewGroup.LayoutParams((int)((float)(this.getPaddingLeft() + this.getPaddingRight()) + (float)tabList.size() * this.tabWidth), (int)((float)(this.getPaddingTop() + this.getPaddingBottom()) + this.tabHeight)));
            }
        }


        /**
         * 跳转到指定位置
         */
        if (selectPosi != null) {
            skipToPosi(selectPosi);
        }
    }
    /**
     * 跳转到指定位置
     */
    @Override
    public void skipToPosi(int posi) {
        if (isAllowChangePosi && posi != selectPosi) {
            if (posi < this.tabList.size()) {
                this.selectPosi = posi;
                //回调位置
                if(tabChangeListener != null)
                tabChangeListener.onChangePosi(this.selectPosi);
                invalidate();
            }
        }
    }
    /**
     * 滑动到指定位置，带百分比滑动
     */
    @Override
    public void slipToPosi(int slipToPosi, float percent) {
        if (isAllowChangePosi && slipToPosi != selectPosi) {
            if (slipToPosi < this.tabList.size()) {
                this.slipToPosi = slipToPosi;
                //回调位置
                if(tabChangeListener != null)
                tabChangeListener.onChangePosi(this.selectPosi);
            }
            changeSlipPercent(percent);
        }
    }
    /**
     * 滑动跳转到指定位置
     */
    @Override
    public void slipSkipToPosi(int slipToPosi) {
        if (isAllowChangePosi && slipToPosi < this.tabList.size()
                && slipToPosi != selectPosi) {
            isAllowChangePosi = false;
            this.slipToPosi = slipToPosi;
            //回调位置
            if(tabChangeListener != null)
            tabChangeListener.onChangePosi(this.selectPosi);
            new Thread(slipSkipToPosiRunnable).start();
        }
    }

    /**
     * 修改滑动进度
     */
    private void changeSlipPercent(Float percent) {
        if (percent.intValue() == 1) {
            this.selectPosi = slipToPosi;
            tabBgSlipPercent = 0f;
        } else {
            if (slipToPosi < selectPosi) {
                tabBgSlipPercent = -percent % 1.0f;
            } else {
                tabBgSlipPercent = percent % 1.0f;
            }
        }
        postInvalidate();
    }
}
