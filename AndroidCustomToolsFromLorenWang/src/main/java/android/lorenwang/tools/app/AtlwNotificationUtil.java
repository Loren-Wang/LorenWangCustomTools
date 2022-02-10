package android.lorenwang.tools.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.lorenwang.tools.AtlwConfig;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：通知工具类
 * 初始注释时间： 2021/5/28 15:57
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 创建通知渠道--createNotificationChannel(channelId,name,desc,importance)
 * 设置进度--setProgress(channelId,notificationId,title,content,smallIconRes,maxProgress,currentProgress,intent)
 * 移除指定的通知--removeNotification(notificationId)
 * 移除全部的通知--removeAllNotification()
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwNotificationUtil {
    private static volatile AtlwNotificationUtil optionsInstance;

    private AtlwNotificationUtil() {
    }

    public static AtlwNotificationUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwNotificationUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwNotificationUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 创建通知渠道
     *
     * @param channelId  渠道id
     * @param name       渠道名称
     * @param desc       渠道描述
     * @param importance 渠道级别
     *                   {@link NotificationManager#IMPORTANCE_HIGH}紧急：发出提示音，并以浮动通知的形式显示
     *                   {@link NotificationManager#IMPORTANCE_DEFAULT}高：发出提示音
     *                   {@link NotificationManager#IMPORTANCE_LOW}中：无提示音
     *                   {@link NotificationManager#IMPORTANCE_MIN}低：无提示音，且不会在状态栏中显示。
     * @return 渠道id
     */
    public String createNotificationChannel(String channelId, String name, String desc, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && JtlwCheckVariateUtil.getInstance().isNotEmpty(channelId) &&
                JtlwCheckVariateUtil.getInstance().isNotEmpty(name)) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(desc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = AtlwConfig.nowApplication.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return channelId;
    }

    /**
     * 设置进度
     *
     * @param channelId       渠道id
     * @param notificationId  通知id
     * @param title           标题
     * @param content         内容
     * @param smallIconRes    小图标
     * @param maxProgress     最大进度
     * @param currentProgress 当前进度
     * @param intent          跳转操作
     */
    public void setProgress(String channelId, int notificationId, String title, String content, @DrawableRes int smallIconRes, int maxProgress,
            int currentProgress, PendingIntent intent) {
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(channelId) && JtlwCheckVariateUtil.getInstance().isNotEmpty(title) &&
                JtlwCheckVariateUtil.getInstance().isNotEmpty(content)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AtlwConfig.nowApplication);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(AtlwConfig.nowApplication, channelId);
            //设置基础信息
            builder.setContentTitle(title).setContentText(content).setSmallIcon(smallIconRes).setPriority(NotificationCompat.PRIORITY_LOW);
            //设置进度
            builder.setProgress(maxProgress, Math.min(maxProgress, currentProgress), false);
            //仅响一次
            builder.setOnlyAlertOnce(true);
            //设置点击打开相关intent操作
            if (intent != null) {
                builder.setContentIntent(intent);
            }
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //取消右上角的时间显示
            builder.setShowWhen(false);
            //普通显示处理
            notificationManager.notify(notificationId, builder.build());

            //显示完成处理
            if (maxProgress == Math.min(maxProgress, currentProgress)) {
                builder.setProgress(0, 0, false);
                notificationManager.notify(notificationId, builder.build());
            }
        }
    }

    /**
     * 移除指定的通知
     *
     * @param notificationId 指定通知id
     */
    public void removeNotification(int notificationId) {
        NotificationManagerCompat.from(AtlwConfig.nowApplication).cancel(notificationId);
    }

    /**
     * 移除全部的通知
     */
    public void removeAllNotification() {
        NotificationManagerCompat.from(AtlwConfig.nowApplication).cancelAll();
    }
}
