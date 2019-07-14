package android.lorenwang.tools.base;

import android.lorenwang.tools.AtlwSetting;
import android.util.Log;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;


/**
 * 日志工具类
 *
 */
public final class AtlwLogUtils {

	// ------------------------------------------------------ Constants
	private static final String DEFAULT_TAG = "";
	private static final String DEFAULT_MSG = "";

	/**
	 * Priority constant for log
	 */
	public static final int VERBOSE = Log.VERBOSE;
	public static final int DEBUG = Log.DEBUG;
	public static final int INFO = Log.INFO;
	public static final int WARN = Log.WARN;
	public static final int ERROR = Log.ERROR;
	public static final int ASSERT = Log.ASSERT;
	private static final int DISABLE = 1024;

	private static final String LOG_FILE_NAME = "ct.log";
	// ------------------------------------------------------ Fields
	//private static boolean isDebuggable = Settings.isDebuggable;
	public static boolean isDebuggable = AtlwSetting.isDebug;
	private static String saveLogNamePath = AtlwSetting.debugLogFileSavePath;//日志保存地址

	// ------------------------------------------------------ Public Methods

	// verbose
	public static void logV(String msg) {
		if (isDebuggable) {
			Log.v(DEFAULT_TAG, msg);
			saveLog("v",DEFAULT_TAG, msg);
		}
	}

	public static void logV(String tag, String msg) {
		if (isDebuggable) {
			Log.v(tag, msg);
			saveLog("v",tag, msg);
		}
	}

	public static void logV(Throwable tr) {
		if (isDebuggable) {
			Log.v(DEFAULT_TAG, DEFAULT_MSG, tr);
			saveLog("v",DEFAULT_TAG, DEFAULT_MSG);
		}
	}

	public static void logV(String tag, String msg, Throwable tr) {
		if (isDebuggable) {
			Log.v(tag, msg, tr);
			saveLog("v",tag, msg);
		}
	}

	// debug
	public static void logD(String msg) {
		if (isDebuggable) {
			Log.d(DEFAULT_TAG, msg);
			saveLog("d",DEFAULT_TAG, msg);
		}
	}

	public static void logD(String tag, String msg) {
		if (isDebuggable) {
			Log.d(tag, msg);
			saveLog("d",tag, msg);
		}
	}

	public static void logD(Throwable tr) {
		if (isDebuggable) {
			Log.d(DEFAULT_TAG, DEFAULT_MSG, tr);
			saveLog("d",DEFAULT_TAG, DEFAULT_MSG);
		}
	}

	public static void logD(String tag, String msg, Throwable tr) {
		if (isDebuggable) {
			Log.d(tag, msg, tr);
			saveLog("d",tag, msg);
		}
	}

	// info
	public static void logI(String msg) {
		if (isDebuggable) {
			Log.i(DEFAULT_TAG, msg);
			saveLog("i",DEFAULT_TAG, msg);
		}
	}

	public static void logI(String tag, String msg) {
		if (isDebuggable) {
			Log.i(tag, msg);
			saveLog("i",tag, msg);
		}
	}

	public static void logI(Throwable tr) {
		if (isDebuggable) {
			Log.i(DEFAULT_TAG, DEFAULT_MSG, tr);
			saveLog("i",DEFAULT_TAG, DEFAULT_MSG);
		}
	}

	public static void logI(String tag, String msg, Throwable tr) {
		if (isDebuggable) {
			Log.i(tag, msg, tr);
			saveLog("i",tag, msg);
		}
	}

	// warning
	public static void logW(String msg) {
		if (isDebuggable) {
			Log.w(DEFAULT_TAG, msg);
			saveLog("w",DEFAULT_TAG, msg);
		}
	}

	public static void logW(String tag, String msg) {
		if (isDebuggable) {
			Log.w(tag, msg);
			saveLog("w",tag, msg);
		}
	}

	public static void logW(Throwable tr) {
		if (isDebuggable) {
			Log.w(DEFAULT_TAG, DEFAULT_MSG, tr);
			saveLog("w",DEFAULT_TAG, DEFAULT_MSG);
		}
	}

	public static void logW(String tag, String msg, Throwable tr) {
		if (isDebuggable) {
			Log.w(tag, msg, tr);
			saveLog("w",tag, msg);
		}
	}

	// error
	public static void logE(String msg) {
		if (isDebuggable) {
			Log.e(DEFAULT_TAG, msg);
			saveLog("error",DEFAULT_TAG, msg);
		}
	}

	public static void logE(String tag, String msg) {
		if (isDebuggable) {
			Log.e(tag, msg);
			saveLog("error",tag, msg);
		}
	}

	public static void logE(Throwable tr) {
		if (isDebuggable) {
			Log.e(DEFAULT_TAG, DEFAULT_MSG, tr);
			saveLog("error",DEFAULT_TAG,DEFAULT_MSG);
		}
	}

	public static void logE(String tag, Throwable tr) {
		if (isDebuggable) {
			Log.e(tag, DEFAULT_MSG, tr);
			saveLog("error",tag,DEFAULT_MSG);
		}
	}

	public static void logE(String tag, String msg, Throwable tr) {
		if (isDebuggable) {
			Log.e(tag, msg, tr);
			saveLog("error",tag,msg);
		}
	}


	private static void saveLog(String type, String tag, String message){
		try {
			if(JtlwCheckVariateUtils.getInstance().isEmpty(saveLogNamePath)){
				return;
			}

			if(JtlwCheckVariateUtils.getInstance().isEmpty(type)){
				type = "";
			}
			if(JtlwCheckVariateUtils.getInstance().isEmpty(tag)){
				tag = "";
			}
			if(JtlwCheckVariateUtils.getInstance().isEmpty(message)){
				message = "";
			}

//			IOUtils.addContentToFile(saveLogNamePath, "logType:" + type + "  logTag:" + tag + "\nlogMessage:" + message + "\n\n");
		}catch (Exception e){
			Log.e("error","error");
		}

	}

	// -------------------------------------------------------- Private Methods
	private boolean isLoggable() {
		return true;
	}

}
