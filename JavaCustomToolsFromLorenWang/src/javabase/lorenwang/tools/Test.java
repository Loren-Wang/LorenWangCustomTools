package javabase.lorenwang.tools;

import javabase.lorenwang.tools.thread.CountDownCallback;
import javabase.lorenwang.tools.thread.TimerUtils;

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
        TimerUtils.getInstance().countDownTask(1, new CountDownCallback() {
            @Override
            public void countDownTime(long sumTime, long nowTime) {
                System.out.print(nowTime);
            }

            @Override
            public void finish() {

            }
        },60000,1000);
    }
}
