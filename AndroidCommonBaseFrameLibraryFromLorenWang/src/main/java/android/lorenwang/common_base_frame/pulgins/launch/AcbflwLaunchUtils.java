package android.lorenwang.common_base_frame.pulgins.launch;

import android.lorenwang.common_base_frame.pulgins.AcbflwPluginUtils;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

/**
 * 功能作用：拉起第三方app工具类
 * 创建时间：2020-06-28 2:35 下午
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

public class AcbflwLaunchUtils {
    private final String TAG = getClass().getName();
    private static volatile AcbflwLaunchUtils optionsInstance;

    private AcbflwLaunchUtils() {
    }

    public static AcbflwLaunchUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwLaunchUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwLaunchUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 跳转到微信小程序
     *
     * @param pagePath      要跳转的页面路径
     * @param jumpToPreview 是否跳转到预览页面
     */
    public void launchWeChatMiniProgram(String pagePath, Boolean jumpToPreview) {
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        // 填小程序原始id
        req.userName = AcbflwPluginUtils.getInstance().getWeChatConfigInfoBean().getWeChatApplyId();
        //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query
        req.path = pagePath;
        //部分，来实现传参效果，如：传入 "?foo=bar"。// 可选打开 开发版，体验版和正式版
        if (jumpToPreview == null || !jumpToPreview) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        }
        AcbflwPluginUtils.getInstance().getApi().sendReq(req);
    }

}
