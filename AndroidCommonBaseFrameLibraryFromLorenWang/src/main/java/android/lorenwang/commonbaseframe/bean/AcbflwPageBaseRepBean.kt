package android.lorenwang.commonbaseframe.bean

/**
 * 功能作用：接口数据基础响应体
 * 初始注释时间： 2021/10/14 17:20
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
interface AcbflwPageBaseRepBean<T> {
    /**
     * 总数据数量
     */
    fun getSumDataCount(): Int

    /**
     * 总页码数量
     */
    fun getSumPageCount(): Int

    /**
     * 当前页
     */
    fun getCurrentPageIndex(): Int

    /**
     * 当前页面数据数量
     */
    fun getCurrentPageSize(): Int

    /**
     * 获取数据
     */
    fun getList():ArrayList<T>
}
