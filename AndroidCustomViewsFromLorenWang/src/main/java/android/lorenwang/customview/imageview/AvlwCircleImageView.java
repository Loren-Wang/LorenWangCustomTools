package android.lorenwang.customview.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.lorenwang.customview.AvlwCustomViewCommon;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

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
public class AvlwCircleImageView extends AppCompatImageView implements AvlwCustomViewCommon {

    /********************************************绘制参数*******************************************/
    /**
     * 圆形画笔
     */
    private Paint circlePaint;

    /**
     * 边框画笔
     */
    private Paint borderCirclePaint;


    public AvlwCircleImageView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AllImageview, defStyleAttr, 0);

        //初始化圆形画笔
        circlePaint = new Paint();
        circlePaint.reset();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        //初始化边框画笔
        borderCirclePaint = new Paint();
        borderCirclePaint.reset();
        borderCirclePaint.setAntiAlias(true);
        borderCirclePaint.setColor(attr.getColor(R.styleable.AllImageview_allImageBorderColor, Color.TRANSPARENT));
        borderCirclePaint.setStrokeWidth(attr.getDimension(R.styleable.AllImageview_allImageBorderWidth, 0));
        borderCirclePaint.setStyle(Paint.Style.STROKE);

        attr.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置圆形图片
        circlePaint.setShader(new BitmapShader(AtlwImageCommonUtil.getInstance().getCircleBitmap(
                getDrawable(), w, h, (int) (Math.min(w, h) * 1.0 / 2)), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            //画圆形
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (int) (Math.min(getWidth(), getHeight()) * 1.0 / 2) - borderCirclePaint.getStrokeWidth(), circlePaint);
            //画边框
            canvas.drawCircle(getWidth() / 2,getHeight() / 2,(int) (Math.min(getWidth(), getHeight()) * 1.0 / 2) - borderCirclePaint.getStrokeWidth(),borderCirclePaint);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public void release() {
    }
}
