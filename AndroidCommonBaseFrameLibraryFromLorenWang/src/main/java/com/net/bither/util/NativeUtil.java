package com.net.bither.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.lorenwang.commonbaseframe.AcbflwBaseApplication;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.file.AtlwFileOptionUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * JNI图片压缩工具类
 *
 * @author XiaoSai
 * @version V1.0.0
 * @Description TODO
 * @Package net.bither.util
 * @Class NativeUtil
 * @Copyright: Copyright (c) 2015
 * @date 2016年3月21日 下午2:13:55
 */
public class NativeUtil {

    private static String TAG = "NativeUtilCompress";
    private static int DEFAULT_QUALITY = 95;

    /**
     * @param bit      bitmap对象
     * @param fileName 指定保存目录名
     * @param optimize 是否采用哈弗曼表数据计算 品质相差5-10倍
     * @Description: JNI基本压缩
     * @author XiaoSai
     * @date 2016年3月23日 下午6:32:49
     * @version V1.0.0
     */
    private static void compressBitmap(Bitmap bit, String fileName, boolean optimize) {
        saveBitmap(bit, DEFAULT_QUALITY, fileName, optimize);
    }

    /**
     * @param image    bitmap对象
     * @param filePath 要保存的指定目录
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     * @author XiaoSai
     * @date 2016年3月23日 下午6:28:15
     * @version V1.0.0
     */
    private static void compressBitmap(Bitmap image, String filePath, Bitmap.CompressFormat format) {
        // 最大图片大小 150KB
        int maxSize = 150;
        // 获取尺寸压缩倍数
        int ratio = NativeUtil.getRatioSize(image.getWidth(), image.getHeight());
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(format, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(format, options, baos);
        }
        // JNI保存图片到SD卡 这个关键
        NativeUtil.saveBitmap(result, options, filePath, true);
        // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
        }
    }


    /**
     * @param image    bitmap对象
     * @param filePath 要保存的指定目录
     * @param maxSize  // 最大图片大小 1000KB
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     */
    public static synchronized void compressBitmap(Bitmap image, String filePath, int maxSize, Bitmap.CompressFormat format) {
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight());
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(format, options, baos);
        // 循环判断如果压缩后图片是否大于最大值,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(format, options, baos);
        }
        // JNI调用保存图片到SD卡 这个关键
        saveBitmap(result, options, filePath, true);
        //如果jni保存失败的话
        File file = new File(filePath);
        if (!file.exists()) {
            AtlwFileOptionUtil.getInstance().writeToFile(true, new File(filePath), result, format);
        }
        file = null;
        // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
            result = null;
        }
    }

    /**
     * 压缩图片
     *
     * @param compressFilePath
     * @param savePath
     * @param maxSize
     * @param isDegree         是否进行旋转
     * @return
     */
    public synchronized static boolean compressFile(String compressFilePath, String savePath, int maxSize, boolean isDegree,
            Bitmap.CompressFormat format) {
        if (isDegree) {
            return compressFile(compressFilePath, savePath, maxSize, AtlwImageCommonUtil.getInstance().readPictureDegree(compressFilePath), format);
        } else {
            return compressFile(compressFilePath, savePath, maxSize, 0, format);
        }
    }

    /**
     * 压缩图片
     *
     * @param compressFilePath
     * @param savePath
     * @param maxSize
     * @param degree           旋转角度
     * @return
     */
    public synchronized static boolean compressFile(String compressFilePath, String savePath, int maxSize, int degree, Bitmap.CompressFormat format) {
        try {
            if (new File(compressFilePath).exists()) {
                Uri uri = Uri.fromFile(new File(compressFilePath));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(AcbflwBaseApplication.getAppContext().getContentResolver(), uri);
                if (degree != 0) {
                    bitmap = AtlwImageCommonUtil.getInstance().toTurnPicture(bitmap, degree);
                }
                AtlwLogUtil.logUtils.logE(TAG, "====开始==压缩==saveFile==" + savePath);
                compressBitmap(bitmap, savePath, maxSize, format);
                AtlwLogUtil.logUtils.logE(TAG, "====完成==压缩==saveFile==" + savePath);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(TAG, "压缩失败");
            return false;
        } catch (java.lang.OutOfMemoryError OutOfMemoryError) {
            AtlwLogUtil.logUtils.logE(TAG, "压缩失败");
            return false;
        }

    }


    /**
     * @param curFilePath    当前图片文件地址
     * @param targetFilePath 要保存的图片文件地址
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     * @author XiaoSai
     * @date 2016年9月28日 下午17:43:15
     * @version V1.0.0
     */
    private void compressBitmap(String curFilePath, String targetFilePath, Bitmap.CompressFormat format) {
        // 最大图片大小 150KB
        int maxSize = 150;
        //根据地址获取bitmap
        Bitmap result = getBitmapFromFile(curFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        result.compress(format, quality, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            quality -= 10;
            // 这里压缩quality，把压缩后的数据存放到baos中
            result.compress(format, quality, baos);
        }
        // JNI保存图片到SD卡 这个关键
        NativeUtil.saveBitmap(result, quality, targetFilePath, true);
        // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
        }

    }

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return int 缩放比
     * @author XiaoSai
     * @date 2016年3月21日 下午3:03:38
     * @version V1.0.0
     */
    private static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 1280;
        int imageWidth = 960;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0) {
            ratio = 1;
        }
        return ratio;
    }

    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     *
     * @param filePath
     * @return
     */
    private Bitmap getBitmapFromFile(String filePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 获取尺寸压缩倍数
        newOpts.inSampleSize = NativeUtil.getRatioSize(w, h);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        newOpts.inDither = false;
        newOpts.inTempStorage = new byte[32 * 1024];
        Bitmap bitmap = null;
        File file = new File(filePath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
                //旋转图片
                int photoDegree = AtlwImageCommonUtil.getInstance().readPictureDegree(filePath);
                if (photoDegree != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(photoDegree);
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    /**
     * 调用native方法
     *
     * @param bit
     * @param quality
     * @param fileName
     * @param optimize
     * @Description:函数描述
     * @author XiaoSai
     * @date 2016年3月23日 下午6:36:46
     * @version V1.0.0
     */
    private static void saveBitmap(Bitmap bit, int quality, String fileName, boolean optimize) {
        compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), optimize);
    }

    /**
     * 调用底层 bitherlibjni.c中的方法
     *
     * @param bit
     * @param w
     * @param h
     * @param quality
     * @param fileNameBytes
     * @param optimize
     * @return
     * @Description:函数描述
     * @author XiaoSai
     * @date 2016年3月23日 下午6:35:53
     * @version V1.0.0
     */
    private static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes, boolean optimize);

    /**
     * 加载lib下两个so文件
     */
    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("bitherjni");
    }

}
