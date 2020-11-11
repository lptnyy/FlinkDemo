package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.CatalogContentDay;
import com.fasterar.smart.server.flink.entity.CatalogKeepContentDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

public class CatalogKeepContentDayAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, CatalogKeepContentDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {

        ConcurrentHashMap<String, CatalogKeepContentDay> concurrentHashMap = (ConcurrentHashMap<String, CatalogKeepContentDay>) o;
//        RedisUtil redisUtil = new RedisUtil();
        CatalogKeepContentDay catalogKeepContentDay = new CatalogKeepContentDay();
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getTid())
                .append(dataLog.getTp())
                .append("/")
                .append(dataLog.getP())
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            catalogKeepContentDay = concurrentHashMap.get(key);
            catalogKeepContentDay.setBrowses(catalogKeepContentDay.getBrowses() +1);
        } else {
            catalogKeepContentDay.setBrowses(1);
            catalogKeepContentDay.setCatalogId(dataLog.getTid());
            catalogKeepContentDay.setPage(dataLog.getP());
            catalogKeepContentDay.setState(dataLog.getSt());
            catalogKeepContentDay.setStatisticsTime(dateTs);
        }
//        String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
//        if (!RedisUtil.hasKey(str)) {
//            catalogKeepContentDay.setBrowses(1);
//            RedisUtil.set(str, dataLog.getUid());
//            RedisUtil.expire(str, (long)86400);
//        } else {
//            catalogKeepContentDay.setVisitors(0);
//        }
        concurrentHashMap.put(key, catalogKeepContentDay);
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
