package android.lorenwang.customview.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.IdRes;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：webview弹窗
 * 创建时间：2020-03-05 15:08
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 功能作用：webview弹窗
 * 初始注释时间： 2020/7/3 10:56 上午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class AvlwBaseWebViewDialog extends AvlwBaseDialog {
    private View webView;

    public AvlwBaseWebViewDialog(Activity context, int dialogViewLayoutResId,
                                 int modelStyleResId, int dialogAnim, boolean isOutSideCancel,
                                 Integer showDialogWidth, Integer showDialogHeight,Integer windowGravity) {
        super(context, dialogViewLayoutResId, modelStyleResId, dialogAnim, isOutSideCancel,
                showDialogWidth, showDialogHeight,windowGravity);
    }

    /**
     * 初始化webview
     *
     * @param webViewResId webview的资源id
     * @param url          要加载的网址，可为空
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView(@IdRes int webViewResId, String url) {
        webView = view.findViewById(webViewResId);
        if (webView != null && webView instanceof WebView) {
            ((WebView) webView).setWebViewClient(new WebViewClient());
            WebSettings webSettings = ((WebView) webView).getSettings();
            // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
            // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setLoadWithOverviewMode(true);
            //允许使用js
            webSettings.setJavaScriptEnabled(true);
            //不使用缓存，只从网络获取数据.
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            //支持屏幕缩放
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            if (!JtlwCheckVariateUtils.getInstance().isEmpty(url)) {
                ((WebView) webView).loadUrl(url);
            }
        }
    }

    /**
     * 加载网址
     *
     * @param url 网址
     */
    public void loadUrl(String url) {
        if (webView != null && webView instanceof WebView
                && !JtlwCheckVariateUtils.getInstance().isEmpty(url)) {
            ((WebView) webView).loadUrl(url);
        }
    }

    @Override
    public void release() {
        super.release();
        webView = null;
    }
}
