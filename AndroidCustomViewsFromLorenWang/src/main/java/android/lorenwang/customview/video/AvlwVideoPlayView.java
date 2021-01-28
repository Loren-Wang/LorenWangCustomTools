package android.lorenwang.customview.video;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.lorenwang.tools.app.AtlwActivityUtil;
import android.lorenwang.tools.app.AtlwPermissionRequestCallback;
import android.lorenwang.tools.mobile.AtlwMobileSystemInfoUtil;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.source.UrlSource;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.FloatRange;

/**
 * 功能作用：视频播放控件
 * 创建时间：2020-09-02 6:11 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AvlwVideoPlayView extends SurfaceView {
    private final String TAG = "AvlwVideoPlayView:";
    /**
     * 是否允许播放
     */
    private boolean allowPlay = false;
    /**
     * 是否显示第一帧
     */
    private boolean showFirstPreview = false;
    /**
     * 播放状态
     */
    private int videoState = IPlayer.unknow;

    /**
     * 视频播放器
     */
    private AliPlayer videoPlayer;
    /**
     * 播放地址
     */
    private String playUrl = null;
    /**
     * 播放回调
     */
    private PlayCallback playCallback;

    /**
     * 修改播放状态的点击事件
     */
    private final OnClickListener changePlayStateOnClick = v -> {
        if (videoState == IPlayer.prepared || videoState == IPlayer.paused) {
            allowPlay = true;
            startPlay();
        } else if (videoState == IPlayer.started) {
            pausePlay();
        } else if (videoState == IPlayer.stopped || videoState == IPlayer.completion) {
            reset();
            startPlay();
        }
    };

    public AvlwVideoPlayView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwVideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwVideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        videoPlayer = AliPlayerFactory.createAliPlayer(context.getApplicationContext());
        //设置显示view
        this.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                videoPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                videoPlayer.redraw();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                videoPlayer.setDisplay(null);
            }
        });
        //设置缓存配置给到播放器
        videoPlayer.setCacheConfig(AvlwVideoPlayManager.getInstance().getVideoPlayCacheConfig());
        //监听处理
        initAllListener();
    }

    /**
     * 初始化所有监听
     */
    private void initAllListener() {
        //准备成功事件
        videoPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                if (playCallback != null) {
                    playCallback.playReadyFinish();
                }
                allowPlay = true;
                //准备成功判定是否要显示首帧
                if (showFirstPreview) {
                    startPlay();
                }
            }
        });
        //首帧渲染显示事件
        videoPlayer.setOnRenderingStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                if (showFirstPreview) {
                    pausePlay();
                    showFirstPreview = false;
                }
            }
        });
        //播放完成事件
        videoPlayer.setOnCompletionListener(this::videoPlayFinish);
        //其他状态监听
        videoPlayer.setOnInfoListener(infoBean -> {
            //其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
            if (infoBean != null) {
                switch (infoBean.getCode()) {
                    //循环播放开始事件
                    case LoopingStart:
                        if (playCallback != null && !showFirstPreview) {
                            playCallback.playStart(true, false);
                        }
                    case AutoPlayStart:
                        if (playCallback != null && !showFirstPreview) {
                            playCallback.playStart(false, true);
                        }
                        break;
                    case CurrentPosition:
                        //设置进度
                        if (playCallback != null && !showFirstPreview) {
                            playCallback.progressChange(infoBean.getExtraValue(), infoBean.getExtraValue() * 1.0F / videoPlayer.getDuration());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        //状态监听
        videoPlayer.setOnStateChangedListener(i -> this.videoState = i);
    }

    /**
     * 设置准备播放
     *
     * @param playUrl          播放地址
     * @param wifiAutoPlay     wifi情况下是否自动播放
     * @param showFirstPreview 是否显示第一帧
     * @param loopPlay         是否循环播放
     */
    public void setReadyPlay(@NotNull String playUrl,@NotNull String uid, boolean loopPlay, boolean showFirstPreview, boolean wifiAutoPlay) {
        this.showFirstPreview = showFirstPreview;
        if (this.playUrl == null) {
            //添加播放控件记录
            AvlwVideoPlayManager.getInstance().addPlayVideoView(getContext(),uid, this);
            this.playUrl = playUrl;
            UrlSource urlSource = new UrlSource();
            urlSource.setUri(playUrl);
            urlSource.setTitle(" ");
            //设置播放源
            videoPlayer.setDataSource(urlSource);
            //准备播放
            videoPlayer.prepare();
        }
        //wifi是否自动播放处理
        if (wifiAutoPlay) {
            //自动播放逻辑处理
            if (getContext() instanceof Activity) {
                AtlwActivityUtil.getInstance().goToRequestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, hashCode() % 1000, new AtlwPermissionRequestCallback() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void permissionRequestSuccessCallback(List<String> permissionList, int permissionsRequestCode) {
                                //自动播放视频逻辑处理，wifi下自动播放
                                videoPlayer.setAutoPlay(allowPlay = AtlwMobileSystemInfoUtil.getNetworkType() == 1);
                            }

                            @Override
                            public void permissionRequestFailCallback(List<String> permissionList, int permissionsRequestCode) {

                            }
                        });
            }
        }
        //循环播放判断处理
        videoPlayer.setLoop(loopPlay);
    }

    /**
     * 开始播放
     */
    public void startPlay() {
        if (allowPlay) {
            videoPlayer.start();
        }
        if (!showFirstPreview) {
            //开始播放需要暂停该页面其他正在播放的视频播放器,并且跳过当前控件
            AvlwVideoPlayManager.getInstance().pausePlayVideoViews(getContext(), this);
            if (playCallback != null) {
                playCallback.playStart(false, false);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {
        videoPlayer.pause();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        videoPlayer.stop();
    }

    /**
     * 播放重置
     */
    public void reset() {
        videoPlayer.reset();
        videoPlayer.seekTo(0);
    }


    /**
     * 设置是否允许播放
     *
     * @param allowPlay true为允许
     */
    public void setAllowPlay(boolean allowPlay) {
        this.allowPlay = allowPlay;
    }

    /**
     * 是否允许播放
     *
     * @return true 允许播放
     */
    public boolean isAllowPlay() {
        return allowPlay;
    }


    /**
     * 是否正在播放
     */
    public boolean isPlay() {
        return videoState == IPlayer.started;
    }

    /**
     * 设置静音状态
     *
     * @param state true为静音
     */
    public void setMuteState(boolean state) {
        videoPlayer.setMute(state);
        if (playCallback != null) {
            playCallback.muteStateChange(state);
        }
    }

    /**
     * 设置音量
     *
     * @param volume 音量，范围0-1
     */
    public void setVolume(@FloatRange(from = 0F, to = 1F) float volume) {
        //设置播放器音量,范围0~1.
        videoPlayer.setVolume(volume);
    }

    /**
     * 跳转到指定时间
     */
    public void seekTo(long time) {
        videoPlayer.seekTo(time);
    }

    /**
     * 设置播放回调
     *
     * @param playCallback 播放回调
     */
    public void setPlayCallback(PlayCallback playCallback) {
        this.playCallback = playCallback;
    }

    /**
     * 播放状态改变点击事件
     *
     * @return 播放状态改变点击事件
     */
    public OnClickListener getChangePlayStateOnClick() {
        return changePlayStateOnClick;
    }

    /**
     * 播放回调
     */
    public abstract static class PlayCallback {
        /**
         * 循环播放开始
         */
        public void loopPlayStart() {}

        /**
         * 播放准备完成
         */
        public void playReadyFinish() {}

        /**
         * 播放开始
         *
         * @param isLoopPlay 是否是循环播放开始
         * @param isAutoPlay 是否是自动播放开始
         */
        public void playStart(boolean isLoopPlay, boolean isAutoPlay) {}

        /**
         * 播放完成
         */
        public void playFinish() {}

        /**
         * 静音状态改变
         *
         * @param state 当前状态,true为静音
         */
        public void muteStateChange(boolean state) {}

        /**
         * 回调进度
         *
         * @param currentTime 进度时间，毫秒值
         * @param progresss   进度
         */
        public void progressChange(long currentTime, float progresss) {}
    }

    /**
     * 视频播放结束
     */
    private void videoPlayFinish() {
        //播放完成事件
        if (playCallback != null) {
            playCallback.playFinish();
        }
        //设置完成状态
        this.videoState = IPlayer.completion;
        //重置播放器
        reset();
    }

}
