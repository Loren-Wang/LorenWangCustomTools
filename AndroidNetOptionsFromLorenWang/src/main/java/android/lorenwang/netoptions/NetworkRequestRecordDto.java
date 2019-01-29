package android.lorenwang.netoptions;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangliang on 0019/2017/9/19.
 * 创建时间： 0019/2017/9/19 13:37
 * 创建人：王亮（Loren wang）
 * 功能作用：网络请求的记录实体类
 * 思路： 请求类型根据paramsMap参数是否为空进行判断
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class NetworkRequestRecordDto {
    public String requestType;//网络请求类型，get，post，参数常量值在网络请求单例中
    public String url;//请求网址
    public Map<String,Object> paramsMap;//post请求参数
    public Call call;//网路请求实体
    public String activityClassName;//请求界面名称
    public long requestTime;//请求时间
    public boolean isFrontRequest;//是否是前台的请求
    public Object networkRequestCallback;//网络请求回到，包括所有请求
    public String resultData = "";//返回数据
    public boolean isRequestFinish = false;//请求是否完成

    public NetworkRequestRecordDto(String requestType, String url, Map<String, Object> paramsMap, Call call
            , String activityClassName, long requestTime, boolean isFrontRequest, Object networkRequestCallback) {
        this.requestType = requestType;
        this.url = url;
        this.paramsMap = paramsMap;
        this.call = call;
        this.activityClassName = activityClassName;
        this.requestTime = requestTime;
        this.isFrontRequest = isFrontRequest;
        this.networkRequestCallback = networkRequestCallback;
    }

    @Override
    public String toString() {
        return "NetworkRequestRecordDto{" +
                "requestType='" + requestType + '\'' +
                ", url='" + url + '\'' +
                ", paramsMap=" + paramsMap +
                ", call=" + call +
                ", activityClassName='" + activityClassName + '\'' +
                ", requestTime=" + requestTime +
                ", isFrontRequest=" + isFrontRequest +
                ", networkRequestCallback=" + networkRequestCallback +
                ", resultData='" + resultData + '\'' +
                ", isRequestFinish=" + isRequestFinish +
                '}';
    }
}
