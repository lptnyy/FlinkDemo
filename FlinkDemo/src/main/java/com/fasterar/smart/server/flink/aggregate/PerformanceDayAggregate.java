package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.entity.PerformanceDay;
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
public class PerformanceDayAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, PerformanceDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, PerformanceDay> concurrentHashMap = (ConcurrentHashMap<String, PerformanceDay>) o;
        PerformanceDay performanceDay;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getTp())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            performanceDay = concurrentHashMap.get(key);
            if (dataLog.getInid().equals(9)) {
                performanceDay.setShares(performanceDay.getShares() + 1);
                performanceDay.setTransponds(performanceDay.getTransponds() + 1);
            } else if (dataLog.getInid().equals(1)) {
                performanceDay.setBrowses(performanceDay.getBrowses() + 1);
            }
            concurrentHashMap.put(key, performanceDay);
        } else {
            performanceDay = new PerformanceDay();
            performanceDay.setUserId(Integer.valueOf(dataLog.getBuid()));
            if (dataLog.getInid().equals(9)) {
                performanceDay.setShares(1);
                performanceDay.setTransponds(1);
            } else if (dataLog.getInid().equals(1)) {
                performanceDay.setBrowses(1);
            }
            try {
                performanceDay.setStatisticsTime(dateTs);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        try {
            String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
            if (!RedisUtil.hasKey(str) && dataLog.getInid().equals(1)) {
                performanceDay.setVisitors(1);
                RedisUtil.set(str, dataLog.getUid());
                RedisUtil.expire(str, (long) 86400);
            }
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
        concurrentHashMap.put(key, performanceDay);
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
