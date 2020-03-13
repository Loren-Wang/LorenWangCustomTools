package android.lorenwang.common_base_frame.pulgins.pay;

import android.app.Activity;
import android.lorenwang.common_base_frame.pulgins.AcbflwPluginCallBack;
import android.lorenwang.common_base_frame.pulgins.AcbflwPluginTargetTypeEnum;

/**
 * 功能作用：支付数据实体
 * 创建时间：2019-12-31 14:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwPayDataBean {
    /**
     * 回调
     */
    private AcbflwPluginCallBack payCallBack;
    /**
     * 目标
     */
    private AcbflwPluginTargetTypeEnum targetType;
    /**
     * 预支付交易会话ID,微信返回的支付交易会话ID
     */
    private String prepayid;
    /**
     * 商户号,微信支付分配的商户号
     */
    private String partnerid;
    /**
     * 签名
     */
    private String sign;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 扩展字段
     */
    private String packageValue;
    /**
     * 支付宝支付数据
     */
    private String aLiPayBody;
    /**
     * activity实例，有些情况下要用到
     */
    private Activity activity;

    public AcbflwPluginCallBack getPayCallBack() {
        return payCallBack;
    }

    public AcbflwPluginTargetTypeEnum getTargetType() {
        return targetType;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getSign() {
        return sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public String getaLiPayBody() {
        return aLiPayBody;
    }

    public Activity getActivity() {
        return activity;
    }

    public static class Build {
        /**
         * 回调
         */
        private AcbflwPluginCallBack payCallBack;
        /**
         * 目标
         */
        private AcbflwPluginTargetTypeEnum targetType;
        /**
         * 预支付交易会话ID,微信返回的支付交易会话ID
         */
        private String prepayid;
        /**
         * 商户号,微信支付分配的商户号
         */
        private String partnerid;
        /**
         * 签名
         */
        private String sign;
        /**
         * 时间戳
         */
        private String timeStamp;
        /**
         * 随机字符串
         */
        private String nonceStr;
        /**
         * 扩展字段
         */
        private String packageValue;
        /**
         * 支付宝支付数据
         */
        private String aLiPayBody;
        /**
         * activity实例，有些情况下要用到
         */
        private Activity activity;

        public Build setPayCallBack(AcbflwPluginCallBack payCallBack) {
            this.payCallBack = payCallBack;
            return this;
        }

        public Build setTargetType(AcbflwPluginTargetTypeEnum targetType) {
            this.targetType = targetType;
            return this;
        }

        public Build setPrepayid(String prepayid) {
            this.prepayid = prepayid;
            return this;
        }

        public Build setPartnerid(String partnerid) {
            this.partnerid = partnerid;
            return this;
        }

        public Build setSign(String sign) {
            this.sign = sign;
            return this;
        }

        public Build setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Build setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public Build setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public Build setaLiPayBody(String aLiPayBody) {
            this.aLiPayBody = aLiPayBody;
            return this;
        }

        public Build setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public AcbflwPayDataBean build() {
            AcbflwPayDataBean bean = new AcbflwPayDataBean();
            bean.payCallBack = this.payCallBack;
            bean.targetType = this.targetType;
            bean.prepayid = this.prepayid;
            bean.partnerid = this.partnerid;
            bean.sign = this.sign;
            bean.timeStamp = this.timeStamp;
            bean.nonceStr = this.nonceStr;
            bean.packageValue = this.packageValue;
            bean.aLiPayBody = this.aLiPayBody;
            bean.activity = this.activity;
            return bean;
        }
    }
}
