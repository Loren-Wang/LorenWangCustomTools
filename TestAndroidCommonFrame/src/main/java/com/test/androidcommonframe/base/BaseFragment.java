package com.test.androidcommonframe.base;

import android.lorenwang.commonbaseframe.AcbflwBaseFragment;
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseView;

import com.test.androidcommonframe.R;
import com.test.androidcommonframe.dialog.LoadingDialog;

import org.jetbrains.annotations.Nullable;

/**
 * 功能作用：基础fragment
 * 创建时间：2020-07-23 11:33 上午
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
public abstract class BaseFragment extends AcbflwBaseFragment {
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
        if (loadingDialog == null && getActivity() != null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show(allowLoadingBackFinishPage);
        }
    }

    @Override
    public void userLoginStatusError(@Nullable Object code, @Nullable String message) {
        hideBaseLoading();
        if (getActivity() != null && getActivity() instanceof AcbflwBaseView) {
            ((AcbflwBaseView) getActivity()).userLoginStatusError(code, message);
        }
    }

    @Override
    public void netReqFail(int netOptionReqCode, @Nullable Object code, @Nullable String message) {

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
