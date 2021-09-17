package android.lorenwang.tools.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.lorenwang.tools.AtlwConfig;
import android.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javabase.lorenwang.dataparse.JdplwJsonUtils;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;

/**
 * 功能作用：Android SharedPreference文件读写操作工具类
 * 初始注释时间： 2021/9/16 15:50
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取指定名称的共享存储（模式为私有）--getSharedPreferences(name)
 * 获取指定名称指定模式的共享存储--getSharedPreferences(name,mode)
 * 清除当前默认共享存储--clear()
 * 清除指定共享存储--clear(pref)
 * 移除默认key的共享存储内容--remove(key)
 * 移除指定存储的key的共享存储内容--remove(pref,key)
 * 默认共享存储是否包含该key内容--contains(key)
 * 指定存储的key的共享存储是否存在--contains(pref,key)
 * 获取默认共享存储整型数据--getInt(key,defValue)
 * 获取共享存储整型数据--getInt(pref,key,defValue)
 * 获取默认共享存储长整型数据--getLong(key,defValue)
 * 获取共享存储长整型数据--getLong(pref,key,defValue)
 * 获取默认共享存储字符串数据--getString(key,defValue)
 * 获取共享存储字符串数据--getString(pref,key,defValue)
 * 获取默认共享存储Set<String>数据--getStringSet(key,defValue)
 * 获取共享存储Set<String>数据--getStringSet(pref,key,defValue)
 * 获取默认共享存储布尔值数据--getBoolean(key,defValue)
 * 获取共享存储布尔值数据--getBoolean(pref,key,defValue)
 * 向默认共享存储整型数据--putInt(key,value)
 * 向指定共享存储整型数据--putInt(pref,key,value)
 * 向默认共享存储长整型数据--putLong(key,value)
 * 向指定共享存储长整型数据--putLong(pref,key,value)
 * 向默认共享存储字符串数据--putString(key,value)
 * 向指定共享存储字符串数据--putString(pref,key,value)
 * 向默认共享存储Set<String>数据--putStringSet(key,value)
 * 向指定共享存储Set<String>数据--putStringSet(pref,key,value)
 * 向默认共享存储布尔值数据--putBoolean(key,value)
 * 向指定共享存储布尔值数据--putBoolean(pref,key,value)
 * 插入用户相关记录,格式id_time_value--putUserRecord(id,key,value)
 * 获取用户相关记录,格式id_time_value--getUserRecord(id,key,clearOld,today)
 * 插入每日相关记录--putDayRecord(key)
 * 获取每日相关记录,格式id_time_value--getDayRecord(key)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwSharedPrefUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwSharedPrefUtil optionsInstance;
    private final SharedPreferences mPref;
    /**
     * 记录的时间格式
     */
    private String recordTimePattern = "yyyyMMdd";

    private AtlwSharedPrefUtil() {
        mPref = PreferenceManager.getDefaultSharedPreferences(AtlwConfig.nowApplication);
    }

    public static AtlwSharedPrefUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwSharedPrefUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwSharedPrefUtil();
                }
            }
        }
        return optionsInstance;
    }

    private static volatile AtlwSharedPrefUtil atlwSharedPrefUtils;

    /**
     * 获取指定名称的共享存储（模式为私有）
     *
     * @param name 名称
     * @return 共享存储实例
     */
    public SharedPreferences getSharedPreferences(String name) {
        return AtlwConfig.nowApplication.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取指定名称指定模式的共享存储
     *
     * @param name 名称
     * @param mode 模式
     * @return 共享存储实例
     */
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return AtlwConfig.nowApplication.getSharedPreferences(name, mode);
    }

    /**
     * 清除当前默认共享存储
     *
     * @return 结果
     */
    public boolean clear() {
        return clear(mPref);
    }

    /**
     * 清除指定共享存储
     *
     * @param pref 共享存储实例
     * @return 结果
     */
    public boolean clear(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }

    /**
     * 移除默认key的共享存储内容
     *
     * @param key 指定key
     * @return 结果
     */
    public boolean remove(String key) {
        return remove(mPref, key);
    }

    /**
     * 移除指定存储的key的共享存储内容
     *
     * @param pref 指定存储
     * @param key  key值
     * @return 结果
     */
    public boolean remove(SharedPreferences pref, String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 默认共享存储是否包含该key内容
     *
     * @param key key值
     * @return 结果
     */
    public boolean contains(String key) {
        return contains(mPref, key);
    }

    /**
     * 指定存储的key的共享存储是否存在
     *
     * @param pref 指定存储
     * @param key  key值
     * @return 结果
     */
    public boolean contains(SharedPreferences pref, String key) {
        return pref.contains(key);
    }

    /**
     * 获取默认共享存储整型数据
     *
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public int getInt(String key, int defValue) {
        return getInt(mPref, key, defValue);
    }

    /**
     * 获取共享存储整型数据
     *
     * @param pref     指定的共享存储
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public int getInt(SharedPreferences pref, String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    /**
     * 获取默认共享存储长整型数据
     *
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public Long getLong(String key, long defValue) {
        return getLong(mPref, key, defValue);
    }

    /**
     * 获取共享存储长整型数据
     *
     * @param pref     指定的共享存储
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public Long getLong(SharedPreferences pref, String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    /**
     * 获取默认共享存储字符串数据
     *
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public String getString(String key, String defValue) {
        return getString(mPref, key, defValue);
    }

    /**
     * 获取共享存储字符串数据
     *
     * @param pref     指定的共享存储
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public String getString(SharedPreferences pref, String key, String defValue) {
        return pref.getString(key, defValue);
    }

    /**
     * 获取默认共享存储Set<String>数据
     *
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public Set<String> getStringSet(String key, Set<String> defValue) {
        return getStringSet(mPref, key, defValue);
    }

    /**
     * 获取共享存储Set<String>数据
     *
     * @param pref     指定的共享存储
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public Set<String> getStringSet(SharedPreferences pref, String key, Set<String> defValue) {
        return pref.getStringSet(key, defValue);
    }

    /**
     * 获取默认共享存储布尔值数据
     *
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public boolean getBoolean(String key, boolean defValue) {
        return getBoolean(mPref, key, defValue);
    }

    /**
     * 获取共享存储布尔值数据
     *
     * @param pref     指定的共享存储
     * @param key      key值
     * @param defValue 默认值
     * @return 整型数值
     */
    public boolean getBoolean(SharedPreferences pref, String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    /**
     * 向默认共享存储整型数据
     *
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putInt(String key, int value) {
        return putInt(mPref, key, value);
    }

    /**
     * 向指定共享存储整型数据
     *
     * @param pref  指定的共享存储
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putInt(SharedPreferences pref, String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 向默认共享存储长整型数据
     *
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putLong(String key, long value) {
        return putLong(mPref, key, value);
    }

    /**
     * 向指定共享存储长整型数据
     *
     * @param pref  指定的共享存储
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putLong(SharedPreferences pref, String key, long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 向默认共享存储字符串数据
     *
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putString(String key, String value) {
        return putString(mPref, key, value);
    }

    /**
     * 向指定共享存储字符串数据
     *
     * @param pref  指定的共享存储
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putString(SharedPreferences pref, String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 向默认共享存储Set<String>数据
     *
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putStringSet(String key, Set<String> value) {
        return putStringSet(mPref, key, value);
    }

    /**
     * 向指定共享存储Set<String>数据
     *
     * @param pref  指定的共享存储
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putStringSet(SharedPreferences pref, String key, Set<String> value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * 向默认共享存储布尔值数据
     *
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putBoolean(String key, Boolean value) {
        return putBoolean(mPref, key, value);
    }

    /**
     * 向指定共享存储布尔值数据
     *
     * @param pref  指定的共享存储
     * @param key   key值
     * @param value 数据
     * @return 整型数值
     */
    public boolean putBoolean(SharedPreferences pref, String key, Boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 插入用户相关记录,格式id_time_value
     * @return 插入结果
     */
    public boolean putUserRecord(@NotNull String id, @NotNull String key, @NotNull String value) {
        //清除旧数据
        getUserRecord(id, key, true, true);
        List<String> list;
        String old = getString(key, null);
        if (old == null || old.isEmpty()) {
            list = new ArrayList<>();
        } else {
            list = JdplwJsonUtils.fromJsonArray(old, String.class);
        }
        list.add(id + "_" + JtlwDateTimeUtils.getInstance().getFormatDateNowTime(recordTimePattern) + "_" + value);
        return putString(key, JdplwJsonUtils.toJson(list));
    }

    /**
     * 获取用户相关记录,格式id_time_value
     *
     * @param clearOld 是否清除旧数据
     * @param today    是否要返回今天的数据
     */
    public String getUserRecord(@NotNull String id, @NotNull String key, boolean clearOld, boolean today) {
        List<String> list;
        String old = getString(key, null);
        if (old == null || old.isEmpty()) {
            list = new ArrayList<>();
        } else {
            list = JdplwJsonUtils.fromJsonArray(old, String.class);
        }
        String[] split;
        //判断是否要清除旧数据
        if (clearOld) {
            //当前记录
            String[] current = null;
            //其他数据
            List<String> otherList = new ArrayList<>();
            for (String item : list) {
                split = item.split("_");
                if (split.length == 3) {
                    //判断当前处理的数据是否是要查找的数据
                    if (split[0].equals(id)) {
                        //判断是否是要当天的数据，如果不是要当天的数据则读取数据返回，同时加入到其他数据列表
                        if (today) {
                            //当前需要的是当天的数据，如果是当天的数据则返回并加入到其他列表中，否则不处理，清除掉之前时间的数据，保证数据量最小
                            if (JtlwDateTimeUtils.getInstance().getFormatDateNowTime(recordTimePattern) == split[1]) {
                                current = split;
                                otherList.add(item);
                            }
                        } else {
                            current = split;
                            otherList.add(item);
                        }
                    } else {
                        otherList.add(item);
                    }
                } else {
                    otherList.add(item);
                }
            }
            //清除数据
            putString(key, JdplwJsonUtils.toJson(otherList));
            //判断是否要返回数据
            if (current != null) {
                return current[2];
            }
        } else {
            for (String item : list) {
                split = item.split("_");
                if (split.length == 3) {
                    if (split[0] == id) {
                        if (today) {
                            if (JtlwDateTimeUtils.getInstance().getFormatDateNowTime(recordTimePattern).equals(split[1])) {
                                return split[2];
                            }
                        } else {
                            return split[2];
                        }
                    }
                }
            }
        }
        return null;

    }

    /**
     * 插入每日相关记录
     * @return 插入结果
     */
    public boolean putDayRecord(String key) {
       return putString(key, JtlwDateTimeUtils.getInstance().getFormatDateNowTime(recordTimePattern));
    }

    /**
     * 获取每日相关记录,格式id_time_value
     */
    public String getDayRecord(String key) {
        String time = getString(key, null);
        if (JtlwDateTimeUtils.getInstance().getFormatDateNowTime(recordTimePattern).equals(time)) {
            return time;
        } else {
            return null;
        }
    }
}
