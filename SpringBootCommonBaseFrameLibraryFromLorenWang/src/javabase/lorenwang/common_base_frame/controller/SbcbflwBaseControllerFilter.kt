package javabase.lorenwang.common_base_frame.controller

import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.SbcbflwPropertiesConfig
import javabase.lorenwang.common_base_frame.database.helper.SbcbflwUserHelper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserInfoRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.tools.JtlwLogUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 功能作用：接口拦截工具
 * 创建时间：2019-09-12 上午 10:57:45
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Transactional
abstract class SbcbflwBaseControllerFilter : Filter {

    private val swaggerPathList = ArrayList<String>()

    init {
        //初始化不用验证token的接口列表
        swaggerPathList.add("/swagger-ui.html")//swaggerui
        swaggerPathList.add("/swagger-resources")//swaggerui
        swaggerPathList.add("/v2/api-docs")//swaggerui
        swaggerPathList.add("/webjars/.+")//网站相关
        swaggerPathList.add("/swagger-resources/.+")//网站相关
    }

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        JtlwLogUtils.logI(javaClass, "初始化筛选器")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = SbcbflwBaseHttpServletRequestWrapper(request as HttpServletRequest)
        val rep = response as HttpServletResponse

        //允许跨域请求
        if (SbcbflwCommonUtils.instance.propertiesConfig.isDebug) {
            //设置允许跨域的配置
            // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
            rep.setHeader("Access-Control-Allow-Origin", "*")
            // 允许的访问方法
            rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH")
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            rep.setHeader("Access-Control-Max-Age", "3600")
            rep.setHeader("Access-Control-Allow-Headers", "${req.ACCESS_TOKEN_KEY},Origin, X-Requested-With, Content-Type, Accept,Access-Control-Allow-Headers,Origin," +
                    " X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model," +
                    " WG-CPU, WG-Sid, WG-App-Id, WG-Token")
            rep.characterEncoding = "UTF-8"

            //有些web接口会在请求接口之前发生options请求，这个直接通过即可
            val method = request.method
            if (method == "OPTIONS") {
                rep.status = 204
                return
            }
        } else {
            //api文档接口不能反回
            val servletPath = req.servletPath
            for (item in swaggerPathList) {
                if (servletPath.matches(Regex(item))) {
                    JtlwLogUtils.logI(javaClass, "请求的是（$servletPath）接口,正式环境下禁止访问!")
                    return
                }
            }
        }

        //响应中允许返回被读取的header，不添加客户端无法读取该key
        response.setHeader("Access-Control-Expose-Headers", req.ACCESS_TOKEN_KEY)

        //做该关键字拦截，因为后面要用到该关键字所有信息，所以此处要拦截，防止被攻击时传递该参数导致能够获取响应用户权限数据
        req.setAttribute(req.REQUEST_SET_USER_INFO_KEY, "")

        //token检测
        JtlwLogUtils.logD(javaClass, "接收到接口请求，开始检测用户登录状态，如果有token的话")
        SbcbflwUserHelper.baseInstance?.getAccessTokenByReqHeader(req)?.let {
            val userStatus = SbcbflwUserHelper.baseInstance?.checkUserLogin(req)
            if (userStatus != null && userStatus.statusResult && userStatus.body != null && userStatus.body is SbcbflwBaseUserInfoTb<*,*>) {
                val accessToken = (userStatus.body as SbcbflwBaseUserInfoTb<*,*>).accessToken
                JtlwLogUtils.logD(javaClass, "该用户存在，token有效，执行刷新逻辑，来决定是否刷新信息")
                SbcbflwUserHelper.baseInstance?.refreshAccessToken(accessToken!!).let { newToken ->
                    if (!accessToken.equals(newToken)) {
                        (userStatus.body as SbcbflwBaseUserInfoTb<*,*>).accessToken = newToken
                        response.setHeader(req.ACCESS_TOKEN_KEY, newToken)
                        req.addHeader(req.ACCESS_TOKEN_KEY, newToken!!)
                        JtlwLogUtils.logI(javaClass, "token已更新")
                    }
                    req.setAttribute(req.REQUEST_SET_USER_INFO_KEY, userStatus.body)
                }
            } else {
                JtlwLogUtils.logD(javaClass, "token无效或者不存在，生成提示信息")
                val responseFailInfo = responseErrorUserLoginEmptyOrTokenNoneffective()
                //通过设置响应头控制浏览器以UTF-8的编码显示数据
                rep.setHeader("content-type", "text/html;charset=UTF-8")
                //获取OutputStream输出流
                rep.outputStream.write(responseFailInfo.toByteArray())
                return
            }
        }

        //正常发起请求
        chain.doFilter(req, response)
    }

    override fun destroy() {
        JtlwLogUtils.logI(javaClass, "销毁筛选器")
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     */
    abstract fun responseErrorUserLoginEmptyOrTokenNoneffective(): String
}
