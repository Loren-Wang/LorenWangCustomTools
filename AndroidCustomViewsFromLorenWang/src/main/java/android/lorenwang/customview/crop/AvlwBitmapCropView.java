package android.lorenwang.customview.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.lorenwang.customview.imageview.AvlwZoomableImageView;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 功能作用：位图裁剪控件
 * 初始注释时间： 2021/4/25 17:28
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
public class AvlwBitmapCropView extends ConstraintLayout {
    /**
     * 背景图
     */
    private AvlwZoomableImageView bgImageView;

    public AvlwBitmapCropView(@NonNull Context context) {
        super(context);
        init();
    }

    public AvlwBitmapCropView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvlwBitmapCropView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //添加背景图
        bgImageView = new AvlwZoomableImageView(getContext());
        bgImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(bgImageView, 0);

    }

    /**
     * 设置背景图片位图
     *
     * @param bitmap 位图
     */
    public void setBgImageViewBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            //需要拼接一个当前控件宽高的透明背景图片
            bgImageView.setImageBitmap(
                    AtlwImageCommonUtil.getInstance().fillBgAspectRatio(bitmap, getWidth() * 1.0F / getHeight(), Color.TRANSPARENT));
        }
    }

    /**
     * 获取裁剪位图
     *
     * @return 裁剪后的位图
     */
    public Bitmap getCropBitmap(@FloatRange(from = 0, to = 1) float leftPercent, @FloatRange(from = 0, to = 1) float topPercent,
            @FloatRange(from = 0, to = 1) float rightPercent, @FloatRange(from = 0, to = 1) float bottomPercent, int cropRadius) {
        Bitmap currentBitmap = bgImageView.getCurrentBitmap();
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            Bitmap bitmap = AtlwImageCommonUtil.getInstance().cropBitmap(currentBitmap, (int) (leftPercent * 100), (int) (topPercent * 100),
                    (int) (rightPercent * 100), (int) (bottomPercent * 100));
            if (cropRadius > 0 && bitmap != null) {
                return AtlwImageCommonUtil.getInstance().getRoundedCornerBitmap(bitmap, cropRadius);
            } else {
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 获取中间区域位图
     *
     * @param centerWidthPercent  中间区域宽度百分比
     * @param centerHeightPercent 中间区域高度百分比
     * @param isCircle            是否是圆形
     * @return 区域位图
     */
    public Bitmap getCenterBitmap(@FloatRange(from = 0, to = 1) float centerWidthPercent, @FloatRange(from = 0, to = 1) float centerHeightPercent,
            boolean isCircle) {
        float hPercent = (1 - centerWidthPercent) / 2;
        float vPercent = (1 - centerHeightPercent) / 2;
        return getCropBitmap(hPercent, vPercent, hPercent, vPercent,
                isCircle ? (int) Math.max(getWidth() * centerWidthPercent, getHeight() * centerHeightPercent) : 0);
    }

    /**
     * 获取中心区域位图
     *
     * @param centerPercent 中心区域百分比，按照最小的处理
     * @param isCircle      是否是圆形
     * @return 区域位图
     */
    public Bitmap getCenterBitmap(@FloatRange(from = 0, to = 1) float centerPercent, boolean isCircle) {
        float userCenter = Math.min(getWidth() * centerPercent, getHeight() * centerPercent);
        float centerWidthPercent = userCenter / getWidth();
        float centerHeightPercent = userCenter / getHeight();
        return getCenterBitmap(centerWidthPercent, centerHeightPercent, isCircle);
    }
}
