package android.lorenwang.customview.dialog;

import android.content.Context;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


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
public abstract class BottomListOptionsDialogType1 extends BaseDialog {

    public BottomListOptionsDialogType1(Context context) {
        super(context, R.layout.dialog_bottom_list_options_type_1,R.style.dialog_bottom_list_options_type_1,R.style.dialog_anim_for_bottom,true);
    }

    /**
     * 设置操作列表
     * @param strings
     * @param textSize
     * @param textColor
     * @param textHeight
     * @param lineColor
     * @param lineHeight
     */
    public void setOptionsList(final String[] strings, Integer textSize, Integer textColor, Integer textHeight, Integer lineColor, Integer lineWidth, Integer lineHeight){
        if(strings != null && strings.length > 0){
            LinearLayout linearLayout = view.findViewById(R.id.lnOptions);
            linearLayout.removeAllViews();
            Button button;
            View lineView;
            int length = strings.length;
            for(int i = 0; i < length ; i++){
                button = new Button(getContext());
                button.setText(strings[i]);
                button.setTextSize(textSize == null ? Color.BLACK : 20);
                button.setTextColor(textColor == null ? Color.BLACK : textColor);
                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,textHeight != null ? textHeight : 200));
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setAllCaps(false);
                button.setGravity(Gravity.CENTER);
                linearLayout.addView(button);
                lineView = new View(getContext());
                lineView.setLayoutParams(new LinearLayout.LayoutParams(lineWidth != null ? lineWidth : ViewGroup.LayoutParams.MATCH_PARENT,lineHeight != null ? lineHeight : 0));
                lineView.setBackgroundColor(lineColor == null ? Color.BLACK : lineColor);
                linearLayout.addView(lineView);
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOptionsItemClick(finalI,strings[finalI]);
                        dismiss();
                    }
                });
            }
        }
    }

    public abstract void onOptionsItemClick(int posi,String text);


    @Override
    public void onBackPressed() {
        if(isShowing()) {
            dismiss();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
