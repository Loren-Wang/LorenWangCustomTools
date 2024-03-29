package android.lorenwang.customview.imageview;

import android.annotation.SuppressLint;
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
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 功能作用：圆角矩形ImageView
 * 创建时间：2019-03-14 上午 10:50:8
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */


public class AvlwRoundedRectangleImageView extends AppCompatImageView implements AvlwCustomViewCommon {
    /*---------------------------------------------绘制参数---------------------------------------------*/
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

    public AvlwRoundedRectangleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwRoundedRectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 控件初始化
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        @SuppressLint("CustomViewStyleable")
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
        borderPaint.setColor(attr.getColor(R.styleable.AllImageview_avlw_aiv_borderColor, Color.TRANSPARENT));
        borderPaint.setStrokeWidth(attr.getDimension(R.styleable.AllImageview_avlw_aiv_borderWidth, 0));
        borderPaint.setStyle(Paint.Style.STROKE);

        //生成圆角的角度数组以及边框角度数组
        this.generateRoundedRectangleAngles(attr.getDimension(R.styleable.AllImageview_avlw_aiv_roundedRectangleAngle, 0.0F),
                attr.getDimension(R.styleable.AllImageview_avlw_aiv_roundedRectangleAngleLeftTop, 0.0F),
                attr.getDimension(R.styleable.AllImageview_avlw_aiv_roundedRectangleAngleRightTop, 0.0F),
                attr.getDimension(R.styleable.AllImageview_avlw_aiv_roundedRectangleAngleLeftBottom, 0.0F),
                attr.getDimension(R.styleable.AllImageview_avlw_aiv_roundedRectangleAngleRightBottom, 0.0F));

        attr.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置圆角图片
        contentPaint.setShader(new BitmapShader(AtlwImageCommonUtil.getInstance().getRoundedCornerBitmap(getDrawable(), w, h,
                roundedRectangleAngles[0],roundedRectangleAngles[2],roundedRectangleAngles[4],roundedRectangleAngles[6])
                , Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//            //画圆形
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (int) (Math.min(getWidth(), getHeight()) * 1.0 / 2) - borderPaint.getStrokeWidth(), contentPaint);
        //画边框
        canvas.drawPath(getRoundedRectanglePath(getWidth(), getHeight(), this.roundedRectangleAngles), contentPaint);
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
    private Path getRoundedRectanglePath(float right, float bottom, float[] radii) {
        Path newPath = new Path();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newPath.addRoundRect((float) 0, (float) 0, right, bottom, radii, Path.Direction.CCW);
        } else {
            newPath.addRoundRect(new RectF((float) 0, (float) 0, right, bottom), radii, Path.Direction.CCW);
        }
        newPath.close();
        return newPath;
    }


    /**
     * 释放内存
     */
    @Override
    public void release() {
    }

}
