package com.test.springboot.applicationListener

import com.qtoolsbaby.servicemmxs.config.PropertiesConfig
import com.test.springboot.config.DataBaseTableVersionUpdateOptions
import com.test.springboot.utils.LogUtils
import javabase.lorenwang.common_base_frame.SbcbflwCommon
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener


/**
 * 功能作用：启动监听
 * 创建时间：2019-09-12 上午 10:57:7
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ApplicationListenerForStart : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {

        LogUtils.instance.logI(javaClass, "开始初始化设置配置信息")
        //启动后初始化阿里云oss的配置信息
        SbcbflwCommon.instance.initBase(event.applicationContext, event.applicationContext.getBean(PropertiesConfig::class.java))
        //数据库版本更新初始化
        event.applicationContext.getBean(DataBaseTableVersionUpdateOptions::class.java).initData(applicationContext = event.applicationContext)
    }
}
