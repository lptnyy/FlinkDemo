package com.flink;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flink.aggregate.BigDataAggregate;
import com.flink.aggregate.BigRegionAggregate;
import com.flink.entity.BigData;
import com.flink.entity.BigFrom;
import com.flink.skin.MysqlDataSkin;
import com.flink.skin.MysqlLocalTestSkin;
import com.flink.skin.MysqlRegionTestSkin;
import java.util.Properties;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class FlnkDemo {
  public static int PARALLELISM = 1;
  public static int WINDOW_TIME = 30;

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers","192.168.1.144:9092");
    properties.setProperty("zookeeper.connect","192.168.1.144:2181");
    properties.setProperty("group.id", "bigData");
    env.setParallelism(PARALLELISM);
    env.enableCheckpointing(10000);
    env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
    env.getCheckpointConfig().setCheckpointTimeout(10000);
    env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

    // 创建kafka消费者 消费数据
    DataStream<String> stream = env.addSource(new FlinkKafkaConsumer<String>("logs", new SimpleStringSchema(), properties))
        .setParallelism(PARALLELISM);

    // 转换json数据 封装java对象
    DataStream<BigFrom> counts = stream.filter(data->{
      // 这里编写过滤条件 垃圾数据过滤不处理
      return true;
    }).map(data ->{ // json转换类型
          JSONObject jsonObject = JSON.parseObject(data);
          BigFrom bigFrom = jsonObject.toJavaObject(BigFrom.class);
          jsonObject = JSON.parseObject(bigFrom.getValue());
          BigData bigData = jsonObject.toJavaObject(BigData.class);
          bigFrom.setBigData(bigData);
          return bigFrom;
        }).setParallelism(PARALLELISM);

    // 统计计算 每个用户+ip
    DataStream ipStream = counts
        .keyBy("attributeId")
        .timeWindow(Time.seconds(WINDOW_TIME))
        .aggregate(new BigDataAggregate())
        .setParallelism(PARALLELISM);

    // 统计省份城市运营商数量
    DataStream regionStream = counts
        .keyBy("attributeId")
        .timeWindow(Time.seconds(WINDOW_TIME))
        .aggregate(new BigRegionAggregate())
        .setParallelism(PARALLELISM);

    // 原数据同步到数据中
    counts.addSink(new MysqlDataSkin())
        .setParallelism(PARALLELISM);

    // ip 统计结果存放到数据库
    ipStream.addSink(new MysqlLocalTestSkin())
        .setParallelism(PARALLELISM);

    // 计算省市运营商统计
    regionStream.addSink(new MysqlRegionTestSkin())
        .setParallelism(PARALLELISM);

    // 执行代码
    env.execute();
  }
}

