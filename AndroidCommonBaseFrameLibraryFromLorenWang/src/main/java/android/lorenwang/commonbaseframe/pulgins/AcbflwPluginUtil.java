package android.lorenwang.commonbaseframe.pulgins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.lorenwang.tools.AtlwConfig;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
 * @author wangliang
 */

public class AcbflwPluginUtil {
    private final String TAG = "QtPluginUtils";
    private static volatile AcbflwPluginUtil optionsInstance;
    /**
     * 微信登陆回调key
     */
    private String weChatLoginCallbackKey = "";

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

    public AcbflwWeChatConfigInfoBean getWeChatConfigInfoBean() {
        return weChatConfigInfoBean;
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
}
