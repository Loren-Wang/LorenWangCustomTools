package android.lorenwang.customview.dialog;

import android.app.Activity;

/**
 * 功能作用：录音弹窗
 * 初始注释时间： 2020/7/3 11:01 上午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwRecordDialog extends AvlwBaseDialog {

    public AvlwRecordDialog(Activity context, int dialogViewLayoutResId, int modelStyleResId,
                            int dialogAnim, boolean isOutSideCancel,
                            Integer showDialogWidth, Integer showDialogHeight,Integer windowGravity) {
        super(context, dialogViewLayoutResId, modelStyleResId, dialogAnim,
                isOutSideCancel, showDialogWidth, showDialogHeight,windowGravity);
    }

    /**
     * 取消录音提示隐藏
     */
    public void cancelRecordHintHide() {

    }

    /**
     * 取消录音提示显示
     */
    public void cancelRecordHintShow() {

    }

    /**
     * 当前录音时间
     * @param nowRecordTime 录音详细时间
     */
    public void nowRecordTime(long nowRecordTime) {
    }

    /**
     * 录音时间过短
     */
    public void recordTimeShort() {

    }

    /**
     * 开始录音
     */
    public void startRecord() {


    }
}
