package android.lorenwang.common_base_frame.pulgins.login;

import android.lorenwang.common_base_frame.pulgins.AcbflwPluginCallBack;
import android.lorenwang.common_base_frame.pulgins.AcbflwPluginUtils;
import android.lorenwang.tools.base.AtlwLogUtils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * 功能作用：登陆工具类
 * 创建时间：2019-12-27 16:41
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwLoginUtils {
    private final String TAG = "QtLoginUtils";
    private static volatile AcbflwLoginUtils optionsInstance;

    private AcbflwLoginUtils() {
    }

    public static AcbflwLoginUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwLoginUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwLoginUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 微信登陆
     */
    public void loginToWeChat(AcbflwPluginCallBack callBack) {
        AtlwLogUtils.logI(TAG, "准备发送微信登陆");
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        String key = String.valueOf(callBack.hashCode());
        AcbflwPluginUtils.getInstance().setWeChatLoginCallbackKey(key);
        AcbflwPluginUtils.getInstance().addCallBack(key, callBack);
        AcbflwPluginUtils.getInstance().getApi().sendReq(req);
    }
}
