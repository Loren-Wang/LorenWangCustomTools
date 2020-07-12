package com.test.springboot.utils

import javabase.lorenwang.common_base_frame.utils.SbcbflwLogUtils

/**
 * 功能作用：日志工具类
 * 创建时间：2020-07-12 3:24 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
class LogUtils private constructor() : SbcbflwLogUtils() {
    companion object {
        init {
            baseInstance = LogUtils()
        }

        private var optionsInstance: LogUtils? = null
        val instance: LogUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = LogUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }


}
