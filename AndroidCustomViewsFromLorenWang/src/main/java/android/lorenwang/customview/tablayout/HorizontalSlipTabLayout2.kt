package android.lorenwang.customview.tablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.lorenwang.customview.R
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*

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
class HorizontalSlipTabLayout2 : View ,BaseHorizontalSlipTabLayout{

    /*******************************************绘制部分参数****************************************/
    /**
     * 文本画笔
     */
    private var tabPaint: Paint = Paint()
    /**
     * 下划线画笔
     */
    private var tabBgPaint: Paint = Paint()
    /**
     * 视图背景画笔
     */
    private var viewBgPaint: Paint = Paint()
    /**
     * tab文本内容坐标，以两个为一组，分别代表着xy坐标,为文本左下角坐标
     */
    private var tabListCoordinate = arrayListOf<Float>()
    /**
     * tab选中颜色
     */
    private var tabTextColorY = Color.BLUE
    /**
     * tab未选中颜色
     */
    private var tabTextColorN = Color.BLACK
    /**
     * tab宽度
     */
    private var tabWidth = 300f
    /**
     * tab高度
     */
    private var tabHeight = 100f
    /**
     * 视图圆角半径
     */
    private var viewRadius = 0f
    /**
     * tab背景顶部位置
     */
    private var tabBgTop = 0f
    /**
     * tab背景底部位置
     */
    private var tabBgBottom = 0f


    /*******************************************配置部分参数****************************************/
    /**
     * 背景滑动百分比
     */
    private var tabBgSlipPercent = 0f
    /**
     * 是否允许触摸修改
     */
    private var isAllowTouchChange = true
    /**
     * 是否允许修改位置
     */
    private var isAllowChangePosi = true


    /*******************************************内容部分参数****************************************/
    /**
     * tab列表
     */
    private var tabList = arrayListOf<String>()
    /**
     * 选中位置
     */
    private var selectPosi = 0
    /**
     * 滑动到指定位置
     */
    private var slipToPosi = 0

    /*******************************************其他部分参数****************************************/
    /**
     * 滑动跳转到指定位置线程
     */
    private val slipSkipToPosiRunnable = Runnable {
        var percent = 0f
        while (true) {
            percent += 0.05f
            /**
             * 修改滑动进度
             */
            changeSlipPercent(percent)
            if (percent.toInt() == 1) {
                break
            }
            try {
                if (percent < 0.75) {
                    Thread.sleep(15)
                } else {
                    Thread.sleep(30)
                }
            } catch (e: Exception) {
            }
        }
        isAllowChangePosi = true;
    }

    constructor(context: Context) : super(context) {
        init(context, null, -1)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, -1)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    /**
     * 控件初始化
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSlipTabLayout, defStyleAttr, 0)
        tabWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabWidth, tabWidth)
        tabHeight = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabHeight, tabHeight)
        tabTextColorY = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabTextColorY, tabTextColorY)
        tabTextColorN = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabTextColorN, tabTextColorN)
        viewRadius = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_viewRadius, viewRadius)

        measure(0, 0)

        //初始化tab文本画笔
        tabPaint.reset()
        tabPaint.textSize = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_tabTextSize, 50f);
        tabPaint.isAntiAlias = true

        //初始化tab背景画笔
        tabBgPaint.reset()
        tabBgPaint.isAntiAlias = true
        tabBgPaint.color = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_tabBgColorY, Color.WHITE)
        tabBgPaint.style = Paint.Style.FILL

        //视图背景画笔
        viewBgPaint.reset()
        viewBgPaint.isAntiAlias = true
        viewBgPaint.color = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_viewBgColor, Color.BLACK)
        viewBgPaint.style = Paint.Style.FILL
        attr.recycle()
    }

    override fun onDraw(canvas: Canvas?) {

        /**
         * 绘制整体背景
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), viewRadius, viewRadius, viewBgPaint)
        } else {
            canvas?.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), viewRadius, viewRadius, viewBgPaint)
        }

        /**
         * 绘制tab背景
         */
        tabBgTop = (height - tabHeight) / 2
        tabBgBottom = tabBgTop + tabHeight
        if (tabBgSlipPercent == 0f) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas?.drawRoundRect(selectPosi * tabWidth + paddingLeft, tabBgTop
                        , (selectPosi + 1) * tabWidth + paddingLeft, tabBgBottom
                        , viewRadius, viewRadius, tabBgPaint)
            } else {
                canvas?.drawRoundRect(RectF(selectPosi * tabWidth + paddingLeft, tabBgTop
                        , (selectPosi + 1) * tabWidth + paddingLeft, tabBgBottom)
                        , viewRadius, viewRadius, tabBgPaint)
            }
        } else {
            /**
             * 根据百分比做移动绘制
             */
            /**
             * 滑动距离为目标x坐标减去当前x坐标乘以百分比
             */
            val slipSpace = tabWidth * tabBgSlipPercent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas?.drawRoundRect(selectPosi * tabWidth + slipSpace + paddingLeft, tabBgTop
                        , (selectPosi + 1) * tabWidth + slipSpace + paddingLeft, tabBgBottom
                        , viewRadius, viewRadius, tabBgPaint)
            } else {
                canvas?.drawRoundRect(RectF(selectPosi * tabWidth + slipSpace + paddingLeft, tabBgTop
                        , (selectPosi + 1) * tabWidth + slipSpace + paddingLeft, tabBgBottom)
                        , viewRadius, viewRadius, tabBgPaint)
            }
        }

        /**
         * 绘制文本
         */
        for (i in 0 until tabList.size) {
            if (i == selectPosi) {
                tabPaint.color = tabTextColorY
            } else {
                tabPaint.color = tabTextColorN
            }
            canvas?.drawText(tabList[i], tabListCoordinate[2 * i], tabListCoordinate[2 * i + 1], tabPaint)
        }


    }

    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (isAllowChangePosi && isAllowTouchChange) {
            when (event?.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.getX()
                    lastY = event.getY()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    skipToPosi(((event.getX() - paddingLeft) / tabWidth).toInt())
                    return true
                }
                else -> true
            }
        } else {
            super.onTouchEvent(event)
        }
    }


    /**
     * 设置tab列表
     */
    override fun setTabList(tabList: ArrayList<String>?, selectPosi: Int?) {
        /**
         * 计算坐标
         */
        if (tabList != null) {
            var fm = tabPaint.getFontMetrics()
            /**
             * 文本底部坐标
             */
            var textCoordinateY = (tabHeight - fm.bottom - fm.top) / 2 + paddingTop
            /**
             * 当前左侧宽度
             */
            var nowLeftWidth = paddingLeft.toFloat()
            /**
             * 文本宽度
             */
            var textWidth: Float
            for (text: String? in tabList) {
                if (text != null && !"".equals(text)) {
                    this.tabList.add(text)
                    textWidth = tabPaint.measureText(text)
                    if (textWidth > tabWidth) {
                        textWidth = tabWidth
                    }
                    /**
                     * 添加文本坐标
                     */
                    this.tabListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2)
                    this.tabListCoordinate.add(textCoordinateY)
                    nowLeftWidth += tabWidth
                }
            }

            /**
             * 重新设置宽高
             */
            if (layoutParams != null) {
                layoutParams.width = (paddingLeft + paddingRight + tabList.size * tabWidth).toInt()
                layoutParams.height = (paddingTop + paddingBottom + tabHeight).toInt()
            } else {
                layoutParams = ViewGroup.LayoutParams((paddingLeft + paddingRight + tabList.size * tabWidth).toInt()
                        , (paddingTop + paddingBottom + tabHeight).toInt())
            }
        }


        /**
         * 跳转到指定位置
         */
        if (selectPosi != null) {
            skipToPosi(selectPosi)
        }
    }

    /**
     * 跳转到指定位置
     */
    override fun skipToPosi(posi: Int) {
        if (isAllowChangePosi && posi != selectPosi) {
            if (posi < this.tabList.size) {
                this.selectPosi = posi
                invalidate()
            }
        }
    }

    /**
     * 滑动到指定位置，带百分比滑动
     */
    override fun slipToPosi(slipToPosi: Int, percent: Float) {
        if (isAllowChangePosi && slipToPosi != selectPosi) {
            if (slipToPosi < this.tabList.size) {
                this.slipToPosi = slipToPosi
            }
            changeSlipPercent(percent)
        }
    }

    /**
     * 滑动跳转到指定位置
     */
    override fun slipSkipToPosi(slipToPosi: Int) {
        if (isAllowChangePosi && slipToPosi < this.tabList.size
                && slipToPosi != selectPosi) {
            isAllowChangePosi = false
            this.slipToPosi = slipToPosi
            Thread(slipSkipToPosiRunnable).start()
        }
    }

    /**
     * 修改滑动进度
     */
    private fun changeSlipPercent(percent: Float) {
        if (percent.toInt() == 1) {
            this.selectPosi = slipToPosi;
            tabBgSlipPercent = 0f
        } else {
            if (slipToPosi < selectPosi) {
                tabBgSlipPercent = -(percent % 1.0).toFloat()
            } else {
                tabBgSlipPercent = (percent % 1.0).toFloat()
            }
        }
        postInvalidate()
    }
}
