package com.test.springboot.base.utils

import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.utils.SptlwFileOptionsUtil

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
class FileOptionsUtils : SptlwFileOptionsUtil() {

    /**
     * 获取未见内容是空情况下状态实例
     */
    override fun getFileEmptyStatusBean(): SpblwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取文件过大情况下状态实例
     */
    override fun getFileTooLargeStatusBean(): SpblwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取非接收文件类型情况下状态实例
     */
    override fun getNotReceiveFileTypeStatusBean(): SpblwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 获取位置文件类型状态实例
     */
    override fun getUnKnowFileTypeStatusBean(): SpblwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }


}
