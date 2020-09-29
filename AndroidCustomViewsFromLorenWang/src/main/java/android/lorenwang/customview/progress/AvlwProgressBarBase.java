package android.lorenwang.customview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.lorenwang.customview.R;
import android.lorenwang.customview.texiview.priceShow.AvlwPriceShowTextView;

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
abstract class AvlwProgressBarBase implements AvlwProgressBarOptions {
    protected final String TAG = getClass().getName();
    protected Context context;
    protected TypedArray attributes;
    /**
     * 控件
     */
    protected AvlwProgressBar avlwProgressBar;
    /**
     * 当前进度
     */
    @FloatRange(from = 0F, to = 1F)
    protected float progress = 0F;

    /**
     * 初始化
     *
     * @param context         上下文
     * @param avlwProgressBar 文本显示控件
     * @param attributes      配置参数
     */
    public void init(Context context, AvlwProgressBar avlwProgressBar, TypedArray attributes) {
        this.context = context;
        this.avlwProgressBar = avlwProgressBar;
        this.attributes = attributes;
        //当前进度
        progress = attributes.getFloat(R.styleable.AvlwProgressBar_avlwProgress, progress);
        if (progress > 1) {
            progress = 1;
        } else if (progress < 0) {
            progress = 0;
        }
    }

    /**
     * 获取布局绘制宽度
     *
     * @param widthMeasureSpec 原始宽度
     * @return 绘制宽度
     */
    public abstract int getMeasureWidth(int widthMeasureSpec);

    /**
     * 获取布局绘制高度
     *
     * @param heightMeasureSpec 原始高度
     * @return 绘制高度
     */
    public abstract int getMeasureHeight(int heightMeasureSpec);

    /**
     * 指定区域绘制
     *
     * @param canvas 画板
     * @param left   区域左侧坐标
     * @param top    区域顶部坐标
     * @param right  区域右侧坐标
     * @param bottom 区域底部坐标
     */
    abstract void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom);

}
