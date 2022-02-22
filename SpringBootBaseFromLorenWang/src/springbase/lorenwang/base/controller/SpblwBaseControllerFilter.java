package springbase.lorenwang.base.controller;

import org.jetbrains.annotations.NotNull;

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

import kotlin.jvm.Throws;
import springbase.lorenwang.base.SpblwConfig;

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
public abstract class SpblwBaseControllerFilter<T extends SpblwBaseHttpServletRequestWrapper> implements Filter {
    protected final String TAG = getClass().getName();
    private final List<String> swaggerPathList = new ArrayList<>();

    public SpblwBaseControllerFilter() {
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
        SpblwConfig.getLogUtils().logI(getClass(), "初始化筛选器", false);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        checkAllowAccess(request, (HttpServletResponse) response);
    }

    /**
     * 检测是否允许访问
     *
     * @param request  请求
     * @param response 响应
     * @return 允许访问返回true
     */
    protected boolean checkAllowAccess(ServletRequest request, ServletResponse response) {
        HttpServletResponse res = (HttpServletResponse) response;
        T req = paramsRequest(request);
        //允许跨域请求
        if (SpblwConfig.getCurrentIsDebug()) {
            //设置允许跨域的配置
            // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
            res.setHeader("Access-Control-Allow-Origin", "*");
            // 允许的访问方法
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            res.setHeader("Access-Control-Max-Age", "3600");
            res.setHeader("Access-Control-Allow-Headers",
                    getHeaderAccessTokenKey() + ",Origin, X-Requested-With, Content-Type, Accept," + "Access-Control-Allow-Headers,Origin," +
                            " X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, " +
                            "WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model," + " WG-CPU, WG-Sid, WG-App-Id, WG-Token");
            res.setCharacterEncoding("UTF-8");

            //有些web接口会在请求接口之前发生options请求，这个直接通过即可
            String method = ((HttpServletRequest) request).getMethod();
            if ("OPTIONS".equals(method)) {
                res.setStatus(204);
                return false;
            }
        } else {
            //api文档接口不能反回
            String servletPath = req.getServletPath();
            for (String item : swaggerPathList) {
                if (servletPath.matches(item)) {
                    SpblwConfig.getLogUtils().logI(getClass(), "请求的是(" + servletPath + ")接口," + "正式环境下禁止访问!", false);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void destroy() {
        SpblwConfig.getLogUtils().logI(getClass(), "销毁筛选器", false);
    }

    /**
     * 格式化
     *
     * @param request 默认进入的请求
     * @return 格式化的请求
     */
    @NotNull
    public abstract T paramsRequest(ServletRequest request);

    /**
     * 获取header中accessToken使用的关键字
     *
     * @return key值关键字
     */
    @NotNull
    public abstract String getHeaderAccessTokenKey();

}
