package android.lorenwang.commonbaseframe.pulgins;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.lorenwang.tools.AtlwConfig;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：插件工具类
 * 创建时间：2019-12-27 15:47
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author wangliang
 */

public class AcbflwPluginUtil {
    private final String TAG = "QtPluginUtils";
    private static volatile AcbflwPluginUtil optionsInstance;

    private AcbflwPluginUtil() {
    }

    public static AcbflwPluginUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwPluginUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwPluginUtil();
                }
            }
        }
        return optionsInstance;
    }


    /**
     * 回调集合,使用线程安全的集合
     */
    private final Map<String, AcbflwPluginCallBack> callBackMap = new ConcurrentHashMap<>();

    /**
     * 添加回调
     *
     * @param key      回调key
     * @param callBack 回调
     */
    public void addCallBack(String key, AcbflwPluginCallBack callBack) {
        callBackMap.put(key, callBack);
    }

    /**
     * 移除回调
     *
     * @param key 要移除的key
     */
    public void removeCallBack(String key) {
        callBackMap.remove(key);
    }

    /**
     * 回调异常信息,回调结束后移除回调
     *
     * @param key       回调key
     * @param errorType 异常类型
     */
    public void callBackError(String key, AcbflwPluginErrorTypeEnum errorType) {
        AcbflwPluginCallBack callBack = callBackMap.get(key);
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(callBack) && errorType != null) {
            callBack.error(errorType);
            removeCallBack(key);
        }
    }

    /**
     * 信息回调
     *
     * @param key  回调key
     * @param info 回调信息，可能会不传
     */
    public void callBackInfo(String key, Object... info) {
        AcbflwPluginCallBack callBack = callBackMap.get(key);
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(callBack)) {
            callBack.info(info);
            removeCallBack(key);
        }
    }

    /**---------------------------------------------微信相关-----------------------------------------*/

    /**
     * 微信登陆回调key
     */
    private String weChatLoginCallbackKey = "";
    private IWXAPI api;
    /**
     * 微信配置信息
     */
    private AcbflwWeChatConfigInfoBean weChatConfigInfoBean;
    /**
     * 是否注册了微信启动广播
     */
    private boolean registerWeChatReceiver = false;
    /**
     * 微信启动广播监听
     */
    private final BroadcastReceiver weChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            assert weChatConfigInfoBean != null;
            assert weChatConfigInfoBean.getWeChatId() != null;
            if (weChatConfigInfoBean != null && api != null) {
                // 将该app注册到微信
                api.registerApp(weChatConfigInfoBean.getWeChatId());
            }
        }
    };
    /**
     * 微信启动广播的intentFilter
     */
    private final IntentFilter weChatFilter = new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP);

    /**
     * 初始化微信
     *
     * @param weChatConfigInfoBean 微信配置信息
     */
    public void initWeChatConfigInfo(AcbflwWeChatConfigInfoBean weChatConfigInfoBean) {
        this.weChatConfigInfoBean = weChatConfigInfoBean;
        String weChatId = getWeChatId();
        api = WXAPIFactory.createWXAPI(AtlwConfig.nowApplication, weChatId, weChatConfigInfoBean.isCheckSignature());
        api.registerApp(weChatId);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver();
    }

    /**
     * 注册微信启动广播
     */
    public void registerReceiver() {
        if (!registerWeChatReceiver) {
            //建议动态监听微信启动广播进行注册到微信
            AtlwConfig.nowApplication.registerReceiver(weChatReceiver, weChatFilter);
            registerWeChatReceiver = true;
        }
    }

    /**
     * 取消注册微信启动广播
     */
    public void unRegisterReceiver() {
        if (registerWeChatReceiver) {
            //建议动态监听微信启动广播进行注册到微信
            AtlwConfig.nowApplication.unregisterReceiver(weChatReceiver);
            registerWeChatReceiver = false;
        }
    }

    public IWXAPI getApi() {
        return api;
    }

    public String getWeChatId() {
        assert weChatConfigInfoBean != null;
        return weChatConfigInfoBean.getWeChatId();
    }

    public String getWeiChatSecret() {
        assert weChatConfigInfoBean != null;
        return weChatConfigInfoBean.getWeiChatSecret();
    }

    /**
     * 获取小程序原始id
     *
     * @return 小程序原始id
     */
    public String getWeChatApplyId() {
        assert weChatConfigInfoBean != null;
        return weChatConfigInfoBean.getWeChatApplyId();
    }

    /**
     * 获取微信登陆回调key
     *
     * @return 回调key
     */
    public String getWeChatLoginCallbackKey() {
        return weChatLoginCallbackKey;
    }

    /**
     * 设置微信回调key
     *
     * @param weChatLoginCallbackKey 微信回调key
     */
    public void setWeChatLoginCallbackKey(String weChatLoginCallbackKey) {
        this.weChatLoginCallbackKey = weChatLoginCallbackKey != null ? weChatLoginCallbackKey : "";
    }

    /**
     * 获取微信配置信息
     *
     * @return 微信配置信息
     */
    public AcbflwWeChatConfigInfoBean getWeChatConfigInfoBean() {
        return weChatConfigInfoBean;
    }


    /**---------------------------------------------微博相关-----------------------------------------*/

    /**
     * 微博配置信息
     */
    private AcbflwSinaConfigInfoBean sinaConfigInfoBean;
    private final Map<String, IWBAPI> sinaApiMap = new HashMap<>();

    /**
     * 初始化微博信息
     *
     * @param bean 微博配置信息
     */
    public void initSinaConfigInfo(AcbflwSinaConfigInfoBean bean) {
        sinaConfigInfoBean = bean;
    }

    /**
     * 获取微博API
     *
     * @param activity 页面实例
     * @return API实例
     */
    public IWBAPI getSinaApi(Activity activity) {
        String key = sinaKey(activity);
        IWBAPI api = sinaApiMap.get(key);
        if (api == null) {
            AuthInfo authInfo = new AuthInfo(activity, sinaConfigInfoBean.getAppKey(), sinaConfigInfoBean.getRedirectUrl(),
                    sinaConfigInfoBean.getScope());
            api = WBAPIFactory.createWBAPI(activity);
            api.registerApp(activity, authInfo);
            sinaApiMap.put(key, api);
        }
        return api;
    }

    /**
     * 移除微博相关API
     *
     * @param activity 页面实例
     */
    public void removeSinaApi(Activity activity) {
        sinaApiMap.remove(sinaKey(activity));
    }

    /**
     * 新浪微博使用的key
     *
     * @param activity 实例
     * @return key
     */
    public String sinaKey(Activity activity) {
        return String.valueOf(activity.hashCode());
    }

    /**----------------------------------------------QQ相关-----------------------------------------*/

    /**
     * qq配置信息
     */
    private Tencent qqApi;

    /**
     * 初始化qq相关信息
     */
    public void initQQConfigInfo(AcbflwQQConfigInfoBean bean) {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        // 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
        qqApi = Tencent.createInstance(bean.getAppId(), AtlwConfig.nowApplication, bean.getAuthorities());
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
    }

    /**
     * 获取qqApi实例
     *
     * @return qq实例
     */
    public Tencent getQqApi() {
        return qqApi;
    }
}
