package kotlinbase.lorenwang.tools.extend;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;


/**
 * Json工具类
 *
 * @author yynie
 */
class JsonUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS"; // 默认的日期，时间字段的格式化模式
    public static final boolean DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE = false; //是否排除未标记字段(默认值)
    public static final String EMPTY_JSON = "{}";        // 空的JSON数据
    public static final String EMPTY_JSON_ARRAY = "[]"; // 空的数组(集合)数据

    /*--------------------------------------------------------------------------
    | 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串
    --------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------
    | 将给定的目标对象根据所指定的条件参数转换成JSON格式的字符串
    --------------------------------------------------------------------------*/
    public static <T> T fromJson(String json, Class<T> cls) {
        return fromJson(json, cls, null);
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
        GsonBuilder builder = getGsonBuilder();
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
     * @param <T>         泛型
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

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //时间处理
        gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> formatTimeToMillisecond(json.getAsString()));
        //价格处理
        gsonBuilder.registerTypeAdapter(BigDecimal.class, (JsonDeserializer<BigDecimal>) (json, typeOfT, context) -> {
            if (Pattern.matches("[\\d.]+", json.getAsString())) {
                return json.getAsBigDecimal();
            } else {
                return null;
            }
        });
        //数值类型处理
        gsonBuilder.registerTypeAdapter(Integer.class, (JsonDeserializer<Integer>) (json, typeOfT, context) -> {
            if (Pattern.matches("\\d+", json.getAsString()) || Pattern.matches("\\d+.0*", json.getAsString())) {
                return json.getAsInt();
            } else {
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(BigInteger.class, (JsonDeserializer<BigInteger>) (json, typeOfT, context) -> {
            if (Pattern.matches("\\d+", json.getAsString()) || Pattern.matches("\\d+.0*", json.getAsString())) {
                return json.getAsBigInteger();
            } else {
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(Double.class, (JsonDeserializer<Double>) (json, typeOfT, context) -> {
            if (Pattern.matches("[\\d.]+", json.getAsString())) {
                return json.getAsDouble();
            } else {
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(Float.class, (JsonDeserializer<Float>) (json, typeOfT, context) -> {
            if (Pattern.matches("[\\d.]+", json.getAsString())) {
                return json.getAsFloat();
            } else {
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(Short.class, (JsonDeserializer<Short>) (json, typeOfT, context) -> {
            if (Pattern.matches("\\d+", json.getAsString()) || Pattern.matches("\\d+.0*", json.getAsString())) {
                return json.getAsShort();
            } else {
                return null;
            }
        });
        //布尔数值处理
        gsonBuilder.registerTypeAdapter(Boolean.class, (JsonDeserializer<Boolean>) (json, typeOfT, context) -> {
            if (!KttlwExtendAnyKt.kttlwIsEmpty(json.getAsString())) {
                return json.getAsBoolean();
            } else {
                return null;
            }
        });
        return gsonBuilder;
    }

    private static Date formatTimeToMillisecond(String result) {
        if (result == null || result.isEmpty()) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());

        Date date;
        try {
            date = format.parse(result);
            return date;
        } catch (ParseException e) {
            try {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                format.setTimeZone(TimeZone.getDefault());
                date = format.parse(result);
                return date;
            } catch (ParseException e1) {
                try {
                    format = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault());
                    format.setTimeZone(TimeZone.getDefault());
                    date = format.parse(result);
                    return date;
                } catch (ParseException e2) {
                    try {
                        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        format.setTimeZone(TimeZone.getDefault());
                        date = format.parse(result);
                        return date;
                    } catch (ParseException e3) {
                        try {
                            format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                            format.setTimeZone(TimeZone.getDefault());
                            date = format.parse(result);
                            return date;
                        } catch (ParseException e4) {
                            try {
                                format = new SimpleDateFormat("yyyy", Locale.getDefault());
                                format.setTimeZone(TimeZone.getDefault());
                                date = format.parse(result);
                                return date;
                            } catch (ParseException ignored) {
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 格式化成json字符串
     *
     * @param target 任意类型实体
     * @return 字符串
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE);
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
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern,
            boolean excludesFieldsWithoutExpose) {
        if (target == null) {
            return EMPTY_JSON;
        }
        GsonBuilder builder = getGsonBuilder();
        if (isSerializeNulls) {
            builder.serializeNulls();
        }
        if (version != null) {
            builder.setVersion(version);
        }
        if (datePattern == null || "".equals(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
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
        if (target == null) {
            return EMPTY_JSON;
        }
        Gson gson;
        if (builder == null) {
            gson = getGsonBuilder().create();
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

}
