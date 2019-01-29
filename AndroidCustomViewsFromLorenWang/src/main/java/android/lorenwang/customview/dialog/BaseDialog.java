package android.lorenwang.customview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 创建时间：2018-11-16 下午 15:11:28
 * 创建人：王亮（Loren wang）
 * 功能作用：${DESCRIPTION}
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class BaseDialog extends AlertDialog {
    protected final String TAG = getClass().getName();
    protected View view;
    protected BaseDialog(Context context) {
        super(context);
    }

    protected BaseDialog(Context context, int style) {
        super(context,style);
    }

    public BaseDialog(Context context, @LayoutRes int dialogViewLayoutResId
            ,@StyleRes int modelStyleResId,@StyleRes int dialogAnimo, boolean isOutSideCancel) {
        super(context,modelStyleResId);
        view = LayoutInflater.from(context).inflate(dialogViewLayoutResId,null);
        new Builder(context,modelStyleResId).create();
        setView(view);
        setCanceledOnTouchOutside(isOutSideCancel);
        getWindow().setWindowAnimations(dialogAnimo);
    }

    /**
     * 释放内存
     */
    public void release(){

    }
}
