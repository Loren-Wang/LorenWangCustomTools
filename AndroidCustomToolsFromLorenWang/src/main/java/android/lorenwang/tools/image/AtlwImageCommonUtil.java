package android.lorenwang.tools.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.lorenwang.tools.app.AtlwViewUtil;
import android.lorenwang.tools.base.AtlwCheckUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.file.AtlwFileOptionUtil;
import android.media.ExifInterface;
import android.util.Base64;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;


/**
 * 功能作用：图片处理通用类
 * 初始注释时间： 2021/1/19 6:27 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 将图片文件转换为base64字符串--imageFileToBase64String(filePath)
 * 将图片转换为base64字符串--imageToBase64String(bitmap)
 * 图片drawable转bitmap--drawableToBitmap(drawable,width,htight)
 * 获取drawable的宽度--getDrawableWidth(drawable)
 * 获取drawable的高度--getDrawableHeight(drawable)
 * 获取圆角bitmap--getRoundedCornerBitmap(bitmap,width,htight,radius)
 * 获取圆角bitmap--getRoundedCornerBitmap(bitmap,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius)
 * 获取圆角bitmap--getRoundedCornerBitmap(drawable/bitmap,width,htight,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius)
 * 获取圆形bitmap--getCircleBitmap(drawable/bitmap,width,height,radius)
 * 位图压缩，不压缩大小--bitmapCompress(bitmap,format,size)
 * 位图压缩--bitmapCompressToByte(bitmap,format,size)
 * 获取位图字节--getBitmapBytes(bitmap)
 * 十进制颜色值转16进制--toHexEncoding(color)
 * 图片的缩放方法--zoomImage(bgImage,newWidth,newHeight)
 * 读取照片exif信息中的旋转角度--readPictureDegree(path)
 * 旋转指定图片一定的角度--toTurnPicture(img,degree)
 * 裁剪位图--cropBitmap(bitmap,leftPercentForBitmapWidth,topPercentForBitmapHeight,rightPercentForBitmapWidth,bottomPercentForBitmapHeight)
 * 从中心裁剪图片到指定的宽高--cropBitmapForCenter(bitmap,cropPercentWidthHeight)
 * 使背景透明--makeBgTransparent(bitmap)
 * 合并位图--mergeBitmap(bitmapBg,bitmapTop,topShowWidth,topShowHeight,leftBgPercent,topBgPercent,Float bottomBgPercent)
 * 图片设置背景--setBitmapBg(bitmap,bgColor,bgContentPadding)
 * 获取两个位图重叠部分位图--getOverlapBitmap(bottomBitmap,topBitmap,showWidth,showHeight)
 * 释放bitmap--releaseBitmap(bitmap)
 * 添加水印到位图中--addWatermarkBitmap(bitmap,textSize,textColor,text,width,height,rotationAngle)
 * 生成水印--generateWatermarkBitmap(textSize,textColor,text,width,height,rotationAngle)
 * 在画布上绘制水印--addWatermarkBitmap(canvas,textSize,textColor,text,width,height,rotationAngle)
 * 在画布上绘制水印--addWatermarkBitmap(canvas,paint,text,width,height,rotationAngle)
 * 图片格式转换--coverImage(path,savePath,format)
 * 根据比例填充背景图片--fillBgAspectRatio(bitmap, ratio, color)
 * <p>
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwImageCommonUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwImageCommonUtil optionsInstance;

    private AtlwImageCommonUtil() {
    }

    public static AtlwImageCommonUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwImageCommonUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwImageCommonUtil();
                }
            }
        }
        return optionsInstance;
    }


    /**
     * 将图片文件转换为base64字符串
     *
     * @param filePath 图片本地地址
     * @return 转换后的字符串
     */
    public String imageFileToBase64String(String filePath) {
        if (AtlwCheckUtil.getInstance().checkFileIsExit(filePath) && AtlwCheckUtil.getInstance().checkFileIsImage(filePath)) {
            AtlwLogUtil.logUtils.logI(TAG, "图片文件地址有效性检测成功，开始获取文件字节");
            byte[] bytes = AtlwFileOptionUtil.getInstance().readImageFileGetBytes(false, false, filePath);
            String base64Str = null;
            if (bytes != null) {
                base64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
                AtlwLogUtil.logUtils.logI(TAG, "图片转换成功，转换后数据：" + base64Str);
            } else {
                AtlwLogUtil.logUtils.logI(TAG, "图片转换失败,失败原因：文件读取异常");
            }
            return base64Str;
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "图片文件转换失败，失败原因可能是以下几点：1、未拥有文件权限；2、文件不存在；3、传输的地址非图片地址");
            return null;
        }
    }

    /**
     * 将图片转换为base64字符串
     *
     * @param bitmap 图片
     * @return 转换后的字符串
     */
    public String imageToBase64String(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            AtlwLogUtil.logUtils.logI(TAG, "图片有效，开始获取文件字节");
            byte[] bytes = getBitmapBytes(bitmap);
            String base64Str = null;
            if (bytes != null) {
                base64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
                AtlwLogUtil.logUtils.logI(TAG, "图片转换成功，转换后数据：" + base64Str);
            } else {
                AtlwLogUtil.logUtils.logI(TAG, "图片转换失败,失败原因：文件读取异常");
            }
            return base64Str;
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "图片文件转换失败，失败原因可能是以下几点：1、未拥有文件权限；2、文件不存在；3、传输的地址非图片地址");
            return null;
        }
    }

    /**
     * 图片drawable转bitmap
     *
     * @param drawable 要转换的drawable
     * @return 转换后的位图
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable != null && drawable.getConstantState() != null) {
            //复制，防止影响原有视图
            drawable = drawable.getConstantState().newDrawable();

            // 取 drawable 的长宽
            int width = getDrawableWidth(drawable);
            int height = getDrawableHeight(drawable);
            if (width <= 0) {
                width = AtlwScreenUtil.getInstance().getScreenWidth();
            }
            if (height <= 0) {
                height = AtlwScreenUtil.getInstance().getScreenHeight();
            }

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
     * 图片drawable转bitmap
     *
     * @param drawable 要转换的drawable
     * @param width    宽度
     * @param height   高度
     * @return 转换后的位图
     */
    public Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable != null && drawable.getConstantState() != null) {
            //复制，防止影响原有视图
            drawable = drawable.getConstantState().newDrawable();

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);

            //做宽高转换
            float showProportion = width * 1.0f / height;
            float drawableProportion = getDrawableWidth(drawable) * 1.0f / (getDrawableHeight(drawable) == 0 ? 1 : getDrawableHeight(drawable));
            int left = 0;
            int top = 0;
            int right = width;
            int bottom = height;
            if (drawableProportion > 0) {
                if (drawableProportion > showProportion) {
                    left = -(int) ((drawableProportion - showProportion) / 2 * width);
                    right = (int) (width * drawableProportion + left);
                } else {
                    top = -(int) ((showProportion - drawableProportion) / 2 * height);
                    bottom = height + left;
                }
            }
            if (left > 0) {
                left = 0;
            }
            if (top > 0) {
                top = 0;
            }
            drawable.setBounds(left, top, right, bottom);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 获取drawable的宽度
     *
     * @param drawable 图片
     * @return 宽度
     */
    public int getDrawableWidth(Drawable drawable) {
        if (drawable == null) {
            return -1;
        } else {
            return drawable.getIntrinsicWidth() > 0 ? drawable.getIntrinsicWidth() : drawable.getBounds().width();
        }
    }

    /**
     * 获取drawable的高度
     *
     * @param drawable 图片
     * @return 宽度
     */
    public int getDrawableHeight(Drawable drawable) {
        if (drawable == null) {
            return -1;
        } else {
            return drawable.getIntrinsicHeight() > 0 ? drawable.getIntrinsicHeight() : drawable.getBounds().height();
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
        return getRoundedCornerBitmap(bitmap, radius, radius, radius, radius);
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
    public Bitmap getRoundedCornerBitmap(Drawable drawable, int width, int height, int radius) {
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
     * @param bitmap            位图
     * @param leftTopRadius     圆角角度
     * @param rightTopRadius    圆角角度
     * @param rightBottomRadius 圆角角度
     * @param leftBottomRadius  圆角角度
     * @return 圆角bitmap
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) {

        //获取输出的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //画板背景透明
        canvas.drawARGB(0, 0, 0, 0);

        //初始化绘制范围
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        //初始化画笔
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        //绘制圆角
        Path path = new Path();
        path.addRoundRect(new RectF(rect),
                new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius,
                        leftBottomRadius}, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制位图
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 获取圆角bitmap
     *
     * @param drawable          图片，需要转换bitmap
     * @param width             宽度
     * @param height            高度
     * @param leftTopRadius     圆角角度
     * @param rightTopRadius    圆角角度
     * @param rightBottomRadius 圆角角度
     * @param leftBottomRadius  圆角角度
     * @return 圆角bitmap
     */
    public Bitmap getRoundedCornerBitmap(Drawable drawable, int width, int height, float leftTopRadius, float rightTopRadius, float rightBottomRadius,
            float leftBottomRadius) {
        if (drawable != null) {
            // 获取位图
            Bitmap bitmap = drawableToBitmap(drawable, width, height);
            return getRoundedCornerBitmap(bitmap, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
        } else {
            return null;
        }
    }

    /**
     * 获取圆形bitmap
     *
     * @param drawable 图片，需要转换bitmap
     * @param width    宽度
     * @param height   高度
     * @param radius   圆角角度
     * @return 圆角bitmap
     */
    public Bitmap getCircleBitmap(Drawable drawable, int width, int height, int radius) {
        if (drawable != null) {
            // 获取位图
            Bitmap bitmap = drawableToBitmap(drawable, width, height);
            return getCircleBitmap(bitmap, radius);
        } else {
            return null;
        }
    }

    /**
     * 获取圆形bitmap
     *
     * @param bitmap 位图
     * @return 圆形bitmap
     */
    public Bitmap getCircleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return getCircleBitmap(bitmap, Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2);

    }

    /**
     * 获取圆形bitmap
     *
     * @param bitmap 位图
     * @param radius 圆角角度
     * @return 圆形bitmap
     */
    public Bitmap getCircleBitmap(Bitmap bitmap, int radius) {
        if (bitmap == null) {
            return null;
        }
        //获取输出的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //画板背景透明
        canvas.drawARGB(0, 0, 0, 0);

        //初始化绘制范围
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        //初始化画笔
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        //绘制圆角
        canvas.drawCircle(bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制位图
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 位图压缩，不压缩大小
     * 它是在保持像素的前提下改变图片的位深及透明度，来达到压缩图片的目的，图片的长，宽，像素都不会改变，那么bitmap所占内存大小是不会变的。
     *
     * @param bitmap 要压缩的位图
     * @param format 压缩格式
     * @param size   大小，单位，字节
     * @return 压缩后返回的位图
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
     * 位图压缩
     *
     * @param bitmap 要压缩的位图
     * @param format 压缩格式
     * @param size   大小，单位，字节
     * @return 压缩后返回的字节
     */
    public byte[] bitmapCompressToByte(Bitmap bitmap, Bitmap.CompressFormat format, int size) {
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
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

    /**
     * 获取位图字节
     *
     * @param bitmap 位图
     * @return 转换成功返回字节，否则返回null
     */
    public byte[] getBitmapBytes(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            int bytes = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

            return buffer.array();//Get the bytes array of the bitmap
        } else {
            return null;
        }
    }

    /**
     * 十进制颜色值转16进制
     *
     * @param color 十进制颜色值
     * @return 16进制颜色值
     */
    public String toHexEncoding(int color) {
        String R, G, B;
        StringBuilder sb = new StringBuilder();
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

    /**
     * 图片的缩放方法
     *
     * @param bgImage   源图片资源
     * @param newWidth  缩放后宽度
     * @param newHeight 缩放后高度
     * @return 压缩后的位图
     */
    public Bitmap zoomImage(Bitmap bgImage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgImage.getWidth();
        float height = bgImage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap((int) newWidth, (int) newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bgImage, matrix, null);
        return bitmap;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转指定图片一定的角度
     *
     * @param img    图片位图
     * @param degree 要旋转的角度
     * @return 旋转后的位图
     */
    public Bitmap toTurnPicture(Bitmap img, int degree) {
        AtlwLogUtil.logUtils.logD(TAG, "toTurnPicture degree" + degree);
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree); /*翻转90度*/
            int width = img.getWidth();
            int height = img.getHeight();
            img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        }
        return img;
    }

    /**
     * 裁剪位图
     *
     * @param bitmap                       原图
     * @param leftPercentForBitmapWidth    左侧相当于位图宽度百分比
     * @param topPercentForBitmapHeight    顶部相当于位图高度百分比
     * @param rightPercentForBitmapWidth   右侧相当于位图宽度百分比
     * @param bottomPercentForBitmapHeight 底部相当于位图高度百分比
     * @return 裁剪后的图像
     */
    public Bitmap cropBitmap(Bitmap bitmap, int leftPercentForBitmapWidth, int topPercentForBitmapHeight, int rightPercentForBitmapWidth,
            int bottomPercentForBitmapHeight) {
        //先进行约束
        if (leftPercentForBitmapWidth < 0) {
            leftPercentForBitmapWidth = 0;
        }
        if (topPercentForBitmapHeight < 0) {
            topPercentForBitmapHeight = 0;
        }
        if (rightPercentForBitmapWidth < 0) {
            rightPercentForBitmapWidth = 0;
        }
        if (bottomPercentForBitmapHeight < 0) {
            bottomPercentForBitmapHeight = 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int x = (int) (width * leftPercentForBitmapWidth / 100.0);
        int y = (int) (height * topPercentForBitmapHeight / 100.0);
        int newWidth = (int) (width * (100 - leftPercentForBitmapWidth - rightPercentForBitmapWidth) / 100.0);
        int newHeight = (int) (height * (100 - topPercentForBitmapHeight - bottomPercentForBitmapHeight) / 100.0);
        if (newWidth > 0 && newHeight > 0) {
            Bitmap cropBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cropBitmap);
            canvas.drawBitmap(bitmap, new Rect(x, y, x + newWidth, y + newHeight), new RectF(0, 0, newWidth, newHeight), null);
            releaseBitmap(bitmap);
            return cropBitmap;
        } else {
            return null;
        }
    }

    /**
     * 从中心裁剪图片到指定的宽高
     *
     * @param bitmap                 位图
     * @param cropPercentWidthHeight 中心相当于宽高百分比
     * @return 裁剪后位图
     */
    public Bitmap cropBitmapForCenter(Bitmap bitmap, double cropPercentWidthHeight) {
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int x = 0;
            int y = 0;
            int cropWidth = w;
            int cropHeight = h;

            double bitmapPercentWidthHeight = w * 1.0 / h;
            if (bitmapPercentWidthHeight > cropPercentWidthHeight) {
                //代表着宽多了，需要把多余的裁掉
                cropWidth = (int) (h * cropPercentWidthHeight);
                x = (w - cropWidth) / 2;
            } else if (bitmapPercentWidthHeight < cropPercentWidthHeight) {
                //代表标注高度多了
                cropHeight = (int) (w * 1.0 / cropPercentWidthHeight);
                y = (h - cropHeight) / 2;
            }

            return Bitmap.createBitmap(bitmap, x, y, cropWidth, cropHeight, null, false);
        }
        return null;
    }

    /**
     * 使背景透明
     *
     * @param bitmap 要透明的图片位图
     * @return 透明后的图片位图
     */
    public Bitmap makeBgTransparent(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            int portraitWidth = bitmap.getWidth();
            int portraitHeight = bitmap.getHeight();
            int[] argbs = new int[portraitWidth * portraitHeight];
            bitmap.getPixels(argbs, 0, portraitWidth, 0, 0, portraitWidth, portraitHeight);//
            // 获得图片的ARGB值
            for (int i = 0; i < argbs.length; i++) {
                int a = Color.alpha(argbs[i]);
                int r = Color.red(argbs[i]);
                int g = Color.green(argbs[i]);
                int b = Color.blue(argbs[i]);
                if (r > 240 && g > 240 && b > 240) {
                    argbs[i] = 0x00FFFFFF;
                }
            }
            return Bitmap.createBitmap(argbs, 0, portraitWidth, portraitWidth, portraitHeight, bitmap.getConfig());
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(TAG, "是位图背景透明处理异常" + (e.getMessage() == null ? "" : e.getMessage()));
            return bitmap;
        }
    }

    /**
     * 合并位图
     *
     * @param bitmapBg        位图背景
     * @param bitmapTop       位图顶部显示图片
     * @param topShowWidth    顶部图片显示宽度
     * @param topShowHeight   顶部图片显示高度
     * @param leftBgPercent   顶部图片左侧距离百分百（相对于背景）
     * @param topBgPercent    顶部图片顶部侧距离百分百（相对于背景），为空时以底部百分比为准
     * @param bottomBgPercent 顶部图片低部侧距离百分百（相对于背景），为空时不做处理
     * @return 合并后的位图
     */
    public Bitmap mergeBitmap(Bitmap bitmapBg, @NonNull Bitmap bitmapTop, int topShowWidth, int topShowHeight,
            @FloatRange(from = 0, to = 1) float leftBgPercent, @FloatRange(from = 0, to = 1) Float topBgPercent,
            @FloatRange(from = 0, to = 1) Float bottomBgPercent) {
        if (bitmapBg == null) {
            return null;
        }
        try {
            //定义顶部要合并显示的坐标矩阵，默认为相应位图宽高，后续要根据参数进行计算修改
            Rect topShowRect = new Rect(0, 0, topShowWidth, topShowHeight);
            //根据百分百进行定位
            topShowRect.left = (int) (bitmapBg.getWidth() * leftBgPercent);
            topShowRect.right = topShowWidth + topShowRect.left;
            //判断是使用顶部背景百分百还是底部百分比，然后换算位置坐标
            if (topBgPercent == null) {
                if (bottomBgPercent != null) {
                    topShowRect.bottom = (int) (bitmapBg.getHeight() - bitmapBg.getHeight() * bottomBgPercent);
                    topShowRect.top = topShowRect.bottom - topShowHeight;
                }
            } else {
                topShowRect.top = (int) (bitmapBg.getHeight() * topBgPercent);
                topShowRect.bottom = topShowHeight + topShowRect.top;
            }

            Canvas canvas = new Canvas(bitmapBg);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(bitmapTop, new Rect(0, 0, bitmapTop.getWidth(), bitmapTop.getHeight()), topShowRect, paint);
            return bitmapBg;
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(TAG, "合并位图异常" + (e.getMessage() == null ? "" : e.getMessage()));
            return bitmapBg;
        }
    }

    /**
     * 图片设置背景
     *
     * @param bitmap           要设置背景的图片位图
     * @param bgColor          背景颜色
     * @param bgContentPadding 内容和背景内边距
     * @return 透明后的图片位图
     */
    public Bitmap setBitmapBg(Bitmap bitmap, int bgColor, Integer bgContentPadding) {
        if (bitmap == null) {
            return null;
        }
        try {
            //先将原始图片背景做透明处理
            bitmap = makeBgTransparent(bitmap);
            //开始处理设置背景
            int portraitWidth = bitmap.getWidth();
            int portraitHeight = bitmap.getHeight();
            //内容背景间距处理
            bgContentPadding = bgContentPadding == null ? 0 : bgContentPadding;
            //新建接收位图
            Bitmap newBitmap = Bitmap.createBitmap(portraitWidth + bgContentPadding * 2, portraitHeight + bgContentPadding * 2, bitmap.getConfig());
            //位图设置画板初始化
            Canvas canvas = new Canvas(newBitmap);
            //画笔初始化
            Paint paint = new Paint();
            paint.setColor(bgColor);
            paint.setAntiAlias(true);
            //绘制背景颜色
            canvas.drawRect(0, 0, newBitmap.getWidth(), newBitmap.getHeight(), paint);
            //绘制图片
            canvas.drawBitmap(bitmap, null,
                    new Rect(bgContentPadding, bgContentPadding, portraitWidth + bgContentPadding, portraitHeight + bgContentPadding), null);
            return newBitmap;
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(TAG, "图片设置背景异常：" + (e.getMessage() == null ? "" : e.getMessage()));
            return bitmap;
        }
    }

    /**
     * 获取两个位图重叠部分位图
     *
     * @param bottom 底部要显示的位图
     * @param top    顶部要取的重叠位置区域的位图，主要用来标记哪部分的底图要绘制显示
     * @return 重叠部分位图
     */
    public Bitmap getOverlapBitmap(@NotNull Bitmap bottom, @NotNull Bitmap top) {
        return getOverlapBitmap(bottom, top, null, null);
    }

    /**
     * 获取两个位图重叠部分位图
     *
     * @param bottom     底部要显示的位图
     * @param top        顶部要取的重叠位置区域的位图，主要用来标记哪部分的底图要绘制显示
     * @param showWidth  显示宽度，默认以top宽度
     * @param showHeight 显示宽度，默认以top高度
     * @return 重叠部分位图
     */
    public Bitmap getOverlapBitmap(@NotNull Bitmap bottom, @NotNull Bitmap top, Integer showWidth, Integer showHeight) {
        try {
            showWidth = showWidth != null ? showWidth : top.getWidth();
            showHeight = showHeight != null ? showHeight : top.getHeight();
            //新建接收位图
            Bitmap newBitmap = Bitmap.createBitmap(showWidth, showHeight, top.getConfig(), true);
            //位图设置画板初始化
            Canvas canvas = new Canvas(newBitmap);
            //画笔初始化
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            //绘制底部
            canvas.drawBitmap(bottom, null, new RectF(0, 0, showWidth, showHeight), paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            //绘制顶部
            canvas.drawBitmap(top, null, new RectF(0, 0, showWidth, showHeight), paint);
            return newBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 释放bitmap
     *
     * @param bitmap 位图
     */
    public void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 添加水印到位图中
     *
     * @param bitmap 位图
     * @return 添加水印
     */
    public Bitmap addWatermarkBitmap(@NotNull Bitmap bitmap, int textSize, int textColor, @NotNull String text, int width, int height,
            int rotationAngle) {
        if (text.isEmpty() || bitmap.isRecycled()) {
            return null;
        }
        try {
            //位图设置画板初始化
            Canvas canvas = new Canvas(bitmap);
            //在画布上绘制水印
            addWatermarkBitmap(canvas, textSize, textColor, text, width, height, rotationAngle);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成水印
     *
     * @param textSize      文本大小px
     * @param textColor     文本颜色
     * @param text          水印为本
     * @param rotationAngle 旋转角度
     * @param width         水印显示范围宽度
     * @param height        水印显示范围高度
     * @return 水印位图
     */
    public Bitmap generateWatermarkBitmap(int textSize, int textColor, @NotNull String text, int width, int height, int rotationAngle) {
        if (text.isEmpty()) {
            return null;
        }
        try {
            //新建接收位图
            Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            //位图设置画板初始化
            Canvas canvas = new Canvas(newBitmap);
            //在画布上绘制水印
            addWatermarkBitmap(canvas, textSize, textColor, text, width, height, rotationAngle);
            return newBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 在画布上绘制水印
     *
     * @param canvas        画布
     * @param textSize      文本大小px
     * @param textColor     文本颜色
     * @param text          水印文本
     * @param rotationAngle 水印旋转角度
     * @param width         水印显示范围宽度
     * @param height        水印显示范围高度
     */
    public void addWatermarkBitmap(@NotNull Canvas canvas, int textSize, int textColor, @NotNull String text, int width, int height,
            int rotationAngle) {
        //画笔初始化
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        //在画布上绘制水印
        addWatermarkBitmap(canvas, paint, text, width, height, rotationAngle);
    }

    /**
     * 在画布上绘制水印
     *
     * @param canvas        画布
     * @param paint         画笔
     * @param text          水印文本
     * @param rotationAngle 水印旋转角度
     * @param width         水印显示范围宽度
     * @param height        水印显示范围高度
     */
    public void addWatermarkBitmap(@NotNull Canvas canvas, @NotNull Paint paint, @NotNull String text, int width, int height, int rotationAngle) {
        //获取文本属性
        float textWidth = AtlwViewUtil.getInstance().getStrTextWidth(paint, text);
        float textHeight = AtlwViewUtil.getInstance().getStrTextHeight(paint);
        float useWidth = 0;
        float useHeight;
        //新建接收位图
        int textMaxWidthHeight = (int) (Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) * 3);
        Bitmap newBitmap = Bitmap.createBitmap(textMaxWidthHeight, textMaxWidthHeight, Bitmap.Config.ARGB_4444);
        //位图设置画板初始化
        Canvas canvasText = new Canvas(newBitmap);
        while (useWidth < textMaxWidthHeight) {
            useHeight = 0;
            while (useHeight < textMaxWidthHeight + textHeight) {
                canvasText.drawText(text, useWidth, useHeight - paint.getFontMetrics().descent, paint);
                useHeight += textHeight - paint.getFontMetrics().descent;
            }
            useWidth += textWidth;
        }
        canvas.drawBitmap(toTurnPicture(newBitmap, -rotationAngle), -width * 2, -height * 2, null);
    }

    /**
     * 图片格式转换
     *
     * @param path   原始图片地址
     * @param format 目标转换格式
     * @return 转换后地址
     */
    public String coverImage(@NotNull String path, @NotNull String savePath, Bitmap.CompressFormat format) {
        try {
            if (AtlwCheckUtil.getInstance().checkFileIsImage(path)) {
                if (AtlwFileOptionUtil.getInstance().writeToFile(true, new File(savePath), BitmapFactory.decodeFile(path), format)) {
                    return savePath;
                }
            }
        } catch (Exception ignored) {
        }
        return path;
    }

    /**
     * 根据比例填充背景图片
     *
     * @param bitmap 图片位图
     * @param ratio  比例,宽比高
     * @param color  背景颜色
     * @return 处理后图片
     */
    public Bitmap fillBgAspectRatio(@NotNull Bitmap bitmap, float ratio, @ColorInt int color) {
        if (bitmap.isRecycled()) {
            return null;
        }
        ColorDrawable colorDrawable = new ColorDrawable(color);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float originRatio = width * 1.0F / height;
        int colorWidth;
        int colorHeight;
        float leftPercent;
        float topPercent;
        float bottomPercent;
        if (originRatio > ratio) {
            colorWidth = width;
            colorHeight = (int) (width / ratio);
            topPercent = bottomPercent = (colorHeight - height) / 2F / colorHeight;
            leftPercent = 0;
        } else if (originRatio < ratio) {
            colorHeight = height;
            colorWidth = (int) (height * ratio);
            topPercent = bottomPercent = 0F;
            leftPercent = (colorWidth - width) / 2F / colorWidth;
        } else {
            colorHeight = height;
            colorWidth = width;
            topPercent = bottomPercent = leftPercent = 0;
        }

        return mergeBitmap(drawableToBitmap(colorDrawable, colorWidth, colorHeight), bitmap, bitmap.getWidth(), bitmap.getHeight(), leftPercent,
                topPercent, bottomPercent);
    }

}
