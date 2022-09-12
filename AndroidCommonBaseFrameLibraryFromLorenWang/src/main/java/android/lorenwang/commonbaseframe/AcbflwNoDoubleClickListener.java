package android.lorenwang.commonbaseframe;

import android.lorenwang.tools.app.AtlwToastHintUtil;
import android.text.TextUtils;
import android.view.View;

import java.util.Calendar;

/**
 * 功能作用：防止二次点击的点击事件
 * 初始注释时间： 2022/9/12 13:54
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
public abstract class AcbflwNoDoubleClickListener implements View.OnClickListener {
    private final long delayTime;
    private long lastClickTime = 0;
    private final String msg;

    /**
     * @param delayTime 延迟时间
     * @param msg       需要提示的字符，如果不需要提示传null
     */
    public AcbflwNoDoubleClickListener(long delayTime, String msg) {
        this.delayTime = delayTime;
        this.msg = msg;
    }

    public AcbflwNoDoubleClickListener(long delayTime) {
        this(delayTime, null);
    }


    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > delayTime) {
            onNoDoubleClick(v);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                AtlwToastHintUtil.getInstance().toastMsg(msg);
            }
        }
        lastClickTime = currentTime;
    }

    public abstract void onNoDoubleClick(View view);
}
