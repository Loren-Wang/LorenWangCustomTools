package android.lorenwang.customview.imageview

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.lorenwang.customview.CustomViewCommon
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * 创建时间：2019-03-14 上午 10:50:8
 * 创建人：王亮（Loren wang）
 * 功能作用：圆角矩形ImageView
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class RoundedRectangleImageView : AppCompatImageView, CustomViewCommon {
    //图片通用处理
    private var imageViewCommonDeal: ImageViewCommonDeal? = null
    private val srcRectF: RectF = RectF() // 图片占的矩形区域

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
        imageViewCommonDeal = ImageViewCommonDeal()
        imageViewCommonDeal?.init(context, attrs, defStyleAttr)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            //里屏缓存当前画布
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
            } else {
                canvas.saveLayer(RectF(0f, 0f, width.toFloat(), height.toFloat()), null)
            }
            super.onDraw(canvas)
            //绘制圆形图片
            imageViewCommonDeal?.drawRoundedRectangle(canvas, width.toFloat(),height.toFloat())
        } else {
            super.onDraw(canvas)
        }
    }

    /**
     * 释放内存
     */
    override fun release() {
        imageViewCommonDeal?.release()
        imageViewCommonDeal = null;
    }

}
