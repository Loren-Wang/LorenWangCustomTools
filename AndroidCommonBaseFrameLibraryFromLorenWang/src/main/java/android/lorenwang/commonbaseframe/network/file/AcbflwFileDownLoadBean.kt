package android.lorenwang.commonbaseframe.network.file

/**
 * 功能作用：文件下载请求时向presenter传参使用
 * 创建时间：2020-08-27 10:00 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 * @param fileUrlPath 文件网络地址
 * @param saveFileName 文件保存名称,当为空时随机生成文件名称
 * @param fileDirPath 文件保存文件夹
 */
class AcbflwFileDownLoadBean(var fileUrlPath: String, var fileDirPath: String,
    var saveFileName: String = System.currentTimeMillis().toString() + "_" + fileUrlPath.substring(fileUrlPath.lastIndexOf("/") + 1))
