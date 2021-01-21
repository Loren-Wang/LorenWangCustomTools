package android.lorenwang.customview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.view.MotionEvent;

/**
 * 功能作用：滑动进度条
 * 创建时间：2020-09-29 4:11 下午
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
class AvlwProgressBarScroll extends AvlwProgressBarBase {
    /**
     * 背景图片
     */
    private Drawable progressBgDrawable;
    private Bitmap progressBgBitmap;

    /**
     * 当前进度条图片
     */
    private Drawable progressCurrentDrawable;
    private Bitmap progressCurrentBitmap;

    /**
     * 正在展示的进度条背景高度
     */
    private int progressShowHeight = 0;

    /**
     * 进度当前进度点高度
     */
    private int progressCurrentHeight = 0;

    /**
     * 进度当前进度点宽度
     */
    private int progressCurrentWidth = 0;


    @Override
    public void init(Context context, AvlwProgressBar avlwProgressBar, TypedArray attributes) {
        super.init(context, avlwProgressBar, attributes);
        //背景图片
        progressBgDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressBgImage);
        //当前进度条图片
        progressCurrentDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressCurrentImage);

        //进度条高度
        progressShowHeight =
                attributes.getDimensionPixelOffset(R.styleable.AvlwProgressBar_avlwProgressShowHeight,
                        progressShowHeight);

        //进度当前进度点高度
        progressCurrentHeight =
                attributes.getDimensionPixelOffset(R.styleable.AvlwProgressBar_avlwProgressCurrentHeight,
                        progressCurrentHeight);

        //进度当前进度点宽度
        progressCurrentWidth =
                attributes.getDimensionPixelOffset(R.styleable.AvlwProgressBar_avlwProgressCurrentWidth,
                        progressCurrentWidth);
    }

    /**
     * 获取布局绘制宽度
     *
     * @param widthMeasureSpec 原始宽度
     * @return 绘制宽度
     */
    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        return widthMeasureSpec;
    }

    /**
     * 获取布局绘制高度
     *
     * @param heightMeasureSpec 原始高度
     * @return 绘制高度
     */
    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        return avlwProgressBar.getPaddingTop() + avlwProgressBar.getPaddingBottom() + Math.max(progressShowHeight, progressCurrentHeight);
    }

    /**
     * 指定区域绘制
     *
     * @param canvas 画板
     * @param left   区域左侧坐标
     * @param top    区域顶部坐标
     * @param right  区域右侧坐标
     * @param bottom 区域底部坐标
     */
    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        //显示区域
        Rect showRect = new Rect((int) left, (int) top, (int) right, (int) bottom);
        if (showRect.width() <= 0 || showRect.height() <= 0) {
            return;
        }
        //绘制的视图
        Bitmap bitmap = null;
        //进度条绘制顶部、底部坐标
        showRect.top = (int) ((showRect.height() - progressShowHeight) / 2 + top);
        showRect.bottom = (int) (showRect.top + progressShowHeight);
        //显示进度相关宽度
        int showProgressWidth = showRect.width();
        //显示进度相关高度
        int showProgressHeight = showRect.height();

        //绘制背景
        if (progressBgDrawable != null && showProgressWidth > 0 && showProgressHeight > 0) {
            progressBgBitmap =
                    AtlwImageCommonUtil.getInstance().drawableToBitmap(progressBgDrawable,
                            showProgressWidth, showProgressHeight);
            //用不到了，开始释放
            progressBgDrawable = null;
        }
        if (progressBgBitmap != null && !progressBgBitmap.isRecycled()) {
            canvas.drawBitmap(progressBgBitmap,
                    new Rect(0, 0, progressBgBitmap.getWidth(), progressBgBitmap.getHeight()),
                    showRect, null);
        }

        //绘制当前
        if (progressCurrentDrawable != null && progressCurrentWidth > 0 && progressCurrentHeight > 0) {
            progressCurrentBitmap =
                    AtlwImageCommonUtil.getInstance().drawableToBitmap(progressCurrentDrawable,
                            progressCurrentWidth, progressCurrentHeight);
            //用不到了，开始释放
            progressCurrentDrawable = null;
        }
        if (progressCurrentBitmap != null && !progressCurrentBitmap.isRecycled()) {
            int drawLeft = (int) (left + (showProgressWidth - progressCurrentWidth) * progress);
            if (drawLeft < 0) {
                drawLeft = 0;
            }
            canvas.drawBitmap(progressCurrentBitmap,
                    new Rect(0, 0, progressCurrentBitmap.getWidth(),
                            progressCurrentBitmap.getHeight()),
                    new Rect(drawLeft, (int) top, drawLeft + progressCurrentWidth,
                            (int) (top + progressCurrentHeight)), null);
        }
    }

    @Override
    Boolean onTouchEvent(MotionEvent event) {
        return null;
    }

}
