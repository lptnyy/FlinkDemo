package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataFlowDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 浏览统计
 */
@Slf4j
public class DataFlowDayAggregate implements AggregateFunction<DataLog, Object, Object> {
    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, DataFlowDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, DataFlowDay> concurrentHashMap = (ConcurrentHashMap<String, DataFlowDay>) o;
        DataFlowDay flowDay;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getTp())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        //同一window 是否有相同数据
        if (concurrentHashMap.get(key) != null) {
            flowDay = concurrentHashMap.get(key);
            if (dataLog.getInid().equals(1)) {
                flowDay.setBrowses(flowDay.getBrowses() + 1);
            }
            concurrentHashMap.put(key, flowDay);
        } else {
            flowDay = new DataFlowDay();
            flowDay.setType(dataLog.getTp());
            flowDay.setState(dataLog.getSt());
            if (dataLog.getInid().equals(1)) {
                flowDay.setBrowses(1);
            }
            try {
                flowDay.setStatisticsTime(dateTs);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        try {
            int allDateT = 0;
            if (RedisUtil.get(key) != null) {
                allDateT = (int)RedisUtil.get(key);
            }
            int dateT = 0;
            if (dataLog.getInid().equals(8)) {
                dateT = Math.toIntExact(DateUtil.getDateT(dataLog.getSts(), dataLog.getEts()));
            }
            String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
            if (!RedisUtil.hasKey(str)) {
                if (dataLog.getInid().equals(1)) {
                    flowDay.setVisitors(1);
                } else if (dataLog.getInid().equals(8)) {
                    flowDay.setStayTime(dateT);
                }
                RedisUtil.set(str, dateT, (long) 86400);
                RedisUtil.set(key, allDateT + dateT, (long) 86400);
            } else {
                // 单个人最大停留时长
                int oneDateT = (int)RedisUtil.get(str);
                //判断当前停留时长是否大于之前的停留时长
                if (dateT > oneDateT && dataLog.getInid().equals(8)) {
                    //更新每个人最大停留时长
                    RedisUtil.del(str);
                    RedisUtil.set(str, dateT, (long) 86400);
                    //更新后的时长
                    int updOneDateT = (int)RedisUtil.get(key);
                    //将所有人停留时间减去当前人旧的停留时长加上新的停留时长
                    RedisUtil.del(key);
                    RedisUtil.set(key, allDateT - oneDateT + updOneDateT, (long) 86400);
                }
            }
            flowDay.setStayTime((int)RedisUtil.get(key));
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
        concurrentHashMap.put(key, flowDay);
        return concurrentHashMap;
    }

    @Override
    public Object getResult(Object o) {
        return null;
    }

    @Override
    public Object merge(Object o, Object acc1) {
        return null;
    }
}
