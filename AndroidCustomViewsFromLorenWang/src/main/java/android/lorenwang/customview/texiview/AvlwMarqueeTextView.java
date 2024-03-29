package android.lorenwang.customview.texiview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 功能作用：跑马灯效果textView
 * 创建时间：2020-11-09 4:06 下午
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
public class AvlwMarqueeTextView extends AppCompatTextView {
    public AvlwMarqueeTextView(Context context) {
        super(context);
        initConfig();
    }

    public AvlwMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }

    public AvlwMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig();
    }

    private void initConfig() {
        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(Integer.MAX_VALUE);
        setSingleLine();
        setHorizontallyScrolling(true);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
