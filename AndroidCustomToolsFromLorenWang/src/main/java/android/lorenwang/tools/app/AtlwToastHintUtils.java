package android.lorenwang.tools.app;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-04-04 下午 17:49:41
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwToastHintUtils {
    private static AtlwToastHintUtils atlwToastHintUtils;

    /**
     * 私有构造方法
     */
    private AtlwToastHintUtils() {
    }

    public static AtlwToastHintUtils getInstance() {
        synchronized (AtlwToastHintUtils.class) {
            if (atlwToastHintUtils == null) {
                atlwToastHintUtils = new AtlwToastHintUtils();
            }
        }
        return atlwToastHintUtils;
    }

    private Toast allToast;//吐司提示弹窗，如果有下一个要弹出则隐藏上一个

    /**
     * 显示提示信息,默认短提示时间
     *
     * @param msg 提示文字
     */
    public void toastMsg(Context context, String msg) {
        toastMsg(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司提示信息,默认短提示时间
     *
     * @param msgResId 提示文字资源id
     */
    public void toastMsg(Context context, @StringRes int msgResId) {
        toastMsg(context, msgResId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示信息
     *
     * @param msg       提示文字
     * @param toastTime 提示时间,为空则使用默认短时间
     */
    public void toastMsg(Context context, String msg, Integer toastTime) {
        if (checkMsg(msg)) {
            if (allToast != null) {
                allToast.cancel();
            }
            allToast = Toast.makeText(context, msg, toastTime != null ? toastTime : Toast.LENGTH_SHORT);
            allToast.show();
        }
    }

    /**
     * 显示吐司提示信息
     *
     * @param msgResId  提示文字资源id
     * @param toastTime 提示时间，为空则使用默认短时间
     */
    public void toastMsg(Context context, @StringRes int msgResId, Integer toastTime) {
        if (allToast != null) {
            allToast.cancel();
        }
        allToast = Toast.makeText(context, msgResId, toastTime != null ? toastTime : Toast.LENGTH_SHORT);
        allToast.show();
    }

    /**
     * 检测msg消息是否为空
     *
     * @param msg
     * @return 不为空则返回true
     */
    private Boolean checkMsg(String msg) {
        return msg != null && !JtlwCheckVariateUtils.getInstance().isEmpty(msg);
    }

}
