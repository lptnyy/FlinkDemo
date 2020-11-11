package com.fasterar.smart.server.flink.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author mango
 */
@Data
@Component
@PropertySource(value = {"classpath:smart-server-flink.properties"})
public class RedissonProperties {

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    @Value("${smart.server.flink.redisson.address}")
    private String address;

    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    @Value("${smart.server.flink.redisson.type}")
    private String type;

    /**
     * redis 连接密码
     */
    @Value("${smart.server.flink.redisson.password}")
    private String password;

    /**
     * 选取那个数据库
     */
    @Value("${smart.server.flink.redisson.database}")
    private int database;

    public RedissonProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedissonProperties setDatabase(int database) {
        this.database = database;
        return this;
    }
}
