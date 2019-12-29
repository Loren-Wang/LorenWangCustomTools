package javabase.lorenwang.tools.common;

/**
 * 创建时间：2019-03-21 下午 13:44:29
 * 创建人：王亮（Loren wang）
 * 功能作用：class 工具类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class JtlwClassUtils {
    private static volatile JtlwClassUtils baseUtils;

    private JtlwClassUtils() {
    }

    public static JtlwClassUtils getInstance() {
        synchronized (JtlwClassUtils.class) {
            if (baseUtils == null) {
                baseUtils = new JtlwClassUtils();
            }
        }
        return baseUtils;
    }

    /**
     * 获取指定类的实体
     */
    public <T> T getClassEntity(Class<T> clazz) {
        // 声明一个空的BaseModel
        T model = null;
        // 判断class对象是不是BaseModel的实例
        try {
            model = clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return model;
    }
}
