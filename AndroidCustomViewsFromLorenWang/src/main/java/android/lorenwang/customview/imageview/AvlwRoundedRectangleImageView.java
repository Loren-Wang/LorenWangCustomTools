package android.lorenwang.customview.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.lorenwang.customview.AvlwCustomViewCommon;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

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


public class AvlwRoundedRectangleImageView extends AppCompatImageView implements AvlwCustomViewCommon {
    /********************************************绘制参数*******************************************/
    /**
     * 圆形画笔
     */
    private Paint contentPaint;
    /**
     * 边框画笔
     */
    private Paint borderPaint;
    /**
     * 边框圆角矩形角度
     */
    private float[] borderRoundedRectangleAngles = new float[0];
    /**
     * 圆角矩形角度
     */
    private float[] roundedRectangleAngles = new float[0];
    /**
     * 内容path
     */
    private Path contentPath;
    /**
     * 边框path
     */
    private Path borderPath;

    public AvlwRoundedRectangleImageView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwRoundedRectangleImageView(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwRoundedRectangleImageView(Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 控件初始化
     */
    private final void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AllImageview, defStyleAttr, 0);

        //初始化圆形画笔
        contentPaint = new Paint();
        contentPaint.reset();
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);

        //初始化边框画笔
        borderPaint = new Paint();
        borderPaint.reset();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(attr.getColor(R.styleable.AllImageview_allImageBorderColor, Color.TRANSPARENT));
        borderPaint.setStrokeWidth(attr.getDimension(R.styleable.AllImageview_allImageBorderWidth, 0));
        borderPaint.setStyle(Paint.Style.STROKE);

        //生成圆角的角度数组以及边框角度数组
        this.generateRoundedRectangleAngles(attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngle, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftTop, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightTop, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleLeftBottom, 0.0F), attr.getDimension(R.styleable.AllImageview_allImageRoundedRectangleAngleRightBottom, 0.0F));

        attr.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置圆角图片
        contentPaint.setShader(new BitmapShader(AtlwImageCommonUtils.getInstance().getRoundedCornerBitmap(getDrawable(), w, h,
                roundedRectangleAngles[0],roundedRectangleAngles[2],roundedRectangleAngles[4],roundedRectangleAngles[6])
                , Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
//            //画圆形
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (int) (Math.min(getWidth(), getHeight()) * 1.0 / 2) - borderPaint.getStrokeWidth(), contentPaint);
            //画边框
            canvas.drawPath(getRoundedRectanglePath(null,0,0,getWidth(),getHeight(),this.roundedRectangleAngles),contentPaint);
        } else {
            super.onDraw(canvas);
        }
    }


    /**
     * 生成圆角的角度数组以及边框角度数组
     */
    private void generateRoundedRectangleAngles(float roundedRectangleAngle, float roundedRectangleAngleLeftTop
            , float roundedRectangleAngleRightTop, float roundedRectangleAngleLeftBottom, float roundedRectangleAngleRightBottm) {
        if (roundedRectangleAngle == 0f) {
            //生成圆角角度数组
            this.borderRoundedRectangleAngles = new float[]{roundedRectangleAngleLeftTop, roundedRectangleAngleLeftTop
                    , roundedRectangleAngleRightTop, roundedRectangleAngleRightTop
                    , roundedRectangleAngleRightBottm, roundedRectangleAngleRightBottm
                    , roundedRectangleAngleLeftBottom, roundedRectangleAngleLeftBottom};
            //生成边框圆角角度数组
            this.roundedRectangleAngles = new float[]{
                    roundedRectangleAngleLeftTop + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngleLeftTop + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngleRightTop + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngleRightTop + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngleRightBottm + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngleRightBottm + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngleLeftBottom + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngleLeftBottom + this.borderPaint.getStrokeWidth() / 2f};
        } else {
            //生成边框圆角角度数组
            this.borderRoundedRectangleAngles = new float[]{roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle
                    , roundedRectangleAngle, roundedRectangleAngle};
            //生成圆角角度数组
            this.roundedRectangleAngles = new float[]{
                    roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f
                    , roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f, roundedRectangleAngle + this.borderPaint.getStrokeWidth() / 2f};
        }
    }

    /**
     * 获取圆角path
     */
    private Path getRoundedRectanglePath(Path path,float left, float top, float right, float bottom,  float[] radii) {
        if (path == null) {
            Path newPath = new Path();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                newPath.addRoundRect(left,top,right,bottom, radii, Path.Direction.CCW);
            }else {
                newPath.addRoundRect(new RectF(left,top,right,bottom), radii, Path.Direction.CCW);
            }
            newPath.close();
            return newPath;
        } else {
            path.reset();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                path.addRoundRect(left,top,right,bottom, radii, Path.Direction.CCW);
            }else {
                path.addRoundRect(new RectF(left,top,right,bottom), radii, Path.Direction.CCW);
            }
            path.close();
            return path;
        }
    }


    /**
     * 释放内存
     */
    @Override
    public void release() {
    }

}
