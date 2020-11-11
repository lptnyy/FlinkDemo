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
public class FlinkProperties {

    @Value("${smart.server.flink.flink.topicName}")
    private String topicName;

    @Value("${smart.server.flink.flink.kafkaServer}")
    private String kafkaServer;

    @Value("${smart.server.flink.flink.zookeeperServer}")
    private String zookeeperServer;

    @Value("${smart.server.flink.flink.groupId}")
    private String groupId;
}
