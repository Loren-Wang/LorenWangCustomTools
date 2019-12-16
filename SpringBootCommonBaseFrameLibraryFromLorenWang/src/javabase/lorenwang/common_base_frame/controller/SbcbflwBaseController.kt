package javabase.lorenwang.common_base_frame.controller

import javabase.lorenwang.common_base_frame.SbcbflwPropertiesConfig
import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.JtlwLogUtils
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import javax.annotation.Resource

/**
 * 功能作用：基础controller
 * 创建时间：2019-12-16 14:03
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、从资源文件中获取文本字符---getMessage(msgCode)
 * 2、从资源文件中获取文本字符---getMessage(msgCode,defaultMsg)
 * 3、接口处理之后的响应返回---responseContent(stateCode,stateMsg,data)
 * 4、接口处理之后的数据列表响应返回---responseDataListContent(stateCode,stateMsg,pageIndex,pageSize,sumCount,dataList)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwBaseController {
    @Resource
    private lateinit var messageSource: MessageSource

    /**
     * 保留方法（所有子方法都要继承这个方法）
     * @param t
     */
    protected fun <T> base(request: SbcbflwBaseHttpServletRequestWrapper, t: T?) {
        JtlwLogUtils.logI(javaClass, "当前编译器环境：${SbcbflwPropertiesConfig.runCompilingEnvironment}")
        JtlwLogUtils.logI(javaClass, "当前请求地址：${request.servletPath}")
        t.let {
            JtlwLogUtils.logI(javaClass, "当前请求数据：${JdplwJsonUtils.toJson(it)}")
        }
    }

    /**
     * 获取文字字符
     * @param code ：对应messages配置的key.
     * @return
     */
    public fun getMessage(code: String?): String {
        //这里使用比较方便的方法，不依赖request.
        return getMessage(code, null)
    }

    /**
     * 获取文字字符
     * @param code ：对应messages配置的key.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    public fun getMessage(code: String?, defaultMessage: String?): String {
        //这里使用比较方便的方法，不依赖request.
        return code?.let {
            messageSource.getMessage(it, null, defaultMessage
                    ?: "", LocaleContextHolder.getLocale())
        } ?: ""
    }

    /**
     * 接口处理之后的响应返回
     * @param stateCode 响应状态吗
     * @param stateMessage 响应状态消息
     * @param obj 响应数据
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    public fun <T> responseContent(stateCode: String, stateMessage: String, obj: T?): String {
        val baseResponseBean = SbcbflwBaseResponseBean(obj)
        baseResponseBean.stateCode = stateCode
        baseResponseBean.stateMessage = stateMessage
        return JdplwJsonUtils.toJson(baseResponseBean)
    }

    /**
     * 接口处理之后的数据列表响应返回
     * @param stateCode 响应状态吗
     * @param stateMessage 响应状态消息
     * @param obj 响应数据
     * @param <T> 泛型
     * @return 格式化后字符串
    </T> */
    public fun <E, T : ArrayList<E>> responseDataListContent(
            stateCode: String, stateMessage: String, pageIndex: Int,
            pageSize: Int, sumCount: Long?, dataList: T): String {
        val baseResponseBean = SbcbflwBaseResponseBean(SbcbflwPageResponseBean(pageIndex, pageSize, sumCount, dataList))
        baseResponseBean.stateCode = stateCode
        baseResponseBean.stateMessage = stateMessage
        return JdplwJsonUtils.toJson(baseResponseBean)
    }

}
