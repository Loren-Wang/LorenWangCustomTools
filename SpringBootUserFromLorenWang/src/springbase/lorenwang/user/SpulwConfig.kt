package springbase.lorenwang.user

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil
import javabase.lorenwang.tools.dataConversion.JtlwCodeConversionUtil
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.SptlwConfig
import springbase.lorenwang.tools.sptlwConfig
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
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

    /**
     * 初始化接口拦截处理
     */
    override fun initFilterDoFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Boolean {
        if (super.initFilterDoFilter(request, response, chain)) {
            val req = paramsAllRequest(request)
            if (req != null) {
                val rep = response as HttpServletResponse
                //响应中允许返回被读取的header，不添加客户端无法读取该key
                rep.setHeader("Access-Control-Expose-Headers", getUserServices().getAccessTokenByReqHeader(req))
                //做该关键字拦截，因为后面要用到该关键字所有信息，所以此处要拦截，防止被攻击时,传递该参数导致能够获取响应用户权限数据
                req.setAttribute(REQUEST_SET_USER_INFO_KEY, "")
                //请求地址
                val reqUrl: String = req.requestURI
                //token检测
                if (JtlwCheckVariateUtil.getInstance().isEmpty<String>(getUserServices().getAccessTokenByReqHeader(req))) {
                    getLogUtil().logI(javaClass, reqUrl + "接收到无token接口请求，正常发起", false)
                    //正常发起请求
                    chain.doFilter(req, response)
                    return true
                } else {
                    getLogUtil().logI(javaClass, reqUrl + "接收到token接口请求，开始校验用户信息", false)
                    val userStatus: SpblwBaseDataDisposeStatusBean = getUserServices().checkUserLogin(req)
                    if (userStatus.statusResult && userStatus.body != null && userStatus.body is SpulwBaseUserInfoTb) {
                        getLogUtil().logD(javaClass, reqUrl + "用户信息校验完毕,当前用户存在并是登录状态", false)
                        //开始进行编码转换
                        val accessToken =
                            JtlwCodeConversionUtil.getInstance().unicodeToChinese((userStatus.body as SpulwBaseUserInfoTb?)!!.accessToken!!)
                        //获取刷新后token信息
                        val newToken: String = getUserServices().refreshAccessToken(accessToken)
                        //判断刷新后token和当前token是否一致，一致则不进行处理
                        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(newToken) && accessToken == newToken) {
                            (userStatus.body as SpulwBaseUserInfoTb?)!!.accessToken = newToken
                            rep.setHeader(getAccessControlAllowHeadersUserTokenKey(), newToken)
                            req.addHeader(getAccessControlAllowHeadersUserTokenKey(), newToken)
                            getLogUtil().logI(javaClass, "token已更新", false)
                        } else {
                            //更新转码后的旧token
                            (userStatus.body as SpulwBaseUserInfoTb?)!!.accessToken = accessToken
                            rep.setHeader(getAccessControlAllowHeadersUserTokenKey(), accessToken)
                            req.addHeader(getAccessControlAllowHeadersUserTokenKey(), accessToken)
                        }
                        req.setAttribute(REQUEST_SET_USER_INFO_KEY, userStatus.body)
                        //正常发起请求
                        getLogUtil().logD(javaClass, reqUrl + "正常发起请求", false)
                        chain.doFilter(req, response)
                        return true
                    } else {
                        getLogUtil().logD(javaClass, reqUrl + "token无效或者不存在", false)
                        val responseFailInfo: String = getUserServices().responseErrorUser(userStatus)
                        //通过设置响应头控制浏览器以UTF-8的编码显示数据
                        rep.setHeader("content-type", "text/html;charset=UTF-8")
                        //获取OutputStream输出流
                        rep.outputStream.write(responseFailInfo.toByteArray())
                        rep.status = 20
                        return false
                    }
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }
}

/**
 * 用户库config
 */
lateinit var spulwConfig: SpulwConfig