package android.lorenwang.commonbaseframe.pulgins;

/**
 * 功能作用：微信配置信息
 * 创建时间：2019-12-31 14:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwWeChatConfigInfoBean {
    /**
     * 微信id
     */
    private String weChatId;
    /**
     * 微信Secret
     */
    private String weiChatSecret;
    /**
     * 微信开放平台审核通过的应用APPID
     */
    private String appid;
    /**
     * 小程序原始id
     */
    private String weChatApplyId;
    private boolean checkSignature;

    public String getWeChatId() {
        return weChatId;
    }

    public String getWeiChatSecret() {
        return weiChatSecret;
    }

    public String getAppid() {
        return appid;
    }

    public String getWeChatApplyId() {
        return weChatApplyId;
    }

    public boolean isCheckSignature() {
        return checkSignature;
    }

    public static class Build {
        /**
         * 微信id
         */
        private String weChatId;
        /**
         * 微信Secret
         */
        private String weiChatSecret;
        /**
         * 微信开放平台审核通过的应用APPID
         */
        private String appid;
        /**
         * 小程序原始id
         */
        private String weChatApplyId;
        private boolean checkSignature;

        public Build setWeChatId(String weChatId) {
            this.weChatId = weChatId;
            return this;
        }

        public Build setWeiChatSecret(String weiChatSecret) {
            this.weiChatSecret = weiChatSecret;
            return this;
        }

        public Build setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public Build setWeChatApplyId(String weChatApplyId) {
            this.weChatApplyId = weChatApplyId;
            return this;
        }

        public Build setCheckSignature(boolean checkSignature) {
            this.checkSignature = checkSignature;
            return this;
        }

        public AcbflwWeChatConfigInfoBean build() {
            AcbflwWeChatConfigInfoBean bean = new AcbflwWeChatConfigInfoBean();
            bean.weChatId = this.weChatId;
            bean.weiChatSecret = this.weiChatSecret;
            bean.appid = this.appid;
            bean.weChatApplyId = this.weChatApplyId;
            bean.checkSignature = this.checkSignature;
            return bean;
        }
    }
}
