package com.fasterar.smart.server.flink;
import com.fasterar.smart.server.flink.configure.FlinkConfig;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.properties.SmartServerFlinkProperties;

public class SmartServerFlinkApplication {
    public static void main(String[] args) throws Exception {
        SpringContextUtils.init();
        SmartServerFlinkProperties smartServerFlinkProperties = (SmartServerFlinkProperties) SpringContextUtils.getBean("smartServerFlinkProperties");
        FlinkConfig flinkConfig = new FlinkConfig();
        flinkConfig.setFlinkProperties(smartServerFlinkProperties);
        flinkConfig.runFlink();
    }
}

