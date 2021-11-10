package android.lorenwang.commonbaseframe.pulgins.login;

import android.app.Activity;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginCallBack;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.tools.base.AtlwLogUtil;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.jetbrains.annotations.NotNull;

import javabase.lorenwang.dataparse.JdplwJsonUtil;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

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
        if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT) != null) {
            AtlwLogUtil.logUtils.logI(TAG, "准备发送微信登陆");
            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            String key = String.valueOf(callBack.hashCode());
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).setWeChatLoginCallbackKey(key);
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT).addCallBack(key, callBack);
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).getApi().sendReq(req);
        }
    }

    /**
     * 微博登陆
     *
     * @param callBack 回调
     */
    public void loginToSina(@NotNull Activity activity, @NotNull AcbflwPluginCallBack callBack) {
        if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA) != null) {
            String key = AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA).sinaKey(activity);
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.SINA).getSinaApi(activity).authorize(new WbAuthListener() {
                @Override
                public void onComplete(Oauth2AccessToken token) {
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT).callBackInfo(key, token);
                }

                @Override
                public void onError(UiError error) {
                    AtlwLogUtil.logUtils.logI("loginToSina", JdplwJsonUtil.toJson(error));
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT).callBackError(key,
                            AcbflwPluginErrorTypeEnum.SINA_LOGIN_AUTH_UN_KNOW_ERROR);
                }

                @Override
                public void onCancel() {
                    AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT).callBackError(key, AcbflwPluginErrorTypeEnum.SINA_LOGIN_AUTH_CANCEL);
                }
            });
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.DEFAULT).addCallBack(key, callBack);
        }
    }

    /**
     * QQ登陆
     *
     * @param callBack 回调
     */
    public void loginToQQ(@NotNull Activity activity, String scope, @NotNull AcbflwPluginCallBack callBack) {
        if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.QQ) != null) {
            Tencent qqApi = AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.QQ).getQqApi();
            if (!qqApi.isSessionValid()) {
                qqApi.login(activity, JtlwCheckVariateUtil.getInstance().isEmpty(scope) ? "all" : scope, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        AtlwLogUtil.logUtils.logI("loginToQQ", JdplwJsonUtil.toJson(o));
                        callBack.info(o);
                    }

                    @Override
                    public void onError(com.tencent.tauth.UiError uiError) {
                        AtlwLogUtil.logUtils.logI("loginToQQ", JdplwJsonUtil.toJson(uiError));
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
}
