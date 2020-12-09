package javabase.lorenwang.common_base_frame.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javabase.lorenwang.common_base_frame.SbcbflwCommon;
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean;
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb;
import javabase.lorenwang.common_base_frame.utils.SbcbfBaseAllUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import kotlin.jvm.Throws;

import static javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapperKt.REQUEST_SET_USER_INFO_KEY;

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
public abstract class SbcbflwBaseControllerFilter implements Filter {
    private final String TAG = "SbcbflwBaseControllerFilter";
    private final List<String> swaggerPathList = new ArrayList<>();

    public SbcbflwBaseControllerFilter() {
        //初始化不用验证token的接口列表
        //SwaggerUi
        swaggerPathList.add("/swagger-ui.html");
        //SwaggerUi
        swaggerPathList.add("/swagger-resources");
        //SwaggerUi
        swaggerPathList.add("/v2/api-docs");
        //网站相关
        swaggerPathList.add("/webjars/.+");
        //网站相关
        swaggerPathList.add("/swagger-resources/.+");
    }

    @Throws(exceptionClasses = ServletException.class)
    @Override
    public void init(FilterConfig filterConfig) {
        SbcbfBaseAllUtils.getLogUtils().logI(getClass(), "初始化筛选器", false);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SbcbflwBaseHttpServletRequestWrapper req =
                new SbcbflwBaseHttpServletRequestWrapper((HttpServletRequest) request);
        HttpServletResponse rep = (HttpServletResponse) response;

        //允许跨域请求
        if (SbcbflwCommon.getInstance().propertiesConfig.isDebug()) {
            //设置允许跨域的配置
            // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
            rep.setHeader("Access-Control-Allow-Origin", "*");
            // 允许的访问方法
            rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            rep.setHeader("Access-Control-Max-Age", "3600");
            rep.setHeader("Access-Control-Allow-Headers", "${SbcbflwCommonUtils.instance" +
                    ".headerKeyUserAccessToken},Origin, X-Requested-With, Content-Type, Accept," +
                    "Access-Control-Allow-Headers,Origin," +
                    " X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, " +
                    "WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model," +
                    " WG-CPU, WG-Sid, WG-App-Id, WG-Token");
            rep.setCharacterEncoding("UTF-8");

            //有些web接口会在请求接口之前发生options请求，这个直接通过即可
            String method = ((HttpServletRequest) request).getMethod();
            if ("OPTIONS".equals(method)) {
                rep.setStatus(204);
                return;
            }
        } else {
            //api文档接口不能反回
            String servletPath = req.getServletPath();
            for (String item : swaggerPathList) {
                if (servletPath.matches(item)) {
                    SbcbfBaseAllUtils.getLogUtils().logI(getClass(), "请求的是（$servletPath）接口," +
                            "正式环境下禁止访问!", false);
                    return;
                }
            }
        }

        if (SbcbflwCommon.getInstance().getPropertiesConfig().getHaveUserSystem()) {
            //响应中允许返回被读取的header，不添加客户端无法读取该key
            rep.setHeader("Access-Control-Expose-Headers", SbcbflwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req));

            //做该关键字拦截，因为后面要用到该关键字所有信息，所以此处要拦截，防止被攻击时传递该参数导致能够获取响应用户权限数据
            req.setAttribute(REQUEST_SET_USER_INFO_KEY, "");

            //token检测
            SbcbfBaseAllUtils.getLogUtils().logD(getClass(), "接收到接口请求，开始检测用户登录状态，如果有token的话", false);
            if (JtlwCheckVariateUtils.getInstance().isEmpty(SbcbflwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req))) {
                //正常发起请求
                chain.doFilter(req, response);
            } else {
                SbcbflwBaseDataDisposeStatusBean userStatus = SbcbflwCommon.getInstance().getUserService().checkUserLogin(req);
                if (userStatus.getStatusResult() && userStatus.getBody() != null && userStatus.getBody() instanceof SbcbflwBaseUserInfoTb) {
                    String accessToken =
                            ((SbcbflwBaseUserInfoTb) userStatus.getBody()).getAccessToken();
                    SbcbfBaseAllUtils.getLogUtils().logD(getClass(), "该用户存在，token有效，执行刷新逻辑，来决定是否刷新信息"
                            , false);
                    String newToken = SbcbflwCommon.getInstance().getUserService().refreshAccessToken(accessToken);
                    if (JtlwCheckVariateUtils.getInstance().isNotEmpty(newToken) && accessToken.equals(newToken)) {
                        ((SbcbflwBaseUserInfoTb) userStatus.getBody()).setAccessToken(newToken);
                        rep.setHeader(SbcbflwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req), newToken);
                        req.addHeader(SbcbflwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req), newToken);
                        SbcbfBaseAllUtils.getLogUtils().logI(getClass(), "token已更新", false);
                    }
                    req.setAttribute(REQUEST_SET_USER_INFO_KEY, userStatus.getBody());
                    //正常发起请求
                    chain.doFilter(req, response);
                } else {
                    SbcbfBaseAllUtils.getLogUtils().logD(getClass(), "token无效或者不存在，生成提示信息", false);
                    String responseFailInfo = responseErrorUser(userStatus);
                    //通过设置响应头控制浏览器以UTF-8的编码显示数据
                    rep.setHeader("content-type", "text/html;charset=UTF-8");
                    //获取OutputStream输出流
                    rep.getOutputStream().write(responseFailInfo.getBytes());
                    rep.setStatus(20);
                }
            }
        } else {
            //正常发起请求
            chain.doFilter(req, response);
        }
    }

    @Override
    public void destroy() {
        SbcbfBaseAllUtils.getLogUtils().logI(getClass(), "销毁筛选器", false);
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     *
     * @param errorInfo 错误信息
     * @return 返回登录验证失败响应字符串
     */
    public String responseErrorUser(SbcbflwBaseDataDisposeStatusBean errorInfo) {
        return null;
    }

}
