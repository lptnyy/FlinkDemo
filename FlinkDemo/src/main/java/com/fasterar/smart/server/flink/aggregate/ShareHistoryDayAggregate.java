package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.entity.ShareHistoryDay;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 分享历史数据计算
 */
@Slf4j
public class ShareHistoryDayAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, ShareHistoryDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, ShareHistoryDay> concurrentHashMap = (ConcurrentHashMap<String, ShareHistoryDay>) o;
        ShareHistoryDay shareHistoryDay;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getTid())
                .append(dataLog.getTp())
                .append(dataLog.getBuid())
                .append("/")
                .append(dataLog.getInid())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            shareHistoryDay = concurrentHashMap.get(key);
            if (dataLog.getInid().equals(1)) {
                shareHistoryDay.setBrowses(shareHistoryDay.getBrowses() + 1);
            } else {
                shareHistoryDay.setTransponds(shareHistoryDay.getTransponds() + 1);
            }
            concurrentHashMap.put(key, shareHistoryDay);
        } else {
            shareHistoryDay = new ShareHistoryDay();
            shareHistoryDay.setUserId(Integer.valueOf(dataLog.getBuid()));
            shareHistoryDay.setLinkId(dataLog.getTid());
            shareHistoryDay.setLinkTitle(dataLog.getTn());
            shareHistoryDay.setLinkType(dataLog.getTp());
            shareHistoryDay.setShareTime(dataLog.getTs());
            if (dataLog.getInid().equals(1)) {
                shareHistoryDay.setBrowses(1);
            } else {
                shareHistoryDay.setTransponds(1);
            }
            try {
                shareHistoryDay.setStatisticsTime(dateTs);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        try {
            String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
            if (!RedisUtil.hasKey(str)) {
                if (dataLog.getInid().equals(1)) {
                    shareHistoryDay.setBrowseNumber(1);
                } else {
                    shareHistoryDay.setTranspondNumber(1);
                }
                RedisUtil.set(str, dataLog.getUid());
                RedisUtil.expire(str, (long) 86400);
            }
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
        concurrentHashMap.put(key, shareHistoryDay);
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
