package android.lorenwang.customview.viewpager.banner;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

/**
 * 功能作用：基础指示器
 * 创建时间：2020-06-28 4:29 下午
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

abstract class AvlwBaseBannerIndicator {
    /**
     * 指示器view
     */
    protected View indicatorView;

    public AvlwBaseBannerIndicator(TypedArray typedArray, View indicatorView) {
        this.indicatorView = indicatorView;
    }

    /**
     * 初始化指示器
     * @param context 上下文
     * @param dataSize 数据总数，非最大值，而是实际数据数量
     * @param showPosition 当前显示的位置
     */
    abstract void initIndicator(Context context,int dataSize,int showPosition);

    /**
     * 切换指示器位置
     * @param showPosition 切换后的指示器位置
     */
    abstract void changeShowIndicator(int showPosition);

    /**
     * 是否左对齐
     * @return 默认不对齐
     */
     boolean getDefaultTheLeft(){
         return false;
     }
    /**
     * 是否上对齐
     * @return 默认不对齐
     */
     boolean getDefaultTheTop(){
         return false;
     }
    /**
     * 是否右对齐
     * @return 默认不对齐
     */
     boolean getDefaultTheRight(){
         return false;
     }
    /**
     * 是否下对齐
     * @return 默认不对齐
     */
     boolean getDefaultTheBottom(){
         return false;
     }
}
