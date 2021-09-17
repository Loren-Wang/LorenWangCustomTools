package android.lorenwang.tools.mobile;

import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * 功能作用：手机品牌判断工具类
 * 初始注释时间： 2018/6/26 16:52
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 是否是小米手机--isXiaoMiMobile()
 * 判断是否是魅族手机--isMeiZuMobile()
 * 是否是华为手机--isHuaWeiMobile()
 * 是否是vivo手机--isVivoMobile()
 * 是否是oppo手机--isOPPOMobile()
 * 是否是Coolpad手机--isCoolpadMobile()
 * 是否是samsung手机--isSamsungMobile()
 * 是否是Sony手机--isSonyMobile()
 * 是否是LG手机--isLGMobile()
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwMobilePhoneBrandUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwMobilePhoneBrandUtil optionsInstance;

    private AtlwMobilePhoneBrandUtil() {
    }

    public static AtlwMobilePhoneBrandUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwMobilePhoneBrandUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwMobilePhoneBrandUtil();
                }
            }
        }
        return optionsInstance;
    }

    private Boolean isXiaoMi;//是否是小米手机
    private Boolean isMeiZu;//判断是否是魅族手机
    private Boolean isHuaWei;//是否是华为手机

    /**
     * 是否是小米手机,参考 https://dev.mi.com/doc/?p=254
     *
     * @return true 是
     */
    public boolean isXiaoMiMobile() {
        if (isXiaoMi == null) {
            if (Build.MANUFACTURER.toLowerCase().contains("xiaomi")) {
                isXiaoMi = true;
            } else {
                try {
                    Class<?> clz = Class.forName("android.os.SystemProperties");
                    Method method = clz.getMethod("get", String.class, String.class);
                    isXiaoMi = !TextUtils.isEmpty((CharSequence) method.invoke(clz, "ro.miui.ui.version.code", null)) || !TextUtils.isEmpty(
                            (CharSequence) method.invoke(clz, "ro.miui.ui.version.name", null)) || !TextUtils.isEmpty(
                            (CharSequence) method.invoke(clz, "ro.miui.internal.storage", null));
                } catch (Exception e) {
                    isXiaoMi = false;
                }
            }
            AtlwLogUtil.logUtils.logD(TAG, "is xiaomi mobile:" + isXiaoMi);
        }
        return isXiaoMi;
    }

    /**
     * 判断是否是魅族手机
     *
     * @return 是否是魅族手机
     */
    public boolean isMeiZuMobile() {
        if (isMeiZu == null) {
            if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
                isMeiZu = true;
            } else {
                try {
                    Class<?> clz = Class.forName("android.os.SystemProperties");
                    Method method = clz.getMethod("get", String.class, String.class);
                    String systemProperty = (String) method.invoke(clz, "ro.build.display.id", "");
                    isMeiZu = !systemProperty.isEmpty() && systemProperty.toLowerCase().contains("flyme");
                } catch (Exception e) {
                    isMeiZu = false;
                }
            }
            AtlwLogUtil.logUtils.logD(TAG, "is meizu mobile:" + isMeiZu);
        }
        return isMeiZu;
    }

    /**
     * 是否是华为手机
     *
     * @return 是否是华为手机
     */
    public boolean isHuaWeiMobile() {
        if (isHuaWei == null) {
            if (Build.MANUFACTURER.toLowerCase().contains("huaswei")) {
                isHuaWei = true;
            } else {
                try {
                    Class<?> clz = Class.forName("android.os.SystemProperties");
                    Method method = clz.getMethod("get", String.class, String.class);
                    isHuaWei = !TextUtils.isEmpty((CharSequence) method.invoke(clz, "ro.build.hw_emui_api_level", null)) || !TextUtils.isEmpty(
                            (CharSequence) method.invoke(clz, "ro.build.version.emui", null)) || !TextUtils.isEmpty(
                            (CharSequence) method.invoke(clz, "ro.confg.hw_systemversion", null));
                } catch (Exception e) {
                    isHuaWei = false;
                }
            }
            AtlwLogUtil.logUtils.logD(TAG, "is huawei mobile:" + isHuaWei);
        }
        return isHuaWei;
    }

    /**
     * 是否是vivo手机
     *
     * @return 是否是vivo手机
     */
    public boolean isVivoMobile() {
        return "vivo".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }

    /**
     * 是否是oppo手机
     *
     * @return 是否是oppo手机
     */
    public boolean isOPPOMobile() {
        return "oppo".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }

    /**
     * 是否是Coolpad手机
     *
     * @return 是否是酷派手机
     */
    public boolean isCoolpadMobile() {
        return "coolpad".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }

    /**
     * 是否是samsung手机
     *
     * @return 是否是三星手机
     */
    public boolean isSamsungMobile() {
        return "samsung".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }

    /**
     * 是否是Sony手机
     *
     * @return 是否是索尼手机
     */
    public boolean isSonyMobile() {
        return "sony".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }

    /**
     * 是否是LG手机
     *
     * @return 是否是LG手机
     */
    public boolean isLGMobile() {
        return "lg".equals(AtlwMobileSystemInfoUtil.getInstance().getMobileBrand().toLowerCase());
    }
}
