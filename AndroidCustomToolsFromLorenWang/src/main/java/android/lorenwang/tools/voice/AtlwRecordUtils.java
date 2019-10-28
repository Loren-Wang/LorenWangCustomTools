package android.lorenwang.tools.voice;

import android.Manifest;
import android.app.Activity;
import android.lorenwang.tools.base.AtlwCheckUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.media.MediaRecorder;

import java.io.File;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-07-19 下午 14:22:28
 * 创建人：王亮（Loren wang）
 * 功能作用：录音工具类
 * 思路：
 * 方法：
 * 1、开始录音
 * 2、结束录音
 * 3、取消录音
 * 4、获取音量值等级级，会根据传递进来的最高等级返回相应的指定等级
 * 5、是否正在录音
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwRecordUtils {
    private static volatile AtlwRecordUtils atlwRecordUtils;
    private final String TAG = getClass().getName();
    /**
     * 是否正在录音
     */
    private boolean recording = false;
    /**
     * 录音实例
     */
    private MediaRecorder mediaRecorder;
    /**
     * 当前录音存储的文件
     */
    private String nowRecordSavePath;
    /**
     * 录音状态的回调通知
     */
    private AtlwRecordCallback atlwRecordCallback;

    /**
     * 私有化单例类
     */
    private AtlwRecordUtils() {
    }

    /**
     * 返回单例
     *
     * @return 当前实例
     */
    public static AtlwRecordUtils getInstance() {
        synchronized (AtlwRecordUtils.class) {
            if (atlwRecordUtils == null) {
                atlwRecordUtils = new AtlwRecordUtils();
            }
        }
        return atlwRecordUtils;
    }


    /**
     * 开启录音
     *
     * @param activity           activity实例
     * @param savePath           目标存储文件
     * @param isCancelLastRecord 是否结束上一个录音
     * @param isEndPlaying       如果正在播放的话是否结束播放
     * @return 返回是否开始成功
     */
    public boolean start(Activity activity, String savePath, boolean isCancelLastRecord, boolean isEndPlaying) {
        //检测传入的存储地址是否为空
        if (JtlwCheckVariateUtils.getInstance().isEmpty(savePath)) {
            AtlwLogUtils.logE(TAG, "The savePath is never empty");
            //回传状态
            recordStart(false);
            return false;
        }

        //开启录音前首先要检测录音权限
        if (!AtlwCheckUtils.getInstance().checkAppPermisstion(activity, Manifest.permission.RECORD_AUDIO
                , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AtlwLogUtils.logE(TAG, "Not record and storage permisstions!");
            //回传状态
            recordStart(false);
            return false;
        }

        //判断当前是否在录音
        if (recording) {
            AtlwLogUtils.logE(TAG, "Now Recording");
            //判断是否要取消上一次播放
            if (isCancelLastRecord) {
                AtlwLogUtils.logE(TAG, "Ready to cancel the last record");
                //停止成功继续
                if (cancel()) {
                    AtlwLogUtils.logE(TAG, "The last record cancel successful.");
                } else {
                    AtlwLogUtils.logE(TAG, "The last record cancel failed.");
                    //回传状态
                    recordStart(false);
                    return false;
                }
            } else {
                //回传状态
                recordStart(false);
                return false;
            }
        }

        //判断当前是否在播放录音
        if (AtlwMediaPlayUtils.getInstance().isPlaying()) {
            AtlwLogUtils.logI(TAG, "The recording is currently playing");
            //判断是否要结束，只有结束成功了才会向下走，不结束或者结束失败都会返回失败，不会继续向下走
            if (isEndPlaying) {
                if (AtlwMediaPlayUtils.getInstance().stop(activity)) {
                    AtlwLogUtils.logI(TAG, "Play ended successfully");
                } else {
                    AtlwLogUtils.logI(TAG, "Play end failed!");
                    //回传状态
                    recordStart(false);
                    return false;
                }
            } else {
                //回传状态
                recordStart(false);
                return false;
            }
        }

        //检测文件夹是否存在，没有存在则创建文件夹，如果创建失败则返回失败
        if (!AtlwFileOptionUtils.getInstance().createDirectory(activity, true, savePath, true)) {
            AtlwLogUtils.logE(TAG, "Directory creation failed");
            //回传状态
            recordStart(false);
            return false;
        }

        //检测录音的存储文件是否存在，文件存在则返回失败
        if (AtlwCheckUtils.getInstance().checkFileIsExit(savePath)) {
            AtlwLogUtils.logE(TAG, "Record exists!");
            //回传状态
            recordStart(false);
            return false;
        }

        //锁住录音的类，同一时间只允许同一个录音使用
        synchronized (AtlwRecordUtils.class) {
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            }
            try {
                // 设置输出文件路径
                mediaRecorder.setOutputFile(savePath);
                // 设置MediaRecorder的音频源为麦克风
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 设置音频格式为RAW_AMR
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                // 设置音频编码为AMR_NB
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // 准备录音
                mediaRecorder.prepare();
                // 开始，必需在prepare()后调用
                mediaRecorder.start();
                //记录状态
                recording = true;
                //记录当前启动的录音的存储路径
                this.nowRecordSavePath = savePath;
                AtlwLogUtils.logI(TAG, "Recording started successfully");
                //回传状态
                recordStart(true);
                return true;
            } catch (Exception e) {
                //记录状态
                recording = false;
                AtlwLogUtils.logI(TAG, "Recording started failed");
                //回传状态
                recordStart(false);
                return false;
            }
        }
    }

    /**
     * 结束录音
     *
     * @return 是否结束成功
     */
    public boolean stop() {
        return endRecord(false);
    }

    /**
     * 取消录音
     *
     * @return 返回是否取消成功
     */
    public boolean cancel() {
        if (endRecord(true)) {
            if (!JtlwCheckVariateUtils.getInstance().isEmpty(nowRecordSavePath)) {
                try {
                    //取消录音后删除对应文件
                    File file = new File(nowRecordSavePath);
                    file.delete();
                    //回传状态
                    recordCancel(true, nowRecordSavePath);
                    nowRecordSavePath = null;
                    return false;
                } catch (Exception e) {
                    //回传状态
                    recordCancel(false, nowRecordSavePath);
                    return false;
                }
            } else {
                //回传状态
                recordCancel(true, nowRecordSavePath);
                return true;
            }
        } else {
            //回传状态
            recordCancel(true, nowRecordSavePath);
            return true;
        }
    }

    /**
     * 获取音量值等级价，会根据传递进来的最高等级返回相应的指定等级
     *
     * @param maxlevel 最高等级
     * @return 当前等级，会在最高等级内
     */
    public synchronized int getVoiceLevel(int maxlevel) {
        if (mediaRecorder != null && recording) {
            try {
                // getMaxAmplitude返回的数值最大是32767
                return maxlevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    /**
     * 是否正在录音
     *
     * @return 当前录音状态
     */
    public boolean isRecording() {
        return recording;
    }


    /**
     * 结束录音
     *
     * @param isCancel 是否是取消状态的结束录音
     * @return 返回结束是否成功
     */
    private boolean endRecord(boolean isCancel) {
        synchronized (AtlwRecordUtils.class) {
            if (mediaRecorder != null && recording) {
                try {
                    //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
                    //报错为：RuntimeException:stop failed
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    recording = false;
                    AtlwLogUtils.logI(TAG, "Record stop Successful!");
                    //回传状态
                    if (!isCancel) {
                        recordStop(true);
                    }
                    return true;
                } catch (Exception e) {
                    AtlwLogUtils.logI(TAG, "Record stop failed!");
                    //回传状态
                    if (!isCancel) {
                        recordStop(false);
                    }
                    return false;
                }
            } else {
                AtlwLogUtils.logI(TAG, "Record stop Successful!");
                //回传状态
                if (!isCancel) {
                    recordStop(true);
                }
                return true;
            }
        }
    }

    /**
     * 录音启动状态
     *
     * @param isSuccess 是否启动成功
     */
    private void recordStart(boolean isSuccess) {
        if (atlwRecordCallback != null) {
            atlwRecordCallback.recordStart(isSuccess);
        }
    }

    /**
     * 录音停止
     *
     * @param isSuccess 是否停止成功
     */
    private void recordStop(boolean isSuccess) {
        if (atlwRecordCallback != null) {
            atlwRecordCallback.recordStop(isSuccess);
        }
    }

    /**
     * 录音取消状态
     *
     * @param isSuccess         是否取消成功
     * @param nowRecordSavePath
     */
    private void recordCancel(boolean isSuccess, String nowRecordSavePath) {
        if (atlwRecordCallback != null) {
            atlwRecordCallback.recordCancel(isSuccess, nowRecordSavePath);
        }
    }
}
