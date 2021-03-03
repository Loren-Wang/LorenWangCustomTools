package android.lorenwang.tools.app;

import android.lorenwang.tools.AtlwConfig;
import android.util.TypedValue;

import androidx.annotation.IntegerRes;

/**
 * 功能作用：资源相关工具类
 * 创建时间：2021-03-03 15:03
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取浮点资源数据--getFloat(resId)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwResourseUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwResourseUtil optionsInstance;

    private AtlwResourseUtil() {
    }

    public static AtlwResourseUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwResourseUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwResourseUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 获取浮点资源数据
     *
     * @return 浮点数
     */
    public float getFloat(@IntegerRes int resId) {
        TypedValue value = new TypedValue();
        AtlwConfig.nowApplication.getResources().getValue(resId, value, true);
        if (value.type == TypedValue.TYPE_FLOAT) {
            return value.getFloat();
        }
        return 0F;
    }
}
