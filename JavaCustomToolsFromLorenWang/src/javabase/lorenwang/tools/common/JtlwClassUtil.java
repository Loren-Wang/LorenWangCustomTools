package javabase.lorenwang.tools.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 功能作用：class 工具类
 * 创建时间：2019-03-21 下午 13:44:29
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 获取指定类的实体--getClassEntity(clazz)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class JtlwClassUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwClassUtil optionUtils;

    /**
     * 私有构造
     */
    private JtlwClassUtil() {
    }

    public static JtlwClassUtil getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwClassUtil.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwClassUtil();
                }
            }
        }
        return optionUtils;
    }

    /**
     * 获取指定类的实体
     *
     * @param <T>   变量泛型
     * @param clazz 指定类
     * @return 指定类实体
     */
    public <T> T getClassEntity(Class<T> clazz) {
        // 声明一个空的BaseModel
        T model = null;
        // 判断class对象是不是BaseModel的实例
        try {
            model = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return model;
    }

    public <T> Class<T> getSuperClassGenericType(Class clazz, int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        try {
            if (clazz.getGenericSuperclass() != null && clazz.getGenericSuperclass() instanceof ParameterizedType) {
                final ParameterizedType genType = (ParameterizedType) clazz.getGenericSuperclass();
                final Type[] params = genType.getActualTypeArguments();
                if (index >= params.length || index < 0) {
                    return null;
                } else if (!(params[index] instanceof Class)) {
                    return null;
                } else {
                    return (Class<T>) params[index];
                }
            }
        } catch (Exception ignore) {
        }
        return null;
    }
}
