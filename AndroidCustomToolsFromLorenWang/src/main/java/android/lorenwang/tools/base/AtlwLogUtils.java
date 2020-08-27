package android.lorenwang.tools.base;

import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.util.Log;

import java.io.File;

import javabase.lorenwang.tools.JtlwLogUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.common.JtlwDateTimeUtils;


/**
 * 日志工具类
 */
public final class AtlwLogUtils extends JtlwLogUtils {

    @Override
    public void logV(String msg) {
        if (showLog) {
            Log.v(DEFAULT_TAG, msg);
            saveLog("V", DEFAULT_TAG, msg);
        }
    }

    @Override
    public void logV(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
            saveLog("v", tag, msg);
        }
    }

    @Override
    public void logV(Throwable tr) {
        if (showLog) {
            Log.v(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("v", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    @Override
    public void logD(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
            saveLog("D", tag, msg);
        }
    }

    @Override
    public void logD(Throwable tr) {
        if (showLog) {
            Log.d(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("d", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    @Override
    public void logE(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
            saveLog("E", tag, msg);
        }
    }

    @Override
    public void logI(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
            saveLog("I", tag, msg);
        }
    }

    @Override
    public void logD(Class cls, String msg) {
        if (showLog) {
            Log.v(cls.getSimpleName(), msg);
            saveLog("D", cls.getSimpleName(), msg);
        }
    }

    @Override
    public void logD(String msg) {
        if (showLog) {
            Log.d(DEFAULT_TAG, msg);
            saveLog("D", DEFAULT_TAG, msg);
        }
    }

    @Override
    public void logD(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.d(tag, msg, tr);
            saveLog("D", tag, msg);
        }
    }

    @Override
    public void logE(Class cls, String msg) {
        if (showLog) {
            Log.v(cls.getSimpleName(), msg);
            saveLog("E", cls.getSimpleName(), msg);
        }
    }

    @Override
    public void logE(Throwable tr) {
        if (showLog) {
            Log.e(DEFAULT_TAG, DEFAULT_MSG, tr);
            saveLog("error", DEFAULT_TAG, DEFAULT_MSG);
        }
    }

    @Override
    public void logI(Class cls, String msg) {
        if (showLog) {
            Log.v(cls.getSimpleName(), msg);
            saveLog("I", cls.getSimpleName(), msg);
        }
    }



//
//    public static void logD(String tag, String msg) {
//        if (isDebuggable) {
//            Log.d(tag, msg);
//            saveLog("d", tag, msg);
//        }
//    }
//


//
//    // info
//    public static void logI(String msg) {
//        if (isDebuggable) {
//            Log.i(DEFAULT_TAG, msg);
//            saveLog("i", DEFAULT_TAG, msg);
//        }
//    }
//
//    public static void logI(String tag, String msg) {
//        if (isDebuggable) {
//            Log.i(tag, msg);
//            saveLog("i", tag, msg);
//        }
//    }
//

//
//    public static void logI(String tag, String msg, Throwable tr) {
//        if (isDebuggable) {
//            Log.i(tag, msg, tr);
//            saveLog("i", tag, msg);
//        }
//    }
//
//    // warning
//    public static void logW(String msg) {
//        if (isDebuggable) {
//            Log.w(DEFAULT_TAG, msg);
//            saveLog("w", DEFAULT_TAG, msg);
//        }
//    }
//
//    public static void logW(String tag, String msg) {
//        if (isDebuggable) {
//            Log.w(tag, msg);
//            saveLog("w", tag, msg);
//        }
//    }
//
//    public static void logW(Throwable tr) {
//        if (isDebuggable) {
//            Log.w(DEFAULT_TAG, DEFAULT_MSG, tr);
//            saveLog("w", DEFAULT_TAG, DEFAULT_MSG);
//        }
//    }
//
//    public static void logW(String tag, String msg, Throwable tr) {
//        if (isDebuggable) {
//            Log.w(tag, msg, tr);
//            saveLog("w", tag, msg);
//        }
//    }
//
//    // error
//    public static void logE(String msg) {
//        if (isDebuggable) {
//            Log.e(DEFAULT_TAG, msg);
//            saveLog("error", DEFAULT_TAG, msg);
//        }
//    }
//
//    public static void logE(String tag, String msg) {
//        if (isDebuggable) {
//            Log.e(tag, msg);
//            saveLog("error", tag, msg);
//        }
//    }
//

//
//    public static void logE(String tag, Throwable tr) {
//        if (isDebuggable) {
//            Log.e(tag, DEFAULT_MSG, tr);
//            saveLog("error", tag, DEFAULT_MSG);
//        }
//    }
//
//    public static void logE(String tag, String msg, Throwable tr) {
//        if (isDebuggable) {
//            Log.e(tag, msg, tr);
//            saveLog("error", tag, msg);
//        }
//    }
//
//

    /**
     * 保存log信息
     *
     * @param type    log类型
     * @param tag     log的tag标记
     * @param message log信息
     */
    private void saveLog(String type, String tag, String message) {
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

            AtlwFileOptionUtils.getInstance().writeToFile(true, saveFile
                    , "logType:" + type + "  logTag:" + tag + "\nlogMessage:" + message + "\n\n"
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
    private File getSaveFile() {
        if (logSaveFile != null) {
            return logSaveFile;
        }
        if (logSaveFileDirPath != null) {
            try {
                //创建日志文件夹
                AtlwFileOptionUtils.getInstance().createDirectory(true, logSaveFileDirPath,false);
                //生成文件名称
                String fileName = JtlwDateTimeUtils.getInstance().getFormatDateNowTime(
                        "yyyy_mm_dd_hh_MM_ss.log");
                //生成文件file
                if (logSaveFileDirPath.lastIndexOf("/") == logSaveFileDirPath.length() - 1) {
                    logSaveFile = new File(logSaveFileDirPath + fileName);
                } else {
                    logSaveFile = new File(logSaveFileDirPath + "/" + fileName);
                }
                return logSaveFile;
            } catch (Exception ignore) {
                return null;
            }
        }
        return null;
    }

}
