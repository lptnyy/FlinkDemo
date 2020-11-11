package com.fasterar.smart.server.flink.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


public class RunServer implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  FlinkConfig flinkConfig;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    try {
      flinkConfig.runFlink();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
