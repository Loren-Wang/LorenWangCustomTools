package android.lorenwang.commonbaseframe.pulgins.launch;

import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginTypeEnum;
import android.lorenwang.commonbaseframe.pulgins.AcbflwPluginUtil;

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

public class AcbflwLaunchUtil {
    private final String TAG = getClass().getName();
    private static volatile AcbflwLaunchUtil optionsInstance;

    private AcbflwLaunchUtil() {
    }

    public static AcbflwLaunchUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwLaunchUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwLaunchUtil();
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
        if (AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT) != null) {
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            // 填小程序原始id
            req.userName = AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).getWeChatConfigInfoBean().getWeChatApplyId();
            //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query
            req.path = pagePath;
            //部分，来实现传参效果，如：传入 "?foo=bar"。// 可选打开 开发版，体验版和正式版
            if (jumpToPreview == null || !jumpToPreview) {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
            } else {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
            }
            AcbflwPluginUtil.getInstance(AcbflwPluginTypeEnum.WECHAT).getApi().sendReq(req);
        }
    }

}
