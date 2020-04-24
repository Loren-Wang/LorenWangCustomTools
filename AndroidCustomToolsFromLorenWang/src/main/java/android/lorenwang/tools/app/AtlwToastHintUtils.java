package android.lorenwang.tools.app;

import android.lorenwang.tools.AtlwSetting;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-04-04 下午 17:49:41
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 1、提示自定义view消息
 * 2、显示提示信息
 * 3、设置弹窗参数，全局使用，以最后一次变更为准
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwToastHintUtils {
    private final String TAG = getClass().getName();
    private static volatile AtlwToastHintUtils optionsInstance;

    private AtlwToastHintUtils() {
    }

    public static AtlwToastHintUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwToastHintUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwToastHintUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 吐司提示弹窗，如果有下一个要弹出则隐藏上一个
     */
    private Toast allToast;
    private Long showTime;
    private Integer gravity;
    private Integer xOffset;
    private Integer yOffset;
    private Float horizontalMargin;
    private Float verticalMargin;
    /**
     * 隐藏吐司的runnable
     */
    private final Runnable hindToastRunnable = new Runnable() {
        @Override
        public void run() {
            if (allToast != null) {
                allToast.cancel();
            }
        }
    };

    /**
     * 提示自定义view消息
     *
     * @param customView 自定义view
     */
    public void toastView(View customView) {
        toastView(customView, showTime);
    }

    /**
     * 提示自定义view消息
     *
     * @param customView 自定义view
     * @param showTime   显示时间
     */
    public void toastView(View customView, Long showTime) {
        if (customView != null) {
            cancelToast();
            initToast("", true);
            try {
                allToast.setView(customView);
                allToast.show();
                sendHideMessageDelayed(showTime);
            } catch (Exception e) {
                cancelToast();
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @param msg 提示文字
     */
    public void toastMsg(String msg) {
        if (checkMsg(msg)) {
            toastMsg(msg, showTime);
        }
    }

    /**
     * 显示吐司提示信息
     *
     * @param msgResId 提示文字资源id
     */
    public void toastMsg(@StringRes int msgResId) {
        if (checkMsg(AtlwSetting.nowApplication.getApplicationContext().getString(msgResId))) {
            toastMsg(AtlwSetting.nowApplication.getApplicationContext().getString(msgResId));
        }
    }

    /**
     * 显示提示信息
     *
     * @param msg      提示文字
     * @param showTime 提示时间,为空则使用默认短时间
     */
    public void toastMsg(String msg, Long showTime) {
        if (checkMsg(msg)) {
            cancelToast();
            initToast(msg, false);
            allToast.show();
            sendHideMessageDelayed(showTime);
        }
    }

    /**
     * 显示吐司提示信息
     *
     * @param msgResId 提示文字资源id
     * @param showTime 提示时间，为空则使用默认短时间
     */
    public void toastMsg(@StringRes int msgResId, Long showTime) {
        if (checkMsg(AtlwSetting.nowApplication.getApplicationContext().getString(msgResId))) {
            toastMsg(AtlwSetting.nowApplication.getString(msgResId), showTime);
        }
    }

    /**
     * 设置弹窗参数，全局使用，以最后一次变更为准
     *
     * @param showTime         显示时间,最长不能超过7s
     * @param gravity          the location at which the notification should appear on the screen.
     * @param xOffset          X offset in pixels to apply to the gravity's location.
     * @param yOffset          Y offset in pixels to apply to the gravity's location.
     * @param horizontalMargin The horizontal margin, in percentage of the
     *                         container width, between the container's edges and the
     *                         notification
     * @param verticalMargin   The vertical margin, in percentage of the
     *                         container height, between the container's edges and the
     *                         notification
     */
    public void setParams(Long showTime, Integer gravity, Integer xOffset, Integer yOffset, Float horizontalMargin, Float verticalMargin) {
        this.showTime = showTime;
        this.gravity = gravity;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
    }


    /**
     * 检测msg消息是否为空
     *
     * @param msg 文本消息
     * @return 不为空则返回true
     */
    private Boolean checkMsg(String msg) {
        return msg != null && !JtlwCheckVariateUtils.getInstance().isEmpty(msg);
    }

    /**
     * 在指定时间之后发送隐藏消息，这样就不用通过反射处理TN类了
     *
     * @param time 间隔时间
     */
    private void sendHideMessageDelayed(Long time) {
        if (time != null) {
            AtlwThreadUtils.getInstance().postOnChildThreadDelayed(hindToastRunnable, time);
        }
    }

    /**
     * 取消吐司提示
     */
    private void cancelToast() {
        if (allToast != null) {
            allToast.cancel();
            //移除取消runnable
            AtlwThreadUtils.getInstance().getChildThreadHandler().removeCallbacks(hindToastRunnable);
        }
    }

    /**
     * 初始化吐司
     *
     * @param showText     显示文本
     * @param isCustomView 是否是自定义view
     */
    private void initToast(String showText, boolean isCustomView) {
        if (!isCustomView) {
            if (showText != null) {
                allToast = Toast.makeText(AtlwSetting.nowApplication, showText, Toast.LENGTH_LONG);
            } else {
                allToast = Toast.makeText(AtlwSetting.nowApplication, "", Toast.LENGTH_LONG);
            }
        } else {
            allToast = new Toast(AtlwSetting.nowApplication);
            allToast.setDuration(Toast.LENGTH_LONG);
        }
        //设置gravity
        if (gravity != null) {
            allToast.setGravity(gravity, xOffset == null ? 0 : xOffset, yOffset == null ? 0 : yOffset);
        }
        //设置margin
        allToast.setMargin(horizontalMargin == null ? 0f : horizontalMargin, verticalMargin == null ? 0f : verticalMargin);
    }


}
