/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.lorenwang.graphic_code_scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 功能作用：解码handler
 * 初始注释时间： 2019/12/17 12:03
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class DecodeHandler extends Handler {

    /**
     * 扫描view
     */
    private MultiFormatReader multiFormatReader;
    private boolean running = true;
    private AgcslwScan agcslwScan;

    public DecodeHandler(Map<DecodeHintType, Object> hints,AgcslwScan agcslwScan) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.agcslwScan = agcslwScan;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case SacnCameraCommon.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case SacnCameraCommon.quit:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        CameraManager cameraManager = agcslwScan.getCameraManager();
        if (cameraManager != null) {
            Size size = cameraManager.getPreviewSize();
            if (size != null) {

                Result rawResult = null;
                PlanarYUVLuminanceSource source;

                if(agcslwScan.getDegree() / 90 % 2 != 0){
                    // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
                    byte[] rotatedData = new byte[data.length];
                    for (int y = 0; y < size.height; y++) {
                        for (int x = 0; x < size.width; x++)
                            rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
                    }
                    // 宽高也要调整
                    int tmp = size.width;
                    size.width = size.height;
                    size.height = tmp;
                    source = buildLuminanceSource(rotatedData, size.width, size.height);
                }else {
                    source = buildLuminanceSource(data, size.width, size.height);
                }


                if (source != null && multiFormatReader != null) {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        rawResult = multiFormatReader.decodeWithState(bitmap);
                    } catch (ReaderException re) {
                        // continue
                    } finally {
                        multiFormatReader.reset();
                    }
                }

                Handler handler = agcslwScan.getHandler();
                if (rawResult != null) {
                    // Don't log the barcode contents for security.
                    if (handler != null) {
                        Message message = Message.obtain(handler, SacnCameraCommon.decode_succeeded, rawResult);
                        Bundle bundle = new Bundle();
                        bundleThumbnail(source, bundle);
                        message.setData(bundle);
                        message.sendToTarget();
                    }
                } else {
                    if (handler != null) {
                        Message message = Message.obtain(handler, SacnCameraCommon.decode_failed);
                        message.sendToTarget();
                    }
                }
            }
        }
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on
     * the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = agcslwScan.getCropRect();
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
    }

    /**
     * 释放内存
     */
    public void release() {
        running = false;
        if (multiFormatReader != null) {
            multiFormatReader.release();
            multiFormatReader = null;
        }
    }

}
