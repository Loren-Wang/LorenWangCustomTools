package android.lorenwang.commonbaseframe.pulgins.share;

/**
 * 功能作用：分享内容类型枚举
 * 创建时间：2019-12-25 18:00
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * 配置参数1、分享图片---IMAGE
 * 配置参数2、分享文本---TEXT
 * 配置参数3、分享音频---MUSIC
 * 配置参数4、分享视频---VIDEO
 * 配置参数5、分享网页---WEB_PAGE
 * 配置参数6、分享文件---FILE
 * 配置参数7、 “”---APP_EXTEND
 * 配置参数8、小程序分享---MINI_PROGRAM
 */

public enum AcbflwShareContentTypeEnum {
    /**
     * 分享图片
     */
    IMAGE(1, "image"),
    /**
     * 分享文本
     */
    TEXT(2, "text"),
    /**
     * 分享音频
     */
    MUSIC(3, "music"),
    /**
     * 分享视频
     */
    VIDEO(4, "video"),
    /**
     * 分享网页
     */
    WEB_PAGE(5, "Web Page"),
    /**
     * 分享文件
     */
    FILE(6, "file"),
    APP_EXTEND(7, "AppExtend"),
    /**
     * 小程序分享
     */
    MINI_PROGRAM(8, "MiniProgram"),
    /**
     * 图片列表
     */
    IMAGE_LIST(9,"image list");

    private final int type;
    private final String des;

    AcbflwShareContentTypeEnum(int type, String des) {
        this.type = type;
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public String getDes() {
        return des;
    }
}
