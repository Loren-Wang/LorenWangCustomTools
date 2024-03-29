package android.lorenwang.tools.desktopShortcut;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageUtil;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import javabase.lorenwang.tools.common.JtlwCommonUtil;

import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.DESKTOP_SHORTCUT_CREATE_SUCCESS;

/**
 * 功能作用：桌面快捷方式
 * 初始注释时间： 2021/9/17 13:54
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 添加桌面快捷方式--addDesktopShortcut(context,openClass,openUrlKey,title,url,bitmap)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwDesktopShortcutUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwDesktopShortcutUtil optionsInstance;

    private AtlwDesktopShortcutUtil() {
    }

    public static AtlwDesktopShortcutUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwDesktopShortcutUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwDesktopShortcutUtil();
                }
            }
        }
        return optionsInstance;
    }

    private DesktopShortcutOptionsCallback desktopShortcutOptionsCallback;

    /**
     * 设置回调
     *
     * @param desktopShortcutOptionsCallback 回调
     */
    public void setDesktopShortcutOptionsCallback(DesktopShortcutOptionsCallback desktopShortcutOptionsCallback) {
        this.desktopShortcutOptionsCallback = desktopShortcutOptionsCallback;
    }

    /**
     * 添加桌面快捷方式
     *
     * @param context    上下文
     * @param openClass  打开的class
     * @param openUrlKey 打开url的key
     * @param title      标题
     * @param url        链接
     * @param bitmap     icon位图
     */
    public <T> void addDesktopShortcut(Context context, Class<T> openClass, String openUrlKey, String title, String url, Bitmap bitmap) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Intent intent = new Intent(AtlwConfig.nowApplication, openClass);
            intent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错
            intent.putExtra(openUrlKey, url);
            Bundle bundle = new Bundle();
            bundle.putString(openUrlKey, url);
            intent.putExtras(bundle);

            //设置快捷方式信息
            ShortcutInfoCompat shortcutInfoCompat = new ShortcutInfoCompat.Builder(context, JtlwCommonUtil.getInstance().generateUuid(true)).setIcon(
                    IconCompat.createWithBitmap(bitmap)).setShortLabel(title).setIntent(intent).build();
            //快捷方式添加回调
            PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, DesktopShortcutReceiver.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);


            //请求添加快捷方式
            if (ShortcutManagerCompat.requestPinShortcut(context, shortcutInfoCompat, shortcutCallbackIntent.getIntentSender())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //创建消息接收监听
                    AtlwFlyMessageUtil.getInstance().registerMsgCallback(context, DESKTOP_SHORTCUT_CREATE_SUCCESS, (msgType, msgs) -> {
                        AtlwLogUtil.logUtils.logI(TAG, "快捷方式添加主屏幕成功");
                        if (desktopShortcutOptionsCallback != null) {
                            desktopShortcutOptionsCallback.addSuccess();
                        }
                    }, true, context instanceof Activity);
                }
            }
        }
    }

}
