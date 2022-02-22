package springbase.lorenwang.user.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;
import javabase.lorenwang.tools.dataConversion.JtlwCodeConversionUtil;
import springbase.lorenwang.base.SpblwConfig;
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean;
import springbase.lorenwang.base.controller.SpblwBaseControllerFilter;
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper;
import springbase.lorenwang.user.SpulwCommon;
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb;

import static springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapperKt.REQUEST_SET_USER_INFO_KEY;

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
public abstract class SpuControllerFilter<T extends SpblwBaseHttpServletRequestWrapper> extends SpblwBaseControllerFilter<T> {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (checkAllowAccess(request, response)) {
            T req = paramsRequest(request);
            HttpServletResponse rep = (HttpServletResponse) response;
            //响应中允许返回被读取的header，不添加客户端无法读取该key
            rep.setHeader("Access-Control-Expose-Headers", SpulwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req));
            //做该关键字拦截，因为后面要用到该关键字所有信息，所以此处要拦截，防止被攻击时传递该参数导致能够获取响应用户权限数据
            req.setAttribute(REQUEST_SET_USER_INFO_KEY, "");
            //请求地址
            String reqUrl = req.getRequestURI();
            //token检测
            if (JtlwCheckVariateUtil.getInstance().isEmpty(SpulwCommon.getInstance().getUserService().getAccessTokenByReqHeader(req))) {
                SpblwConfig.getLogUtils().logI(getClass(), reqUrl + "接收到无token接口请求，正常发起", false);
                //正常发起请求
                chain.doFilter(req, response);
            } else {
                SpblwConfig.getLogUtils().logI(getClass(), reqUrl + "接收到token接口请求，开始校验用户信息", false);
                SpblwBaseDataDisposeStatusBean userStatus = SpulwCommon.getInstance().getUserService().checkUserLogin(req);
                if (userStatus.getStatusResult() && userStatus.getBody() != null && userStatus.getBody() instanceof SpulwBaseUserInfoTb) {
                    SpblwConfig.getLogUtils().logD(getClass(), reqUrl + "用户信息校验完毕,当前用户存在并是登录状态", false);
                    //开始进行编码转换
                    String accessToken = JtlwCodeConversionUtil.getInstance().unicodeToChinese(
                            ((SpulwBaseUserInfoTb) userStatus.getBody()).getAccessToken());
                    //获取刷新后token信息
                    String newToken = SpulwCommon.getInstance().getUserService().refreshAccessToken(accessToken);
                    //判断刷新后token和当前token是否一致，一致则不进行处理
                    if (JtlwCheckVariateUtil.getInstance().isNotEmpty(newToken) && accessToken.equals(newToken)) {
                        ((SpulwBaseUserInfoTb) userStatus.getBody()).setAccessToken(newToken);
                        rep.setHeader(getHeaderAccessTokenKey(), newToken);
                        req.addHeader(getHeaderAccessTokenKey(), newToken);
                        SpblwConfig.getLogUtils().logI(getClass(), "token已更新", false);
                    } else {
                        //更新转码后的旧token
                        ((SpulwBaseUserInfoTb) userStatus.getBody()).setAccessToken(accessToken);
                        rep.setHeader(getHeaderAccessTokenKey(), accessToken);
                        req.addHeader(getHeaderAccessTokenKey(), accessToken);
                    }
                    req.setAttribute(REQUEST_SET_USER_INFO_KEY, userStatus.getBody());
                    //正常发起请求
                    SpblwConfig.getLogUtils().logD(getClass(), reqUrl + "正常发起请求", false);
                    chain.doFilter(req, response);
                } else {
                    SpblwConfig.getLogUtils().logD(getClass(), reqUrl + "token无效或者不存在", false);
                    String responseFailInfo = responseErrorUser(userStatus);
                    //通过设置响应头控制浏览器以UTF-8的编码显示数据
                    rep.setHeader("content-type", "text/html;charset=UTF-8");
                    //获取OutputStream输出流
                    rep.getOutputStream().write(responseFailInfo.getBytes());
                    rep.setStatus(20);
                }
            }
        }
    }


    /**
     * 登录验证失败,用户未登录或者token失效
     *
     * @param errorInfo 错误信息
     * @return 返回登录验证失败响应字符串
     */
    public String responseErrorUser(SpblwBaseDataDisposeStatusBean errorInfo) {
        return null;
    }
}
