package android.lorenwang.common_base_frame.pulgins;


/**
 * 功能作用：第三方结果回调
 * 创建时间：2019-12-27 10:26
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、分享异常---error（errorType）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AcbflwPluginCallBack {
    /**
     * 分享异常
     *
     * @param errorType 异常类型
     */
    void error(AcbflwPluginErrorTypeEnum errorType);

    /**
     * 回调信息
     * 微信登陆返回顺序：token--openId
     * 微信分享参数为空
     * 支付宝支付成功为空
     * @param info 信息
     */
    void info(Object... info);
}
