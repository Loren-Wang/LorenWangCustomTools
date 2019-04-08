package android.lorenwang.tools.app;

import android.database.ContentObserver;
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

public class AdBrightnessChangeContentObserver extends ContentObserver {
    private boolean isRegist = false;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public AdBrightnessChangeContentObserver(Handler handler) {
        super(handler);
    }

    public boolean isRegist() {
        return isRegist;
    }

    public void setRegist(boolean regist) {
        isRegist = regist;
    }
}
