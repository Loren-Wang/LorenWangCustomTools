package android.lorenwang.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

/**
 * 创建时间：2019-03-20 下午 16:00:41
 * 创建人：王亮（Loren wang）
 * 功能作用：间隔线视图
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class KerleyView : View,CustomViewCommon {

    private var spaceLeft = 0f
    private var spaceRight = 0f
    private var spaceTop = 0f
    private var spaceBottom = 0f
    private var orientation = 0
    private var paint = Paint()

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
        val attr = context.obtainStyledAttributes(attrs, R.styleable.KerleyView, defStyleAttr, 0)
        spaceLeft = attr.getDimension(R.styleable.KerleyView_kv_spaceLeft,spaceLeft)
        spaceRight = attr.getDimension(R.styleable.KerleyView_kv_spaceRight,spaceRight)
        spaceTop = attr.getDimension(R.styleable.KerleyView_kv_spaceTop,spaceTop)
        spaceBottom = attr.getDimension(R.styleable.KerleyView_kv_spaceBottom,spaceBottom)
        orientation = attr.getInt(R.styleable.KerleyView_kv_orientation,orientation)
        paint.isAntiAlias = true
        paint.strokeWidth = attr.getDimension(R.styleable.KerleyView_kv_kerleyHeight,2f)
        paint.color = attr.getColor(R.styleable.KerleyView_kv_kerleyColor,Color.BLACK)
        attr.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        if(orientation == 0) {
            canvas.drawLine(spaceLeft, paint.strokeWidth / 2 + spaceTop, width - spaceRight, paint.strokeWidth / 2 - spaceBottom, paint)
        }else if(orientation == 1){
            canvas.drawLine((width / 2).toFloat(), spaceTop, (width / 2).toFloat(), height - spaceBottom, paint)
        }
    }

    override fun setBackground(background: Drawable?) {
    }

    override fun setBackgroundColor(color: Int) {
    }

    override fun setBackgroundResource(resid: Int) {
    }

    override fun release() {

    }
}
