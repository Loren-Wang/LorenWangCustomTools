package android.lorenwang.commonbaseframe.pulgins.login;

import android.app.Activity;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginCallBack;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.tools.base.AtlwLogUtil;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.jetbrains.annotations.NotNull;

import javabase.lorenwang.dataparse.JdplwJsonUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

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

public class AcbflwLoginUtil {
    private final String TAG = "QtLoginUtils";
    private static volatile AcbflwLoginUtil optionsInstance;

    private AcbflwLoginUtil() {
    }

    public static AcbflwLoginUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwLoginUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwLoginUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 微信登陆
     */
    public void loginToWeChat(AcbflwPluginCallBack callBack) {
        AtlwLogUtil.logUtils.logI(TAG, "准备发送微信登陆");
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        String key = String.valueOf(callBack.hashCode());
        AcbflwPluginUtil.getInstance().setWeChatLoginCallbackKey(key);
        AcbflwPluginUtil.getInstance().addCallBack(key, callBack);
        AcbflwPluginUtil.getInstance().getApi().sendReq(req);
    }

    /**
     * 微博登陆
     *
     * @param callBack 回调
     */
    public void loginToSina(@NotNull Activity activity, @NotNull AcbflwPluginCallBack callBack) {
        String key = AcbflwPluginUtil.getInstance().sinaKey(activity);
        AcbflwPluginUtil.getInstance().getSinaApi(activity).authorize(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                AcbflwPluginUtil.getInstance().callBackInfo(key, token);
            }

            @Override
            public void onError(UiError error) {
                AtlwLogUtil.logUtils.logI("loginToSina", JdplwJsonUtils.toJson(error));
                AcbflwPluginUtil.getInstance().callBackError(key, AcbflwPluginErrorTypeEnum.SINA_LOGIN_AUTH_UN_KNOW_ERROR);
            }

            @Override
            public void onCancel() {
                AcbflwPluginUtil.getInstance().callBackError(key, AcbflwPluginErrorTypeEnum.SINA_LOGIN_AUTH_CANCEL);
            }
        });
        AcbflwPluginUtil.getInstance().addCallBack(key, callBack);
    }

    /**
     * QQ登陆
     *
     * @param callBack 回调
     */
    public void loginToQQ(@NotNull Activity activity, String scope, @NotNull AcbflwPluginCallBack callBack) {
        Tencent qqApi = AcbflwPluginUtil.getInstance().getQqApi();
        if (!qqApi.isSessionValid()) {
            qqApi.login(activity, JtlwCheckVariateUtils.getInstance().isEmpty(scope) ? "all" : scope, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    AtlwLogUtil.logUtils.logI("loginToQQ",JdplwJsonUtils.toJson(o));
                    callBack.info(o);
                }

                @Override
                public void onError(com.tencent.tauth.UiError uiError) {
                    AtlwLogUtil.logUtils.logI("loginToQQ",JdplwJsonUtils.toJson(uiError));
                    callBack.error(AcbflwPluginErrorTypeEnum.QQ_LOGIN_AUTH_UN_KNOW_ERROR);
                }

                @Override
                public void onCancel() {
                    callBack.error(AcbflwPluginErrorTypeEnum.QQ_LOGIN_AUTH_CANCEL);
                }

                @Override
                public void onWarning(int i) {
                    callBack.error(AcbflwPluginErrorTypeEnum.QQ_LOGIN_AUTH_UN_KNOW_ERROR);
                }
            });
        }
    }
}
