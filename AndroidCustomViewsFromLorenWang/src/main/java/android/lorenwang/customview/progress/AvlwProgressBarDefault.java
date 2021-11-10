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
 * 功能作用：默认进度条
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
class AvlwProgressBarDefault extends AvlwProgressBarBase {
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
     * 进度条背景高度
     */
    private float progressBgHeight = -1;

    /**
     * 进度条高度（居中背景）
     */
    private float progressHeight = -1;

    @Override
    public void init(Context context, AvlwProgressBar avlwProgressBar, TypedArray attributes) {
        super.init(context, avlwProgressBar, attributes);
        //背景图片
        progressBgDrawable = attributes.getDrawable(R.styleable.AvlwProgressBar_avlw_pb_progressBgImage);
        //进度条图片
        progressShowDrawable = attributes.getDrawable(R.styleable.AvlwProgressBar_avlw_pb_progressImage);
        //进度条背景高度
        progressBgHeight = attributes.getDimension(R.styleable.AvlwProgressBar_avlw_pb_progressBgHeight, progressBgHeight);
        //进度条高度（居中背景）
        progressHeight = attributes.getDimension(R.styleable.AvlwProgressBar_avlw_pb_progressHeight, progressHeight);
        if (progressHeight > progressBgHeight) {
            progressHeight = progressBgHeight;
        }
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
        return (int) (avlwProgressBar.getPaddingTop() + avlwProgressBar.getPaddingBottom() + progressBgHeight);
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
        showRect.top = (int) ((showRect.height() - progressBgHeight) / 2 + top);
        showRect.bottom = (int) (showRect.top + progressBgHeight);
        //显示进度相关宽度
        int showProgressWidth = showRect.width();
        //显示进度相关高度
        int showProgressHeight = showRect.height();

        //绘制背景
        if (progressBgDrawable != null && showProgressWidth > 0 && showProgressHeight > 0) {
            progressBgBitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(progressBgDrawable, showProgressWidth, showProgressHeight);
            //用不到了，开始释放
            progressBgDrawable = null;
        }
        if (progressBgBitmap != null && !progressBgBitmap.isRecycled()) {
            canvas.drawBitmap(progressBgBitmap, new Rect(0, 0, progressBgBitmap.getWidth(), progressBgBitmap.getHeight()), showRect, null);
        }
        //绘制进度
        if (progressShowDrawable != null && showProgressWidth > 0 && showProgressHeight > 0) {
            //缓存进度百分比为0-1
            showRect.right = (int) (left + showProgressWidth * progress);
            if (showRect.width() > 0 && showRect.height() > 0) {
                //获取当前显示位图
                bitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(progressShowDrawable, showRect.width(), showRect.height());
                //绘制当前位图
                if (bitmap != null && !bitmap.isRecycled()) {
                    canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), showRect, null);
                }
            }
        }
    }

    @Override
    Boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && avlwProgressBar != null) {
            //手指抬起，判断位置，设置进度
            int progressWidth = avlwProgressBar.getWidth() - avlwProgressBar.getPaddingStart() - avlwProgressBar.getPaddingEnd();
            setProgress(event.getX() / progressWidth, true);
        }
        return true;
    }

}
