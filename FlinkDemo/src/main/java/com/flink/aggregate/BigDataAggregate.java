package com.flink.aggregate;
import com.flink.entity.AggregLocalSum;
import com.flink.entity.BigFrom;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * ip以及用户 总数统计  聚合
 */
public class BigDataAggregate implements AggregateFunction<BigFrom, Object, Object> {

  @Override
  public Object createAccumulator() {
    return new ConcurrentHashMap<String, AggregLocalSum>();
  }

  @Override
  public Object add(BigFrom bigFrom, Object o) {
    ConcurrentHashMap<String, AggregLocalSum> baseCreateAccumulator = (ConcurrentHashMap<String, AggregLocalSum>) o;
    AggregLocalSum aggregLocalSum = null;
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("uid")
        .append(bigFrom.getBigData().getUid())
        .append("ip").append(bigFrom.getBigData().getIp());
    if (baseCreateAccumulator.get(stringBuffer.toString()) != null) {
      aggregLocalSum = baseCreateAccumulator.get(stringBuffer.toString());
      aggregLocalSum.setSum(aggregLocalSum.getSum() + 1);
      baseCreateAccumulator.put(stringBuffer.toString(), aggregLocalSum);
    } else {
      aggregLocalSum = new AggregLocalSum();
      aggregLocalSum.setUid(bigFrom.getBigData().getUid());
      aggregLocalSum.setIp(bigFrom.getBigData().getIp());
      aggregLocalSum.setSum(1);
      baseCreateAccumulator.put(stringBuffer.toString(), aggregLocalSum);
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
