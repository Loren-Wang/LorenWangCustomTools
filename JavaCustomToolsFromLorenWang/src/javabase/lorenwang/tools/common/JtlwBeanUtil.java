package javabase.lorenwang.tools.common;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能作用：实体类相关单例
 * 初始注释时间： 2021/7/5 18:11
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 复制相同参数数据--copyWithTheParameters(origin,target)
 * 复制相同参数数据--copyWithTheParameters(origin,target,filterNull)
 * 复制相同参数数据--copyWithTheParameters(originList,target,filterNull)
 * 获取实体类所有参数map集合--getBeanParameters(data)
 * 获取实体类痛的参数以及对应返回值--getBeanFieldsAndReturnType(beanClass)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwBeanUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwBeanUtil optionsInstance;

    private JtlwBeanUtil() {
    }

    public static JtlwBeanUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (JtlwBeanUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new JtlwBeanUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 复制相同参数数据
     *
     * @param origin 原始
     * @param target 目标
     * @param <T>    原始泛型
     * @param <R>    目标泛型
     * @return 复制结果，可能为空
     */
    public <T, R> R copyWithTheParameters(T origin, Class<R> target) {
        if (origin != null && target != null) {
            try {
                Map<String, Class<?>> originMap = getBeanFieldsAndReturnType(origin.getClass());
                Map<String, ?> originDataParams = getBeanParameters(origin);
                Map targetMap = new HashMap<>();
                for (Map.Entry<String, Class<?>> entry : getBeanFieldsAndReturnType(target).entrySet()) {
                    if (entry.getValue() != null && originDataParams.get(entry.getKey()) != null && entry.getValue().getName().equals(
                            originMap.get(entry.getKey()).getName())) {
                        targetMap.put(entry.getKey(), originDataParams.get(entry.getKey()));
                    }
                }
                Gson gson = new Gson();
                return gson.fromJson(gson.toJson(targetMap), target);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 复制相同参数数据
     *
     * @param origin     原始
     * @param target     目标
     * @param filterNull 原始中包含空是否复制
     * @param <T>        原始泛型
     * @param <R>        目标泛型
     * @return 复制结果，可能为空
     */
    public <T, R> R copyWithTheParameters(T origin, R target, boolean filterNull) {
        if (origin != null && target != null) {
            try {
                Map<String, Class<?>> originMap = getBeanFieldsAndReturnType(origin.getClass());
                Map<String, ?> originDataParams = getBeanParameters(origin);
                Map<String, Object> targetDataParams = getBeanParameters(target);
                Object value;
                for (Map.Entry<String, Class<?>> entry : getBeanFieldsAndReturnType(target.getClass()).entrySet()) {
                    if (entry.getValue() != null && originDataParams.get(entry.getKey()) != null && entry.getValue().getName().equals(
                            originMap.get(entry.getKey()).getName())) {
                        value = originDataParams.get(entry.getKey());
                        if (value == null) {
                            if (filterNull) {
                                targetDataParams.put(entry.getKey(), null);
                            }
                        } else {
                            targetDataParams.put(entry.getKey(), value);
                        }
                    }
                }
                Gson gson = new Gson();
                return (R) gson.fromJson(gson.toJson(targetDataParams), target.getClass());
                //
                //                Map<Object, Object> map = new HashMap<>();
                //                map.putAll(getBeanParameters(target));
                //                Map originMap = getBeanParameters(origin);
                //                Object originValue;
                //                Object targetValue;
                //                for (Object key : getBeanParameters(originMap).keySet()) {
                //                    if (map.containsKey(key)) {
                //                        originValue = originMap.get(key);
                //                        targetValue = map.get(key);
                //                        if (originValue == null) {
                //                            if (!filterNull) {
                //                                map.put(key, originValue);
                //                            }
                //                        } else if (targetValue == null) {
                //                            map.put(key, originValue);
                //                        } else if (originValue instanceof CharSequence) {
                //                            if (targetValue instanceof CharSequence) {
                //                                map.put(key, originValue);
                //                            }
                //                        } else if (originValue instanceof Number) {
                //                            if (targetValue instanceof Number) {
                //                                map.put(key, originValue);
                //                            }
                //                        } else if (originValue instanceof Boolean) {
                //                            if (targetValue instanceof Boolean) {
                //                                map.put(key, originValue);
                //                            }
                //                        } else if (originValue instanceof Collection) {
                //                            if (targetValue instanceof Collection) {
                //                            }
                //                        } else {
                //                            map.put(key, originValue);
                //                        }
                //                    }
                //                }
                //                Gson gson = new Gson();
                //                return (R) gson.fromJson(gson.toJson(map), Class.forName(target.getClass().getName()));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 复制相同参数数据
     *
     * @param origin 原始
     * @param target 目标
     * @param <T>    原始泛型
     * @param <R>    目标泛型
     * @return 复制结果，可能为空
     */
    public <T, R> R copyWithTheParameters(T origin, R target) {
        return copyWithTheParameters(origin, target, false);
    }

    /**
     * 复制相同参数数据
     *
     * @param originList 原始数据集合
     * @param target     目标
     * @param filterNull 原始中包含空是否复制
     * @param <T>        原始泛型
     * @param <R>        目标泛型
     * @return 复制结果，可能为空
     */
    public <T, R> Collection<R> copyWithTheParameters(Collection<T> originList, R target, boolean filterNull) {
        List<R> list = new ArrayList<>();
        R data;
        for (T item : originList) {
            data = copyWithTheParameters(item, target, filterNull);
            if (data != null) {
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 复制相同参数数据
     *
     * @param originList 原始数据集合
     * @param target     目标
     * @param <T>        原始泛型
     * @param <R>        目标泛型
     * @return 复制结果，可能为空
     */
    public <T, R> Collection<R> copyWithTheParameters(Collection<T> originList, Class<R> target) {
        List<R> list = new ArrayList<>();
        R data;
        for (T item : originList) {
            data = copyWithTheParameters(item, target);
            if (data != null) {
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 获取实体类所有参数map集合
     *
     * @param <T>  实体泛型
     * @param data 实体类
     * @return 参数map集合
     */
    public <T> Map getBeanParameters(T data) {
        if (data != null) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(data);
                if (json != null) {
                    return gson.fromJson(json, Map.class);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取实体类痛的参数以及对应返回值
     *
     * @param beanClass 实体类Class
     * @param <T>       实体类泛型
     * @return 参数名以及返回类型map
     */
    public <T> Map<String, Class<?>> getBeanFieldsAndReturnType(Class<T> beanClass) {
        if (beanClass != null) {
            Map<String, Class<?>> map = new HashMap<>();
            for (Field item : beanClass.getDeclaredFields()) {
                map.put(item.getName(), item.getType());
            }
            return map;
        }
        return null;
    }
}
