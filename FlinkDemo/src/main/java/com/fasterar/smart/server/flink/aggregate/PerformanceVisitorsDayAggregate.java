package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.entity.PerformanceDay;
import com.fasterar.smart.server.flink.entity.PerformanceVisitorsDay;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 业绩管理计算
 */
@Slf4j
public class PerformanceVisitorsDayAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, PerformanceDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, PerformanceVisitorsDay> concurrentHashMap = (ConcurrentHashMap<String, PerformanceVisitorsDay>) o;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getBuid())
                .append("/")
                .append(dataLog.getUid())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        try {
            if (!RedisUtil.hasKey(key)) {
                PerformanceVisitorsDay visitorsDay = new PerformanceVisitorsDay();
                visitorsDay.setUserId(Integer.valueOf(dataLog.getUid()));
                visitorsDay.setBeVisitedId(dataLog.getBuid());
                visitorsDay.setStatisticsTime(dateTs);
                RedisUtil.set(key, dataLog.getUid());
                RedisUtil.expire(key, (long) 86400);
                concurrentHashMap.put(key, visitorsDay);
            }
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
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
