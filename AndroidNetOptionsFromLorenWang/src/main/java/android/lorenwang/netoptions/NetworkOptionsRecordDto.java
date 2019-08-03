package android.lorenwang.netoptions;

import java.util.Map;

/**
 * 创建时间：2018-12-17 下午 15:39:39
 * 创建人：王亮（Loren wang）
 * 功能作用：网络操作的记录实体类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class NetworkOptionsRecordDto {
    public String requestType;//网络请求类型，get，post，参数常量值在网络请求单例中
    public String url;//请求网址
    public Map<String, Object> paramsMapHeader;//head请求参数
    public Map<String, Object> paramsMap;//post请求参数
    public String jsonStr;//post请求参数
    public Object requestUtil;//请求实体
    public String activityClassName;//请求界面名称
    public long requestTime;//请求时间
    public boolean isFrontRequest;//是否是前台的请求
    public Object networkRequestCallback;//网络请求回到，包括所有请求
    public String resultData = "";//返回数据
    public boolean isRequestFinish = false;//请求是否完成

    public NetworkOptionsRecordDto(String requestType, String url, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, String jsonStr
            , Object requestUtil, String activityClassName, long requestTime, boolean isFrontRequest
            , Object networkRequestCallback) {
        this.requestType = requestType;
        this.url = url;
        this.paramsMapHeader = paramsMapHeader;
        this.paramsMap = paramsMap;
        this.jsonStr = jsonStr;
        this.requestUtil = requestUtil;
        this.activityClassName = activityClassName;
        this.requestTime = requestTime;
        this.isFrontRequest = isFrontRequest;
        this.networkRequestCallback = networkRequestCallback;
    }

    @Override
    public String toString() {
        return "NetworkOptionsRecordDto{" +
                "requestType='" + requestType + '\'' +
                ", url='" + url + '\'' +
                ", paramsMap=" + paramsMap +
                ", requestUtil=" + requestUtil +
                ", activityClassName='" + activityClassName + '\'' +
                ", requestTime=" + requestTime +
                ", isFrontRequest=" + isFrontRequest +
                ", networkRequestCallback=" + networkRequestCallback +
                ", resultData='" + resultData + '\'' +
                ", isRequestFinish=" + isRequestFinish +
                '}';
    }
}
