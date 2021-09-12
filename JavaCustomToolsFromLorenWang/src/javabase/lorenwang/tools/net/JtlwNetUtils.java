package javabase.lorenwang.tools.net;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javabase.lorenwang.tools.JtlwMatchesRegularCommon;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：网络相关工具类
 * 创建时间：2020-11-24 3:14 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取url域名--getUrlHost(urlPath)
 * 获取url协议--getUrlProtocol(urlPath)
 * 获取url端口--getUrlPort(urlPath)
 * 获取url链接地址--getUrlLinkPath(urlPath)
 * 获取url中指定key的参数--getUrlParams(urlPath,key)
 * 添加网址参数--addUrlParams(urlPath,key,value)
 * 添加网址参数--addUrlParams(urlPath,keys,values)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JtlwNetUtils {
    private final String TAG = getClass().getName();
    private static volatile JtlwNetUtils optionsInstance;
    /**
     * 默认请求协议
     */
    private final String DEFAULT_PROTOCOL = "http";
    /**
     * 默认请求host
     */
    private final String DEFAULT_HOST = "asdfa.asfda.sadfadf";

    private JtlwNetUtils() {
    }

    public static JtlwNetUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (JtlwNetUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new JtlwNetUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 获取url域名
     *
     * @param urlPath url地址
     * @return 域名，空代表没查到
     */
    public String getUrlHost(@NotNull String urlPath) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            String host = url.getHost();
            if (DEFAULT_HOST.equals(host)) {
                return null;
            }
            return host;
        }
        return null;
    }

    /**
     * 获取url协议
     *
     * @param urlPath url地址
     * @return 协议，空代表没查到
     */
    public String getUrlProtocol(@NotNull String urlPath) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            return url.getProtocol();
        }
        return null;
    }

    /**
     * 获取url端口
     *
     * @param urlPath url地址
     * @return 端口，空代表没查到
     */
    public Integer getUrlPort(@NotNull String urlPath) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            return url.getPort();
        }
        return null;
    }

    /**
     * 获取url链接地址
     *
     * @param urlPath url地址
     * @return 链接地址，空代表没查到
     */
    public String getUrlLinkPath(@NotNull String urlPath) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            return url.getPath();
        }
        return null;
    }

    /**
     * 获取url中指定key的参数
     *
     * @param urlPath url地址
     * @param key     要查找的key
     * @return 说明如下
     * <p>
     * null:key为空同时没有任何参数或者单纯没有任何参数或者连接中没有这个参数
     * ""：指定的key的参数值为空
     * value：指定的key的参数值
     */
    public String getUrlParams(@NotNull String urlPath, String key) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            String query = url.getQuery();
            //如果参数结果全部都是？或者&则代表无参数
            if (query != null && query.matches("[?&]+")) {
                query = null;
            }
            if (JtlwCheckVariateUtils.getInstance().isEmpty(key)
                    || JtlwCheckVariateUtils.getInstance().isEmpty(query)) {
                return "".equals(query) ? null : query;
            } else {
                Pattern pattern = Pattern.compile(key + "=\\S+[^&]?");
                Matcher matcher = pattern.matcher(query);
                if (matcher.find()) {
                    try {
                        return matcher.group(0).replace(key + "=", "");
                    } catch (Exception ignore) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * 添加网址参数
     *
     * @param urlPath 网址
     * @param key     参数key
     * @param value   参数值
     * @return 添加后网址参数
     */
    public String addUrlParams(@NotNull String urlPath, @NotNull String key, @NotNull Object value) {
        ArrayList<String> keys = new ArrayList<>(1);
        keys.add(key);
        ArrayList<Object> values = new ArrayList<>(1);
        values.add(value);
        String path = addUrlParams(urlPath, keys, values);
        keys.clear();
        values.clear();
        return path;
    }

    /**
     * 添加网址参数
     *
     * @param urlPath 网址
     * @param keys    参数key
     * @param values  参数值
     * @return 添加后网址参数
     */
    public String addUrlParams(@NotNull String urlPath, @NotNull List<String> keys, @NotNull List<Object> values) {
        if (keys.size() == values.size() && keys.size() > 0 && values.size() > 0) {
            //判断是否有相关参数
            String oldQuery = getUrlParams(urlPath, null);
            //清除掉原始地址的后缀？和&
            StringBuilder resultUrl = new StringBuilder(urlPath.replaceAll("[?&]+$", ""));
            int size = keys.size();
            for (int i = 0; i < size; i++) {
                if (i == 0 && oldQuery == null) {
                    resultUrl.append("?");
                } else {
                    resultUrl.append("&");
                }
                resultUrl.append(keys.get(i)).append("=").append(values.get(i));
            }
            String path = resultUrl.toString();
            resultUrl.setLength(0);
            return path;
        } else {
            return urlPath;
        }
    }

    /**
     * 格式化url地址
     *
     * @param url url地址
     * @return URL对象
     */
    private URL paramsUrl(@NotNull String url) {
        StringBuilder urlPath = new StringBuilder(url);
        //判断是否有请求协议，没有则添加
        if (!url.matches("^[a-zA-Z]+://\\S+")) {
            urlPath.insert(0, "://").insert(0, DEFAULT_PROTOCOL);
        }
        //判断是否有请求域名，没有则添加
        if (!urlPath.toString().matches("\\S+://" + JtlwMatchesRegularCommon.EXP_URL_HOST + "\\S+")) {
            urlPath.insert(urlPath.indexOf("://") + 3, DEFAULT_HOST);
        }
        try {
            URL optionsUrl = new URL(urlPath.toString());
            urlPath.setLength(0);
            return optionsUrl;
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
