package springbase.lorenwang.user

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil
import javabase.lorenwang.tools.dataConversion.JtlwCodeConversionUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import springbase.lorenwang.tools.SptlwConfig
import springbase.lorenwang.tools.sptlwConfig
import springbase.lorenwang.user.enums.SpulwUserLoginFromEnum
import springbase.lorenwang.user.service.SpulwPlatformTokenService
import springbase.lorenwang.user.service.SpulwUserService
import springbase.lorenwang.user.service.SpulwUserService.Companion.REQUEST_SET_USER_INFO_KEY
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

/**
 * 功能作用：SpringBoot用户库接口
 * 初始注释时间： 2022/3/5 10:13
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
abstract class SpulwConfig : SptlwConfig() {
    init {
        sptlwConfig = this
    }

    /**
     * 初始化用户框架配置
     */
    abstract fun initUserConfig()

    /**
     * 获取用户服务
     */
    abstract fun getUserServices(): SpulwUserService

    /**
     * 获取token加解密key
     */
    abstract fun getDecryptAccessTokenKey(): String

    /**
     * 获取token加解密ivs
     */
    abstract fun getDecryptAccessTokenIvs(): String

    override fun getAccessControlAllowHeadersAddKey(): String {
        return "${getAccessControlAllowHeadersUserLoginFrom()},${getAccessControlAllowHeadersAccessToken()}"
    }

    /**
     * 获取登录来源请求头参数
     */
    abstract fun getAccessControlAllowHeadersUserLoginFrom(): String

    /**
     * 获取登录请求的参数token
     */
    abstract fun getAccessControlAllowHeadersAccessToken(): String

    /**
     * 初始化接口拦截处理
     */
    override fun initFilterDoFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Boolean {
        if (super.initFilterDoFilter(request, response, chain)) {
            val req = paramsAllRequest(request)
            if (req != null) {
                //请求地址
                val reqUrl: String = req.requestURI
                //请求携带的token信息
                val reqToken = getUserServices().getAccessTokenByReqHeader(req)?.let { JtlwCodeConversionUtil.getInstance().unicodeToChinese(it) }
                //响应
                val rep = response as HttpServletResponse
                //响应中允许返回被读取的header，不添加客户端无法读取该key
                rep.setHeader("Access-Control-Expose-Headers", reqToken)
                //登录来源
                val fromEnum =
                    req.getHeader(getAccessControlAllowHeadersUserLoginFrom())?.let { SpulwUserLoginFromEnum.getFrom(it) }.kttlwGetNotEmptyData {
                        if (spulwConfig.currentIsDebug()) SpulwUserLoginFromEnum.WEB else null
                    }
                if (fromEnum == null) {
                    return filterError(rep, reqUrl, "用户请求来源为空")
                } else {
                    //token检测
                    if (JtlwCheckVariateUtil.getInstance().isEmpty<String>(reqToken)) {
                        getLogUtil().logI(javaClass, reqUrl + "接收到无token接口请求，正常发起", false)
                        //正常发起请求
                        chain.doFilter(req, response)
                        return true
                    } else {
                        getLogUtil().logI(javaClass, reqUrl + "接收到token接口请求，开始校验token信息", false)
                        val userId = getUserServices().getUserIdByAccessToken(reqToken)
                        if (userId.isNullOrEmpty()) {
                            return filterError(rep, reqUrl, "非正常格式token")
                        } else {
                            //有token信息，更新token信息
                            val newToken = getUserServices().refreshAccessToken(reqToken, fromEnum)
                            if (newToken.isNullOrEmpty()) {
                                return filterError(rep, reqUrl, "无法生成新token，旧token异常或来源异常")
                            } else {
                                //更新token表，成功的话返回用户信息
                                if (newToken == reqToken || spulwConfig.applicationContext.getBean(SpulwPlatformTokenService::class.java)
                                        .updateUserTokenInfo(userId, newToken, getUserServices().getAccessTokenTimeOut())) {
                                    val userInfo = getUserServices().getUserInfo(userId, null, null, null, null, null, null)
                                    return if (userInfo == null) {
                                        filterError(rep, reqUrl, "用户信息不存在")
                                    } else {
                                        rep.setHeader(getAccessControlAllowHeadersAccessToken(), newToken)
                                        req.addHeader(getAccessControlAllowHeadersAccessToken(), newToken)
                                        req.setAttribute(REQUEST_SET_USER_INFO_KEY, userInfo)
                                        //正常发起请求
                                        getLogUtil().logD(javaClass, reqUrl + "正常发起请求", false)
                                        chain.doFilter(req, response)
                                        true
                                    }
                                } else {
                                    return filterError(rep, reqUrl, "token生成异常，无法登陆")
                                }
                            }
                        }
                    }
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    /**
     * 接口筛选异常
     */
    private fun filterError(rep: HttpServletResponse, reqUrl: String, msg: String): Boolean {
        getLogUtil().logD(javaClass, reqUrl + msg, false)
        //通过设置响应头控制浏览器以UTF-8的编码显示数据
        rep.setHeader("content-type", "text/html;charset=UTF-8")
        //获取OutputStream输出流
        rep.outputStream.write(msg.toByteArray())
        rep.status = 20
        return false
    }
}

/**
 * 用户库config
 */
lateinit var spulwConfig: SpulwConfig