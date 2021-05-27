package android.lorenwang.tools.app;

import android.graphics.Typeface;
import android.lorenwang.tools.AtlwConfig;
import android.util.TypedValue;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.IntegerRes;
import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

/**
 * 功能作用：资源相关工具类
 * 创建时间：2021-03-03 15:03
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取浮点资源数据--getFloat(resId)
 * 获取资源字节--getAssets(assetsName)
 * 获取字体文件的typeFace--getTypeFace(typeFacePath)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwResourcesUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwResourcesUtil optionsInstance;

    private AtlwResourcesUtil() {
    }

    public static AtlwResourcesUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwResourcesUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwResourcesUtil();
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

    /**
     * 获取资源字节
     *
     * @param assetsName 资源名称，包含路径
     * @return 文件内容字节
     */
    public byte[] getAssets(@NotNull String assetsName) {
        try {
            return JtlwFileOptionUtils.getInstance().readBytes(AtlwConfig.nowApplication.getResources().getAssets().open(assetsName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * 获取字体文件的typeFace
     *
     * @param typeFacePath 例如"fonts/HelveticaNeueLTPro-UltLt.otf"
     * @return 字体文件的typeface
     */
    public Typeface getTypeFace(@NotNull String typeFacePath) {
        return Typeface.createFromAsset(AtlwConfig.nowApplication.getAssets(), typeFacePath);
    }

}
