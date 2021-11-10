package android.lorenwang.customview.texiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.lorenwang.customview.R;
import android.util.AttributeSet;

import java.text.MessageFormat;
import java.util.Date;

import androidx.appcompat.widget.AppCompatTextView;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;
import javabase.lorenwang.tools.common.JtlwDateTimeUtil;

/**
 * 功能作用：时间显示控件
 * 创建时间：2020-07-17 5:32 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
public class AvlwTimeShowTextView extends AppCompatTextView {
    /**
     * 时间的格式起始值的格式类型
     */
    private String formatPatternStart = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * 时间的格式结束值的格式类型
     */
    private String formatPatternEnd;

    /**
     * 显示文本的模板，替换的位置使用{位置}做标记，例：{0}-{1}范围内有降雨
     */
    private String showTextTemplate;

    public AvlwTimeShowTextView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwTimeShowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwTimeShowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwTimeShowTextView);
        String pattern = attributes.getString(R.styleable.AvlwTimeShowTextView_avlw_tst_formatPatternStart);
        if (pattern != null) {
            formatPatternStart = pattern;
        }
        formatPatternEnd = attributes.getString(R.styleable.AvlwTimeShowTextView_avlw_tst_formatPatternEnd);
        showTextTemplate = attributes.getString(R.styleable.AvlwTimeShowTextView_avlw_tst_showTextTemplate);
        attributes.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    /**
     * 设置显示时间
     *
     * @param date 时间
     */
    public void setShowTime(Date date) {
        setShowTime(date, null);
    }

    /**
     * 设置显示时间
     *
     * @param time 13位时间戳
     */
    public void setShowTime(Long time) {
        if (time != null) {
            setShowTime(new Date(time), null);
        }
    }

    /**
     * 设置时间范围
     *
     * @param statTime 起始时间
     * @param endTime  结束时间
     */
    public void setShowTime(Long statTime, Long endTime) {
        if (statTime != null && endTime != null) {
            setShowTime(new Date(statTime), new Date(endTime));
        }
    }

    /**
     * 设置显示的格式化字符
     *
     * @param showTextTemplate 格式化时间后要再次格式显示的字符模板
     */
    public void setShowTextTemplate(String showTextTemplate) {
        this.showTextTemplate = showTextTemplate;
    }

    /**
     * 设置开始的格式化字符串
     *
     * @param formatPatternStart 解析格式
     */
    public void setFormatPatternStart(String formatPatternStart) {
        this.formatPatternStart = formatPatternStart;
    }

    /**
     * 设置结束的格式化字符串
     *
     * @param formatPatternEnd 解析格式
     */
    public void setFormatPatternEnd(String formatPatternEnd) {
        this.formatPatternEnd = formatPatternEnd;
    }

    /**
     * 设置时间范围
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    public void setShowTime(Date startTime, Date endTime) {
        if (startTime == null && endTime == null) {
            return;
        }
        String start = "";
        String end = "";
        if (startTime != null) {
            start = JtlwDateTimeUtil.getInstance().getFormatDateTime(formatPatternStart,
                    startTime);
            if (start == null) {
                start = "";
            }
        }
        if (endTime != null) {
            end = JtlwDateTimeUtil.getInstance().getFormatDateTime(formatPatternEnd, endTime);
            if (end == null) {
                end = "";
            }
        }

        //设置文本内容
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(showTextTemplate)) {
            setText(MessageFormat.format(showTextTemplate, start, end));
        } else {
            setText(start);
            append(end);
        }
    }
}
