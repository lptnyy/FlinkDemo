package com.fasterar.smart.server.flink.service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Data
public class FlinkBaseServices {

  /**
   * 张思佳 start
   */
  @Autowired
  ICardContentDayService cardContentDayService;
  /**
   * 张思佳结束
   */
}
