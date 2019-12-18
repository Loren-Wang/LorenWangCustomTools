package android.lorenwang.graphic_code_scan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.androidgraphiccodescanfromlorenwang.R;

/**
 * 功能作用：扫描view视图,会绘制所有的阴影区域，同时提供方法给子view课可以自定义扫描框
 * 创建时间：2019-12-18 10:34
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：子view许重写onDrawChild(Canvas canvas, Rect cropRect)方法
 * 修改人：
 * 修改时间：
 * 备注：
 *     <declare-styleable name="AgcslwScanView">
 *         <!--阴影部分颜色-->
 *         <attr name="shadowColor" format="color" />
 *         <!--左侧阴影百分比，值范围0-1-->
 *         <attr name="shadowPercentLeft" format="float" />
 *         <!--右侧阴影百分比，值范围0-1-->
 *         <attr name="shadowPercentRight" format="float" />
 *         <!--上方阴影百分比，值范围0-1-->
 *         <attr name="shadowPercentTop" format="float" />
 *         <!--下方阴影百分比，值范围0-1-->
 *         <attr name="shadowPercentBottom" format="float" />
 *         <!--非阴影是否是正方形的，是的话则以-->
 *         <attr name="outShadowSquare" format="boolean" />
 *     </declare-styleable>
 */

public class AgcslwScanView extends FrameLayout {
    /**
     * 预览view
     */
    private SurfaceView surfaceView;

    /**************配置参数部分**************/
    /**
     * 阴影部分颜色，默认黑色
     */
    private int shadowColor = Color.BLACK;
    /**
     * 左侧阴影百分比
     */
    private float shadowPercentLeft = 0f;
    /**
     * 右侧阴影百分比
     */
    private float shadowPercentRight = 0f;
    /**
     * 顶部阴影百分比
     */
    private float shadowPercentTop = 0f;
    /**
     * 顶部阴影百分比
     */
    private float shadowPercentBottom = 0f;
    /**
     * 非阴影区域是否是正方形的
     */
    private boolean outShadowSquare = false;

    /***************绘制参数***********************/
    /**
     * 阴影画笔
     */
    private Paint shadowPaint;

    public AgcslwScanView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AgcslwScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AgcslwScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AgcslwScanView);
        shadowColor = attributes.getColor(R.styleable.AgcslwScanView_shadowColor, shadowColor);
        //取余1使其百分比保持在0-1之间
        shadowPercentLeft = attributes.getFloat(R.styleable.AgcslwScanView_shadowPercentLeft, shadowPercentLeft) % 1;
        shadowPercentRight = attributes.getFloat(R.styleable.AgcslwScanView_shadowPercentRight, shadowPercentRight) % 1;
        shadowPercentTop = attributes.getFloat(R.styleable.AgcslwScanView_shadowPercentTop, shadowPercentTop) % 1;
        shadowPercentBottom = attributes.getFloat(R.styleable.AgcslwScanView_shadowPercentBottom, shadowPercentBottom) % 1;
        outShadowSquare = attributes.getBoolean(R.styleable.AgcslwScanView_outShadowSquare, outShadowSquare);

        shadowPaint = new Paint();
        shadowPaint.setColor(shadowColor);
        shadowPaint.setAntiAlias(true);

        surfaceView = new SurfaceView(context, attrs, defStyleAttr);
        addView(surfaceView);
        addView(new View(context, attrs, defStyleAttr) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //如果非阴影区域是正方形，那么则取非阴影部分最小宽度向左、上适配
                AgcslwScanView.this.onDrawChild(canvas, AgcslwScanUtils.getInstance().parseShowCropRect(shadowPercentLeft, shadowPercentTop, shadowPercentRight, shadowPercentBottom, outShadowSquare, surfaceView))
                ;
            }
        });
    }

    /**
     * 获取视图预览
     *
     * @return 视图预览
     */
    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    /**
     * 绘制子视图
     *
     * @param canvas 画板
     */
    protected void onDrawChild(Canvas canvas, Rect cropRect) {
        if (cropRect != null) {
            //绘制阴影区域
            //顶部
            canvas.drawRect(0, 0, getWidth(), cropRect.top, shadowPaint);
            //左侧
            canvas.drawRect(0, cropRect.top, cropRect.left, getHeight(), shadowPaint);
            //右侧
            canvas.drawRect(cropRect.right, cropRect.top, getWidth(), getHeight(), shadowPaint);
            //底部
            canvas.drawRect(cropRect.left, cropRect.bottom, cropRect.right, getHeight(), shadowPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawChild(canvas, AgcslwScanUtils.getInstance().parseShowCropRect(shadowPercentLeft, shadowPercentTop, shadowPercentRight, shadowPercentBottom, outShadowSquare, surfaceView));
    }
}
