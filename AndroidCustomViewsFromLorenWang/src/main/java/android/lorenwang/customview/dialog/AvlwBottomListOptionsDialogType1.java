package android.lorenwang.customview.dialog;

import android.content.Context;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;


/**
 * 创建时间：2018-11-16 下午 15:10:8
 * 创建人：王亮（Loren wang）
 * 功能作用：加载中弹窗
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwBottomListOptionsDialogType1 extends AvlwBaseDialog {

    private AvlwOptionsItemClickListener onOptionsItemClick;

    public AvlwBottomListOptionsDialogType1(Context context) {
        super(context, R.layout.avlw_dialog_bottom_list_options_type_1
                , R.style.avlw_dialog_bottom_list_options_type_1, R.style.avlw_dialog_anim_for_bottom, true, true, false);
    }

    /**
     * 设置操作列表
     *
     * @param strings    操作列表字符纯
     * @param textSize   文本大小
     * @param textColor  文本颜色
     * @param textHeight 文本高度
     * @param lineColor  下划线颜色
     * @param lineHeight 下划线高度
     */
    public void setOptionsList(final String[] strings, Integer textSize, Integer textColor, Integer textHeight, Integer lineColor, Integer lineWidth, Integer lineHeight) {
        if (strings != null && strings.length > 0) {
            LinearLayout linearLayout = view.findViewById(R.id.lnOptions);
            linearLayout.removeAllViews();
            AppCompatButton button;
            View lineView;
            int length = strings.length;
            for (int i = 0; i < length; i++) {
                button = new AppCompatButton(getContext());
                button.setText(strings[i]);
                button.setTextSize(textSize == null ? Color.BLACK : 20);
                button.setTextColor(textColor == null ? Color.BLACK : textColor);
                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, textHeight != null ? textHeight : 200));
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setAllCaps(false);
                button.setMinHeight(0);
                button.setGravity(Gravity.CENTER);
                linearLayout.addView(button);
                lineView = new View(getContext());
                lineView.setLayoutParams(new LinearLayout.LayoutParams(lineWidth != null ? lineWidth : ViewGroup.LayoutParams.MATCH_PARENT, lineHeight != null ? lineHeight : 0));
                lineView.setBackgroundColor(lineColor == null ? Color.BLACK : lineColor);
                linearLayout.addView(lineView);
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onOptionsItemClick != null) {
                            onOptionsItemClick.onOptionsItemClick(finalI, strings[finalI]);
                        }
                        dismiss();
                    }
                });
            }
        }
    }

    public void setOnOptionsItemClick(AvlwOptionsItemClickListener onOptionsItemClick) {
        this.onOptionsItemClick = onOptionsItemClick;
    }

    @Override
    public void onBackPressed() {
        if (isShowing()) {
            dismiss();
        } else {
            super.onBackPressed();
        }
    }

}
