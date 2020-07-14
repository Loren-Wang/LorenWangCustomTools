package android.lorenwang.customview.dialog;

import android.app.Activity;
import android.lorenwang.customview.R;
import android.lorenwang.customview.imageview.AvlwFrescoZoomableImageView;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 功能作用：缩放预览弹窗
 * 创建时间：2020-07-14 2:05 下午
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

public class AvlwZoomablePreviewDialog extends AvlwBaseCenterDialog {
    public AvlwZoomablePreviewDialog(Activity context) {
        super(context, R.layout.avlw_dialog_zoomable_preview, false,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //点击隐藏弹窗
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 设置图片地址
     *
     * @param path 图片地址
     */
    public void setImagePath(String path) {
        ((AvlwFrescoZoomableImageView) view.findViewById(R.id.imgZoom)).setImageURI(Uri.parse(path));
    }

    /**
     * 获取图片组件
     */
    public ImageView getImageView() {
        return ((AvlwFrescoZoomableImageView) view.findViewById(R.id.imgZoom));
    }

}
