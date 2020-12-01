package javabase.lorenwang.network;

/**
 * 功能作用：接口请求回调
 * 创建时间：2020-12-01 2:20 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public interface JnlwNetworkCallback {
    /**
     * 成功
     *
     * @param protocol 协议名称,http、https等
     * @param major    协议主版本号
     * @param minor    协议副版本号
     * @param data     数据体
     */
    void success(String protocol, int major, int minor, String data);

    /**
     * 失败
     *
     * @param protocol        协议名称,http、https等
     * @param major           协议主版本号
     * @param minor           协议副版本号
     * @param failStatuesCode 失败状态code
     */
    void fail(String protocol, int major, int minor, int failStatuesCode);

    /**
     * 请求异常
     *
     * @param e 异常信息
     */
    void error(Exception e);
}
