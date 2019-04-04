package android.lorenwang.tools.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * 创建时间：2019-04-04 下午 17:29:20
 * 创建人：王亮（Loren wang）
 * 功能作用：Android prefence文件读写操作工具类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class SharedPrefUtils {
    private static SharedPrefUtils sharedPrefUtils;
    private SharedPreferences mPref;

    public SharedPrefUtils(Context context) {
        mPref = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static SharedPrefUtils getInstance(Context context) {
        if (sharedPrefUtils == null && context != null) {
            sharedPrefUtils = new SharedPrefUtils(context);
        }
        return sharedPrefUtils;
    }


    public SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences(Context context, String name, int mode) {
        return context.getApplicationContext().getSharedPreferences(name, mode);
    }

    public Boolean clear() {
        return clear(mPref);
    }

    public Boolean clear(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }

    public Boolean remove(String key) {
        return remove(mPref, key);
    }


    public Boolean remove(SharedPreferences pref, String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        return editor.commit();
    }

    public Boolean contains(String key) {
        return contains(mPref, key);
    }

    public Boolean contains(SharedPreferences pref, String key) {
        return pref.contains(key);
    }

    /*--------------------------------------------------------------------------
	| 读数据
	--------------------------------------------------------------------------*/
    public int getInt(String key, int defValue) {
        return getInt(mPref, key, defValue);
    }


    public int getInt(SharedPreferences pref, String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public Long getLong(String key, Long defValue) {
        return getLong(mPref, key, defValue);
    }


    public Long getLong(SharedPreferences pref, String key, Long defValue) {
        return pref.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return getString(mPref, key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return getStringSet(mPref, key, defValue);
    }


    public String getString(SharedPreferences pref, String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public Set<String> getStringSet(SharedPreferences pref, String key, Set<String> defValue) {
        return pref.getStringSet(key, defValue);
    }

    public Boolean getBoolean(String key, Boolean defValue) {
        return getBoolean(mPref, key, defValue);
    }


    public Boolean getBoolean(SharedPreferences pref, String key, Boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    /*--------------------------------------------------------------------------
	| 写数据
	--------------------------------------------------------------------------*/
    public Boolean putInt(String key, int value) {
        return putInt(mPref, key, value);
    }


    public Boolean putInt(SharedPreferences pref, String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public Boolean putLong(String key, Long value) {
        return putLong(mPref, key, value);
    }


    public Boolean putLong(SharedPreferences pref, String key, Long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public Boolean putString(String key, String value) {
        return putString(mPref, key, value);
    }

    public Boolean putString(String key, Set<String> value) {
        return putStringSet(mPref, key, value);
    }

    public Boolean putString(SharedPreferences pref, String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public Boolean putStringSet(SharedPreferences pref, String key, Set<String> value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    public Boolean putBoolean(String key, Boolean value) {
        return putBoolean(mPref, key, value);
    }

    public Boolean putBoolean(SharedPreferences pref, String key, Boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

}
