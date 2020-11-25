package javabase.lorenwang.common_base_frame.utils

import javabase.lorenwang.common_base_frame.SbcbflwCommon
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * 功能作用：日志打印工具
 * 创建时间：2019-09-12 上午 11:00:35
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：子类必须将构造函数私有化
 */
open class SbcbflwLogUtils{
    /**
     * 运行标签
     */
    protected val runTag = "runTag---"

    //日志控制器记录
    private val logControllerMap = ConcurrentHashMap<Class<*>, Logger>()

    /**
     * 打印debug日志
     *
     * @param cls 打印日志的class类
     * @param msg 日志内容
     * @param logMust 必须显示log
     */
    fun logD(cls: Class<*>, msg: String, logMust: Boolean = false) {
        val logger = getLogger(cls, logMust)
        synchronized(logControllerMap) {
            logger?.debug("${runTag}${cls.simpleName}:::${msg}")
        }
    }

    /**
     * 打印info日志
     *
     * @param cls 打印日志的class类
     * @param msg 日志内容
     * @param logMust 必须显示log
     */
    fun logI(cls: Class<*>, msg: String, logMust: Boolean = false) {
        val logger = getLogger(cls, logMust)
        synchronized(logControllerMap) {
            logger?.info("${runTag}${cls.simpleName}:::${msg}")
        }
    }

    /**
     * 打印error日志
     *
     * @param cls 打印日志的class类
     * @param msg 日志内容
     * @param logMust 必须显示log
     */
    fun logE(cls: Class<*>, msg: String, logMust: Boolean = false) {
        val logger = getLogger(cls, logMust)
        synchronized(logControllerMap) {
            logger?.error("${runTag}${cls.simpleName}:::${msg}")
        }
    }

    /**
     * 打印操作日志，是需要进行日志保存的
     *
     * @param cls 打印日志的class类
     * @param msg 日志内容
     * @param logMust 必须显示log
     */
    fun logOptions(cls: Class<*>, msg: String, logMust: Boolean = false) {
        val logger = getLogger(cls, logMust)
        synchronized(logControllerMap) {
            logger?.error("${runTag}${cls.simpleName}:::${msg}")
        }
    }

    /**
     * 获取logger
     *
     * @param cls class
     * @return cls相应的logger
     */
    private fun getLogger(cls: Class<*>, logMust: Boolean): Logger? {
        synchronized(logControllerMap) {
            var logger: Logger? = logControllerMap[cls]
            try {
                if (logger == null &&
                        (SbcbflwCommon.instance.propertiesConfig.showLog || logMust)) {
                    logger = LoggerFactory.getLogger(cls)
                    logControllerMap[cls] = logger
                }
            } catch (e: Exception) {
                //数据无法初始化不打印日志
            }
            return logger
        }
    }


}
