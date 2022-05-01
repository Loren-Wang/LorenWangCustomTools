package com.test.springboot;

import org.springframework.stereotype.Component;

import springbase.lorenwang.user.SpulwRedisClient;

/**
 * 功能作用：redis客户端实现类
 * 初始注释时间： 2022/5/1 13:05
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
@Component
public class RedisClient extends SpulwRedisClient {
    @Override
    public long getRedisCacheTimeout() {
        return 0;
    }
}
