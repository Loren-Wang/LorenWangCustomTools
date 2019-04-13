package android.lorenwang.tools.app;

import android.app.Activity;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * 创建时间：2019-04-05 下午 21:51:11
 * 创建人：王亮（Loren wang）
 * 功能作用：亮度改变的ContentObserver
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class AtlwBrightnessChangeContentObserver extends ContentObserver {
    private boolean isRegist = false;
    private Activity activity;

    /**
     * Creates a content observer.
     */
    public AtlwBrightnessChangeContentObserver(Activity activity) {
        super(new Handler());
        this.activity = activity;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //如果是Activity自动模式才会将系统的亮度调用返回
        if (activity.getWindow().getAttributes().screenBrightness < 0) {
            onBrightnessChange(AtlwBrightnessChangeUtils.getInstance().getScreenBrightness());
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        //如果是Activity自动模式才会将系统的亮度调用返回
        if (activity.getWindow().getAttributes().screenBrightness < 0) {
            onBrightnessChange(AtlwBrightnessChangeUtils.getInstance().getScreenBrightness());
        }
    }

    /**
     * 亮度改变
     *
     * @param brightness
     */
    public abstract void onBrightnessChange(Float brightness);

    public boolean isRegist() {
        return isRegist;
    }

    public void setRegist(boolean regist) {
        isRegist = regist;
    }
}
