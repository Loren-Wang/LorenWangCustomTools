package javabase.lorenwang.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;


/**
 * 功能作用：日志打印工具类
 * 初始注释时间： 2020/8/27 11:38 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwLogUtils {
    /**
     * 当前实例
     */
    public static JtlwLogUtils logUtils = new JtlwLogUtils();
    /**
     * 是否打印日志
     */
    protected boolean showLog = false;
    /**
     * 日志保存地址
     */
    protected String logSaveFileDirPath;
    protected File logSaveFile;
    protected final String DEFAULT_TAG = "";
    protected final String DEFAULT_MSG = "";

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setLogSaveFileDirPath(String logSaveFileDirPath) {
        this.logSaveFileDirPath = logSaveFileDirPath;
    }

    public String getLogSaveFileDirPath() {
        return logSaveFileDirPath;
    }

    /**
     * 日志控制器记录
     */
    private ConcurrentHashMap logControllerMap;

    public void logV(String msg) {

    }

    public void logV(String tag, String msg) {

    }

    public void logV(Throwable tr) {

    }

    public void logD(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.debug(tag + msg);
            }
        }
    }


    public void logE(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.error(tag + msg);
            }
        }
    }

    public void logE(Throwable e) {
        Logger logger = getLogger(JtlwLogUtils.class);
        if (logger != null) {
            logger.error(DEFAULT_TAG + (e != null && e.getMessage() != null ? e.getMessage() :
                    DEFAULT_MSG));
        }
    }

    public void logI(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.info(tag + msg);
            }
        }
    }

    public void logD(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.debug(msg);
            }
        }
    }

    public void logD(String msg) {

    }

    public void logD(String tag, String msg, Throwable tr) {

    }

    public void logE(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.error(msg);
            }
        }
    }

    public void logD(Throwable tr) {

    }

    public void logI(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.info(msg);
            }
        }
    }

    public void logI(Throwable tr) {
    }

    private Logger getLogger(Class cls) {
        if (logControllerMap == null) {
            logControllerMap = new ConcurrentHashMap<Class, Logger>();
        }
        synchronized (logControllerMap) {
            Logger logger = (Logger) logControllerMap.get(cls);
            if (logger == null && showLog) {
                logger = LoggerFactory.getLogger(cls);
                logControllerMap.put(cls, logger);
            }
            return logger;
        }
    }
}
