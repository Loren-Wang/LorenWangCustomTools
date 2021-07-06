package javabase.lorenwang.tools.common;

import com.google.gson.Gson;

import org.junit.Test;

import javabase.lorenwang.tools.bean.A;
import javabase.lorenwang.tools.bean.B;

public class JtlwBeanUtilTest {

    @Test
    public void copyWithTheParameters() {
        System.out.println(new Gson().toJson(JtlwBeanUtil.getInstance().copyWithTheParameters(new B(), new A(), false)));
        System.out.println(new Gson().toJson(JtlwBeanUtil.getInstance().copyWithTheParameters(new A(), B.class)));
    }

    @Test
    public void getBeanParameters() {
    }
}
