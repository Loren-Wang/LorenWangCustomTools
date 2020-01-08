package android.lorenwang.customview.texiview.recordChat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.lorenwang.customview.dialog.AvlwRecordDialog;
import android.lorenwang.tools.base.AtlwCheckUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.lorenwang.tools.mobile.AtlwMobileOptionsUtils;
import android.lorenwang.tools.voice.AtlwRecordUtils;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import javabase.lorenwang.tools.common.JtlwCommonUtils;

/**
 * 创建时间：2019-07-19 下午 14:14:46
 * 创建人：王亮（Loren wang）
 * 功能作用：仿微信聊天按住语音录音按钮
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwRecordButton extends AppCompatButton {
    private AppCompatActivity activity;
    private static final String TAG = "AvlwRecordChatButton";
    /**
     * 长按的判断时间
     */
    private final long LONG_PRESSED_JUDGE_TIME = 1000l;
    /**
     * 长按的判断runnable
     */
    private Runnable longPressedJudgeRunnable;
    /**
     * 录音倒计时总时间
     */
    private long recordEndTheCountdownTime = 60000l;
    /**
     * 提示录音时间过短的录音时间
     */
    private long recordTimeForShort = 1000l;
    /**
     * 当前录音倒计时时间
     */
    private long nowRecordEndTheCountdownTime = 0;
    /**
     * 倒计时runnable
     */
    private Runnable recordEndTheCountdownRunnable;
    /**
     * 消息传递使用,使用主线程
     */
    private Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 是否正在录音，只是记录当前页面状态，和真正的录音是否开启无关
     */
    private boolean isRecord = false;
    /**
     * 是否取消录音提示
     */
    private boolean isCancelRecordHint = false;
    /**
     * 录音文件保存的文件夹
     */
    private String recordSavePathDir;
    /**
     * 录音弹窗
     */
    private AvlwRecordDialog avlwRecordDialog;
    /**
     * 录音按钮录音监听
     */
    private AvlwRecordListener avlwRecordListener;
    /**
     * 录音时间过短线程
     */
    private Runnable recordTimeShortRunnable;


    public AvlwRecordButton(AppCompatActivity activity) {
        super(activity);
        init(activity);
    }

    public AvlwRecordButton(AppCompatActivity activity, AttributeSet attrs) {
        super(activity, attrs);
        init(activity);
    }

    public AvlwRecordButton(AppCompatActivity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        init(activity);
    }

    private void init(final AppCompatActivity context) {
        this.activity = activity;
    }

    /*****************************************手势逻辑部分******************************************/

    /**
     * 判断在Button上滑动距离，以判断 是否取消
     */
    private static final int DISTANCE_Y_CANCEL = 50;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDown();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecord) {
                    //根据x,y的坐标判断是否需要取消
                    if (wantToCancel(event.getX(), event.getY())) {
                        cancelRecordHintShow();
                    } else {
                        cancelRecordHintHide();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                endUp();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 是否取消判断
     *
     * @param x 移动后位置x
     * @param y 移动后位置y
     * @return 是否需要取消录音
     */
    private boolean wantToCancel(float x, float y) {
        // 超过按钮的宽度
        if (x < 0 || x > getWidth()) {
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    /**
     * 按钮从初始状态第一次被按下,禁用按钮，同时开始长按判断，剩余对状态的返回
     */
    private void firstDown() {
        //禁用按钮
        setEnabled(false);
        //开启长按事件的逻辑判断
        startLongPressedJudgeRunnable();
    }

    /**
     * 手势抬起，判断是否开始录音，如果没有开始录音则返回录音重置，如果开始录音了则判断录音时间的长短，如果有取消录音就取消录音
     */
    private void endUp() {
        //判断是否正在录音
        if (isRecord) {
            //判断录音时间
            if (Long.valueOf(nowRecordEndTheCountdownTime).compareTo(recordTimeForShort) <= 0) {
                recordTimeShort();
            } else if (isCancelRecordHint) {
                //判断是否取消录音
                cancelRecord();
            } else {
                stopRecord();
            }
        }
        //取消倒计时
        cancelEndTheCountdown();
        //重置按钮
        resetRecord();
    }

    /*******************************************录音操作逻辑部分*************************************/

    /**
     * 开始录音
     */
    @SuppressLint("MissingPermission")
    private void startRecord() {
        //取消长按监听线程
        cancelLongPressedJudgeRunnable();
        this.isRecord = true;
        //开始录音
        boolean start = AtlwRecordUtils.getInstance().start(activity, recordSavePathDir
                + JtlwCommonUtils.getInstance().generateUuid(false), true, true);
        if (start) {
            //初始化开始时间
            this.nowRecordEndTheCountdownTime = 0;
            //开始倒计时
            startEndTheCountdown();
            if (isRecord && avlwRecordDialog != null && !avlwRecordDialog.isShowing()) {
                avlwRecordDialog.show();
                avlwRecordDialog.startRecord();
                //回传状态
                if (avlwRecordListener != null) {
                    avlwRecordListener.startRecord();
                }
                //震动设备
                if (AtlwCheckUtils.getInstance().checkAppPermission(Manifest.permission.VIBRATE)) {
                    AtlwMobileOptionsUtils.getInstance().vibrate(100);
                }
            }
        }
    }

    /**
     * 停止录音
     */
    private void stopRecord() {
        this.isRecord = false;
        AtlwRecordUtils.getInstance().stop();
        if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
            //回传状态
            if (avlwRecordListener != null) {
                avlwRecordListener.stopRecord();
            }
            avlwRecordDialog.dismiss();
        }
    }

    /**
     * 取消录音
     */
    private void cancelRecord() {
        if (isRecord && isCancelRecordHint) {
            //取消录音
            AtlwRecordUtils.getInstance().cancel();
            //隐藏弹窗
            if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
                avlwRecordDialog.dismiss();
            }
            //回传状态
            if (avlwRecordListener != null) {
                avlwRecordListener.cancelRecord();
            }
        }
    }

    /**
     * 取消录音提示显示
     */
    private void cancelRecordHintShow() {
        isCancelRecordHint = true;
        if (isRecord) {
            //隐藏弹窗
            if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
                avlwRecordDialog.cancelRecordHintShow();
            }
            //回传状态
            if (avlwRecordListener != null) {
                avlwRecordListener.cancelRecordHintShow();
            }
        }
    }

    /**
     * 取消录音提示隐藏
     */
    private void cancelRecordHintHide() {
        isCancelRecordHint = false;
        if (isRecord) {
            //隐藏弹窗
            if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
                avlwRecordDialog.cancelRecordHintHide();
            }
            //回传状态
            if (avlwRecordListener != null) {
                avlwRecordListener.cancelRecordHintHide();
            }
        }
    }

    /**
     * 当前录音时间
     *
     * @param nowRecordTime 已经被录音的时间
     */
    private void nowRecordTime(long nowRecordTime) {
        if (isRecord) {
            if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
                avlwRecordDialog.nowRecordTime(nowRecordTime);
            }
            //回传状态
            if (avlwRecordListener != null) {
                avlwRecordListener.nowRecordTime(nowRecordTime);
            }
        }
    }

    /**
     * 录音时间过短
     */
    private synchronized void recordTimeShort() {
        if (recordTimeShortRunnable == null) {
            recordTimeShortRunnable = new Runnable() {
                @Override
                public void run() {
                    //取消录音
                    cancelRecord();
                }
            };
        }
        if (avlwRecordDialog != null && avlwRecordDialog.isShowing()) {
            avlwRecordDialog.recordTimeShort();
        }
        //回传状态
        if (avlwRecordListener != null) {
            avlwRecordListener.recordTimeShort();
        }
        //延迟时间后取消录音
        handler.postDelayed(recordTimeShortRunnable, 1500);

    }

    /**
     * 重置录音
     */
    private void resetRecord() {

    }


    /********************************************线程操作部分***************************************/

    /**
     * 开启长按判断线程，一旦线程在指定时间后被执行代表着开启了长按事件
     */
    private void startLongPressedJudgeRunnable() {
        synchronized (handler) {
            if (longPressedJudgeRunnable == null) {
                longPressedJudgeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        longPressedJudgeRunnable = null;
                        startRecord();
                    }
                };
            }
            handler.postDelayed(longPressedJudgeRunnable, LONG_PRESSED_JUDGE_TIME);
        }
    }

    /**
     * 取消长按监听线程
     */
    private void cancelLongPressedJudgeRunnable() {
        synchronized (handler) {
            if (longPressedJudgeRunnable != null) {
                handler.removeCallbacks(longPressedJudgeRunnable);
                longPressedJudgeRunnable = null;
            }
        }
    }

    /**
     * 开始结束倒计时
     */
    private synchronized void startEndTheCountdown() {
        if (recordEndTheCountdownRunnable == null) {
            recordEndTheCountdownRunnable = new Runnable() {
                @Override
                public void run() {
                    nowRecordEndTheCountdownTime += 1000;
                    if (nowRecordEndTheCountdownTime < recordEndTheCountdownTime) {
                        nowRecordTime(nowRecordEndTheCountdownTime);
                        //每隔1s回调一次
                        handler.postDelayed(recordEndTheCountdownRunnable, 1000);
                    } else if (Long.valueOf(recordEndTheCountdownTime).compareTo(nowRecordEndTheCountdownTime) <= 0) {
                        nowRecordTime(nowRecordEndTheCountdownTime);
                        //达到时间，结束录音
                        stopRecord();
                    }
                }
            };
        }
        //每隔1s回调一次
        handler.postDelayed(recordEndTheCountdownRunnable, 1000);
    }

    /**
     * 取消结束倒计时
     */
    private synchronized void cancelEndTheCountdown() {
        if (recordEndTheCountdownRunnable != null) {
            nowRecordEndTheCountdownTime = recordEndTheCountdownTime;
            handler.removeCallbacks(recordEndTheCountdownRunnable);
        }
    }

    /*****************************************用户设置逻辑部分***************************************/

    /**
     * 设置录音保存的文件夹
     *
     * @param recordSavePathDir 录音保存的文件夹
     */
    public void setRecordSavePathDir(String recordSavePathDir) {
        //判断最后一位是不是斜线，不是的话则要补上
        if (Integer.valueOf(recordSavePathDir.lastIndexOf("/")).compareTo(recordSavePathDir.length() - 1) != 0) {
            recordSavePathDir = recordSavePathDir + "/";
        }
        this.recordSavePathDir = recordSavePathDir;
        //不管文件夹是否存在，都创建个文件夹
        AtlwFileOptionUtils.getInstance().createDirectory(true, recordSavePathDir, false);
    }

    /**
     * 设置录音倒计时时间，默认60s
     *
     * @param recordEndTheCountdownTime 倒计时时间，单位毫秒
     */
    public void setRecordEndTheCountdownTime(long recordEndTheCountdownTime) {
        //做一下运算，时间必须为s的正数倍
        this.recordEndTheCountdownTime = (recordEndTheCountdownTime / 1000 + 1) * 1000;
    }

    /**
     * 设置录音弹窗
     *
     * @param avlwRecordDialog 录音弹窗
     */
    public void setAvlwRecordDialog(AvlwRecordDialog avlwRecordDialog) {
        this.avlwRecordDialog = avlwRecordDialog;
    }

    /**
     * 设置录音监听
     *
     * @param avlwRecordListener 监听实例
     */
    public void setAvlwRecordListener(AvlwRecordListener avlwRecordListener) {
        this.avlwRecordListener = avlwRecordListener;
    }
}
