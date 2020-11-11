package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.entity.ShareHistoryTranspondDay;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 分享历史转发人数数据计算
 */
@Slf4j
public class ShareHistoryTranspondDayAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, ShareHistoryTranspondDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, ShareHistoryTranspondDay> concurrentHashMap = (ConcurrentHashMap<String, ShareHistoryTranspondDay>) o;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getTid())
                .append(dataLog.getTp())
                .append(dataLog.getUid())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        try {
            if (!RedisUtil.hasKey(key)) {
                ShareHistoryTranspondDay transpondDay = new ShareHistoryTranspondDay();
                transpondDay.setLinkId(dataLog.getTid());
                transpondDay.setLinkType(dataLog.getTp());
                transpondDay.setBeVisitedId(dataLog.getBuid());
                transpondDay.setStatisticsTime(dateTs);
                transpondDay.setUserId(Integer.valueOf(dataLog.getUid()));
                RedisUtil.set(key, dataLog.getUid());
                RedisUtil.expire(key, (long) 86400);
                concurrentHashMap.put(key, transpondDay);
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
