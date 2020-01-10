package android.lorenwang.common_base_frame.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.luck.picture.lib.engine.ImageEngine;

import org.jetbrains.annotations.NotNull;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * 功能作用：glide图片加载引擎
 * 创建时间：2019-12-10 16:14
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class GlideEngine implements ImageEngine {
    private static GlideEngine optionsInstance;

    private GlideEngine() {
    }

    public static GlideEngine getInstance() {
        if (optionsInstance == null) {
            synchronized (GlideEngine.class) {
                if (optionsInstance == null) {
                    optionsInstance = new GlideEngine();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    public void loadGridImage(@NotNull Context context, @NotNull String url, @NotNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    public void loadImage(@NotNull Context context, @NotNull String url, @NotNull ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    /**
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    public void loadAsGifImage(@NotNull final Context context, @NotNull String url, @NotNull final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(url)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    public void loadFolderImage(@NotNull final Context context, @NotNull String url, @NotNull final ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
