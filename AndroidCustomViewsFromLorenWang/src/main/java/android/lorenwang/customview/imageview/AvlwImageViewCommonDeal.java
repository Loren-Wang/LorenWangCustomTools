package android.lorenwang.customview.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.lorenwang.customview.AvlwCustomViewCommon;
import android.lorenwang.customview.R;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


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
public class AvlwImageViewCommonDeal implements AvlwCustomViewCommon {
    //圆形图片路径
    private Paint circlePaint;
    //圆形图片路径
    private Path circlePath;
    //圆形边框路径
    private Path borderCirclePath;
    //圆角图片路径
    private Paint roundedRectanglePaint;
    //圆角图片路径
    private Path roundedRectanglePath;
    //圆角矩形区域
    @Nullable
    private RectF roundedRectangleAngleRectf;
    //边框圆角矩形区域
    @Nullable
    private RectF borderRoundedRectangleAngleRectf;
    //圆角矩形角度
    private float[] roundedRectangleAngles = new float[0];


    //边框宽度（默认无宽度）
    private float borderWidth = 0f;
    //边框颜色(默认透明)
    private int borderColor = Color.WHITE;
    //边框画笔
    private Paint borderPaint = new Paint();
    //圆角边框路径
    private Path borderRoundedRectanglePath;
    //边框圆角矩形角度
    private float[] borderRoundedRectangleAngles = new float[0];


    /**
     * 控件参数
     */
    public final void init( Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AllImageview, defStyleAttr, 0);
        this.borderWidth = attr.getDimension(R.styleable.AllImageview_allImageBorderWidth, this.borderWidth);
        this.borderColor = attr.getColor(R.styleable.AllImageview_allImageBorderColor, this.borderColor);
        //生成圆角的角度数组以及边框角度数组
        this.generateRoundedRectangleAngles(attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngle, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftTop, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightTop, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftBottom, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightBottom, 0.0F));
        attr.recycle();
    }

    /**
     * 绘制圆形
     */
    public void drawCircle( Canvas canvas, float centerX, float centerY, float radius) {
        this.circlePath = this.getCirclePath(this.circlePath, centerX, centerY, radius);
        this.circlePaint = this.getImagePaint(this.circlePaint);
        //绘制内容区域
        this.drawContent(canvas, this.circlePath, this.circlePaint);

        //边框部分
        if (this.borderWidth > (float) 0) {
            this.borderCirclePath = this.getCirclePath(this.borderCirclePath, centerX, centerY, radius - this.borderWidth / (float) 2 + (float) 1);
            this.drawBorder(canvas, this.borderCirclePath);
        }

        // 恢复画布
        canvas.restore();
    }

    /**
     * 绘制圆角矩形
     */
    public void drawRoundedRectangle( Canvas canvas, float width, float height) {
        //内容范围
        if (this.roundedRectangleAngleRectf == null) {
            this.roundedRectangleAngleRectf = new RectF(0.0F, 0.0F, width, height);
        }
        //内容path
        roundedRectanglePath = getRoundedRectanglePath(roundedRectanglePath, roundedRectangleAngleRectf, roundedRectangleAngles);
        //内容画笔
        roundedRectanglePaint = getImagePaint(roundedRectanglePaint);
        //绘制内容区域
        drawContent(canvas, roundedRectanglePath, roundedRectanglePaint);

        //边框部分
        if (borderWidth > 0) {
            //边框范围
            if (borderRoundedRectangleAngleRectf == null) {
                borderRoundedRectangleAngleRectf = new RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2);
            }
            //边框路径
            borderRoundedRectanglePath = getRoundedRectanglePath(borderRoundedRectanglePath
                    , borderRoundedRectangleAngleRectf, borderRoundedRectangleAngles);
            //绘制边框
            drawBorder(canvas, borderRoundedRectanglePath);
        }
        // 恢复画布
        canvas.restore();
    }

    /**
     * 绘制内容区域
     */
    private void drawContent(Canvas canvas, Path path, Paint paint) {
        if (path != null && paint != null) {
            paint.setXfermode((Xfermode) (new PorterDuffXfermode(PorterDuff.Mode.DST_IN)));
            canvas.drawPath(path, paint);
            paint.setXfermode((Xfermode) null);
        }
    }

    /**
     * 绘制边框
     */
    private void drawBorder(Canvas canvas, Path path) {
        if (this.borderWidth > (float) 0 && path != null) {
            this.borderPaint = this.getBorderPaint(this.borderPaint, this.borderWidth, this.borderColor);
            canvas.drawPath(path, this.borderPaint);
        }

    }

    /**
     * 获取圆形path
     */
    private Path getCirclePath(Path path, float centerX, float centerY, float radius) {
        if (path == null) {
            Path newPath = new Path();
            newPath.addCircle(centerX, centerY, radius, Path.Direction.CCW);
            return newPath;
        } else {
            path.reset();
            path.addCircle(centerX, centerY, radius, Path.Direction.CCW);
            return path;
        }
    }

    /**
     * 获取图片部分画笔
     */
    private Paint getImagePaint(Paint paint) {
        if (paint == null) {
            Paint newPaint = new Paint();
            newPaint.setAntiAlias(true);
            newPaint.setStyle(Paint.Style.FILL);
            return newPaint;
        } else {
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            return paint;
        }
    }

    /**
     * 获取圆角path
     */
    private Path getRoundedRectanglePath(Path path, RectF rectF, float[] radii) {
        if (path == null) {
            Path newPath = new Path();
            newPath.addRoundRect(rectF, radii, Path.Direction.CCW);
            newPath.close();
            return newPath;
        } else {
            path.reset();
            path.addRoundRect(rectF, radii, Path.Direction.CCW);
            path.close();
            return path;
        }
    }

    /**
     * 获取边框画笔
     */
    private Paint getBorderPaint(Paint paint, float borderWidth, int borderColor) {
        if (paint == null) {
            Paint newPaint = new Paint();
            newPaint.setAntiAlias(true);
            newPaint.setStyle(Paint.Style.STROKE);
            newPaint.setStrokeWidth(borderWidth);
            newPaint.setColor(borderColor);
            return newPaint;
        } else {
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            paint.setColor(borderColor);
            return paint;
        }
    }

    /**
     * 生成圆角的角度数组以及边框角度数组
     */
    private void generateRoundedRectangleAngles(float roundedRectangleAngle
            , float roundedRectangleAngleLeftTop, float roundedRectangleAngleRightTop
            , float roundedRectangleAngleLeftBottom, float roundedRectangleAngleRightBottm) {

        if (roundedRectangleAngle == 0f) {
            //生成圆角角度数组
            this.borderRoundedRectangleAngles = new float[]{roundedRectangleAngleLeftTop, roundedRectangleAngleLeftTop
                    , roundedRectangleAngleRightTop, roundedRectangleAngleRightTop
                    , roundedRectangleAngleRightBottm, roundedRectangleAngleRightBottm
                    , roundedRectangleAngleLeftBottom, roundedRectangleAngleLeftBottom};
            //生成边框圆角角度数组
            this.roundedRectangleAngles = new float[]{
                    roundedRectangleAngleLeftTop + this.borderWidth / 2f, roundedRectangleAngleLeftTop + this.borderWidth / 2f
                    , roundedRectangleAngleRightTop + this.borderWidth / 2f, roundedRectangleAngleRightTop + this.borderWidth / 2f
                    , roundedRectangleAngleRightBottm + this.borderWidth / 2f, roundedRectangleAngleRightBottm + this.borderWidth / 2f
                    , roundedRectangleAngleLeftBottom + this.borderWidth / 2f, roundedRectangleAngleLeftBottom + this.borderWidth / 2f};
        } else {
            //生成边框圆角角度数组
            this.borderRoundedRectangleAngles = new float[]{roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle};
            //生成圆角角度数组
            this.roundedRectangleAngles = new float[]{
                    roundedRectangleAngle + this.borderWidth / 2f, roundedRectangleAngle + this.borderWidth / 2f
                    , roundedRectangleAngle + this.borderWidth / 2f, roundedRectangleAngle + this.borderWidth / 2f
                    , roundedRectangleAngle + this.borderWidth / 2f, roundedRectangleAngle + this.borderWidth / 2f
                    , roundedRectangleAngle + this.borderWidth / 2f, roundedRectangleAngle + this.borderWidth / 2f};
        }
    }

    @Override
    public void release() {

    }
}
