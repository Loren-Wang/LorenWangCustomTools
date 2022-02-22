package springbase.lorenwang.tools.plugins.oss.aliyun

/**
 * 功能作用：阿里云配置文件
 * 创建时间：2019-12-16 11:13
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * @param domain 阿里云oss系统域名
 * @param endpoint 阿里云oss访问域名
 * @param accessKeyId 阿里云oss系统keyId
 * @param accessKeySecret 阿里云oss系统密钥
 * @param bucket 阿里云存储空间名
 * @param ossTypeProtocol oss系统协议，通用参数
 */
class SptlwALiYunOssConfig(var ossTypeProtocol: String, var domain: String, var endpoint: String, var accessKeyId: String,
    var accessKeySecret: String, var bucket: String)
