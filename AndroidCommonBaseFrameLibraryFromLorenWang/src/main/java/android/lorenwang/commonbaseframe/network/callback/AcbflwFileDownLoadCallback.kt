package android.lorenwang.commonbaseframe.network.callback

import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean

/**
 * 功能作用：文件下载回调
 * 初始注释时间： 2021/5/28 15:03
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
interface AcbflwFileDownLoadCallback {
    /**
     * 更新下载进度
     */
    fun updateProgress(progress: Int)

    /**
     * 下载失败
     */
    fun downloadFail(bean: AcbflwFileDownLoadBean)

    /**
     * 下载成功
     */
    fun downloadSuccess(bean: AcbflwFileDownLoadBean, absolutePath: String)
}
