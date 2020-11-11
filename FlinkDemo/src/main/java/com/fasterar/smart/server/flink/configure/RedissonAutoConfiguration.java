package com.fasterar.smart.server.flink.configure;

import com.fasterar.smart.server.flink.properties.SmartServerFlinkProperties;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedissonAutoConfiguration {
    @Autowired
    SmartServerFlinkProperties flinkProperties;

    @Autowired
    RedissonUtil redissonUtil;

    @Autowired
    RedissonConfig redissonConfig;

    @Bean
    public RedissonUtil redissonUtil(RedissonConfig redissonConfig) {
        RedissonUtil redissonUtil = new RedissonUtil(redissonConfig);
        log.info("[RedissonLock]组装完毕");
        return redissonUtil;
    }

    @Bean
    public RedissonConfig redissonConfig() {
        RedissonConfig redissonManager =
                new RedissonConfig(flinkProperties.getRedisson());
        log.info("[RedissonManager]组装完毕,当前连接方式:" + flinkProperties.getRedisson().getType() +
                ",连接地址:" + flinkProperties.getRedisson().getAddress());
        return redissonManager;
    }
}
