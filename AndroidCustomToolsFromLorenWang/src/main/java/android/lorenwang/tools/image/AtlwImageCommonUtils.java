package android.lorenwang.tools.image;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.base.CheckUtils;
import android.lorenwang.tools.base.LogUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;


/**
 * 创建时间：2018-11-16 上午 10:15:2
 * 创建人：王亮（Loren wang）
 * 功能作用：图片处理通用类
 * 思路：
 * 方法：
 * 1、将图片文件转换为base64字符串
 * 2、对Drawable着色
 * 3、设置背景图片着色
 * 4、设置图片控件的src资源的着色
 * 5、设置文本控件的Drawable左上右下图片着色
 * 6、图片drawable转bitmap
 * 7、位图压缩、
 * 8、十进制颜色值转16进制
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AtlwImageCommonUtils {
    private final String TAG = getClass().getName();
    private static AtlwImageCommonUtils atlwImageCommonUtils;

    public static AtlwImageCommonUtils getInstance() {
        if (atlwImageCommonUtils == null) {
            atlwImageCommonUtils = new AtlwImageCommonUtils();
        }
        return atlwImageCommonUtils;
    }


    /**
     * 将图片文件转换为base64字符串
     *
     * @param filePath
     * @return
     */
    public String imageFileToBase64String(Context context, String filePath) {
        if (CheckUtils.getInstance().checkFileOptionsPermisstion(context)
                && CheckUtils.getInstance().checkFileIsExit(filePath)
                && CheckUtils.getInstance().checkFileIsImage(filePath)) {
            LogUtils.logI(TAG, "图片文件地址有效性检测成功，开始获取文件字节");
            byte[] bytes = AtlwFileOptionUtils.getInstance()
                    .readImageFileGetBytes(context, false, false, filePath);
            String base64Str = null;
            if (bytes != null) {
                base64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
                LogUtils.logI(TAG, "图片转换成功，转换后数据：" + base64Str);
            } else {
                LogUtils.logI(TAG, "图片转换失败,失败原因：文件读取异常");
            }
            return base64Str;
        } else {
            LogUtils.logI(TAG, "图片文件转换失败，失败原因可能是以下几点：1、未拥有文件权限；2、文件不存在；3、传输的地址非图片地址");
            return null;
        }
    }

    /**
     * 对Drawable着色
     *
     * @param drawable 要着色的Drawable
     * @param colors   着色的颜色
     * @return f返回着色后的Drawable
     */
    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 设置背景图片着色
     *
     * @param view           控件
     * @param colorStateList 颜色
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBackgroundTint(View view, ColorStateList colorStateList) {
        if (view != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackgroundTintList(colorStateList);
            } else {
                Drawable background = view.getBackground();
                if (background != null) {
                    background = tintDrawable(background, colorStateList);
                    view.setBackground(background);
                }
            }
        }
    }

    /**
     * 设置图片控件的src资源的着色
     *
     * @param imageView      图片控件
     * @param colorStateList 颜色
     */
    public void setImageSrcTint(ImageView imageView, ColorStateList colorStateList) {
        if (imageView != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setImageTintList(colorStateList);
            } else {
                Drawable background = imageView.getDrawable();
                if (background != null) {
                    background = tintDrawable(background, colorStateList);
                    imageView.setImageDrawable(background);
                }
            }
        }
    }

    /**
     * 设置文本控件的Drawable左上右下图片着色
     *
     * @param textView       控件
     * @param colorStateList 颜色
     */
    public void setTextViewDrawableLRTBTint(TextView textView, ColorStateList colorStateList) {
        if (textView != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setCompoundDrawableTintList(colorStateList);
            } else {
                //获取图片进行着色
                Drawable[] compoundDrawables = textView.getCompoundDrawables();
                for (int i = 0; i < compoundDrawables.length; i++) {
                    if (compoundDrawables[i] != null)
                        compoundDrawables[i] = tintDrawable(compoundDrawables[i], colorStateList);
                }
                //设置Drawable
                textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
            }
        }
    }

    /**
     * 图片drawable转bitmap
     *
     * @param drawable 要转换的drawable
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable != null) {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 图片drawable转bitmap
     *
     * @param drawable 要转换的drawable
     */
    public Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable != null) {

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 获取圆角bitmap
     *
     * @param drawable 图片，需要转换bitmap
     * @param width    宽度
     * @param height   高度
     * @param radius   圆角角度
     * @return 圆角bitmap
     */
    public Bitmap drawableToBitmap(Drawable drawable, int width, int height, int radius) {
        if (drawable != null) {
            // 获取位图
            Bitmap bitmap = drawableToBitmap(drawable, width, height);
            return getRoundedCornerBitmap(bitmap, radius);
        } else {
            return null;
        }
    }

    /**
     * 获取圆角bitmap
     *
     * @param bitmap 位图
     * @param radius 圆角角度
     * @return 圆角bitmap
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {

        //获取输出的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //画板背景透明
        canvas.drawARGB(0, 0, 0, 0);

        //初始化绘制范围
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        //初始化画笔
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        //绘制圆角
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制位图
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 位图压缩
     *
     * @param bitmap 要压缩的位图
     * @param size   大小，单位，字节
     */
    public Bitmap bitmapCompress(Bitmap bitmap, Bitmap.CompressFormat format, int size) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int quality = 100;
            do {
                outputStream.reset();
                bitmap.compress(format, quality, outputStream);
                quality -= 10;
                if (quality < 0) {
                    break;
                }
            } while (outputStream.size() > size);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
        } else {
            return null;
        }
    }

    /**
     * 十进制颜色值转16进制
     * @param color 十进制颜色值
     * @return 16进制颜色值
     */
    public String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }
}
