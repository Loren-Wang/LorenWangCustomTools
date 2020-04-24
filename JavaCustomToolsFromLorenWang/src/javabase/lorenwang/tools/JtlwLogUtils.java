package javabase.lorenwang.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-04-15 下午 16:57:58
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class JtlwLogUtils {
    //日志控制器记录
    private static final ConcurrentHashMap<Class, Logger> logControllerMap =
            new ConcurrentHashMap<>();
    /**
     * 是否打印日志
     */
    public static boolean showLog = false;

    public static void logD(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.debug(tag + msg);
            }
        }
    }

    public static void logE(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.error(tag + msg);
            }
        }
    }

    public static void logI(String tag, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(tag)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(JtlwLogUtils.class);
            if (logger != null) {
                logger.info(tag + msg);
            }
        }
    }

    public static void logD(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.debug(msg);
            }
        }
    }

    public static void logE(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.error(msg);
            }
        }
    }

    public static void logI(Class cls, String msg) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(msg)) {
            Logger logger = getLogger(cls);
            if (logger != null) {
                logger.info(msg);
            }
        }
    }

    private static Logger getLogger(Class cls) {
        synchronized (logControllerMap) {
            Logger logger = logControllerMap.get(cls);
            if (logger == null && showLog) {
                logger = LoggerFactory.getLogger(cls);
                logControllerMap.put(cls, logger);
            }
            return logger;
        }
    }
}
