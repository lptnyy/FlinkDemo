package com.fasterar.smart.server.flink.service;

import com.fasterar.smart.server.flink.properties.RedissonProperties;
import org.redisson.config.Config;

/**
 * @author mango
 */
public interface IRedissonService {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
