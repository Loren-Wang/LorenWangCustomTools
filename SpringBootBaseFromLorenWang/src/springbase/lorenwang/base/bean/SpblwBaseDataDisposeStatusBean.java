package springbase.lorenwang.base.bean;

import java.util.List;

import springbase.lorenwang.base.SpblwConfigKt;

public class SpblwBaseDataDisposeStatusBean {
    /**
     * 状态是成功还是失败
     */
    private final boolean statusResult;
    /**
     * 状态code
     */
    private final String statusCode;
    /**
     * 状态消息，如果不是消息key的话就按照消息内容来，默认要按照消息key来
     */
    private final String statusMsg;
    /**
     * 响应的body
     */
    private final Object body;
    /**
     * 页码
     */
    private final Integer pageIndex;
    /**
     * 每页数量
     */
    private final Integer pageSize;
    /**
     * 总数
     */
    private final Integer sumCount;
    /**
     * 数据实体
     */
    private final List<Object> dataList;
    /**
     * 相应的是数组吗
     */
    private boolean repDataList = false;

    public SpblwBaseDataDisposeStatusBean(boolean status) {
        this(status, null, null, null, null, null, null, null);
    }

    public SpblwBaseDataDisposeStatusBean(boolean status, String body) {
        this(status, null, null, body, null, null, null, null);
    }

    public SpblwBaseDataDisposeStatusBean(boolean status, String code, String msg, Object body) {
        this(status, code, msg, body, null, null, null, null);
    }

    public SpblwBaseDataDisposeStatusBean(boolean status, String code, String msg, Integer pageIndex, Integer pageSize, Integer sumCount,
            List<Object> list) {
        this(status, code, msg, null, pageIndex, pageSize, sumCount, list);
        repDataList = true;
    }

    public SpblwBaseDataDisposeStatusBean(boolean status, String code, String msg, Object body, Integer pageIndex, Integer pageSize, Integer sumCount,
            List<Object> list) {
        this.statusResult = status;
        this.statusCode = code;
        this.statusMsg = SpblwConfigKt.spblwConfig.getMessageResourceValue(msg, msg);
        this.body = body;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sumCount = sumCount;
        this.dataList = list;
    }

    public boolean isStatusResult() {
        return statusResult;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public Object getBody() {
        return body;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getSumCount() {
        return sumCount;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public boolean isRepDataList() {
        return repDataList;
    }
}
