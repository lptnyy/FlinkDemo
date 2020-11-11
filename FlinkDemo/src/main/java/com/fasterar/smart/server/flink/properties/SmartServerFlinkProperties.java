package com.fasterar.smart.server.flink.properties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author mango
 */
@Primary
@Data
@Component
@PropertySource(value = {"classpath:smart-server-flink.properties"})
public class SmartServerFlinkProperties {
    /**
     * 免认证 URI，多个值的话以逗号分隔
     */
    @Value("${smart.server.flink.anonUrl}")
    private String anonUrl;
    /**
     * 批量插入当批次可插入的最大值
     */
    private Integer batchInsertMaxNum = 1000;

    @Autowired
    private FlinkProperties flink;

    @Autowired
    private RedissonProperties redisson;
}
