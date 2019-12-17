/*
 * Copyright (C) 2008 ZXing authors
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
class CaptureActivityHandler extends Handler {

    private DecodeThread decodeThread;
    private CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CameraManager cameraManager, int decodeMode) {
        decodeThread = new DecodeThread(decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case SacnCameraCommon.restart_preview:
                restartPreviewAndDecode();
                break;
            case SacnCameraCommon.decode_succeeded:
                state = State.SUCCESS;
                Bundle bundle = message.getData();
                AgcslwScanUtils.getInstance().handleDecode((Result) message.obj, bundle);
                break;
            case SacnCameraCommon.decode_failed:
                // We're decoding as fast as possible, so when one decode fails,
                // start another.
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), SacnCameraCommon.decode);
                break;
            default:
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        if (decodeThread != null && decodeThread.getHandler() != null) {
            Message quit = Message.obtain(decodeThread.getHandler(), SacnCameraCommon.quit);
            quit.sendToTarget();
            try {
                // Wait at most half a second; should be enough time, and onPause()
                // will timeout quickly
                decodeThread.join(500L);
            } catch (InterruptedException e) {
                // continue
            }
        }
        // Be absolutely sure we don't send any queued up messages
        removeMessages(SacnCameraCommon.decode_succeeded);
        removeMessages(SacnCameraCommon.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS && decodeThread != null && decodeThread.getHandler() != null) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), SacnCameraCommon.decode);
        }
    }

    /**
     * 释放内存
     */
    public void release() {
        quitSynchronously();
        if (decodeThread != null) {
            decodeThread.release();
            decodeThread = null;
        }
        if (cameraManager != null) {
            cameraManager.release();
            cameraManager = null;
        }
    }


}
