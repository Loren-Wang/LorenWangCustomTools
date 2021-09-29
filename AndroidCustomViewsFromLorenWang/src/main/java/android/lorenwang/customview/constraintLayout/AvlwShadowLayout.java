package android.lorenwang.customview.constraintLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 功能作用：阴影布局
 * 初始注释时间： 2021/9/28 16:03
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AvlwShadowLayout extends ConstraintLayout {
    /**
     * 阴影路径
     */
    protected Path shadowPath;
    /**
     * 阴影画笔
     */
    private final Paint shadowPaint = new Paint();
    /**
     * 阴影颜色
     */
    protected int shadowColor = Color.TRANSPARENT;
    /**
     * 阴影宽度
     */
    protected int shadowWidth = 0;

    public AvlwShadowLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwShadowLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwShadowLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    protected void init(Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvlwShadowLayout);
        shadowWidth = typedArray.getDimensionPixelOffset(R.styleable.AvlwShadowLayout_avlw_sl_shadowWidth, shadowWidth);
        shadowColor = typedArray.getColor(R.styleable.AvlwShadowLayout_avlw_sl_shadowColor, shadowColor);
        typedArray.recycle();
        //重新设置画笔
        resetPaint();
        //设置内间距给阴影留空间
        setPadding(shadowWidth + getPaddingLeft(), shadowWidth + getPaddingTop(), shadowWidth + getPaddingRight(), shadowWidth + getPaddingBottom());
    }

    /**
     * 设置阴影背景的绘制路径
     *
     * @param shadowPath 绘制路径
     */
    public void setShadowPath(Path shadowPath) {
        this.shadowPath = shadowPath;
    }


    /**
     * 重新设置画笔
     */
    protected void resetPaint() {
        shadowPaint.setColor(shadowColor);
        shadowPaint.setAntiAlias(true);
        //内部不绘制，外部绘制
        if (shadowWidth > 0) {
            shadowPaint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.OUTER));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shadowWidth > 0 && shadowPath != null) {
            //关闭硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            canvas.drawPath(shadowPath, shadowPaint);
        }
        super.onDraw(canvas);
    }
}
