package android.lorenwang.commonbaseframe.pulgins.pay;

import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.commonbaseframe.pulgins.AcbflwWeChatConfigInfoBean;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwLogUtil;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;

/**
 * 功能作用：支付工具类
 * 创建时间：2019-12-31 14:21
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwPayUtil {
    private final String TAG = "AcbflwPayUtils";
    private static volatile AcbflwPayUtil optionsInstance;

    private AcbflwPayUtil() {
    }

    public static AcbflwPayUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwPayUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwPayUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 发送支付数据
     *
     * @param payDataBean 分享数据实体
     */
    public void sendPayData(AcbflwPayDataBean payDataBean) {
        assert payDataBean != null;
        assert payDataBean.getTargetType() != null;
        assert payDataBean.getPayCallBack() != null;
        switch (payDataBean.getTargetType()) {
            case PAY_WE_CHAT:
                sendPayWeChat(payDataBean);
                break;
            case PAY_ALI:
                sendPayAli(payDataBean);
                break;
            default:
                break;
        }
    }

    /**
     * 发送微信支付
     */
    private void sendPayWeChat(AcbflwPayDataBean payDataBean) {
        AtlwLogUtil.logUtils.logI(TAG, "开始调用微信支付");
        if (AcbflwPluginUtil.getInstance().getApi().isWXAppInstalled()) {
            AcbflwWeChatConfigInfoBean configInfoBean = AcbflwPluginUtil.getInstance().getWeChatConfigInfoBean();
            assert configInfoBean != null;
            AtlwLogUtil.logUtils.logI(TAG, "微信配置信息验证通过，开始初始化支付请求实体");
            PayReq request = new PayReq();
            request.appId = configInfoBean.getAppid();
            //商户id
            request.partnerId = payDataBean.getPartnerid();
            //预支付交易会话ID
            request.prepayId = payDataBean.getPrepayid();
            //扩展字段
            request.packageValue = payDataBean.getPackageValue();
            //时间戳
            request.timeStamp = payDataBean.getTimeStamp() != null ? payDataBean.getTimeStamp() : String.valueOf(JtlwDateTimeUtils.getInstance().getSecond());
            //随机字符串，不长于32位。
            String str = payDataBean.getTimeStamp() != null ? payDataBean.getTimeStamp() : request.timeStamp;
            request.nonceStr = str.substring(0, Math.min(str.length(), 32));
            //签名
            request.sign = payDataBean.getSign();
            AtlwLogUtil.logUtils.logI(TAG, "微信支付请求实体初始化完成，向微信发送支付请求");
            //发起请求
            AcbflwPluginUtil.getInstance().getApi().sendReq(request);
            //记录回调
            if (!JtlwCheckVariateUtils.getInstance().isEmpty(payDataBean.getPayCallBack())) {
                AcbflwPluginUtil.getInstance().addCallBack(request.nonceStr, payDataBean.getPayCallBack());
            }
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "微信应用未安装");
            if (payDataBean.getPayCallBack() != null) {
                payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.WECHAT_NOT_INSTALL);
            }
        }
    }

    /**
     * 发送支付宝支付
     *
     * @param payDataBean 支付信息
     */
    private void sendPayAli(final AcbflwPayDataBean payDataBean) {
        AtlwLogUtil.logUtils.logI(TAG, "开始调用支付宝支付");
        assert payDataBean.getaLiPayBody() != null;
        assert payDataBean.getActivity() != null;
        AtlwLogUtil.logUtils.logI(TAG, "支付宝支付数据验证通过，准备发起支付");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AtlwLogUtil.logUtils.logI(TAG, "支付宝开始初始化请求实体");
                PayTask alipay = new PayTask(payDataBean.getActivity());
                AtlwLogUtil.logUtils.logI(TAG, "支付宝请求实体初始化完成，向支付宝发送支付请求");
                Map<String, String> result = alipay.payV2(payDataBean.getaLiPayBody(), true);
                String resultStatus = result.get("resultStatus");
                if (JtlwCheckVariateUtils.getInstance().isEmpty(resultStatus)
                        && payDataBean.getPayCallBack() != null) {
//                    9000	订单支付成功
//                    8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//                    4000	订单支付失败
//                    5000	重复请求
//                    6001	用户中途取消
//                    6002	网络连接出错
//                    6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//                    其它	其它支付错误
                    switch (resultStatus) {
                        case "9000":
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付成功");
                            payDataBean.getPayCallBack().info();
                            break;
                        case "4000":
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付失败");
                            payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_FAIL);
                            break;
                        case "5000":
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付错误：发起了重复请求");
                            payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_ERROR_REPETITION);
                            break;
                        case "6001":
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付错误：用户中途取消");
                            payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_CANCEL);
                            break;
                        case "6002":
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付错误：网络连接出错");
                            payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_ERROR_CONNECT);
                            break;
                        default:
                            AtlwLogUtil.logUtils.logI(TAG, "支付宝支付错误：其它支付错误");
                            payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_ERROR_OTHER);
                            break;
                    }
                } else {
                    if (payDataBean.getPayCallBack() != null) {
                        AtlwLogUtil.logUtils.logI(TAG, "支付宝支付错误：其它支付错误");
                        payDataBean.getPayCallBack().error(AcbflwPluginErrorTypeEnum.ALI_PAY_ERROR_OTHER);
                    }
                }
            }
        };
        AtlwThreadUtil.getInstance().postOnChildThread(runnable);
    }
}
