package javabase.lorenwang.dataparse;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Json工具类
 *
 * @author yynie
 */
public class JdplwJsonUtils {

    private static final String TAG = JdplwJsonUtils.class.getName();

    public static final String EMPTY_JSON = "{}";        // 空的JSON数据
    public static final String EMPTY_JSON_ARRAY = "[]"; // 空的数组(集合)数据
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS"; // 默认的日期，时间字段的格式化模式
    public static final boolean DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE = false; //是否排除未标记字段(默认值)

    public static final double SINCE_VERSION_10 = 1.0d;
    public static final double SINCE_VERSION_11 = 1.1d;
    public static final double SINCE_VERSION_12 = 1.2d;

    /*--------------------------------------------------------------------------
    | 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串
    --------------------------------------------------------------------------*/

    /**
     * 格式化成json字符串
     *
     * @param target 任意类型实体
     * @return 字符串
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE);
    }

    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE);
    }

    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE);
    }

    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串
     *
     * @param target                      目标对象
     * @param targetType                  目标对象的类型
     * @param isSerializeNulls            是否序列化Null值字段
     * @param version                     字段的版本号注解
     * @param datePattern                 日期字段的格式化模式
     * @param excludesFieldsWithoutExpose 是否排除未标注@Expose注解的字段
     * @return 目标对象的JSON格式的字符串
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern, boolean excludesFieldsWithoutExpose) {
        if (target == null)
            return EMPTY_JSON;
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls)
            builder.serializeNulls();
        if (version != null)
            builder.setVersion(version);
        if (datePattern == null || "".equals(datePattern))
            datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose)
            builder.excludeFieldsWithoutExposeAnnotation();
        return toJson(target, targetType, builder);
    }

    /**
     * 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串。
     * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，对象返回"{}"，集合或数组对象返回 "[]"， 其本基本类型，返回相应的基本值
     *
     * @param target     目标对象。
     * @param targetType 目标对象的类型
     * @param builder    可定制的Gson构建器
     * @return 目标对象的JSON格式的字符串
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null)
            return EMPTY_JSON;
        Gson gson;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception e) {
            if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?> || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }


    /*--------------------------------------------------------------------------
    | 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串
    --------------------------------------------------------------------------*/
    public static <T> T fromJson(String json, Class<T> cls) {
        return fromJson(json, cls, null);
    }

    /**
     * 格式化json字符串
     *
     * @param json  字符串
     * @param token 特殊类型
     * @param <T>   泛型
     * @return 特殊类型实体
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }

    /**
     * 将给定的JSON字符串转换成指定的类型对象
     *
     * @param json        给定的JSON字符串
     * @param cls         要转换的目标类
     * @param datePattern 日期格式
     * @param <T>         泛型
     * @return 给定的JSON字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, Class<T> cls, String datePattern) {
        if (json == null || "".equals(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将给定的JSON字符串转换成指定的类型对象
     *
     * @param json        给定的JSON字符串
     * @param token       类型
     * @param datePattern 日期格式
     * @param <T> 泛型
     * @return 给定的JSON字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (json == null || "".equals(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, token.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将Map转化为Json
     *
     * @param map 将Map转化为Json
     * @param cls 要转换的实体类class
     * @param <T> 实体类泛型
     * @return 实体类
     */
    public static <T> T fromJson(Map<String, Object> map, Class<T> cls) {
        String toJson = toJson(map);
        return fromJson(toJson, cls);
    }

    /**
     * 格式json集合
     *
     * @param json json字符串
     * @param cls  集合内实体cls
     * @param <T>  泛型
     * @return 集合
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            if (json != null) {
                List<T> dataList = new Gson().fromJson(json, new TypeToken<T>() {
                }.getType());
                if (dataList != null) {
                    for (T t : dataList) {
                        if (t != null) {
                            list.add(fromJson(toJson(t), cls));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 格式化json集合
     *
     * @param jsonArray json集合
     * @param cls       集合内实体cls
     * @param <T>       泛型
     * @return 集合
     */
    public static <T> List<T> fromJsonArray2(JsonArray jsonArray, Class<T> cls) {
        try {
            List<T> list = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                list.add(fromJson(jsonElement.getAsString(), cls));
            }
            return list;
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
