package android.lorenwang.customview.tablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.lorenwang.customview.R
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * 创建时间：2019-03-15 下午 13:55:20
 * 创建人：王亮（Loren wang）
 * 功能作用：水平滑动tab布局
 * 思路：首先每个tab标签的宽高都是固定的,下划线的宽高也是,同时tab标签以及下划线的高度以及各个间距的高度不能超过总高度
 *      超过总高度会对所有的高度做等比缩小,当前通过布局文件可设置的参数如下
 *      tabWidth--tab宽度;
 *      tabHeight--tab高度；
 *      tabTextSize--tab文本大小；
 *      tabTextColorY--tab文本选中颜色；
 *      tabTextColorN--tab文本未选中颜色；
 *      lineWidth--下划线宽度；
 *      lineHeight--下划线高度；
 *      lineColor--下划线颜色；
 *      lineTextSpace--文本下划线之间间距；
 *
 *      在设置文本资源时对所有文本的坐标位置进行计算并存储，此时ondraw就可以直接根据坐标进行绘制
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class HorizontalSlipTabLayout : View {

    /*******************************************绘制部分参数****************************************/
    /**
     * 文本画笔
     */
    private var tabPaint: Paint? = null
    /**
     * 下划线画笔
     */
    private var linePaint: Paint? = null
    /**
     * 下划线坐标列表，以两个为一组，分别代表着startX坐标,为下划线左侧中心坐标
     */
    private var tabLineListCoordinate = arrayListOf<Float>()
    /**
     * tab文本内容坐标，以两个为一组，分别代表着xy坐标,为文本左下角坐标
     */
    private var tabListCoordinate = arrayListOf<Float>()
    /**
     * 所有的文本宽度
     */
    private var tabTextListWidth = arrayListOf<Float>()
    /**
     * tab选中颜色
     */
    private var tabTextColorY = Color.BLUE
    /**
     * tab选中颜色
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
     * 文本以及下划线之间的间距
     */
    private var lineTextSpace = 0f


    /*******************************************配置部分参数****************************************/
    /**
     * 下划线宽度，如果下划线宽度大于0，则所有的下划线都是用固定宽度，否则的话则下划线宽度和文本宽度相同
     */
    private var lineWidth = 0f
    /**
     * 下划线滑动百分比
     */
    private var lineSlipPercent = 0f
    /**
     * 是否允许触摸修改
     */
    private var isAllowTouchChange = true



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
     * 上一个选中位置
     */
    private var lastSelectPosi = 0


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
        tabWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_tabWidth, tabWidth)
        tabHeight = attr.getDimension(R.styleable.HorizontalSlipTabLayout_tabHeight, tabHeight)
        tabTextColorY = attr.getColor(R.styleable.HorizontalSlipTabLayout_tabTextColorY, tabTextColorY)
        tabTextColorN = attr.getColor(R.styleable.HorizontalSlipTabLayout_tabTextColorN, tabTextColorN)
        lineWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_lineWidth, lineWidth)
        lineTextSpace = attr.getDimension(R.styleable.HorizontalSlipTabLayout_lineTextSpace, lineTextSpace)

        //数据合理性检测，首先检测下划线宽度是否大于tab宽度
        if(lineWidth.compareTo(tabWidth) > 0){
            lineWidth = tabWidth
        }

        //初始化tab文本画笔
        tabPaint = Paint()
        tabPaint?.textSize = attr.getDimension(R.styleable.HorizontalSlipTabLayout_tabTextSize, 50f);
        tabPaint?.isAntiAlias = true

        //初始化下划线画笔
        linePaint = Paint();
        linePaint?.isAntiAlias = true
        linePaint?.style = Paint.Style.STROKE
        linePaint?.strokeWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_lineWidth, 10f)
        linePaint?.color = attr.getColor(R.styleable.HorizontalSlipTabLayout_lineColor, tabTextColorY)
        attr.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        /**
         * 绘制文本
         */
        for (i in 0 until tabList.size) {
            if(i == selectPosi){
                tabPaint?.color = tabTextColorY
            }else{
                tabPaint?.color = tabTextColorN
            }
            canvas?.drawText(tabList[i],tabListCoordinate[2 * i],tabListCoordinate[2 * i + 1],tabPaint)
        }

        /**
         * 绘制下划线
         */
        /**
         * 滑动距离为上一个x坐标减去当前坐标y乘以百分比
         */
        val slipSpace = (tabLineListCoordinate[lastSelectPosi * 2] - tabLineListCoordinate[selectPosi * 2]) * lineSlipPercent
        /**
         * 判断是否需要变动下划线宽度
         */
        if(lineWidth > 0){
            /**
             * 从上一个坐标开始安卓滑动距离进行滑动
             */
            canvas?.drawLine(tabLineListCoordinate[lastSelectPosi * 2] + slipSpace
                    ,tabLineListCoordinate[lastSelectPosi* 2 + 1]
                    ,tabLineListCoordinate[lastSelectPosi * 2] + lineWidth + slipSpace
                    , tabLineListCoordinate[lastSelectPosi* 2 + 1],linePaint)
        }else{
            /**
             * 获取宽度变化值,值为上一个位置文本宽度减去当前要跳转的位置的宽度乘以百分比
             */
            val widChange = (tabTextListWidth[lastSelectPosi] - tabTextListWidth[selectPosi]) * lineSlipPercent
            /**
             * 此时的结尾值为当前位置加上放大或缩小的比例加上移动的距离为endx坐标
             */
            canvas?.drawLine(tabLineListCoordinate[lastSelectPosi* 2] + slipSpace
                    ,tabLineListCoordinate[lastSelectPosi* 2 + 1]
                    ,tabLineListCoordinate[lastSelectPosi* 2] + widChange + slipSpace + tabTextListWidth[selectPosi]
                    , tabLineListCoordinate[lastSelectPosi* 2 + 1],linePaint)
        }
    }

    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (isAllowTouchChange) {
            when (event?.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.getX()
                    lastY = event.getY()
                    true
                }
                MotionEvent.ACTION_UP ->{
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
    fun setTabList(tabList: ArrayList<String>?, selectPosi: Int?) {
        /**
         * 计算坐标
         */
        if (tabList != null) {
            var fm = tabPaint?.getFontMetrics()
            /**
             * 文本底部坐标
             */
            var textCoordinateY = (tabHeight - fm!!.bottom + fm.top) / 2 - fm.top + paddingTop
            /**
             * 当前左侧宽度
             */
            var nowLeftWidth = paddingLeft.toFloat()
            /**
             * 文本宽度
             */
            var textWidth = 0f;
            for (text: String? in tabList) {
                if (text != null && !"".equals(text)) {
                    this.tabList.add(text)
                    textWidth = tabPaint?.measureText(text)!!
                    if (textWidth > tabWidth) {
                        textWidth = tabWidth
                    }
                    /**
                     * 添加文本坐标
                     */
                    this.tabListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2)
                    this.tabListCoordinate.add(textCoordinateY)
                    /**
                     * 判断是否需要使用和文本相同宽度的下划线
                     */
                    if(lineWidth != 0f){
                        tabLineListCoordinate.add(nowLeftWidth + (tabWidth - lineWidth) / 2)
                        tabLineListCoordinate.add(textCoordinateY  + linePaint?.strokeWidth!! / 2 + lineTextSpace)
                    }else{
                        tabLineListCoordinate.add(nowLeftWidth + (tabWidth - textWidth) / 2)
                        tabLineListCoordinate.add(paddingTop + tabHeight  + linePaint?.strokeWidth!! / 2 + lineTextSpace)
                    }
                    /**
                     * 存储所有文本宽度
                     */
                    tabTextListWidth.add(textWidth)
                    nowLeftWidth += tabWidth
                }
            }
        }
        /**
         * 跳转到指定位置
         */
        if(selectPosi != null){
            skipToPosi(selectPosi)
        }
    }

    /**
     * 跳转到指定位置
     */
    fun skipToPosi(posi:Int){
        if(posi < this.tabList.size) {
            this.selectPosi = posi
            invalidate()
            this.lastSelectPosi = posi
        }
    }
}
