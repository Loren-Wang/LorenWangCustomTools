package android.lorenwang.tools.app;

import android.lorenwang.tools.AtlwConfig;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：吐司弹窗工具类
 * 初始注释时间： 2021/9/17 11:02
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 提示自定义view消息--toastView(customView)
 * 提示自定义view消息--toastView(customView,showTime)
 * 显示提示信息--toastMsg(msg)
 * 显示吐司提示信息--toastMsg(msgResId)
 * 显示提示信息--toastMsg(msg,showTime)
 * 显示吐司提示信息--toastMsg(msgResId,showTime)
 * 设置弹窗参数，全局使用，以最后一次变更为准--setParams(showTime,gravity,xOffset,yOffset,horizontalMargin,verticalMargin)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwToastHintUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwToastHintUtil optionsInstance;

    private AtlwToastHintUtil() {
    }

    public static AtlwToastHintUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwToastHintUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwToastHintUtil();
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
        if (checkMsg(AtlwConfig.nowApplication.getApplicationContext().getString(msgResId))) {
            toastMsg(AtlwConfig.nowApplication.getApplicationContext().getString(msgResId));
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
        if (checkMsg(AtlwConfig.nowApplication.getApplicationContext().getString(msgResId))) {
            toastMsg(AtlwConfig.nowApplication.getString(msgResId), showTime);
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
            AtlwThreadUtil.getInstance().postOnChildThreadDelayed(hindToastRunnable, time);
        }
    }

    /**
     * 取消吐司提示
     */
    private void cancelToast() {
        if (allToast != null) {
            allToast.cancel();
            //移除取消runnable
            AtlwThreadUtil.getInstance().getChildThreadHandler().removeCallbacks(hindToastRunnable);
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
                allToast = Toast.makeText(AtlwConfig.nowApplication, showText, Toast.LENGTH_LONG);
            } else {
                allToast = Toast.makeText(AtlwConfig.nowApplication, "", Toast.LENGTH_LONG);
            }
        } else {
            allToast = new Toast(AtlwConfig.nowApplication);
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
