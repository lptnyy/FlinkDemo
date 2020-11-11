package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.entity.ShareStatisticsOpenNumber;
import com.fasterar.smart.server.flink.utils.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SQSJ-UI
 */
@Slf4j
public class DataShareRelationAggregate implements AggregateFunction<DataLog, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, ShareStatisticsOpenNumber>();
    }

    @SneakyThrows
    @Override
    public Object add(DataLog dataLog, Object o) {
//        RedisUtil redisUtil = new RedisUtil();
        ConcurrentHashMap<String, ShareStatisticsOpenNumber> concurrentHashMap = (ConcurrentHashMap<String, ShareStatisticsOpenNumber>) o;
        ShareStatisticsOpenNumber shareStatisticsOpenNumber;
        StringBuffer stringBuffer = new StringBuffer();
        String dateTs = DateUtil.ofDateDdString(dataLog.getTs());
        stringBuffer.append(dataLog.getInid())
                .append("/")
                .append(dataLog.getUid())
                .append(dataLog.getTid())
                .append(dataLog.getTp())
                .append(dateTs);
        if (concurrentHashMap.get(stringBuffer.toString()) != null) {
            shareStatisticsOpenNumber = concurrentHashMap.get(stringBuffer.toString());
            if (dataLog.getInid().equals(9)||dataLog.getInid().equals(6)) {
                shareStatisticsOpenNumber.setShareNumber(shareStatisticsOpenNumber.getShareNumber() + 1);
            }
            concurrentHashMap.put(stringBuffer.toString(), shareStatisticsOpenNumber);
        }else {
            shareStatisticsOpenNumber = new ShareStatisticsOpenNumber();
            shareStatisticsOpenNumber.setUid(Long.valueOf(dataLog.getUid()));
            if (dataLog.getInid().equals(9)||dataLog.getInid().equals(6)) {
                shareStatisticsOpenNumber.setShareNumber(1);
            }
            shareStatisticsOpenNumber.setType(dataLog.getTp());
            shareStatisticsOpenNumber.setTypeId(Long.valueOf(dataLog.getTid()));
            shareStatisticsOpenNumber.setStatisticsTime(DateUtil.ofDdDate(new Date()));
            shareStatisticsOpenNumber.setUid(Long.valueOf(dataLog.getUid()));
            concurrentHashMap.put(stringBuffer.toString(), shareStatisticsOpenNumber);
        }
        String str = stringBuffer.append("/").append(dataLog.getUid()).toString();
//        if (!redisUtil.hasKey(str)) {
//            redisUtil.setKey(str, dataLog.getUid());
//        }
        return concurrentHashMap;
    }

    @Override
    public Object getResult(Object o) {
        return o;
    }

    @Override
    public Object merge(Object a, Object b) {
        return null;
    }
}
