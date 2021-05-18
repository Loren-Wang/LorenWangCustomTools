package android.lorenwang.commonbaseframe.pulgins.share;

import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginErrorTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;

import javabase.lorenwang.dataparse.JdplwJsonUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：QQ分享
 * 创建时间：2019-12-25 18:36
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwQQShare {

    /**
     * 分享图片位图
     *
     * @param shareDataBean 分享信息
     */
    public void shareImage(AcbflwShareDataBean shareDataBean) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getActivity())) {
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_ACTIVITY_EMPTY);
            return;
        }

        if (JtlwCheckVariateUtils.getInstance().isEmpty(shareDataBean.getQqImageLocalPath())) {
            //回调异常
            callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_IMAGE_EMPTY);
        } else {
            Bundle params = new Bundle();
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, shareDataBean.getQqImageLocalPath());
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            AcbflwPluginUtil.getInstance().getQqApi().shareToQQ(shareDataBean.getActivity(), params, getCallbackListener(shareDataBean));
        }
    }

    /**
     * 回调异常信息
     *
     * @param bean      分享实体
     * @param errorType 异常类型
     */
    public void callBackError(AcbflwShareDataBean bean, AcbflwPluginErrorTypeEnum errorType) {
        if (bean != null && bean.getShareCallBack() != null && errorType != null) {
            bean.getShareCallBack().error(errorType);
        }
    }

    /**
     * 获取回调
     *
     * @param shareDataBean 实体信息
     * @return 回调
     */
    private IUiListener getCallbackListener(AcbflwShareDataBean shareDataBean) {
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                AtlwLogUtil.logUtils.logI("shareToQQ", JdplwJsonUtils.toJson(o));
                if (shareDataBean != null && shareDataBean.getShareCallBack() != null) {
                    shareDataBean.getShareCallBack().info(o);
                }
            }

            @Override
            public void onError(com.tencent.tauth.UiError uiError) {
                AtlwLogUtil.logUtils.logI("shareToQQ", JdplwJsonUtils.toJson(uiError));
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FAIL);
            }

            @Override
            public void onCancel() {
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_CANCEL);
            }

            @Override
            public void onWarning(int i) {
                callBackError(shareDataBean, AcbflwPluginErrorTypeEnum.SHARE_FAIL);
            }
        };
    }
}
