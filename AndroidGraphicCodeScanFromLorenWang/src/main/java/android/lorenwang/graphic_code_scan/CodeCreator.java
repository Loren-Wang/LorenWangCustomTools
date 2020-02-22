package android.lorenwang.graphic_code_scan;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

class CodeCreator {

    /**
     * 生成二维码
     *
     * @param str        二维码字符串
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoBitmap logo图片位图
     * @return 生成的二维码位图
     * @throws WriterException 生成二维码异常
     */
    public static Bitmap createQRCode(String str, int width, int height, Bitmap logoBitmap) throws WriterException {

        try {
            // 判断URL合法性
            if (str == null || str.isEmpty()) {
                return null;
            }
            /*偏移量*/
            int offsetX = width / 2;
            int offsetY = height / 2;
            /*生成logo*/
            if (logoBitmap != null) {
                Matrix matrix = new Matrix();
                float scaleFactor = Math.min(width * 1.0f / 5 / logoBitmap.getWidth(), height * 1.0f / 5 / logoBitmap.getHeight());
                matrix.postScale(scaleFactor, scaleFactor);
                logoBitmap = Bitmap.createBitmap(logoBitmap, 0, 0, logoBitmap.getWidth(), logoBitmap.getHeight(), matrix, true);
            }


            /*如果log不为null,重新计算偏移量*/
            int logoW = 0;
            int logoH = 0;
            if (logoBitmap != null) {
                logoW = logoBitmap.getWidth();
                logoH = logoBitmap.getHeight();
                offsetX = (width - logoW) / 2;
                offsetY = (height - logoH) / 2;
            }

            /*指定为UTF-8*/
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0);
            // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x >= offsetX && x < offsetX + logoW && y >= offsetY && y < offsetY + logoH && logoBitmap != null) {
                        int pixel = logoBitmap.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (matrix.get(x, y)) {
                                pixel = 0xff000000;
                            } else {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * width + x] = pixel;
                    } else {
                        if (matrix.get(x, y)) {
                            pixels[y * width + x] = 0xff000000;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
