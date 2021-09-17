package android.lorenwang.tools.app;

import android.lorenwang.tools.bean.AtlwSpannableClickBean;
import android.lorenwang.tools.bean.AvlwSpannableTagBean;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;

/**
 * 功能作用：Spannable文本处理
 * 创建时间：2021-03-01 15:21
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 格式化点击消息数据--paramsClickMessage(msg, AtlwSpannableClickBean... dataBeans)
 * 格式化点击消息数据--paramsClickMessage(msg, ArrayList<AtlwSpannableClickBean> dataBeans)
 * 格式化消息数据--paramsTagMessage(msg, AvlwSpannableTagBean... dataBeans)
 * 格式化消息数据--paramsTagMessage(msg, ArrayList<AvlwSpannableTagBean> dataBeans)
 * textview设置字符串，保证spannable的click生效--setSpannableString(textView, SpannableString string)
 * textview设置字符串，保证spannable的click生效--setSpannableString(textView, SpannableStringBuilder string)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwSpannableUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwSpannableUtil optionsInstance;

    private AtlwSpannableUtil() {
    }

    public static AtlwSpannableUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwSpannableUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwSpannableUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 格式化点击消息数据
     *
     * @param msg       消息数据
     * @param dataBeans 数据要格式化实例
     * @return 格式化后数据
     */
    public SpannableString paramsClickMessage(@NonNull String msg, AtlwSpannableClickBean... dataBeans) {
        if (dataBeans != null) {
            ArrayList<AtlwSpannableClickBean> list = new ArrayList<>(dataBeans.length);
            Collections.addAll(list, dataBeans);
            return paramsClickMessage(msg, list);
        } else {
            return paramsClickMessage(msg, new ArrayList<AtlwSpannableClickBean>(0));
        }
    }

    /**
     * 格式化点击消息数据
     *
     * @param msg       消息数据
     * @param dataBeans 数据要格式化实例
     * @return 格式化后数据
     */
    public SpannableString paramsClickMessage(@NonNull String msg, ArrayList<AtlwSpannableClickBean> dataBeans) {
        SpannableString spannableString = new SpannableString(msg);
        int index = -1;
        for (AtlwSpannableClickBean dataBean : dataBeans) {
            //从上一个位置开始查起
            index = msg.indexOf(dataBean.getParamsMsg(), index);
            if (index >= 0 && dataBean.getColor() != null) {
                //设置数据
                spannableString.setSpan(dataBean, index, index + dataBean.getParamsMsg().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //更新位置坐标
            if (index >= 0) {
                index = index + dataBean.getParamsMsg().length();
            }
        }
        return spannableString;
    }

    /**
     * 格式化消息数据
     *
     * @param msg       消息数据
     * @param dataBeans 数据要格式化实例
     * @return 格式化后数据
     */
    public SpannableString paramsTagMessage(@NonNull String msg, AvlwSpannableTagBean... dataBeans) {
        if (dataBeans != null) {
            ArrayList<AvlwSpannableTagBean> list = new ArrayList<>(dataBeans.length);
            Collections.addAll(list, dataBeans);
            return paramsTagMessage(msg, list);
        } else {
            return paramsTagMessage(msg, new ArrayList<AvlwSpannableTagBean>(0));
        }
    }

    /**
     * 格式化消息数据
     *
     * @param msg       消息数据
     * @param dataBeans 数据要格式化实例
     * @return 格式化后数据
     */
    public SpannableString paramsTagMessage(@NonNull String msg, @NonNull ArrayList<AvlwSpannableTagBean> dataBeans) {
        SpannableString spannableString = new SpannableString(msg);
        int index = -1;
        for (AvlwSpannableTagBean dataBean : dataBeans) {
            //从上一个位置开始查起
            index = msg.indexOf(dataBean.getParamsMsg(), index);
            if (index >= 0 && dataBean.getBgColor() != null && dataBean.getTextColor() != null) {
                //设置数据
                spannableString.setSpan(dataBean, index, index + dataBean.getParamsMsg().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //更新位置坐标
            if (index >= 0) {
                index = index + dataBean.getParamsMsg().length();
            }
        }
        return spannableString;
    }

    /**
     * textview设置字符串，保证spannable的click生效
     * 参考链接：Link{https://www.jianshu.com/p/d4349a4ca216}
     *
     * @param textView 控件
     * @param string   字符串
     */
    public void setSpannableString(@NotNull TextView textView, @NotNull SpannableString string) {
        textView.setText(string);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * textview设置字符串，保证spannable的click生效
     * 参考链接：Link{https://www.jianshu.com/p/d4349a4ca216}
     *
     * @param textView 控件
     * @param string   字符串
     */
    public void setSpannableString(@NotNull TextView textView, @NotNull SpannableStringBuilder string) {
        textView.setText(string);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
