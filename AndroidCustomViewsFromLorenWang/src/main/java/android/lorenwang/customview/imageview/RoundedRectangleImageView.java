package android.lorenwang.customview.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.lorenwang.customview.CustomViewCommon;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

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


public class RoundedRectangleImageView extends AppCompatImageView implements CustomViewCommon {
    /**
     * 图片通用处理
     */
    private ImageViewCommonDeal imageViewCommonDeal;

    /**
     * 图片占的矩形区域
     */
    private final RectF srcRectF = new RectF();

    public RoundedRectangleImageView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public RoundedRectangleImageView(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public RoundedRectangleImageView(Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 控件初始化
     */
    private final void init(Context context, AttributeSet attrs, int defStyleAttr) {
        imageViewCommonDeal = new ImageViewCommonDeal();
        imageViewCommonDeal.init(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            //里屏缓存当前画布
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(0f, 0f, getWidth(), getHeight(), null);
            } else {
                canvas.saveLayer(new RectF(0f, 0f, getWidth(), getHeight()), null, ALL_SAVE_FLAG);
            }
            super.onDraw(canvas);
            //绘制圆形图片
            imageViewCommonDeal.drawRoundedRectangle(canvas, getWidth(), getHeight());
        } else {
            super.onDraw(canvas);
        }
    }


    /**
     * 释放内存
     */
    @Override
    public void release() {
        if (imageViewCommonDeal != null)
            imageViewCommonDeal.release();
        imageViewCommonDeal = null;
    }

}
