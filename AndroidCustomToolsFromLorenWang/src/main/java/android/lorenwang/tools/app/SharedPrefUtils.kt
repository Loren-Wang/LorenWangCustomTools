package android.lorenwang.tools.app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


/**
 * Android prefence文件读写操作工具类
 *
 * @author yynie
 */
class SharedPrefUtils(context: Context) {

    private val mPref: SharedPreferences

    init {
        mPref = PreferenceManager
                .getDefaultSharedPreferences(context.applicationContext)
    }

    companion object {
        private var baseUtils: SharedPrefUtils? = null

        fun getInstance(context: Context):SharedPrefUtils{
            if(baseUtils == null){
                baseUtils = SharedPrefUtils(context)
            }
            return baseUtils as SharedPrefUtils;
        }
    }


    fun getSharedPreferences(context: Context?, name: String): SharedPreferences? {
        return context?.applicationContext?.getSharedPreferences(name,
                Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(context: Context?, name: String, mode: Int): SharedPreferences? {
        return context?.applicationContext?.getSharedPreferences(name, mode)
    }

    fun clear(): Boolean {
        return clear(mPref)
    }

    fun clear(pref: SharedPreferences = mPref): Boolean {
        val editor = pref.edit()
        editor.clear()
        return editor.commit()
    }

    fun remove(key: String): Boolean {
        return remove(mPref, key)
    }


    fun remove(pref: SharedPreferences, key: String): Boolean {
        val editor = pref.edit()
        editor.remove(key)
        return editor.commit()
    }

    operator fun contains(key: String): Boolean {
        return contains(mPref, key)
    }

    fun contains(pref: SharedPreferences, key: String): Boolean {
        return pref.contains(key)
    }

    /*--------------------------------------------------------------------------
	| 读数据
	--------------------------------------------------------------------------*/
    fun getInt(key: String, defValue: Int): Int {
        return getInt(mPref, key, defValue)
    }


    fun getInt(pref: SharedPreferences, key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return getLong(mPref, key, defValue)
    }


    fun getLong(pref: SharedPreferences, key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun getString(key: String, defValue: String?): String? {
        return getString(mPref, key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>?): Set<String>? {
        return getStringSet(mPref, key, defValue)
    }


    fun getString(pref: SharedPreferences, key: String,
                  defValue: String?): String? {
        return pref.getString(key, defValue)
    }

    fun getStringSet(pref: SharedPreferences, key: String,
                     defValue: Set<String>?): Set<String>? {
        return pref.getStringSet(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getBoolean(mPref, key, defValue)
    }


    fun getBoolean(pref: SharedPreferences, key: String,
                   defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    /*--------------------------------------------------------------------------
	| 写数据
	--------------------------------------------------------------------------*/
    fun putInt(key: String, value: Int): Boolean {
        return putInt(mPref, key, value)
    }


    fun putInt(pref: SharedPreferences, key: String, value: Int): Boolean {
        val editor = pref.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    fun putLong(key: String, value: Long): Boolean {
        return putLong(mPref, key, value)
    }


    fun putLong(pref: SharedPreferences, key: String, value: Long): Boolean {
        val editor = pref.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun putString(key: String, value: String?): Boolean {
        return putString(mPref, key, value)
    }

    fun putString(key: String, value: Set<String>?): Boolean {
        return putStringSet(mPref, key, value)
    }

    fun putString(pref: SharedPreferences, key: String,
                  value: String?): Boolean {
        val editor = pref.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    fun putStringSet(pref: SharedPreferences, key: String,
                     value: Set<String>?): Boolean {
        val editor = pref.edit()
        editor.putStringSet(key, value)
        return editor.commit()
    }

    fun putBoolean(key: String, value: Boolean): Boolean {
        return putBoolean(mPref, key, value)
    }

    fun putBoolean(pref: SharedPreferences, key: String,
                   value: Boolean): Boolean {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }


}
