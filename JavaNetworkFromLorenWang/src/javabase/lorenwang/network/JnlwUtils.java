package javabase.lorenwang.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JnlwUtils {
    /**
     * 默认请求协议
     */
    private final String DEFAULT_PROTOCOL = "http";
    /**
     * 默认请求host
     */
    private final String DEFAULT_HOST = "asdfa.asfda.sadfadf";
    private final String TAG = getClass().getName();


    /**
     * http、Https网址协议
     */
    private static final String EXP_URL_SCHEME_HTTP_S = "[hH][tT]{2}[pP][sS]?://";
    /**
     * url的用户名密码正则
     */
    private static final String EXP_URL_USER_PWD = "(\\S+:\\S+@)";
    /**
     * 匹配IP地址
     */
    public static final String EXP_IP = "((25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    /**
     * url的ip域名
     */
    private static final String EXP_URL_DOMAIN_NAME_IP = EXP_IP + "(:[1-9][0-9]{0,4})?";
    /**
     * url的字符串域名
     */
    private static final String EXP_URL_DOMAIN_NAME_STR = "([0-9a-zA-Z]+\\.)+[a-zA-Z]+(:[1-9][0-9]{0,4})?";
    /**
     * 纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(仅为协议和ip)
     */
    public static final String EXP_URL_AGREEMENT_DOMAIN_IP = "^" + EXP_URL_SCHEME_HTTP_S + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_IP;
    /**
     * 纯字符串的url正则(仅为协议和ip)
     */
    public static final String EXP_URL_AGREEMENT_DOMAIN_STR = "^" + EXP_URL_SCHEME_HTTP_S + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_STR;
    /**
     * 纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(匹配网址全部)
     */
    public static final String EXP_URL_IP = EXP_URL_AGREEMENT_DOMAIN_IP + ".*";
    /**
     * 纯字符串的url正则(匹配网址全部)
     */
    public static final String EXP_URL_STR = EXP_URL_AGREEMENT_DOMAIN_STR + ".*";
    /**
     * 匹配网址(仅为协议和ip)
     */
    public static final String EXP_URL_AGREEMENT_DOMAIN = "(" + EXP_URL_AGREEMENT_DOMAIN_IP + "|" + EXP_URL_AGREEMENT_DOMAIN_STR + ")";
    /**
     * 匹配网址(仅为协议和ip)
     */
    public static final String EXP_URL = "(" + EXP_URL_IP + "|" + EXP_URL_STR + ")";
    /**
     * 匹配网址域名
     */
    public static final String EXP_URL_HOST =
            "(" + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_IP + "|" + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_STR + ")";
    private static volatile JnlwUtils optionsInstance;

    private JnlwUtils() {
        threadPoolExecutor = new ScheduledThreadPoolExecutor(1, (ThreadFactory) Thread::new);
    }

    public static JnlwUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (JnlwUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new JnlwUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 判断变量是否为空
     *
     * @param <T> 变量泛型
     * @param str String
     * @return boolean
     */
    public <T> boolean isEmpty(T str) {
        if (str instanceof String) {
            return "".equals(str);
        } else {
            return str == null;
        }
    }

    /**
     * 判断变量是否为不为空
     *
     * @param <T> 变量泛型
     * @param str String
     * @return boolean
     */
    public <T> boolean isNotEmpty(T str) {
        return !isEmpty(str);
    }

    /**
     * 添加网址参数
     *
     * @param urlPath 网址
     * @param key     参数key
     * @param value   参数值
     * @return 添加后网址参数
     */
    public String addUrlParams(String urlPath, String key, Object value) {
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
    public String addUrlParams(String urlPath, List<String> keys, List<Object> values) {
        if (keys != null && values != null && keys.size() == values.size() && keys.size() > 0) {
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
    public String getUrlParams(String urlPath, String key) {
        URL url = paramsUrl(urlPath);
        if (url != null) {
            String query = url.getQuery();
            //如果参数结果全部都是？或者&则代表无参数
            if (query != null && query.matches("[?&]+")) {
                query = null;
            }
            if (isEmpty(key) || isEmpty(query)) {
                return "".equals(query) ? null : query;
            } else {
                Pattern pattern = Pattern.compile(key + "=\\S+[^&]?");
                assert query != null;
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
     * 格式化url地址
     *
     * @param url url地址
     * @return URL对象
     */
    private URL paramsUrl(String url) {
        if (isNotEmpty(url)) {
            StringBuilder urlPath = new StringBuilder(url);
            //判断是否有请求协议，没有则添加
            if (!url.matches("^[a-zA-Z]+://\\S+")) {
                urlPath.insert(0, "://").insert(0, DEFAULT_PROTOCOL);
            }
            //判断是否有请求域名，没有则添加
            if (!urlPath.toString().matches("\\S+://" + EXP_URL_HOST + "\\S+")) {
                urlPath.insert(urlPath.indexOf("://") + 3, DEFAULT_HOST);
            }
            try {
                URL optionsUrl = new URL(urlPath.toString());
                urlPath.setLength(0);
                return optionsUrl;
            } catch (MalformedURLException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private final ScheduledThreadPoolExecutor threadPoolExecutor;
    /**
     * 任务map集合记录
     */
    private final Map<Integer, Runnable> TIMING_TASK_MAP = new ConcurrentHashMap<>();
    private final Map<Integer, ScheduledFuture> TIMING_TASK_MAP_SCHEDULED = new ConcurrentHashMap<>();


    /**
     * 开启一个定时器，在指定时间之后执行runnable
     *
     * @param taskId   任务id
     * @param runnable 线程
     * @param delay    等待时间
     */
    public void schedule(int taskId, final Runnable runnable, long delay) {
        if (runnable == null) {
            return;
        }
        //先取消旧任务
        cancelTimingTask(taskId);
        //启动新任务
        ScheduledFuture<?> schedule = threadPoolExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        //存储记录
        TIMING_TASK_MAP.put(taskId, runnable);
        TIMING_TASK_MAP_SCHEDULED.put(taskId, schedule);
    }

    /**
     * 取消计时器
     *
     * @param taskId 计时器任务id
     */
    public void cancelTimingTask(int taskId) {
        Runnable task = TIMING_TASK_MAP.get(taskId);
        if (task != null) {
            threadPoolExecutor.remove(task);
            threadPoolExecutor.purge();
            TIMING_TASK_MAP.remove(taskId);
            task = null;
        }
        ScheduledFuture scheduledFuture = TIMING_TASK_MAP_SCHEDULED.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            TIMING_TASK_MAP_SCHEDULED.remove(taskId);
        }
    }
}
