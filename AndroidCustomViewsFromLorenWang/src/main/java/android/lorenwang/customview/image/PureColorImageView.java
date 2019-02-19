package android.lorenwang.customview.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.lorenwang.customview.R;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建时间：2019-02-15 下午 15:45:52
 * 创建人：王亮（Loren wang）
 * 功能作用：纯色ImageView，直接覆盖掉原始图片颜色，然后绘制一个纯色的全部图片，仅显示相交的部分，不过会将原始图片的颜色覆盖
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

@SuppressLint("AppCompatCustomView")
public class PureColorImageView extends ImageView {
    private int color;
    private Paint paint;
    private RectF rectF;

    public PureColorImageView(Context context) {
        super(context);
        init(context,null,-1);
    }

    public PureColorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,-1);
    }

    public PureColorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PureColorImageView, defStyleAttr, 0);
        //获取要显示的color颜色值
        color = a.getColor(R.styleable.PureColorImageView_pureColor, 0);
        if(color != 0){
            paint = new Paint();
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectF = new RectF(0,0,getWidth(),getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint != null){
            canvas.drawRect(rectF,paint);
        }
    }
}
