package com.fasterar.smart.server.flink.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class FlinkSharecalCulationService {

    @Autowired
    IDataShareRelationService dataShareRelationService;

    @Autowired
    IShareStatisticsOpenNumberService shareStatisticsOpenNumberService;
}
