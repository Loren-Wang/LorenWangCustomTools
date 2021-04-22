package android.lorenwang.commonbaseframe;

import android.content.Intent;
import android.lorenwang.commonbaseframe.network.AcbflwNetworkManager;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.commonbaseframe.pulgins.api.AcbflwPluginApi;
import android.lorenwang.commonbaseframe.pulgins.api.AcbflwWeChatResponse;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.jetbrains.annotations.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能作用：微信基础回调Activity
 * 创建时间：2019-12-27 16:53
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 备注：
 */

public class AcbflwBaseWxEntryActivity extends AcbflwBaseActivity implements IWXAPIEventHandler {
    private final String TAG = getClass().getName();

    @Override public void hideBaseLoading() {

    }

    @Override public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override public void showBaseLoading(boolean allowLoadingBackFinishPage) {

    }

    @Override public void userLoginStatusError(@Nullable Object code, @Nullable String message) {

    }


    @Override public <T> void netReqSuccess(int netOptionReqCode, T data) {

    }

    @Override public void netReqFail(int netOptionReqCode, @Nullable String message) {

    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        AcbflwPluginUtil.getInstance().getApi().handleIntent(getIntent(), this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        AcbflwPluginUtil.getInstance().getApi().handleIntent(intent, this);
    }

    @Override public void onReq(BaseReq baseReq) {
    }

    //请求回调结果处理
    @Override public void onResp(BaseResp baseResp) {
        AtlwLogUtil.logUtils.logE(this.TAG,
                baseResp.getType() + "    " + baseResp.errCode + "    " + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp instanceof PayResp) {
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        AtlwLogUtil.logUtils.logI(TAG, "微信支付成功");
                        AcbflwPluginUtil.getInstance().callBackInfo(
                                ((PayResp) baseResp).prepayId);
                        break;
                    case BaseResp.ErrCode.ERR_COMM:
                        AtlwLogUtil.logUtils.logI(TAG, "微信支付未知错误");
                        AcbflwPluginUtil.getInstance().callBackError(
                                ((PayResp) baseResp).prepayId,
                                AcbflwPluginErrorTypeEnum.WECHAT_PAY_UNKNOW_ERROR);
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        AtlwLogUtil.logUtils.logI(TAG, "微信支付用户取消支付");
                        AcbflwPluginUtil.getInstance().callBackError(
                                ((PayResp) baseResp).prepayId,
                                AcbflwPluginErrorTypeEnum.WECHAT_PAY_CANCEL);
                        break;
                    default:
                        break;
                }
            }
            finish();
        } else {
            if (baseResp instanceof SendAuth.Resp) {
                //登陆成功
                String state = ((SendAuth.Resp) baseResp).state;
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        AtlwLogUtil.logUtils.logI(TAG, "微信登陆成功");
                        String code = ((SendAuth.Resp) baseResp).code;
                        //获取用户信息
                        getAccessToken(state, code);
                        break;
                    //用户拒绝授权
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        AtlwLogUtil.logUtils.logI(TAG, "微信登陆失败：用户拒绝授权");
                        AcbflwPluginUtil.getInstance().callBackError(
                                AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                                AcbflwPluginErrorTypeEnum.WECHAT_LOGIN_AUTH_DENIED);
                        finish();
                        break;
                    //用户取消
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        AtlwLogUtil.logUtils.logI(TAG, "微信登陆失败：用户取消");
                        AcbflwPluginUtil.getInstance().callBackError(
                                AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                                AcbflwPluginErrorTypeEnum.WECHAT_LOGIN_AUTH_CANCEL);
                        finish();
                        break;
                    default:
                        AtlwLogUtil.logUtils.logI(TAG, "微信登陆失败：未知错误");
                        AcbflwPluginUtil.getInstance().callBackError(
                                AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                                AcbflwPluginErrorTypeEnum.WECHAT_LOGIN_AUTH_UNKNOW_ERROR);
                        finish();
                        break;
                }
            } else if (baseResp instanceof SendMessageToWX.Resp) {
                //微信分享数据返回
                String transaction = baseResp.transaction;
                if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                    AtlwLogUtil.logUtils.logI(TAG, "微信分享成功");
                    AcbflwPluginUtil.getInstance().callBackInfo(transaction);
                } else {
                    AtlwLogUtil.logUtils.logI(TAG, "微信分享失败：未知错误");
                    AcbflwPluginUtil.getInstance().callBackError(transaction,
                            AcbflwPluginErrorTypeEnum.WECHAT_SHARE_UNKNOW_ERROR);
                    finish();
                }
            }
        }
    }

    @Override protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    private void getAccessToken(final String state, String code) {
        //获取授权
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?appid=" + AcbflwPluginUtil
                .getInstance().getWeChatId() + "&secret=" + AcbflwPluginUtil.getInstance()
                .getWeiChatSecret() + "&code=" + code + "&grant_type=authorization_code";
        AcbflwPluginApi api = AcbflwNetworkManager.getInstance().create(AcbflwPluginApi.class);
        if (api == null) {
            AtlwLogUtil.logUtils.logI(TAG, "微信登陆接口调用异常");
            AcbflwPluginUtil.getInstance().callBackError(
                    AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                    AcbflwPluginErrorTypeEnum.WECHAT_LOGIN_AUTH_UNKNOW_ERROR);
        } else {
            api.getWeiXinToken(url).compose(
                    new ObservableTransformer<AcbflwWeChatResponse, AcbflwWeChatResponse>() {
                        @Override public ObservableSource<AcbflwWeChatResponse> apply(
                                Observable<AcbflwWeChatResponse> upstream) {
                            return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(
                                    Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                        }
                    }).subscribe(new Observer<AcbflwWeChatResponse>() {
                @Override public void onSubscribe(Disposable d) {

                }

                @Override public void onNext(AcbflwWeChatResponse AcbflwWeChatResponse) {
                    AtlwLogUtil.logUtils.logI(TAG, "微信登陆获取用户部分参数成功");
                    AcbflwPluginUtil.getInstance().callBackInfo(
                            AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                            AcbflwWeChatResponse.getAccess_token(),
                            AcbflwWeChatResponse.getOpenid());
                }

                @Override public void onError(Throwable e) {
                    AtlwLogUtil.logUtils.logI(TAG, "微信登陆获取用户部分参数失败");
                    AcbflwPluginUtil.getInstance().callBackError(
                            AcbflwPluginUtil.getInstance().getWeChatLoginCallbackKey(),
                            AcbflwPluginErrorTypeEnum.WECHAT_LOGIN_AUTH_UNKNOW_ERROR);
                }

                @Override public void onComplete() {
                    finish();
                }
            });
        }

    }
}
