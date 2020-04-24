package android.lorenwang.tools.base;

import android.lorenwang.tools.AtlwSetting;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.util.Log;

import java.io.File;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;


/**
 * 日志工具类
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
    /**
     * 日志保存的File
     */
    private static File logSaveFile;

    // ------------------------------------------------------ Public Methods

    // verbose
    public static void logV(String msg) {
        if (isDebuggable) {
            Log.v(DEFAULT_TAG, msg);
            saveLog("v", DEFAULT_TAG, msg);
        }
    }

    public static void logV(String tag, String msg) {
        if (isDebuggable) {
            Log.v(tag, msg);
            saveLog("v", tag, msg);
        }
    }

    public static void logV(Throwable tr) {
        if (isDebuggable) {
            Log.v(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("v", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    public static void logV(String tag, String msg, Throwable tr) {
        if (isDebuggable) {
            Log.v(tag, msg, tr);
            saveLog("v", tag, msg);
        }
    }

    // debug
    public static void logD(String msg) {
        if (isDebuggable) {
            Log.d(DEFAULT_TAG, msg);
            saveLog("d", DEFAULT_TAG, msg);
        }
    }

    public static void logD(String tag, String msg) {
        if (isDebuggable) {
            Log.d(tag, msg);
            saveLog("d", tag, msg);
        }
    }

    public static void logD(Throwable tr) {
        if (isDebuggable) {
            Log.d(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("d", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    public static void logD(String tag, String msg, Throwable tr) {
        if (isDebuggable) {
            Log.d(tag, msg, tr);
            saveLog("d", tag, msg);
        }
    }

    // info
    public static void logI(String msg) {
        if (isDebuggable) {
            Log.i(DEFAULT_TAG, msg);
            saveLog("i", DEFAULT_TAG, msg);
        }
    }

    public static void logI(String tag, String msg) {
        if (isDebuggable) {
            Log.i(tag, msg);
            saveLog("i", tag, msg);
        }
    }

    public static void logI(Throwable tr) {
        if (isDebuggable) {
            Log.i(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("i", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    public static void logI(String tag, String msg, Throwable tr) {
        if (isDebuggable) {
            Log.i(tag, msg, tr);
            saveLog("i", tag, msg);
        }
    }

    // warning
    public static void logW(String msg) {
        if (isDebuggable) {
            Log.w(DEFAULT_TAG, msg);
            saveLog("w", DEFAULT_TAG, msg);
        }
    }

    public static void logW(String tag, String msg) {
        if (isDebuggable) {
            Log.w(tag, msg);
            saveLog("w", tag, msg);
        }
    }

    public static void logW(Throwable tr) {
        if (isDebuggable) {
            Log.w(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("w", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    public static void logW(String tag, String msg, Throwable tr) {
        if (isDebuggable) {
            Log.w(tag, msg, tr);
            saveLog("w", tag, msg);
        }
    }

    // error
    public static void logE(String msg) {
        if (isDebuggable) {
            Log.e(DEFAULT_TAG, msg);
            saveLog("error", DEFAULT_TAG, msg);
        }
    }

    public static void logE(String tag, String msg) {
        if (isDebuggable) {
            Log.e(tag, msg);
            saveLog("error", tag, msg);
        }
    }

    public static void logE(Throwable tr) {
        if (isDebuggable) {
            Log.e(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("error", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    public static void logE(String tag, Throwable tr) {
        if (isDebuggable) {
            Log.e(tag, DEFAULT_MSG, tr);
            saveLog("error", tag, DEFAULT_MSG);
        }
    }

    public static void logE(String tag, String msg, Throwable tr) {
        if (isDebuggable) {
            Log.e(tag, msg, tr);
            saveLog("error", tag, msg);
        }
    }


    private static void saveLog(String type, String tag, String message) {
        try {
            File saveFile = getSaveFile();
            if (JtlwCheckVariateUtils.getInstance().isEmpty(saveFile)) {
                return;
            }

            if (JtlwCheckVariateUtils.getInstance().isEmpty(type)) {
                type = "";
            }
            if (JtlwCheckVariateUtils.getInstance().isEmpty(tag)) {
                tag = "";
            }
            if (JtlwCheckVariateUtils.getInstance().isEmpty(message)) {
                message = "";
            }

//            "logType:" + type + "  logTag:" + tag +/*
//                    "\nlogMessage:" + message + "\n\n"*/
            AtlwFileOptionUtils.getInstance().writeToFile(true, saveFile
                    , ""
                    , "utf-8", true);
        } catch (Exception e) {
            Log.e("error", "error");
        }

    }

    // -------------------------------------------------------- Private Methods
    private boolean isLoggable() {
        return true;
    }

    /**
     * 获取保存文件File
     *
     * @return 要保存到的文件
     */
    private static File getSaveFile() {
        if (logSaveFile != null) {
            return logSaveFile;
        }
        if (AtlwSetting.debugLogFileSavePath == null) {
            return null;
        }
        File file = new File(AtlwSetting.debugLogFileSavePath);
        //创建文件夹
        AtlwFileOptionUtils.getInstance().createDirectory(true,
                AtlwSetting.debugLogFileSavePath, file.isDirectory());
        //返回文件夹判断
        if (!file.isFile()) {
            String fileName = JtlwDateTimeUtils.getInstance().getFormatDateNowTime(
                    "yyyy_mm_dd_hh_MM_ss.log");
            if (AtlwSetting.debugLogFileSavePath.lastIndexOf("/") == AtlwSetting.debugLogFileSavePath.length() - 1) {
                file = new File(AtlwSetting.debugLogFileSavePath + fileName);
            } else {
                file = new File(AtlwSetting.debugLogFileSavePath + "/" + fileName);
            }
        }
        logSaveFile = file;
        return logSaveFile;
    }

}
