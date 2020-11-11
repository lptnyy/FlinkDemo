package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.CatalogVrModuleDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 视频组件计算
 */
@Slf4j
public class CatalogVrDayAggregate implements AggregateFunction<DataLog, Object, Object> {
    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, CatalogVrModuleDay>();
    }

    @Override
    public ConcurrentHashMap<String, CatalogVrModuleDay> add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, CatalogVrModuleDay> concurrentHashMap = (ConcurrentHashMap<String, CatalogVrModuleDay>) o;
        CatalogVrModuleDay vrModuleDay;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getBuid())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            vrModuleDay = concurrentHashMap.get(key);
            if (dataLog.getInid().equals(10)) {
                vrModuleDay.setPlacePage(dataLog.getInp());
                vrModuleDay.setPlays(vrModuleDay.getPlays() + 1);
                vrModuleDay.setClicks(vrModuleDay.getClicks() + 1);
            }
            concurrentHashMap.put(key, vrModuleDay);
        } else {
            vrModuleDay = new CatalogVrModuleDay();
            vrModuleDay.setCatalogId(dataLog.getTid());
            vrModuleDay.setVrName(dataLog.getMn());
            vrModuleDay.setVrUrl(dataLog.getMu());
            if (dataLog.getInid().equals(10)) {
                vrModuleDay.setPlacePage(dataLog.getInp());
                vrModuleDay.setPlays(1);
                vrModuleDay.setClicks(1);
            }
            try {
                vrModuleDay.setStatisticsTime(dateTs);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        try {
            String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
            if (!RedisUtil.hasKey(str)) {
                if (dataLog.getInid().equals(10)) {
                    vrModuleDay.setClickNumber(1);
                } else if (dataLog.getInid().equals(12)) {
                }
                RedisUtil.set(str, dataLog.getUid());
                RedisUtil.expire(str, (long) 86400);
            }
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
        concurrentHashMap.put(key, vrModuleDay);
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
