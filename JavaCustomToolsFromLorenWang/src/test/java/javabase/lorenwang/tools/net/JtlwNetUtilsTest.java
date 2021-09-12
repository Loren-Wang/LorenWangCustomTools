package javabase.lorenwang.tools.net;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * 功能作用：
 * 创建时间：2020-11-24 3:30 下午
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
public class JtlwNetUtilsTest {
    @Test
    public void getUrlHost() {
        System.out.println(JtlwNetUtils.getInstance().getUrlHost("/app/test"));
        System.out.println(JtlwNetUtils.getInstance().getUrlHost("www.baidu.com/app/test"));
        System.out.println(JtlwNetUtils.getInstance().getUrlHost("www.baidu.com"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("/app/test", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com/app/test", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("/app/test?id=1", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com/app/test?id=1", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com?a=2&id=4", "id"));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com?", null));
        System.out.println(JtlwNetUtils.getInstance().getUrlParams("www.baidu.com&", null));
        System.out.println(JtlwNetUtils.getInstance().addUrlParams("www.baidu.com&&&&", "id", "1"));
        System.out.println(JtlwNetUtils.getInstance().addUrlParams("www.baidu.com", "id", "1"));
        System.out.println(JtlwNetUtils.getInstance().addUrlParams("www.baidu.com?a=2", "id", "1"));
        System.out.println(JtlwNetUtils.getInstance().addUrlParams("www.baidu.com???", "id", "1"));
        ArrayList<String> keys = new ArrayList<>();
        keys.add("key");
        keys.add("a");
        keys.add("cccc");
        ArrayList<Object> values = new ArrayList<>();
        values.add(1);
        values.add("2");
        values.add(true);
        System.out.println(JtlwNetUtils.getInstance().addUrlParams("www.baidu.com???", keys, values));
    }
}
