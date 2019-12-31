package android.lorenwang.customview.dialog;

import android.content.Context;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.List;

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
public class AvlwBottomListOptionsDialogType2 extends AvlwBaseDialog {

    private AvlwOptionsItemClickListener onOptionsItemClick;

    public AvlwBottomListOptionsDialogType2(Context context) {
        super(context, R.layout.avlw_dialog_bottom_list_options_type_2, R.style.avlw_dialog_bottom_list_options_type_1
                , R.style.avlw_dialog_anim_for_bottom, true,true,false);
    }

    /**
     * 设置操作列表
     *
     * @param list 显示列表属性集合
     */
    public void setOptionsList(final List<AvlwOptionsItemAttribute> list) {
        if (list != null && !list.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.lnOptions);
            linearLayout.removeAllViews();
            Iterator<AvlwOptionsItemAttribute> iterator = list.iterator();
            AvlwOptionsItemAttribute itemAttribute;
            AppCompatButton button;
            while (iterator.hasNext()) {
                itemAttribute = iterator.next();
                if (itemAttribute != null) {
                    button = new AppCompatButton(getContext());
                    button.setText(itemAttribute.getTitle() != null ? itemAttribute.getTitle() : "");
                    button.setTextSize(itemAttribute.getTextSizeUnit() != null ? itemAttribute.getTextSizeUnit() : TypedValue.COMPLEX_UNIT_SP,itemAttribute.getTextSize() != null ? itemAttribute.getTextSize() : 20);
                    button.setTextColor(itemAttribute.getTextColor() != null ? itemAttribute.getTextColor() : Color.BLACK);
                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            itemAttribute.getTextHeight() != null ? itemAttribute.getTextHeight() : 200));
                    button.setBackgroundColor(Color.TRANSPARENT);
                    button.setMinHeight(0);
                    button.setAllCaps(false);
                    button.setPadding(
                            itemAttribute.getPaddingLeft() != null ? itemAttribute.getPaddingLeft() : 20,
                            itemAttribute.getPaddingTop() != null ? itemAttribute.getPaddingTop() : 20,
                            itemAttribute.getPaddingRight() != null ? itemAttribute.getPaddingRight() : 20,
                            itemAttribute.getPaddingBottom() != null ? itemAttribute.getPaddingBottom() : 20);
                    button.setGravity(itemAttribute.getGravity() != null ? itemAttribute.getGravity() : Gravity.CENTER);
                    linearLayout.addView(button);
                    final AvlwOptionsItemAttribute finalItemAttribute = itemAttribute;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onOptionsItemClick != null) {
                                onOptionsItemClick.onOptionsItemClick(list.indexOf(finalItemAttribute), finalItemAttribute);
                            }
                            dismiss();
                        }
                    });
                }
            }
        }
    }

    public void setOnOptionsItemClick(AvlwOptionsItemClickListener onOptionsItemClick) {
        this.onOptionsItemClick = onOptionsItemClick;
    }



}
