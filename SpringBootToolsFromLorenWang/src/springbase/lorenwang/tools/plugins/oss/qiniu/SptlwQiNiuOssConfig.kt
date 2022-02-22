package springbase.lorenwang.tools.plugins.oss.qiniu

/**
 * 功能作用：七牛配置文件
 * 创建时间：2019-12-16 11:13
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * @param domain 七牛oss系统文件域名
 * @param secretKey 七牛oss系统secretKey
 * @param accessKey 七牛oss系统accessKey
 * @param bucket 七牛oss系统bucket
 * @param ossTypeProtocol oss系统协议，通用参数
 */
class SptlwQiNiuOssConfig(var ossTypeProtocol: String, var domain: String, var secretKey: String, var accessKey: String, var bucket: String)
