package javabase.lorenwang.network;

/**
 * 功能作用：网络请求类型枚举
 * 创建时间：2020-12-01 10:26 上午
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
enum JnlwNetworkTypeEnum {
    /**
     * 未知请求类型
     */
    NONE,
    /**
     * 请求类型get
     */
    GET,
    /**
     * 请求类型post
     */
    POST,
    /**
     * 请求类型put
     */
    PUT,
    /**
     * 请求类型delete
     */
    DELETE,
    /**
     * 请求类型options
     */
    OPTIONS
}
