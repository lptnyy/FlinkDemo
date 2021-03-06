package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.CatalogContentDay;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedisUtil;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.concurrent.ConcurrentHashMap;

public class CatalogContentForShareAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, CatalogContentDay>();
    }

    @Override
    public Object add(DataLog dataLog, Object o) {

        ConcurrentHashMap<String, CatalogContentDay> concurrentHashMap = (ConcurrentHashMap<String, CatalogContentDay>) o;
//        RedisUtil redisUtil = new RedisUtil();
        CatalogContentDay catalogContent = new CatalogContentDay();
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getTid())
                .append(dataLog.getTp())
                .append(dateTs);
        String key = stringBuffer.toString();
        if (concurrentHashMap.get(key) != null) {
            catalogContent = concurrentHashMap.get(key);
            catalogContent.setShares(catalogContent.getShares() +1);
        } else {
            catalogContent.setCatalogId(dataLog.getTid());
            catalogContent.setShares(1);
            catalogContent.setCatalogName(dataLog.getRin());
            catalogContent.setState(dataLog.getSt());
            catalogContent.setCompanyId(dataLog.getCid());
            catalogContent.setStatisticsTime(dateTs);
        }
        String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
        if (!RedisUtil.hasKey(str)) {
            catalogContent.setShareNumber(1);
            RedisUtil.set(str, dataLog.getUid());
            RedisUtil.expire(str, 86400L);
        } else {
            catalogContent.setShareNumber(0);
        }
        concurrentHashMap.put(key, catalogContent);
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
