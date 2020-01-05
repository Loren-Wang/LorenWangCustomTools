package javabase.lorenwang.common_base_frame.bean

/**
 * 功能作用：数据处理状态实体
 * 创建时间：2020-01-05 下午 21:51:35
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwBaseDataDisposeStatusBean<T> {
    /**
     * 状态是否成功？
     */
    var statusSuccess = true;
    /**
     * 状态码
     */
    var statusCode: String? = null;
    /**
     * 状态消息码
     */
    var statusMsgCode: String? = null
    /**
     * 数据实体
     */
    var data: T? = null
    /**
     * 页码
     */
    var pageIndex: Int? = null
    /**
     * 每页数量
     */
    var pageSize: Int? = null
    /**
     * 总数
     */
    var sumCount: Long? = null
    /**
     * 相应的是数组吗
     */
    var repDataList = false
    /**
     * 数据实体
     */
    var dataList: ArrayList<T> = arrayListOf()

    constructor(statusCode: String, statusMsgCode: String) {
        this.statusSuccess = false;
        this.statusCode = statusCode
        this.statusMsgCode = statusMsgCode
    }

    constructor(data: T) {
        this.statusSuccess = true;
        this.data = data;
    }

    constructor(statusCode: String, statusMsgCode: String, pageIndex: Int,
                pageSize: Int, sumCount: Long, dataList:  ArrayList<T>) {
        this.statusCode = statusCode;
        this.statusMsgCode = statusMsgCode;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sumCount = sumCount;
        this.statusSuccess = true;
        this.repDataList = true;
        this.dataList = dataList;
    }
}
