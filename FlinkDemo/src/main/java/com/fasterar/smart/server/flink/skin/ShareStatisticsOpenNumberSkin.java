package com.fasterar.smart.server.flink.skin;

//import com.fasterar.smart.server.flink.configure.HikariConfigration;

import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.ShareStatisticsOpenNumber;
import com.fasterar.smart.server.flink.service.FlinkSharecalCulationService;
import com.fasterar.smart.server.flink.service.IDataShareRelationService;
import com.fasterar.smart.server.flink.service.IShareStatisticsOpenNumberService;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SQSJ-UI
 */
public class ShareStatisticsOpenNumberSkin extends RichSinkFunction<ConcurrentHashMap<String, ShareStatisticsOpenNumber>> {
    Connection connection;
    PreparedStatement stmt = null;
    RedissonUtil redissonUtil;

    FlinkSharecalCulationService flinkSharecalCulationService;
    IDataShareRelationService dataShareRelationService;

    IShareStatisticsOpenNumberService shareStatisticsOpenNumberService;


    @Override
    public void open(Configuration parameters) throws Exception {
//        flinkSharecalCulationService = (FlinkSharecalCulationService) SpringContextUtils.getBean("flinkSharecalCulationService");
//        dataShareRelationService = flinkSharecalCulationService.getDataShareRelationService();
        flinkSharecalCulationService = (FlinkSharecalCulationService) SpringContextUtils.getBean("flinkSharecalCulationService");
        shareStatisticsOpenNumberService = flinkSharecalCulationService.getShareStatisticsOpenNumberService();

        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, ShareStatisticsOpenNumber> value, Context context) throws Exception {
        value.forEach((k, v)->{
            try {
                RedissonUtil.lock(k, 10);
                shareStatisticsOpenNumberService.queryShareStatisticsOpenNumber(v);
                RedissonUtil.unlock(k);
            } catch (Exception e) {
                RedissonUtil.unlock(k);
                e.printStackTrace();
            }
        });
    }

}
