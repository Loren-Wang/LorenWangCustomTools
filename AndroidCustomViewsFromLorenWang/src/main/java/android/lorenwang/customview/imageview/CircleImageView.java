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
public class CircleImageView extends AppCompatImageView implements CustomViewCommon {
    //图片通用处理
    private ImageViewCommonDeal imageViewCommonDeal;
    public CircleImageView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        imageViewCommonDeal = new ImageViewCommonDeal();
        imageViewCommonDeal.init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            //里屏缓存当前画布
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(0f, 0f, getWidth(), getHeight(), null);
            }else{
                canvas.saveLayer(new RectF(0f, 0f, getWidth(),getHeight()), null,ALL_SAVE_FLAG);
            }
            super.onDraw(canvas);
            //绘制圆形图片
            imageViewCommonDeal.drawCircle(canvas, getWidth() / 2, getHeight() / 2
                    , Math.min(getWidth(), getHeight()) / 2);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public void release() {
        imageViewCommonDeal.release();
        imageViewCommonDeal = null;
    }
}
