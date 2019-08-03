package android.lorenwang.netoptions;

/**
 * 创建时间：2018-12-17 下午 12:05:52
 * 创建人：王亮（Loren wang）
 * 功能作用：网络操作常量类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class NetworkOptionsConstant {
    //网络请求成功
    public static int networkRequestSuccess;//网络请求成功
    //网络请求异常
    public static final int NETWORK_DATA_REQUEST_ERROR = -1;//网络请求发起异常
    public static final int NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR = -2;//网络请求文件下载异常
    //网络请求失败原因
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_REQUESTACTNAME_IS_NULL = -1;//上下文为空
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE = -2;//url为空或无效
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE = -3;//不是当前用户请求的
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_TIME_INTERVALIN = -4;//在限定的时间请求间隔内再次请求了
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_LAST_REQUESNOT_FINISH = -5;//上一次相同网址key的请求还未完成
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_RESULT_DATA_PARAMS_FAIL = -6;//返回数据格式化失败
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL = -7;//网络请求发起失败，可能出现了404,500等错误
    public static final int NETWORK_DATA_REQUEST_NOT_ONESELF_RESULT_DATA = -8;//网络请求发起成功，但返回的数据不是自己的服务器返回的
    public static final int NETWORK_DATA_REQUEST_NOT_CONNECTION_NET = -9;//没有网络连接
    public static final int NETWORK_DATA_REQUEST_ERROR_URL_PARSE_FAIL = -10;//网址格式化失败


    //get网络请求
    public static final String NETWORK_REQUEST_FOR_GET = "GET";
    //post网络请求
    public static final String NETWORK_REQUEST_FOR_POST = "POST";
    //delete网络请求
    public static final String NETWORK_REQUEST_FOR_DELETE = "DELETE";
    //put网络请求
    public static final String NETWORK_REQUEST_FOR_PUT = "PUT";
    //网络请求类型之post请求
    public static final String NETWORK_REQUEST_FOR_POST_JSON = "POST_JSON";
    //网络请求类型之上传文件
    public static final String NETWORK_REQUEST_FOR_UP_LOAD_FILE = "upLoadFile";
    //网络请求类型之下载文件
    public static final String NETWORK_REQUEST_FOR_DOWN_LOAD_FILE = "downLoadFile";
}
