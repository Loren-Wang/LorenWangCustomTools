package android.lorenwang.customview.dialog;

import android.content.Context;
import android.lorenwang.customview.R;

/**
 * 创建时间：2019-07-21 下午 22:08:43
 * 创建人：王亮（Loren wang）
 * 功能作用：录音弹窗
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class RecordDialog extends BaseDialog{
    public RecordDialog(Context context) {
        super(context, R.layout.dialog_confirm_cancel_1, R.style.dialog_confirm_cancel_1
                , R.style.dialog_anim_for_center, false,false,false);
    }

    /**
     * 开始录音
     */
    public void startRecord() {


    }
}
