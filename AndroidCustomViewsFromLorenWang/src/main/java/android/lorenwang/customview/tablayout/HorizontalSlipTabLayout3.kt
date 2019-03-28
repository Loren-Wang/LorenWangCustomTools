package android.lorenwang.customview.tablayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.lorenwang.customview.R
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
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
class HorizontalSlipTabLayout3 : FrameLayout,BaseHorizontalSlipTabLayout {

    private lateinit var tabView:HorizontalSlipTabLayout
    /**
     * 下划线容器画笔
     */
    private lateinit var lineContainerPaint:Paint
    private var lineContainerWidth:Float? = null


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
    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        tabView = HorizontalSlipTabLayout(context,attrs,defStyleAttr)
        addView(tabView)
        var params:ViewGroup.LayoutParams? = tabView.layoutParams
        if(params == null){
            params = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        }
        if(params is LayoutParams) {
            params.gravity = Gravity.CENTER
        }
        tabView.layoutParams = params

        val attr = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSlipTabLayout, defStyleAttr, 0)
        lineContainerPaint = Paint()
        lineContainerPaint.isAntiAlias = true
        lineContainerPaint.color = attr.getColor(R.styleable.HorizontalSlipTabLayout_hstl_lineContainerColor, Color.TRANSPARENT)
        lineContainerPaint.strokeWidth = attr.getDimension(R.styleable.HorizontalSlipTabLayout_hstl_lineContainerHeight, 0f)
        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        var lineContainerWidthPercent = attr.getFloat(R.styleable.HorizontalSlipTabLayout_hstl_lineContainerWidthPercent,-1f)
        if(lineContainerWidthPercent > 0){
            if(lineContainerWidthPercent > 1){
                lineContainerWidthPercent = 1f
            }
            lineContainerWidth = resources.displayMetrics.widthPixels * lineContainerWidthPercent
        }
        attr.recycle()
        //设置需要绘制，否则的话底部线条会无法绘制
        setWillNotDraw(false)
    }


    private var startX:Float = 0f
    private var stopX:Float = 0f
    private var lingContainerY:Float = 0f
    override fun onDraw(canvas: Canvas?) {
        lingContainerY = tabView.getLineCoordinateY()
        if(lineContainerWidth == null || lineContainerWidth!! > width){
            startX = paddingLeft.toFloat()
            stopX = (width - paddingRight).toFloat()
        }else{
            startX = (paddingLeft + (width - lineContainerWidth!!) / 2)
            stopX = (startX - lineContainerWidth!!)
        }
        canvas?.drawLine(startX,lingContainerY, stopX,lingContainerY,lineContainerPaint)
    }


    /**
     * 设置tab列表
     */
    override fun setTabList(tabList: ArrayList<String>?, selectPosi: Int?) {
        tabView.setTabList(tabList,selectPosi)
    }

    /**
     * 跳转到指定位置
     */
    override fun skipToPosi(posi:Int){
        tabView.skipToPosi(posi)
    }

    /**
     * 滑动到指定位置，带百分比滑动
     */
    override fun slipToPosi(slipToPosi: Int,percent:Float){
        tabView.slipToPosi(slipToPosi,percent)
    }

    /**
     * 滑动跳转到指定位置
     */
    override fun slipSkipToPosi(slipToPosi:Int){
        tabView.slipSkipToPosi(slipToPosi)
    }

}
