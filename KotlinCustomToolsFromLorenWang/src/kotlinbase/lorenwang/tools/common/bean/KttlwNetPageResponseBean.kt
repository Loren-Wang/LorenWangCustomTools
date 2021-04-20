package kotlinbase.lorenwang.tools.common.bean

import com.google.gson.annotations.SerializedName
import io.swagger.annotations.ApiModelProperty

/**
 * 功能作用：列表分页数据请求响应实体
 * 创建时间：2019-10-22 上午 11:45:27
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1：分页的页码---pageIndex
 * 配置参数2：分页的每页请求数量---pageSize
 * 配置参数3：当前条件下的取到的数据总数---sumCount
 * 配置参数4：列表数据实体，不能为空，但是可以为空数组---dataList
 */
open class KttlwNetPageResponseBean<T> {
    @ApiModelProperty(value = "分页的页码，由传递进来的数据决定", required = true)
    @SerializedName(value = "index", alternate = ["current", "pageIndex", "currentPage"])
    var pageIndex: Int? = null

    @ApiModelProperty(value = "分页的每页请求数量，为实际数量而不是请求数量", required = true)
    @SerializedName(value = "size", alternate = ["pageSize", "count", "pageCount", "everyPage"])
    var pageSize: Int? = null

    @ApiModelProperty(value = "当前条件下的取到的数据总数", required = true)
    @SerializedName(value = "total", alternate = ["sumDataCount", "totalCount"])
    var sumDataCount: Int? = null

    @ApiModelProperty(value = "数据总页数")
    @SerializedName(value = "pages", alternate = ["sumPageCount", "totalPage"])
    var sumPageCount: Int? = null

    @ApiModelProperty(value = "列表数据实体，不能为空，但是可以为空数组", required = true)
    @SerializedName(value = "list", alternate = ["result", "records", "data", "dataList", "resultList"])
    var dataList: ArrayList<T>? = null

    /**
     * 是否有下一页
     */
    @SerializedName(value = "hasNextPage")
    var hasNextPage: Boolean? = null

    /**
     * 是否有上一页
     */
    @SerializedName(value = "hasPrePage")
    var hasPrePage: Boolean? = null

    constructor()
    constructor(pageIndex: Int, pageSize: Int, sumDataCount: Int, sumPageCount: Int) : this(pageIndex, pageSize, sumDataCount, sumPageCount,
        arrayListOf())

    constructor(pageIndex: Int, pageSize: Int, sumDataCount: Int, sumPageCount: Int, dataList: ArrayList<T>) {
        this.pageIndex = pageIndex
        this.pageSize = pageSize
        this.sumDataCount = pageIndex
        this.sumPageCount = pageIndex
        this.dataList = dataList
    }

}
