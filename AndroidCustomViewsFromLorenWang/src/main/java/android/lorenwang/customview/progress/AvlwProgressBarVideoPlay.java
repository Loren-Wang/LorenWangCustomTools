package android.lorenwang.customview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.view.MotionEvent;

import androidx.annotation.FloatRange;

/**
 * 功能作用：基础进度条
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
class AvlwProgressBarVideoPlay extends AvlwProgressBarBase {
    /**
     * 背景图片
     */
    private Drawable progressBgDrawable;
    private Bitmap progressBgBitmap;

    /**
     * 进度条图片
     */
    private Drawable progressShowDrawable;

    /**
     * 已缓存部分进度条图片
     */
    private Drawable progressCacheDrawable;

    /**
     * 当前进度条图片
     */
    private Drawable progressCurrentDrawable;
    private Bitmap progressCurrentBitmap;

    /**
     * 进度条背景高度（大模式显示）
     */
    private float progressBigHeight = -1;

    /**
     * 进度条背景高度（小模式显示）
     */
    private float progressSmallHeight = -1;

    /**
     * 正在展示的进度条背景高度
     */
    private float progressShowHeight = 0;

    /**
     * 进度当前进度点高度
     */
    private int progressCurrentHeight = 0;

    /**
     * 进度当前进度点宽度
     */
    private int progressCurrentWidth = 0;

    /**
     * 是否显示大模式进度条
     */
    private boolean showBig = true;
    /**
     * 当前缓存进度
     */
    @FloatRange(from = 0F, to = 1F)
    private float progressCache = 0F;

    @Override
    public void init(Context context, AvlwProgressBar avlwProgressBar, TypedArray attributes) {
        super.init(context, avlwProgressBar, attributes);
        //当前缓存进度
        progressCache = attributes.getFloat(R.styleable.AvlwProgressBar_avlwProgressCache,
                progressCache);
        if (progressCache > 1) {
            progressCache = 1;
        } else if (progress < 0) {
            progressCache = 0;
        }
        //背景图片
        progressBgDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressBgImage);
        //进度条图片
        progressShowDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressImage);
        //已缓存部分进度条图片
        progressCacheDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressCacheImage);
        //当前进度条图片
        progressCurrentDrawable =
                attributes.getDrawable(R.styleable.AvlwProgressBar_avlwProgressCurrentImage);

        //进度条背景高度（大模式显示）
        progressBigHeight =
                attributes.getDimension(R.styleable.AvlwProgressBar_avlwProgressBigHeight,
                        progressBigHeight);

        //进度条背景高度（小模式显示）
        progressSmallHeight =
                attributes.getDimension(R.styleable.AvlwProgressBar_avlwProgressSmallHeight,
                        progressSmallHeight);

        //进度当前进度点高度
        progressCurrentHeight =
                attributes.getDimensionPixelOffset(R.styleable.AvlwProgressBar_avlwProgressCurrentHeight,
                        progressCurrentHeight);

        //进度当前进度点宽度
        progressCurrentWidth =
                attributes.getDimensionPixelOffset(R.styleable.AvlwProgressBar_avlwProgressCurrentWidth,
                        progressCurrentWidth);
        //设置是否显示大模式
        setShowBig(showBig);
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
        return (int) (avlwProgressBar.getPaddingTop() + avlwProgressBar.getPaddingBottom() +
                (showBig ? Math.max(progressBigHeight, progressCurrentHeight) :
                        Math.max(progressSmallHeight, progressCurrentHeight)));
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
        //绘制的视图
        Bitmap bitmap = null;
        //显示区域
        Rect showRect = new Rect((int) left, (int) top, (int) right, (int) bottom);
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
                    AtlwImageCommonUtils.getInstance().drawableToBitmap(progressBgDrawable,
                            showProgressWidth, showProgressHeight);
            //用不到了，开始释放
            progressBgDrawable = null;
        }
        if (progressBgBitmap != null && !progressBgBitmap.isRecycled()) {
            canvas.drawBitmap(progressBgBitmap,
                    new Rect(0, 0, progressBgBitmap.getWidth(), progressBgBitmap.getHeight()),
                    showRect, null);
        }

        //绘制缓存进度
        if (progressCacheDrawable != null && showProgressWidth > 0 && showProgressHeight > 0) {
            //缓存进度百分比为0-1
            showRect.right = (int) (left + showProgressWidth * progressCache);
            //释放旧的位图
            AtlwImageCommonUtils.getInstance().releaseBitmap(bitmap);
            //获取当前显示位图
            bitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(progressCacheDrawable,
                    showRect.width(), showRect.height());
            //绘制当前位图
            if (bitmap != null && !bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                        showRect, null);
            }
        }

        //绘制进度
        if (progressShowDrawable != null && showProgressWidth > 0 && showProgressHeight > 0) {
            //缓存进度百分比为0-1
            showRect.right = (int) (left + showProgressWidth * progress);
            //释放旧的位图
            AtlwImageCommonUtils.getInstance().releaseBitmap(bitmap);
            //获取当前显示位图
            bitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(progressShowDrawable,
                    showRect.width(), showRect.height());
            //绘制当前位图
            if (bitmap != null && !bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                        showRect, null);
            }
        }

        //绘制当前进度图
        if (showBig) {
            if (progressCurrentDrawable != null && progressCurrentWidth > 0 && progressCurrentHeight > 0) {
                progressCurrentBitmap =
                        AtlwImageCommonUtils.getInstance().drawableToBitmap(progressCurrentDrawable,
                                progressCurrentWidth, progressCurrentHeight);
                //用不到了，开始释放
                progressCurrentDrawable = null;
            }
            if (progressCurrentBitmap != null && !progressCurrentBitmap.isRecycled()) {
                int drawLeft = (int) (left + showProgressWidth * progress - progressCurrentWidth);
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
    }

    @Override
    Boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && avlwProgressBar != null) {
            //手指抬起，判断位置，设置进度
            int progressWidth =
                    avlwProgressBar.getWidth() - avlwProgressBar.getPaddingStart() - avlwProgressBar.getPaddingEnd();
            setProgress(event.getX() / progressWidth, true);
        }
        return true;
    }

    /**
     * 设置是否显示大模式
     *
     * @param showBig 大模式
     */
    public void setShowBig(boolean showBig) {
        this.showBig = showBig;
        if (showBig) {
            progressShowHeight = progressBigHeight;
        } else {
            progressShowHeight = progressSmallHeight;
        }
    }

    /**
     * 设置缓存进度
     *
     * @param progressCache 缓存进度
     */
    public void setProgressCache(@FloatRange(from = 0, to = 1) float progressCache) {
        this.progressCache = progressCache;
    }

    /**
     * 获取缓存进度
     *
     * @return 缓存进度
     */
    public float getProgressCache() {
        return progressCache;
    }

}
