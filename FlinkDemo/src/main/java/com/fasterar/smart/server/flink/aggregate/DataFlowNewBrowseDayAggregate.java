package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataFlowNewBrowseDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 浏览访客数统计
 */
@Slf4j
public class DataFlowNewBrowseDayAggregate implements AggregateFunction<DataLog, Object, Object> {
    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, DataFlowNewBrowseDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, DataFlowNewBrowseDay> concurrentHashMap = (ConcurrentHashMap<String,
                DataFlowNewBrowseDay>) o;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getUid())
                .append("/")
                .append(dataLog.getTp());
        String key = stringBuffer.toString();
        try {
            if (!RedisUtil.hasKey(key)) {
                DataFlowNewBrowseDay browseDay = new DataFlowNewBrowseDay();
                browseDay.setCompanyId(dataLog.getCid());
                browseDay.setUserId(Integer.valueOf(dataLog.getUid()));
                browseDay.setType(dataLog.getTp());
                browseDay.setStatisticsTime(dateTs);
                RedisUtil.set(key, dataLog.getUid(), (long) 86400);
                concurrentHashMap.put(key, browseDay);
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
