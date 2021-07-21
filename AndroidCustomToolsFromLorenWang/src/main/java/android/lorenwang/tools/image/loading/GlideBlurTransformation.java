package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

class GlideBlurTransformation extends BitmapTransformation {
    private final RenderScript rs = RenderScript.create(AtlwConfig.nowApplication);
    /**
     * 高斯模糊半径
     */
    private Integer blurRadius;

    public GlideBlurTransformation(Integer blurRadius) {
        this.blurRadius = blurRadius;
    }

    @Override
    public void updateDiskCacheKey(@NonNull @NotNull MessageDigest messageDigest) {

    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);
        // Set the blur radius
        script.setRadius(blurRadius);
        // Start the ScriptIntrinisicBlur
        script.forEach(output);
        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);
        toTransform.recycle();
        return blurredBitmap;
    }
}
