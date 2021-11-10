package android.lorenwang.tools.base;

import android.lorenwang.tools.file.AtlwFileOptionUtil;
import android.util.Log;

import java.io.File;

import javabase.lorenwang.tools.JtlwLogUtil;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;
import javabase.lorenwang.tools.common.JtlwDateTimeUtil;


/**
 * 日志工具类
 */
public final class AtlwLogUtil extends JtlwLogUtil {

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
            if (JtlwCheckVariateUtil.getInstance().isEmpty(saveFile)) {
                return;
            }

            if (JtlwCheckVariateUtil.getInstance().isEmpty(type)) {
                type = "";
            }
            if (JtlwCheckVariateUtil.getInstance().isEmpty(tag)) {
                tag = "";
            }
            if (JtlwCheckVariateUtil.getInstance().isEmpty(message)) {
                message = "";
            }

            AtlwFileOptionUtil.getInstance().writeToFile(true, saveFile, "logType:" + type + "  logTag:" + tag + "\nlogMessage:" + message + "\n\n",
                    "utf-8", true);
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
                AtlwFileOptionUtil.getInstance().createDirectory(true, logSaveFileDirPath, false);
                //生成文件名称
                String fileName = JtlwDateTimeUtil.getInstance().getFormatDateNowTime("yyyy_mm_dd_hh_MM_ss.log");
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
