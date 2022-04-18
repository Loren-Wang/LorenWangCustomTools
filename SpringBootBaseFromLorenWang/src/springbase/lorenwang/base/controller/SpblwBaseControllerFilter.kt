package springbase.lorenwang.base.controller

import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import springbase.lorenwang.base.spblwConfig
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 功能作用：接口拦截工具
 * 创建时间：2020-09-27 2:42 下午
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
open class SpblwBaseControllerFilter : Filter {
    private val swaggerPathList: MutableList<String> = ArrayList()

    init {
        //初始化不用验证token的接口列表
        //SwaggerUi
        swaggerPathList.add("/swagger-ui.html")
        //SwaggerUi
        swaggerPathList.add("/swagger-resources")
        //SwaggerUi
        swaggerPathList.add("/v2/api-docs")
        //网站相关
        swaggerPathList.add("/webjars/.+")
        //网站相关
        swaggerPathList.add("/swagger-resources/.+")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        //检测是否允许访问
        if (checkAllowAccess(request, response as HttpServletResponse)) {
            //初始化接口拦截处理
            spblwConfig.initFilterDoFilter(request, response, chain)
        }
    }

    /**
     * 检测是否允许访问
     *
     * @param request  请求
     * @param response 响应
     * @return 允许访问返回true
     */
    private fun checkAllowAccess(request: ServletRequest, response: ServletResponse): Boolean {
        val res: HttpServletResponse = response as HttpServletResponse
        val req = spblwConfig.paramsAllRequest(request)
        //允许跨域请求
        if (spblwConfig.currentIsDebug()) {
            //设置允许跨域的配置
            // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
            res.setHeader("Access-Control-Allow-Origin", "*")
            // 允许的访问方法
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH")
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            res.setHeader("Access-Control-Max-Age", "3600")
            res.setHeader("Access-Control-Allow-Headers",
                "${spblwConfig.getAccessControlAllowHeadersAddKey()},Origin, X-Requested-With, Content-Type, Accept,Access-Control-Allow-Headers,Origin, X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token")
            res.characterEncoding = "UTF-8"

            //有些web接口会在请求接口之前发生options请求，这个直接通过即可
            val method: String = (request as HttpServletRequest).method
            if ("OPTIONS" == method) {
                res.status = 204
                return false
            }
        } else {
            //api文档接口不能反回
            val servletPath = req!!.servletPath
            for (item in swaggerPathList) {
                if (servletPath?.toRegex()?.matches(item).kttlwGetNotEmptyData { false }) {
                    spblwConfig.getLogUtil().logI(javaClass, "请求的是($servletPath)接口,正式环境下禁止访问!", false)
                    return false
                }
            }
        }
        return true
    }

    override fun destroy() {
        spblwConfig.getLogUtil().logI(javaClass, "销毁筛选器", false)
    }
}