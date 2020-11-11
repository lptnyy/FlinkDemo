package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataFlowCollectDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 浏览收藏数统计
 */
@Slf4j
public class DataFlowCollectDayAggregate implements AggregateFunction<DataLog, Object, Object> {
    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, DataFlowCollectDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, DataFlowCollectDay> concurrentHashMap = (ConcurrentHashMap<String,
                DataFlowCollectDay>) o;
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        DataFlowCollectDay collectDay = new DataFlowCollectDay();
        collectDay.setCompanyId(dataLog.getCid());
        collectDay.setUserId(Integer.valueOf(dataLog.getUid()));
        collectDay.setFav(dataLog.getFav());
        collectDay.setStatisticsTime(dateTs);
        return concurrentHashMap;
    }

    @Override
    public Object getResult(Object o) {
        return o;
    }

    @Override
    public Object merge(Object o, Object acc1) {
        return null;
    }
}
