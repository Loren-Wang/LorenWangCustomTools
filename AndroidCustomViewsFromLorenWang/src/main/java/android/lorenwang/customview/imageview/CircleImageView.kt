package android.lorenwang.customview.imageview

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.lorenwang.customview.CustomViewCommon
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet


/**
 * 创建时间：2019-03-12 下午 16:09:33
 * 创建人：王亮（Loren wang）
 * 功能作用：圆角图片控件，仅仅只显示圆角图片，其他的一概不显示
 * 思路：使用PorterDuff.Mode合成图片
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class CircleImageView : AppCompatImageView, CustomViewCommon {
    //图片通用处理
    private var imageViewCommonDeal: ImageViewCommonDeal? = null

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
            }else{
                canvas.saveLayer(RectF(0f, 0f, width.toFloat(), height.toFloat()), null)
            }
            super.onDraw(canvas)
            //绘制圆形图片
            imageViewCommonDeal?.drawCircle(canvas, (width / 2).toFloat(), (height / 2).toFloat(), (Math.min(width, height) / 2).toFloat())
        } else {
            super.onDraw(canvas)
        }
    }

    /**
     * 释放内存相关的
     */
    override fun release() {
        imageViewCommonDeal?.release()
        imageViewCommonDeal = null
    }
}
