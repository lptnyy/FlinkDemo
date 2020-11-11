package com.fasterar.smart.server.flink.aggregate;

import com.fasterar.smart.server.flink.entity.AggregRegionSum;
import com.fasterar.smart.server.flink.entity.BigFrom;
import com.fasterar.smart.server.flink.entity.IpEntity;
import com.fasterar.smart.server.flink.ip.IpUtil;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ip以及用户 总数统计  聚合
 */
public class BigRegionAggregate implements AggregateFunction<BigFrom, Object, Object> {

    @Override
    public Object createAccumulator() {
        return new ConcurrentHashMap<String, AggregRegionSum>();
    }

    @Override
    public Object add(BigFrom bigFrom, Object o) {
        IpUtil ipUtil = IpUtil.getInstance();
        ConcurrentHashMap<String, AggregRegionSum> baseCreateAccumulator = (ConcurrentHashMap<String,
                AggregRegionSum>) o;
        AggregRegionSum aggregRegionSum = null;
        IpEntity ipEntity = null;

        try {
            ipEntity = ipUtil.getIpInfo(bigFrom.getBigData().getIp());
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("country")
                .append(ipEntity.getCountry())
                .append("city")
                .append(ipEntity.getCity())
                .append("province")
                .append(ipEntity.getProvince())
                .append("operators")
                .append(ipEntity.getOperators());

        if (baseCreateAccumulator.get(stringBuffer.toString()) != null) {
            aggregRegionSum = baseCreateAccumulator.get(stringBuffer.toString());
            aggregRegionSum.setSum(aggregRegionSum.getSum() + 1);
            baseCreateAccumulator.put(stringBuffer.toString(), aggregRegionSum);
        } else {
            aggregRegionSum = new AggregRegionSum();
            aggregRegionSum.setCity(ipEntity.getCity());
            aggregRegionSum.setProvince(ipEntity.getProvince());
            aggregRegionSum.setOperators(ipEntity.getOperators());
            aggregRegionSum.setCountry(ipEntity.getCountry());
            aggregRegionSum.setSum(1);
            baseCreateAccumulator.put(stringBuffer.toString(), aggregRegionSum);
        }
        return baseCreateAccumulator;
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
