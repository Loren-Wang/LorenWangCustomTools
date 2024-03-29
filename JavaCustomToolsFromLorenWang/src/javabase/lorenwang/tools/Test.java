package javabase.lorenwang.tools;

import java.util.List;

import javabase.lorenwang.tools.common.JtlwDateTimeUtil;

/**
 * 创建时间：2019-01-28 下午 15:15:21
 * 创建人：王亮（Loren wang）
 * 功能作用：${DESCRIPTION}
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class Test {
    public static void main(String[] args) {
        List<Long> longList = JtlwDateTimeUtil.getInstance().getYearList(12, 0);
        for (Long item : longList) {
            System.out.println(JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy", item));
        }
    }
}
