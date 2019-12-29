package android.lorenwang.tools.voice;

import android.Manifest;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.lorenwang.tools.app.AtlwActivityUtils;
import android.lorenwang.tools.base.AtlwCheckUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.mobile.AtlwMobileOptionsUtils;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_VOICE_CALL;

/**
 * 创建时间：2019-07-19 下午 14:32:32
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwMediaPlayUtils {
    private static volatile AtlwMediaPlayUtils atlwMediaPlayUtils;
    private final String TAG = getClass().getName();
    /**
     * 是否正在播放
     */
    private boolean playing = false;
    /**
     * 语音文件的读取流
     */
    private FileInputStream playFileInputStream;
    /**
     * 语音播放类
     */
    private MediaPlayer mediaPlayer;
    /**
     * 当前音频播放类型
     */
    private int nowPlayOutputStreamType = STREAM_MUSIC;
    /**
     * 当前播放地址
     */
    private String nowPlayPath;
    /**
     * 距离传感器
     */
    private SensorEventListener proximityListener;
    /**
     * 是否允许使用电源锁
     */
    private boolean isUsePowerWakeLock = false;
    /**
     * 是否允许修改输出设备
     */
    private boolean isAllowChangeOutputType = false;
    /**
     * 音频播放回调
     */
    private AtlwMediaPlayCallback atlwMediaPlayCallback;


    /**
     * 私有化构造
     */
    private AtlwMediaPlayUtils() {

    }

    /**
     * 获取单例
     *
     * @return 返回当前的唯一实例
     */
    public static AtlwMediaPlayUtils getInstance() {
        synchronized (AtlwMediaPlayUtils.class) {
            if (atlwMediaPlayUtils == null) {
                atlwMediaPlayUtils = new AtlwMediaPlayUtils();
            }
        }
        return atlwMediaPlayUtils;
    }

    /**
     * 开始播放
     *
     * @param activity         activity 实例
     * @param playPath         播放地址
     * @param type             播放类型，是听筒还是扬声器播放
     * @param isEndRecord      如果正在录音的话是否结束录音
     * @param isCancelLastPlay 如果上一个正在播放的话是否结束上一个播放
     * @return 是否开始
     */
    public boolean start(Activity activity, final String playPath, @AtlwMediaPlayOutputType int type, boolean isEndRecord, boolean isCancelLastPlay) {
        //检测传入的地址是否为空
        if (JtlwCheckVariateUtils.getInstance().isEmpty(playPath)) {
            AtlwLogUtils.logE(TAG, "The playPath is never empty");
            //回传状态
            playStart(false);
            return false;
        }
        //开启前首先要检测文件权限
        if (!AtlwCheckUtils.getInstance().checkAppPermission(AtlwActivityUtils.getInstance().getApplicationContext(activity)
                , Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AtlwLogUtils.logE(TAG, "Not read and storage permisstions!");
            //回传状态
            playStart(false);
            return false;
        }
        //判断当前是否在播放
        if (playing) {
            AtlwLogUtils.logE(TAG, "Now playing");
            //判断是否要取消上一次播放
            if (isCancelLastPlay) {
                AtlwLogUtils.logE(TAG, "Ready to cancel the last play");
                //停止成功继续
                if (stop(activity)) {
                    AtlwLogUtils.logE(TAG, "The last play ending successful.");
                } else {
                    AtlwLogUtils.logE(TAG, "The last play ending failed.");
                    //回传状态
                    playStart(false);
                    return false;
                }
            } else {
                //回传状态
                playStart(false);
                return false;
            }
        }

        //判断当前是否在录音
        if (AtlwRecordUtils.getInstance().isRecording()) {
            AtlwLogUtils.logI(TAG, "Now recording");
            //判断是否要结束，只有结束成功了才会向下走，不结束或者结束失败都会返回失败，不会继续向下走
            if (isEndRecord) {
                if (AtlwRecordUtils.getInstance().stop()) {
                    AtlwLogUtils.logI(TAG, "Record ended successfully!");
                } else {
                    AtlwLogUtils.logI(TAG, "Record end failed!");
                    //回传状态
                    playStart(false);
                    return false;
                }
            } else {
                //回传状态
                playStart(false);
                return false;
            }
        }

        //检测文件是否存在
        if (!AtlwCheckUtils.getInstance().checkFileIsExit(playPath)) {
            AtlwLogUtils.logE(TAG, "File not Exists.");
            //回传状态
            playStart(false);
            return false;
        }

        //锁住录音的类，同一时间只允许同一个录音使用
        synchronized (AtlwMediaPlayUtils.class) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            try {
                //设置播放的硬件设备
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    AudioAttributes.Builder builder = new AudioAttributes.Builder();
                    //设置播放的硬件设备
                    if (type == AtlwMediaPlayOutputType.EARPIECE) {
                        nowPlayOutputStreamType = STREAM_VOICE_CALL;
                        builder.setLegacyStreamType(STREAM_VOICE_CALL);
                    } else if (type == AtlwMediaPlayOutputType.SPEAKER) {
                        nowPlayOutputStreamType = STREAM_MUSIC;
                        builder.setLegacyStreamType(STREAM_MUSIC);
                    }
                    mediaPlayer.setAudioAttributes(builder.build());
                } else {
                    //设置播放的硬件设备
                    if (type == AtlwMediaPlayOutputType.EARPIECE) {
                        nowPlayOutputStreamType = STREAM_VOICE_CALL;
                        mediaPlayer.setAudioStreamType(STREAM_VOICE_CALL);
                    } else if (type == AtlwMediaPlayOutputType.SPEAKER) {
                        nowPlayOutputStreamType = STREAM_MUSIC;
                        mediaPlayer.setAudioStreamType(STREAM_MUSIC);
                    }
                }
                //设置播放完成处理
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playEnd();
                    }
                });
                playFileInputStream = new FileInputStream(playPath);
                //设置播放源
                mediaPlayer.setDataSource(playFileInputStream.getFD());
                //准备播放
                mediaPlayer.prepare();
                //初始化准备回调
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        playing = true;
                        nowPlayPath = playPath;
                        AtlwLogUtils.logI(TAG, "Playing started successfully");
                        //回传状态
                        playStart(true);
                    }
                });
                //注册监听器，内部会判断是否允许创建
                registProximitySensorListener(activity);
                return true;
            } catch (Exception e) {
                //记录状态
                playing = false;
                //关闭播放流
                if (playFileInputStream != null) {
                    try {
                        playFileInputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        playFileInputStream = null;
                    }
                }
                AtlwLogUtils.logI(TAG, "Playing started failed");
                //回传状态
                playStart(false);
                return false;
            }
        }
    }

    /**
     * 停止播放，要取消监听器
     *
     * @param activity 页面实例
     * @return 返回停止状态
     */
    public boolean stop(Activity activity) {
        synchronized (AtlwMediaPlayUtils.class) {
            if (mediaPlayer != null && playing) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                //停止播放就取消注册监听
                unRegistProximitySensorListener(activity);
                //打印日志
                AtlwLogUtils.logI(TAG, "Play stop successful!");
                //回传状态
                playStop(true, nowPlayPath);
                return true;
            } else {
                AtlwLogUtils.logI(TAG, "Play stop failed!");
                //回传状态
                playStop(false, nowPlayPath);
                return false;
            }
        }
    }

    /**
     * 是否在播放
     *
     * @return 返回当前状态
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * 设置播放类型
     *
     * @param isUsePowerWakeLock      是否使用电源锁，默认不使用
     * @param isAllowChangeOutputType 是否允许切换输出硬件，默认不修改
     */
    public void setPlayState(boolean isUsePowerWakeLock, boolean isAllowChangeOutputType) {
        this.isUsePowerWakeLock = isUsePowerWakeLock;
        this.isAllowChangeOutputType = isAllowChangeOutputType;
    }


    /**
     * 获取距离传感器监听,同步锁住方法，使其仅会被初始化一次或者说只有没有的时候才会被初始化
     *
     * @return 距离传感器监听
     */
    private synchronized SensorEventListener getProximityListener(final Activity activity) {
        if (proximityListener == null) {
            proximityListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float[] values = event.values;
                    int type = event.sensor.getType();
                    if (type == Sensor.TYPE_PROXIMITY) {
                        //判断是否要允许修改电源锁
                        if (isUsePowerWakeLock) {
                            if (values[0] == 0.0) {
                                if (playing) {
                                    AtlwLogUtils.logI(TAG, "申请电源设备锁");
                                    AtlwMobileOptionsUtils.getInstance().applyForPowerLocalWakeLock(activity);
                                }
                            } else {
                                AtlwLogUtils.logI(TAG, "释放设备电源锁");
                                AtlwMobileOptionsUtils.getInstance().releasePowerLocalWakeLock(activity);
                            }
                        }
                        //判断是否允许修改输出设备
                        if (isAllowChangeOutputType) {
                            if (playing) {
                                if (values[0] == 0.0) {
                                    if (nowPlayOutputStreamType == STREAM_MUSIC) {
                                        AtlwLogUtils.logI(TAG, "贴近手机，切换到听筒播放");
                                        nowPlayOutputStreamType = STREAM_VOICE_CALL;
                                        AtlwMobileOptionsUtils.getInstance().useHandsetToPlay(activity);
                                        start(activity, nowPlayPath, AtlwMediaPlayOutputType.EARPIECE, true, true);
                                    }
                                } else {
                                    if (nowPlayOutputStreamType == STREAM_VOICE_CALL) {
                                        AtlwLogUtils.logI(TAG, "远离手机，切换到扬声器播放");
                                        nowPlayOutputStreamType = STREAM_MUSIC;
                                        AtlwMobileOptionsUtils.getInstance().useSpeakersToPlay(activity);
                                        start(activity, nowPlayPath, AtlwMediaPlayOutputType.SPEAKER, true, true);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
        return proximityListener;
    }

    /**
     * 注册距离传感器监听
     */
    private void registProximitySensorListener(Activity activity) {
        if (isUsePowerWakeLock || isAllowChangeOutputType) {
            AtlwMobileOptionsUtils.getInstance().registProximitySensorListener(activity, getProximityListener(activity));
        }
    }

    /**
     * 取消注册距离传感器监听
     */
    private void unRegistProximitySensorListener(Activity activity) {
        if (proximityListener != null) {
            AtlwMobileOptionsUtils.getInstance().unRegistProximitySensorListener(activity, getProximityListener(activity));
            proximityListener = null;
        }
    }

    /**
     * 回调播放状态
     *
     * @param isSuccess 是否成功
     */
    private void playStart(boolean isSuccess) {
        if (atlwMediaPlayCallback != null) {
            atlwMediaPlayCallback.playStart(isSuccess);
        }
    }

    /**
     * 播放结束
     */
    private void playEnd() {
        if (atlwMediaPlayCallback != null) {
            atlwMediaPlayCallback.playEnd();
        }
    }

    /**
     * 播放停止
     *
     * @param isSuccess   停止是否成功
     * @param nowPlayPath 当前被停止的播放地址
     */
    private void playStop(boolean isSuccess, String nowPlayPath) {
        if (atlwMediaPlayCallback != null) {
            atlwMediaPlayCallback.playStop(isSuccess, nowPlayPath);
        }
    }

}
