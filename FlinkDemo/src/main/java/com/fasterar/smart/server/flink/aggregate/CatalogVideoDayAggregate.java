package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.CatalogVideoModuleDay;
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
public class CatalogVideoDayAggregate implements AggregateFunction<DataLog, Object, Object> {
    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, CatalogVideoModuleDay>();
    }

    @Override
    public ConcurrentHashMap<String, CatalogVideoModuleDay> add(DataLog dataLog, Object o) {
        ConcurrentHashMap<String, CatalogVideoModuleDay> concurrentHashMap = (ConcurrentHashMap<String, CatalogVideoModuleDay>) o;
        CatalogVideoModuleDay catalogVideoModuleDay;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getTid())
                .append("/")
                .append(dataLog.getMn())
                .append("/")
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            catalogVideoModuleDay = concurrentHashMap.get(key);
            catalogVideoModuleDay.setPlacePage(dataLog.getInp());
            if (dataLog.getInid().equals(10)) {
                catalogVideoModuleDay.setPlays(catalogVideoModuleDay.getPlays() + 1);
                catalogVideoModuleDay.setClicks(catalogVideoModuleDay.getClicks() + 1);
            }
            concurrentHashMap.put(key, catalogVideoModuleDay);
        } else {
            catalogVideoModuleDay = new CatalogVideoModuleDay();
            catalogVideoModuleDay.setCatalogId(dataLog.getTid());
            catalogVideoModuleDay.setVideoName(dataLog.getMn());
            catalogVideoModuleDay.setVideoUrl(dataLog.getMu());
            catalogVideoModuleDay.setPlacePage(dataLog.getInp());
            if (dataLog.getInid().equals(10)) {
                catalogVideoModuleDay.setPlays(1);
                catalogVideoModuleDay.setClicks(1);
            }
            try {
                catalogVideoModuleDay.setStatisticsTime(dateTs);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        try {
            double progressThan = 0;
            if (RedisUtil.get(key) != null) {
                progressThan = (double)RedisUtil.get(key);
            }
            String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
            if (!RedisUtil.hasKey(str)) {
                if (dataLog.getInid().equals(10)) {
                    catalogVideoModuleDay.setPlayNumber(1);
                    catalogVideoModuleDay.setClickNumber(1);
                }
                RedisUtil.set(str, dataLog.getBfb(), (long) 86400);
                RedisUtil.set(key, progressThan + dataLog.getBfb(), (long) 86400);
                if (dataLog.getBfb() == 100) {
                    catalogVideoModuleDay.setAfterPlay(1);
                }
            } else {
                //单个人最大bfb
                double bfb = (double) RedisUtil.get(str);
                if (dataLog.getBfb() > bfb && dataLog.getInid().equals(12)) {
                    //更新后的单个人最大百分比
                    RedisUtil.del(str);
                    RedisUtil.set(str, dataLog.getBfb(), (long) 86400);
                    double bfb1 = (double) RedisUtil.get(str);
                    //所有人的最大百分比的合
                    RedisUtil.del(key);
                    //更新后的所有人最大百分比的和
                    double allBfb = progressThan + bfb1 - bfb;
                    RedisUtil.set(key, allBfb, (long) 86400);
                    if (dataLog.getBfb() == 100) {
                        catalogVideoModuleDay.setAfterPlay(1);
                    }
                }
            }
            catalogVideoModuleDay.setProgressThan((double)RedisUtil.get(key));
        } catch (Exception e) {
            log.error("redis缓存异常", e);
        }
        concurrentHashMap.put(key, catalogVideoModuleDay);
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
