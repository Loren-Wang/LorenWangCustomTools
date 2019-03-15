package android.lorenwang.customview.imageview

import android.content.Context
import android.graphics.*
import android.lorenwang.customview.CustomViewCommon
import android.lorenwang.customview.R
import android.util.AttributeSet

/**
 * 创建时间：2019-03-12 下午 17:33:56
 * 创建人：王亮（Loren wang）
 * 功能作用：ImageView通用处理实体
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class ImageViewCommonDeal : CustomViewCommon {

    //圆形图片路径
    private var circlePaint: Paint? = null
    //圆形图片路径
    private var circlePath: Path? = null
    //圆形边框路径
    private var borderCirclePath: Path? = null
    //圆角图片路径
    private var roundedRectanglePaint: Paint? = null
    //圆角图片路径
    private var roundedRectanglePath: Path? = null
    //圆角矩形区域
    var roundedRectangleAngleRectf: RectF? = null;
    //边框圆角矩形区域
    var borderRoundedRectangleAngleRectf: RectF? = null;
    //圆角矩形角度
    private var roundedRectangleAngles = FloatArray(0)


    //边框宽度（默认无宽度）
    private var borderWidth: Float = 0f
    //边框颜色(默认透明)
    private var borderColor: Int = Color.WHITE
    //边框画笔
    private var borderPaint: Paint? = null
    //圆角边框路径
    private var borderRoundedRectanglePath: Path? = null
    //边框圆角矩形角度
    private var borderRoundedRectangleAngles = FloatArray(0)


    /**
     * 控件参数
     */
    fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AllImageview, defStyleAttr, 0)
        borderWidth = attr.getDimension(R.styleable.AllImageview_allImageBorderWidth, borderWidth)
        borderColor = attr.getColor(R.styleable.AllImageview_allImageBorderColor, borderColor)
        var roundedRectangleAngle = attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngle, 0f)
        var roundedRectangleAngleLeftTop = attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftTop, 0f)
        var roundedRectangleAngleRightTop = attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightTop, 0f)
        var roundedRectangleAngleLeftBottom = attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftBottom, 0f)
        var roundedRectangleAngleRightBottm = attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightBottom, 0f)
        attr.recycle()
        //生成圆角的角度数组以及边框角度数组
        generateRoundedRectangleAngles(roundedRectangleAngle, roundedRectangleAngleLeftTop
                , roundedRectangleAngleRightTop, roundedRectangleAngleLeftBottom, roundedRectangleAngleRightBottm)
    }

    /**
     * 绘制圆形
     */
    fun drawCircle(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        circlePath = getCirclePath(circlePath, centerX, centerY, radius)
        circlePaint = getImagePaint(circlePaint)
        //绘制内容区域
        drawContent(canvas, circlePath, circlePaint)
        //边框部分
        if(borderWidth > 0) {
            //边框路径
            borderCirclePath = getCirclePath(borderCirclePath, centerX, centerY, radius - borderWidth / 2 + 1)
            //绘制边框
            drawBorder(canvas, borderCirclePath)
        }
        // 恢复画布
        canvas.restore();
    }

    /**
     * 绘制圆角矩形
     */
    fun drawRoundedRectangle(canvas: Canvas, width: Float, height: Float) {
        //内容范围
        if (roundedRectangleAngleRectf == null) {
            roundedRectangleAngleRectf = RectF(0f, 0f, width, height);
        }
        //内容path
        roundedRectanglePath = getRoundedRectanglePath(roundedRectanglePath, roundedRectangleAngleRectf!!, roundedRectangleAngles)
        //内容画笔
        roundedRectanglePaint = getImagePaint(roundedRectanglePaint)
        //绘制内容区域
        drawContent(canvas, roundedRectanglePath, roundedRectanglePaint)

        //边框部分
        if(borderWidth > 0) {
            //边框范围
            if (borderRoundedRectangleAngleRectf == null) {
                borderRoundedRectangleAngleRectf = RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2);
            }
            //边框路径
            borderRoundedRectanglePath = getRoundedRectanglePath(borderRoundedRectanglePath, borderRoundedRectangleAngleRectf!!, borderRoundedRectangleAngles)
            //绘制边框
            drawBorder(canvas, borderRoundedRectanglePath)
        }
        // 恢复画布
        canvas.restore();

    }

    /**
     * 绘制内容区域
     */
    private fun drawContent(canvas: Canvas, path: Path?, paint: Paint?) {
        if (path != null && paint != null) {
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
            canvas.drawPath(path, paint);
            paint.setXfermode(null)
        }
    }

    /**
     * 绘制边框
     */
    private fun drawBorder(canvas: Canvas, path: Path?) {
        if (borderWidth > 0 && path != null) {
            borderPaint = getBorderPaint(borderPaint, borderWidth, borderColor)
            canvas.drawPath(path, borderPaint)
        }
    }

    /**
     * 获取圆形path
     */
    private fun getCirclePath(path: Path?, centerX: Float, centerY: Float, radius: Float): Path? {
        if (path == null) {
            var newPath = Path()
            newPath.addCircle(centerX, centerY, radius, Path.Direction.CCW)
            return newPath;
        } else {
            path.reset()
            path.addCircle(centerX, centerY, radius, Path.Direction.CCW)
            return path
        }
    }

    /**
     * 获取图片部分画笔
     */
    private fun getImagePaint(paint: Paint?): Paint? {
        if (paint == null) {
            var newPaint = Paint()
            newPaint.setAntiAlias(true);
            newPaint.setStyle(Paint.Style.FILL);
            return newPaint
        } else {
            paint.reset()
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            return paint;
        }
    }

    /**
     * 获取圆角path
     */
    private fun getRoundedRectanglePath(path: Path?, rectF: RectF, radii: FloatArray): Path? {
        if (path == null) {
            var newPath = Path()
            newPath.addRoundRect(rectF, radii, Path.Direction.CCW)
            newPath.close()
            return newPath;
        } else {
            path.reset()
            path.addRoundRect(rectF, radii, Path.Direction.CCW)
            path.close()
            return path
        }
    }

    /**
     * 获取边框画笔
     */
    private fun getBorderPaint(paint: Paint?, borderWidth: Float, borderColor: Int): Paint? {
        if (paint == null) {
            var newPaint = Paint()
            newPaint.setAntiAlias(true)
            newPaint.setStyle(Paint.Style.STROKE)
            newPaint.strokeWidth = borderWidth
            newPaint.color = borderColor
            return newPaint
        } else {
            paint.reset()
            paint.setAntiAlias(true)
            paint.setStyle(Paint.Style.STROKE)
            paint.strokeWidth = borderWidth
            paint.color = borderColor
            return paint;
        }
    }

    /**
     * 生成圆角的角度数组以及边框角度数组
     */
    private fun generateRoundedRectangleAngles(roundedRectangleAngle: Float, roundedRectangleAngleLeftTop: Float, roundedRectangleAngleRightTop: Float
                                               , roundedRectangleAngleLeftBottom: Float, roundedRectangleAngleRightBottm: Float) {
        if (roundedRectangleAngle == 0f) {
            //生成圆角角度数组
            borderRoundedRectangleAngles = floatArrayOf(roundedRectangleAngleLeftTop, roundedRectangleAngleLeftTop
                    , roundedRectangleAngleRightTop, roundedRectangleAngleRightTop
                    , roundedRectangleAngleRightBottm, roundedRectangleAngleRightBottm
                    , roundedRectangleAngleLeftBottom, roundedRectangleAngleLeftBottom);
            //生成边框圆角角度数组
            roundedRectangleAngles = floatArrayOf(roundedRectangleAngleLeftTop + borderWidth / 2, roundedRectangleAngleLeftTop + borderWidth / 2
                    , roundedRectangleAngleRightTop + borderWidth / 2, roundedRectangleAngleRightTop + borderWidth / 2
                    , roundedRectangleAngleRightBottm + borderWidth / 2, roundedRectangleAngleRightBottm + borderWidth / 2
                    , roundedRectangleAngleLeftBottom + borderWidth / 2, roundedRectangleAngleLeftBottom + borderWidth / 2);
        }else{
            //生成边框圆角角度数组
            borderRoundedRectangleAngles = floatArrayOf(roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle)
            //生成圆角角度数组
            roundedRectangleAngles = floatArrayOf(roundedRectangleAngle + borderWidth / 2, roundedRectangleAngle + borderWidth / 2
                    , roundedRectangleAngle + borderWidth / 2, roundedRectangleAngle + borderWidth / 2
                    , roundedRectangleAngle + borderWidth / 2, roundedRectangleAngle + borderWidth / 2
                    , roundedRectangleAngle + borderWidth / 2, roundedRectangleAngle + borderWidth / 2)
        }
    }

    override fun release() {

    }
}
