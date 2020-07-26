package com.test.androidcommonframe.base;

import android.lorenwang.commonbaseframe.AcbflwBaseActivity;
import android.lorenwang.tools.app.AtlwToastHintUtils;

import com.test.androidcommonframe.R;
import com.test.androidcommonframe.dialog.LoadingDialog;

import org.jetbrains.annotations.Nullable;

import javabase.lorenwang.dataparse.JdplwJsonUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean;

/**
 * 功能作用：基础Activity
 * 创建时间：2020-07-23 11:20 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
public abstract class BaseActivity extends AcbflwBaseActivity {
    private LoadingDialog loadingDialog;

    @Override
    public void hideBaseLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void addContentView(int resId) {
        super.addContentView(resId, R.layout.title_bar_head_view_type_1);
    }

    /**
     * 显示空数据
     */
    protected void showEmptyData() {
        super.showEmptyData(R.layout.empty_data_default);
    }

    @Override
    public void showBaseLoading(boolean allowLoadingBackFinishPage) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show(allowLoadingBackFinishPage);
        }
    }

    @Override
    public void userLoginStatusError(@Nullable Object code, @Nullable String message) {
        hideBaseLoading();
    }

    @Override
    public void netReqFail(int netOptionReqCode, @Nullable Object code, @Nullable String message) {
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(message)) {
            KttlwBaseNetResponseBean bean = JdplwJsonUtils.fromJson(message,
                    KttlwBaseNetResponseBean.class);
            if (bean != null) {
                if (JtlwCheckVariateUtils.getInstance().isNotEmpty(bean.getStateMessage())) {
                    AtlwToastHintUtils.getInstance().toastMsg(bean.getStateMessage());
                }
            } else {
                AtlwToastHintUtils.getInstance().toastMsg(message);
            }
        }
    }

    /**
     * 网络请求成功
     *
     * @param data             响应数据
     * @param netOptionReqCode 网络操作请求code
     */
    @Override
    public <T> void netReqSuccess(int netOptionReqCode, T data) {
    }
}
