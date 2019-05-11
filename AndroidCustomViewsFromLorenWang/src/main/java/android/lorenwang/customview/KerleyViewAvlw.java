package android.lorenwang.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建时间：2019-03-20 下午 16:00:41
 * 创建人：王亮（Loren wang）
 * 功能作用：间隔线视图
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class KerleyViewAvlw extends View implements AvlwCustomViewCommon {
    private float spaceLeft;
    private float spaceRight;
    private float spaceTop;
    private float spaceBottom;
    private int orientation;
    private Paint paint;

    public KerleyViewAvlw(Context context) {
        super(context);
        init(context, null, -1);
    }

    public KerleyViewAvlw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public KerleyViewAvlw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.KerleyViewAvlw, defStyleAttr, 0);
        this.spaceLeft = attr.getDimension(R.styleable.KerleyViewAvlw_kv_spaceLeft, this.spaceLeft);
        this.spaceRight = attr.getDimension(R.styleable.KerleyViewAvlw_kv_spaceRight, this.spaceRight);
        this.spaceTop = attr.getDimension(R.styleable.KerleyViewAvlw_kv_spaceTop, this.spaceTop);
        this.spaceBottom = attr.getDimension(R.styleable.KerleyViewAvlw_kv_spaceBottom, this.spaceBottom);
        this.orientation = attr.getInt(R.styleable.KerleyViewAvlw_kv_orientation, this.orientation);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(attr.getDimension(R.styleable.KerleyViewAvlw_kv_kerleyHeight, 2.0F));
        this.paint.setColor(attr.getColor(R.styleable.KerleyViewAvlw_kv_kerleyColor, -16777216));
        attr.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(orientation == 0) {
            canvas.drawLine(spaceLeft, paint.getStrokeWidth() / 2 + spaceTop, getWidth() - spaceRight, paint.getStrokeWidth() / 2 - spaceBottom, paint);
        }else if(orientation == 1){
            canvas.drawLine(getWidth() / 2, spaceTop, getWidth() / 2, getHeight() - spaceBottom, paint);
        }
    }

    @Override
    public void release() {

    }
}
