package javabase.lorenwang.common_base_frame.controller

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


/**
 * 功能作用：请求数据拦截，用来对请求头做处理
 * 创建时间：2019-09-12 上午 10:59:46
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、添加header---addHeader(name,value)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwBaseHttpServletRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val headerMap = HashMap<String, String>()
    /**
     * 所有的token令牌相关的关键字key
     */
    val ACCESS_TOKEN_KEY = "accesstoken"
    /**
     * 请求中设置用户信息key
     */
    val REQUEST_SET_USER_INFO_KEY = "userinfokey"

    /**
     * add a header with given name and value
     *
     * @param name
     * @param value
     */
    fun addHeader(name: String, value: String) {
        headerMap[name] = value
    }

    override fun getHeader(name: String): String? {
        var headerValue = super.getHeader(name)
        if (headerMap.containsKey(name)) {
            headerValue = headerMap[name]
        }
        return headerValue
    }

    /**
     * get the Header names
     */
    override fun getHeaderNames(): Enumeration<String> {
        val names = Collections.list(super.getHeaderNames())
        for (name in headerMap.keys) {
            names.add(name)
        }
        return Collections.enumeration(names)
    }

    override fun getHeaders(name: String): Enumeration<String> {
        var values = Collections.list(super.getHeaders(name))
        if (headerMap.containsKey(name) && headerMap[name] != null) {
            values = arrayListOf<String>(headerMap[name]!!)
        }
        return Collections.enumeration(values)
    }

}
