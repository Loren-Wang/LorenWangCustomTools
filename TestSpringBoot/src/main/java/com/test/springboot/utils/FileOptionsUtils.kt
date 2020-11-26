package com.test.springboot.utils

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.utils.SbcbfBaseAllUtils
import javabase.lorenwang.common_base_frame.utils.SbcbflwBaseFileOptionsUtils

/**
 * 功能作用：文件操作工具类
 * 创建时间：2020-07-02 3:53 下午
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
class FileOptionsUtils private constructor() : SbcbflwBaseFileOptionsUtils() {

    companion object {
        private var optionsInstance : FileOptionsUtils? = null
        val instance : FileOptionsUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = FileOptionsUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 获取未见内容是空情况下状态实例
     */
    override fun getFileEmptyStatusBean() : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取文件过大情况下状态实例
     */
    override fun getFileTooLargeStatusBean() : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取非接收文件类型情况下状态实例
     */
    override fun getNotReceiveFileTypeStatusBean() : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取位置文件类型状态实例
     */
    override fun getUnKnowFileTypeStatusBean() : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }


}
