package android.lorenwang.common_base_frame.pulgins;


import android.lorenwang.common_base_frame.R;

import androidx.annotation.StringRes;

/**
 * 功能作用：分享错误类型枚举
 * 创建时间：2019-12-25 18:58
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * SHARE_TITLE_IS_TOO_LONG(1, "分享标题过长"),
 * SHARE_DESCRIPTION_IS_TOO_LONG(2, "描述内容过长"),
 * SHARE_TEXT_IS_TOO_LONG(3, "文本内容过长"),
 * SHARE_SHARE_MUSIC_URL_EMPTY_OR_IS_TOO_LONG(4, "音频链接为空或长度过长"),
 * SHARE_VIDEO_URL_EMPTY_OR_IS_TOO_LONG(5, "视频链接为空或长度过长"),
 * SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG(5, "网页链接为空或长度过长"),
 * SHARE_APPLET_OF_WECHAT_USER_NAME_EMPTY(6, "小程序id为空"),
 * SHARE_APPLET_OF_WECHAT_PATH_EMPTY(7, "小程序路径为空"),
 * SHARE_IMAGE_EMPTY(8, "图片为空"),
 * SHARE_IMAGE_ERROR(9, "图片异常"),
 * SHARE_FILE_IS_TOO_LONG(10, "文件过大"),
 * SHARE_FILE_EMPTY(11, "文件为空"),
 * WECHAT_NOT_INIT(12, "微信未初始化"),
 * WECHAT_NOT_INSTALL(13, "微信未安装");
 */

public enum AcbflwPluginErrorTypeEnum {
    SHARE_TITLE_IS_TOO_LONG(1, R.string.share_title_is_too_long),
    SHARE_DESCRIPTION_IS_TOO_LONG(2, R.string.share_description_is_too_long),
    SHARE_TEXT_IS_TOO_LONG(3, R.string.share_text_is_too_long),
    SHARE_MUSIC_URL_EMPTY_OR_IS_TOO_LONG(4, R.string.share_music_url_empty_or_is_too_long),
    SHARE_VIDEO_URL_EMPTY_OR_IS_TOO_LONG(5, R.string.share_video_url_empty_or_is_too_long),
    SHARE_WEB_PAGE_URL_EMPTY_OR_IS_TOO_LONG(5, R.string.share_web_page_url_empty_or_is_too_long),
    SHARE_APPLET_OF_WECHAT_USER_NAME_EMPTY(6, R.string.share_applet_of_wechat_user_name_empty),
    SHARE_APPLET_OF_WECHAT_PATH_EMPTY(7, R.string.share_applet_of_wechat_path_empty),
    SHARE_IMAGE_EMPTY(8, R.string.share_image_empty),
    SHARE_IMAGE_ERROR(9, R.string.share_image_error),
    SHARE_FILE_IS_TOO_LONG(10, R.string.share_file_is_too_long),
    SHARE_FILE_EMPTY(11, R.string.share_file_empty),
    WECHAT_NOT_INIT(12, R.string.wechat_not_init),
    WECHAT_NOT_INSTALL(13, R.string.wechat_not_install),
    WECHAT_LOGIN_AUTH_DENIED(14, R.string.wechat_login_auth_denied),
    WECHAT_LOGIN_AUTH_CANCEL(15, R.string.wechat_login_auth_cancel),
    WECHAT_LOGIN_AUTH_UNKNOW_ERROR(16, R.string.wechat_login_auth_unknow_error),
    WECHAT_SHARE_UNKNOW_ERROR(17, R.string.wechat_share_unknow_error),
    WECHAT_PAY_UNKNOW_ERROR(18, R.string.wechat_pay_unknow_error),
    WECHAT_PAY_CANCEL(19, R.string.wechat_pay_cancel),
    ALI_PAY_FAIL(20, R.string.ali_pay_fail),
    ALI_PAY_CANCEL(21, R.string.ali_pay_cancel),
    ALI_PAY_ERROR_REPETITION(22, R.string.ali_pay_error_repetition),
    ALI_PAY_ERROR_CONNECT(23, R.string.ali_pay_error_connect),
    ALI_PAY_ERROR_OTHER(24, R.string.ali_pay_error_other);

    /**
     * 错误类型
     */
    private int type;
    /**
     * 错误描述
     */
    @StringRes
    private int desResId;

    AcbflwPluginErrorTypeEnum(int type, @StringRes int desResId) {
        this.type = type;
        this.desResId = desResId;
    }

    public int getType() {
        return type;
    }

    public int getDesResId() {
        return desResId;
    }
}
