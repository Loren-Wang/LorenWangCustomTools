package android.lorenwang.common_base_frame.image

import android.content.Context
import android.graphics.Bitmap
import android.lorenwang.common_base_frame.R
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.luck.picture.lib.engine.ImageEngine


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
class AcbflwGlideEngine : ImageEngine {

    companion object {
        private var instance: AcbflwGlideEngine? = null

        fun createGlideEngine(): AcbflwGlideEngine {
            if (null == instance) {
                synchronized(AcbflwGlideEngine::class.java) {
                    if (null == instance) {
                        instance = AcbflwGlideEngine()
                    }
                }
            }
            return instance!!
        }
    }


    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.picture_image_placeholder)
                .into(imageView)
    }


    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
                .asBitmap()
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.picture_icon_placeholder)
                .load(url)
                .into(object : BitmapImageViewTarget(imageView) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource)
                        circularBitmapDrawable.cornerRadius = 8f
                        imageView.setImageDrawable(circularBitmapDrawable)
                    }
                })
    }


    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }
}
