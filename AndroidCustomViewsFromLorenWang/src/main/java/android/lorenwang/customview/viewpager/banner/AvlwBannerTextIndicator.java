package android.lorenwang.customview.viewpager.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.lorenwang.customview.R;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 功能作用：banner文本指示器
 * 创建时间：2020-06-28 6:11 下午
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

class AvlwBannerTextIndicator extends AvlwBaseBannerIndicator {
    private final AppCompatTextView tvIndicator;

    public AvlwBannerTextIndicator(TypedArray typedArray, View indicatorView) {
        super(typedArray, indicatorView);
        tvIndicator = (AppCompatTextView) indicatorView;
        tvIndicator.setTextColor(typedArray.getColor(R.styleable.AvlwBannerView_avlw_bv_indicatorTextColor,
                tvIndicator.getCurrentTextColor()));
    }

    /**
     * 初始化指示器
     *
     * @param context      上下文
     * @param dataSize     数据总数，非最大值，而是实际数据数量
     * @param showPosition 当前显示的位置
     */
    @Override
    void initIndicator(Context context, int dataSize, int showPosition) {
        String text = (showPosition + 1) + "/" + dataSize;
        tvIndicator.setText(text);
    }

    /**
     * 切换指示器位置
     *
     * @param showPosition 切换后的指示器位置
     */
    @Override
    void changeShowIndicator(int showPosition) {
        String text = tvIndicator.getText().toString();
        text = text.replaceFirst("^\\d+/", (showPosition + 1) + "/");
        tvIndicator.setText(text);
    }

    @Override
    boolean getDefaultTheBottom() {
        return true;
    }

    @Override
    boolean getDefaultTheRight() {
        return true;
    }
}
