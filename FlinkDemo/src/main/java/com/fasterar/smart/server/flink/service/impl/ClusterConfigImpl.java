package com.fasterar.smart.server.flink.service.impl;
import com.fasterar.smart.server.flink.enums.GlobalConstant;
import com.fasterar.smart.server.flink.properties.RedissonProperties;
import com.fasterar.smart.server.flink.service.IRedissonService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mango
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ClusterConfigImpl implements IRedissonService {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            String[] addrTokens = address.split(",");
            //设置cluster节点的服务IP和端口
            for (int i = 0; i < addrTokens.length; i++) {
                config.useClusterServers()
                        .addNodeAddress(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
                if (StringUtils.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("初始化[集群部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("集群部署 Redisson init error", e);
            e.printStackTrace();
        }
        return config;
    }
}
