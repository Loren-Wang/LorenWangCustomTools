package springbase.lorenwang.base.bean

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

open class SpblwBaseDataDisposeStatusBean(var statusResult: Boolean, var statusCode: String?, var statusMsgCode: String?, var statusMsg: String?,
    var body: Any?) {
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
    var dataList: ArrayList<Any?> = arrayListOf()

    constructor(statusResult: Boolean, body: Any?) : this(statusResult, null, null, null, body)
    constructor(statusResult: Boolean) : this(statusResult, null, null, null, null)
    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String?) : this(statusResult, statusCode, statusMsgCode, null, null)
    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String?, statusMsg: String?) : this(statusResult, statusCode, statusMsgCode,
        statusMsg, null)

    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String, statusMsg: String?, pageIndex: Int, pageSize: Int, sumCount: Long,
        dataList: ArrayList<Any?>) : this(statusResult, statusCode, statusMsgCode, statusMsg, null) {
        this.statusCode = statusCode;
        this.statusMsgCode = statusMsgCode;
        this.statusMsg = statusMsg;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sumCount = sumCount;
        this.statusResult = true;
        this.repDataList = true;
        this.dataList = dataList;

    }

    constructor(statusResult: Boolean, statusCode: String, statusMsgCode: String, pageIndex: Int, pageSize: Int, sumCount: Long,
        dataList: ArrayList<Any?>) : this(statusResult, statusCode, statusMsgCode, null, null) {
        this.statusCode = statusCode;
        this.statusMsgCode = statusMsgCode;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sumCount = sumCount;
        this.statusResult = true;
        this.repDataList = true;
        this.dataList = dataList;

    }
}
