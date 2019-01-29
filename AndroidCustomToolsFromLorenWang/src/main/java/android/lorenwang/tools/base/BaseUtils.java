package android.lorenwang.tools.base;

/**
 * 创建时间：2019-01-28 下午 19:24:39
 * 创建人：王亮（Loren wang）
 * 功能作用：基类utils
 * 思路：
 * 方法：1、销毁单例
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class BaseUtils {
    protected static BaseUtils baseUtils;


    /**
     * 销毁当前单例
     */
    public void destory(){
        baseUtils = null;
    }
}
