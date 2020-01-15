package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;


/**
 * 创建时间：2019-05-05 下午 19:41:4
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwHorizontalSlipTabLayout extends View implements AvlwBaseHorizontalSlipTabLayout {
    private final String TAG = getClass().getName();
    /*******************************************绘制部分参数****************************************/
    /**
     * 文本画笔
     */
    private Paint tabPaint = new Paint();
    /**
     * 下划线画笔
     */
    private Paint linePaint = new Paint();
    /**
     * 下划线坐标列表，以两个为一组，分别代表着startX坐标,为下划线左侧中心坐标
     */
    private ArrayList<Float> tabLineListCoordinate = new ArrayList<>();
    /**
     * tab文本内容坐标，以两个为一组，分别代表着xy坐标,为文本左下角坐标
     */
    private ArrayList<Float> tabListCoordinate = new ArrayList<>();
    /**
     * 所有的文本宽度
     */
    private ArrayList<Float> tabTextListWidth = new ArrayList<>();
    /**
     * tab选中颜色
     */
    private int tabTextColorY = Color.BLUE;
    /**
     * tab选中颜色
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
     * 文本以及下划线之间的间距
     */
    private float lineTextSpace = 0f;
    /**
     * 是否允许绘制线条
     */
    private boolean isAllowDrawLine = false;
    /**
     * 选中文本是否加粗
     */
    private boolean tabTextBoldY = false;
    /**
     * 未选中文本是否加粗
     */
    private boolean tabTextBoldN = false;


    /*******************************************配置部分参数****************************************/
    /**
     * 下划线宽度，如果下划线宽度大于0，则所有的下划线都是用固定宽度，否则的话则下划线宽度和文本宽度相同
     */
    private float lineWidth = 0f;
    /**
     * 下划线高度
     */
    private float lineHeight = 10.0F;
    /**
     * 下划线滑动百分比
     */
    private float lineSlipPercent = 0f;
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
    private int selectPosi = 0;
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
            float percent = 0f;
            while (true) {
                percent += 0.05f;
                //修改滑动进度
                changeSlipPercent(percent);
                if ((int) percent == 1) {
                    break;
                }
                try {
                    if (percent < 0.75) {
                        Thread.sleep(15);
                    } else {
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    AtlwLogUtils.logE(TAG, "滑动异常");
                }
            }
            isAllowChangePosi = true;
        }
    };

    /**
     * tab改变监听
     */
    private AvlwBaseHorizontalSlipTabLayoutChangeListener tabChangeListener;

    public AvlwHorizontalSlipTabLayout(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwHorizontalSlipTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwHorizontalSlipTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    /**
     * 控件初始化
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AvlwHorizontalSlipTabLayout, defStyleAttr, 0);
        this.tabWidth = attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabWidth, this.tabWidth);
        this.tabHeight = attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabHeight, this.tabHeight);
        this.tabTextColorY = attr.getColor(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextColorY, this.tabTextColorY);
        this.tabTextColorN = attr.getColor(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextColorN, this.tabTextColorN);
        this.lineWidth = attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineWidth, this.lineWidth);
        this.lineTextSpace = attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineTextSpace, this.lineTextSpace);
        this.tabTextBoldN = attr.getBoolean(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextBoldN, this.tabTextBoldN);
        this.tabTextBoldY = attr.getBoolean(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextBoldY, this.tabTextBoldY);
        //使用默认位置
        this.selectPosi = attr.getInt(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabDefaultPosition, this.selectPosi);

        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float tabWidthPercent = attr.getFloat(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabWidthPercent, -1.0F);
        if (tabWidthPercent > (float) 0) {
            if (tabWidthPercent > (float) 1) {
                tabWidthPercent = 1.0F;
            }

            this.tabWidth = (float) getResources().getDisplayMetrics().widthPixels * tabWidthPercent;
        }
        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float lineWidthPercent = attr.getFloat(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineWidthPercent, -1.0F);
        if (lineWidthPercent > (float) 0) {
            if (lineWidthPercent > (float) 1) {
                lineWidthPercent = 1.0F;
            }

            this.lineWidth = (float) getResources().getDisplayMetrics().widthPixels * lineWidthPercent;
        }

        //数据合理性检测，首先检测下划线宽度是否大于tab宽度
        if (Float.compare(this.lineWidth, this.tabWidth) > 0) {
            this.lineWidth = this.tabWidth;
        }

        //初始化tab文本画笔
        this.tabPaint.reset();
        this.tabPaint.setTextSize(attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextSize, 50.0F));
        this.tabPaint.setAntiAlias(true);
        if (this.tabTextBoldY || this.tabTextBoldN) {
            this.tabPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        //初始化下划线画笔
        this.linePaint.reset();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeWidth(lineHeight = attr.getDimension(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineHeight, lineHeight));
        this.linePaint.setColor(attr.getColor(R.styleable.AvlwHorizontalSlipTabLayout_hstl_lineColor, this.tabTextColorY));

        //读取tab文本列表hstl_tabTextList
        String texts = attr.getString(R.styleable.AvlwHorizontalSlipTabLayout_hstl_tabTextList);
        texts = texts == null ? "" : texts;
        //格式化处理文本列表
        String[] split = texts.split("~~");
        setTabList(Arrays.asList(split), selectPosi);
        attr.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文本
        for (int i = 0; i < tabList.size(); i++) {
            if (i == this.selectPosi) {
                this.tabPaint.setColor(this.tabTextColorY);
                if (this.tabTextBoldY) {
                    this.tabPaint.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    this.tabPaint.setTypeface(Typeface.DEFAULT);
                }
            } else {
                this.tabPaint.setColor(this.tabTextColorN);
                if (this.tabTextBoldN) {
                    this.tabPaint.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    this.tabPaint.setTypeface(Typeface.DEFAULT);
                }
            }
            canvas.drawText(tabList.get(i), tabListCoordinate.get(2 * i), tabListCoordinate.get(2 * i + 1), tabPaint);
        }

        //绘制下划线
        if (isAllowDrawLine) {
            if (lineSlipPercent == 0f) {
                //此时没有百分比存在，直接定位显示
                if (lineWidth > 0) {
                    canvas.drawLine(tabLineListCoordinate.get(selectPosi * 2)
                            , tabLineListCoordinate.get(selectPosi * 2 + 1)
                            , tabLineListCoordinate.get(selectPosi * 2) + lineWidth
                            , tabLineListCoordinate.get(selectPosi * 2 + 1), linePaint);
                } else {
                    canvas.drawLine(tabLineListCoordinate.get(selectPosi * 2)
                            , tabLineListCoordinate.get(selectPosi * 2 + 1)
                            , tabLineListCoordinate.get(selectPosi * 2) + tabTextListWidth.get(selectPosi)
                            , tabLineListCoordinate.get(selectPosi * 2 + 1), linePaint);
                }
            } else {
                //滑动距离为目标x坐标减去当前x坐标乘以百分比
                float slipSpace = (tabLineListCoordinate.get(slipToPosi * 2) - tabLineListCoordinate.get(selectPosi * 2)) * lineSlipPercent;
                //判断是否需要变动下划线宽度
                if (lineWidth > 0) {
                    canvas.drawLine(tabLineListCoordinate.get(selectPosi * 2) + slipSpace
                            , tabLineListCoordinate.get(selectPosi * 2 + 1)
                            , tabLineListCoordinate.get(selectPosi * 2) + lineWidth + slipSpace
                            , tabLineListCoordinate.get(selectPosi * 2 + 1), linePaint);
                } else {
                    //获取宽度变化值,值为目标位置文本宽度减去当前位置文本的宽度乘以百分比
                    float widthChange = (tabTextListWidth.get(slipToPosi) - tabTextListWidth.get(selectPosi)) * lineSlipPercent;
                    //此时的结尾值为当前位置加上放大或缩小的比例加上移动的距离为endx坐标
                    canvas.drawLine(tabLineListCoordinate.get(selectPosi * 2) + slipSpace
                            , tabLineListCoordinate.get(selectPosi * 2 + 1)
                            , tabLineListCoordinate.get(selectPosi * 2) + widthChange + slipSpace + tabTextListWidth.get(selectPosi)
                            , tabLineListCoordinate.get(selectPosi * 2 + 1), linePaint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                (int) (tabHeight + lineTextSpace + lineHeight / 2 + getPaddingTop() + getPaddingBottom()));
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAllowChangePosi && isAllowTouchChange) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    lastX = event.getX();
                    lastY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    skipToPosi(Float.valueOf(((event.getX() - getPaddingLeft()) / tabWidth)).intValue());
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
    public void setTabList(@Nullable List<String> tabList, @Nullable Integer selectPosi) {
        //计算坐标
        if (tabList != null) {
            this.tabList.clear();
            this.tabListCoordinate.clear();
            this.tabLineListCoordinate.clear();
            this.tabTextListWidth.clear();

            Paint.FontMetrics fm = this.tabPaint.getFontMetrics();
            //文本底部坐标
            float textCoordinateY = (this.tabHeight - fm.bottom - fm.top) / (float) 2 + (float) this.getPaddingTop();
            //当前左侧宽度
            float nowLeftWidth = (float) this.getPaddingLeft();
            //文本宽度
            float textWidth = 0.0F;
            for (String text : tabList) {
                if (text != null && !"".equals(text)) {
                    this.tabList.add(text);
                    textWidth = tabPaint.measureText(text);
                    if (textWidth > tabWidth) {
                        textWidth = tabWidth;
                    }
                    //添加文本坐标
                    this.tabListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2);
                    this.tabListCoordinate.add(textCoordinateY);
                    //判断是否需要使用和文本相同宽度的下划线
                    if (lineWidth != 0f) {
                        tabLineListCoordinate.add(nowLeftWidth + (tabWidth - lineWidth) / 2);
                        tabLineListCoordinate.add(textCoordinateY + linePaint.getStrokeWidth() / 2 + lineTextSpace);
                    } else {
                        tabLineListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2);
                        tabLineListCoordinate.add(getPaddingTop() + tabHeight + linePaint.getStrokeWidth() / 2 + lineTextSpace);
                    }
                    //存储所有文本宽度
                    tabTextListWidth.add(textWidth);
                    nowLeftWidth += tabWidth;
                }
            }

            //是否允许绘制线条
            isAllowDrawLine = tabLineListCoordinate.size() == tabList.size() * 2;

            //重新设置宽高
            if (getLayoutParams() != null) {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = Float.valueOf(getPaddingLeft() + getPaddingRight() + tabList.size() * tabWidth).intValue();
                layoutParams.height = Float.valueOf(getPaddingTop() + getPaddingBottom() + tabHeight).intValue();
                setLayoutParams(layoutParams);
            } else {
                setLayoutParams(new ViewGroup.LayoutParams(Float.valueOf(getPaddingLeft() + getPaddingRight() + tabList.size() * tabWidth).intValue()
                        , Float.valueOf(getPaddingTop() + getPaddingBottom() + tabHeight).intValue()));
            }

        }
        //跳转到指定位置
        if (selectPosi != null) {
            skipToPosi(selectPosi);
        } else {
            //重绘
            invalidate();
        }

    }

    /**
     * 跳转到指定位置
     */
    @Override
    public void skipToPosi(int posi) {
        if (isAllowChangePosi) {
            if (posi < this.tabList.size()) {
                this.selectPosi = posi;
                invalidate();
                //回调位置
                if (tabChangeListener != null)
                    tabChangeListener.onChangePosi(this.selectPosi);
            }
        }
    }

    /**
     * 滑动到指定位置，带百分比滑动
     */
    @Override
    public void slipToPosi(int slipToPosi, float percent) {
        if (isAllowChangePosi) {
            if (slipToPosi < this.tabList.size()) {
                this.slipToPosi = slipToPosi;
                //回调位置
                if (tabChangeListener != null)
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
        if (isAllowChangePosi && slipToPosi < this.tabList.size()) {
            isAllowChangePosi = false;
            this.slipToPosi = slipToPosi;
            //回调位置
            if (tabChangeListener != null)
                tabChangeListener.onChangePosi(this.selectPosi);
            new Thread(slipSkipToPosiRunnable).start();
        }
    }

    /**
     * 获取下划线y轴坐标
     */
    public final float getLineCoordinateY() {
        if (tabLineListCoordinate.size() > 1) {
            return tabLineListCoordinate.get(1);
        } else {
            return 0f;
        }
    }

    /**
     * 修改滑动进度
     */
    private void changeSlipPercent(Float percent) {
        if (percent == 1) {
            this.selectPosi = slipToPosi;
            lineSlipPercent = 0f;
        } else {
            if (slipToPosi < selectPosi) {
                lineSlipPercent = -percent % 1.0f;
            } else {
                lineSlipPercent = percent % 1.0f;
            }
        }
        postInvalidate();
    }

    public void setTabChangeListener(AvlwBaseHorizontalSlipTabLayoutChangeListener tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }
}
