package android.lorenwang.customview.video;

import android.content.Context;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwActivityUtil;

import com.aliyun.player.nativeclass.CacheConfig;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 功能作用：视频播放工具类
 * 创建时间：2020-09-25 11:09 上午
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
public class AvlwVideoPlayManager {
    private final String TAG = getClass().getName();
    private static volatile AvlwVideoPlayManager optionsInstance;
    private final Map<Context, ConcurrentHashMap<String, AvlwVideoPlayView>> playViewMap = new HashMap<>();

    /**
     * 视频缓存地址
     */
    private String videoCachePath;

    private AvlwVideoPlayManager() {
    }

    public static AvlwVideoPlayManager getInstance() {
        if (optionsInstance == null) {
            synchronized (AvlwVideoPlayManager.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AvlwVideoPlayManager();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 添加播放视图控件
     *
     * @param context       页面上下文
     * @param uid           视频播放存储使用的uid
     * @param videoPlayView 视频播放控件
     */
    public void addPlayVideoView(Context context, @NotNull String uid, AvlwVideoPlayView videoPlayView) {
        if (videoPlayView != null && context != null) {
            ConcurrentHashMap<String, AvlwVideoPlayView> map = playViewMap.get(context);
            if (map == null) {
                map = new ConcurrentHashMap<>();
            }
            map.put(uid, videoPlayView);
            playViewMap.put(context, map);
        }
    }

    /**
     * 移除相应商详问题view
     *
     * @param context 上下文
     */
    public void removePlayVideoViews(Context context) {
        //移除Activity下的fragment页面的视频播放
        if (context instanceof AppCompatActivity) {
            for (androidx.fragment.app.Fragment fragment : ((AppCompatActivity) context).getSupportFragmentManager().getFragments()) {
                removePlayVideoViews(fragment.getContext());
            }
        }
        if (context != null) {
            ConcurrentHashMap<String, AvlwVideoPlayView> map = playViewMap.get(context);
            if (map != null) {
                //遍历移除数据
                Iterator<AvlwVideoPlayView> iterator = map.values().iterator();
                AvlwVideoPlayView view;
                while (iterator.hasNext()) {
                    view = iterator.next();
                    if (view != null) {
                        view.stopPlay();
                    }
                    iterator.remove();
                }
                playViewMap.remove(context);
            }
        }
    }

    /**
     * 暂停视频播放
     *
     * @param context     上下文
     * @param noPauseView 不暂停的控件
     */
    public void pausePlayVideoViews(Context context, AvlwVideoPlayView noPauseView) {
        //暂停Activity下的fragment页面的视频播放
        if (context instanceof AppCompatActivity) {
            for (androidx.fragment.app.Fragment fragment : ((AppCompatActivity) context).getSupportFragmentManager().getFragments()) {
                pausePlayVideoViews(fragment.getContext(), noPauseView);
            }
        }
        if (context != null) {
            ConcurrentHashMap<String, AvlwVideoPlayView> map = playViewMap.get(context);
            if (map != null) {
                //遍历暂停数据
                Iterator<AvlwVideoPlayView> iterator = map.values().iterator();
                AvlwVideoPlayView view;
                while (iterator.hasNext()) {
                    view = iterator.next();
                    //判断该控件是否为暂停控件
                    if (view != null && (noPauseView == null || view != noPauseView)) {
                        view.pausePlay();
                    }
                }
            }
        }
    }

    /**
     * 设置视频缓存地址
     *
     * @param videoCachePath 视频缓存地址
     */
    public void setVideoCachePath(String videoCachePath) {
        this.videoCachePath = videoCachePath;
    }

    /**
     * 获取视频播放缓存配置
     *
     * @return 视频播放缓存配置
     */
    public CacheConfig getVideoPlayCacheConfig() {
        //设置边播边缓存
        CacheConfig cacheConfig = new CacheConfig();
        //开启缓存功能
        cacheConfig.mEnable = true;
        //能够缓存的单个文件最大时长。超过此长度则不缓存
        cacheConfig.mMaxDurationS = 100;
        //缓存目录的位置
        if (videoCachePath == null || videoCachePath.isEmpty()) {
            if (AtlwActivityUtil.getInstance().getApplicationContext(AtlwConfig.nowApplication).getExternalFilesDir("videoCache") != null) {
                AvlwVideoPlayManager.getInstance().setVideoCachePath(Objects.requireNonNull(
                        AtlwActivityUtil.getInstance().getApplicationContext(AtlwConfig.nowApplication).getExternalFilesDir("videoCache"))
                        .getAbsolutePath());
            }
        }
        cacheConfig.mDir = videoCachePath;
        //缓存目录的最大大小。超过此大小，将会删除最旧的缓存文件
        cacheConfig.mMaxSizeMB = 200;
        return cacheConfig;
    }

    /**
     * 获取指定的uid所标记的播放视图
     *
     * @param context 当前页面上下文
     * @param uid     uid标记
     * @return 如果有找到返回，没有找到返回null
     */
    public AvlwVideoPlayView getUidVideoPlayView(@NotNull Context context, @NotNull String uid) {
        ConcurrentHashMap<String, AvlwVideoPlayView> map = playViewMap.get(context);
        if (map != null) {
            for (String key : map.keySet()) {
                if (key != null && key.equals(uid)) {
                    return map.get(key);
                }
            }
        }
        return null;
    }
}
