package com.fasterar.smart.server.flink.configure;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterar.smart.server.flink.aggregate.*;
import com.fasterar.smart.server.flink.entity.DataLog;
import com.fasterar.smart.server.flink.properties.SmartServerFlinkProperties;
import com.fasterar.smart.server.flink.skin.*;
import lombok.Data;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import java.util.Properties;

@Data
public class FlinkConfig {

    private SmartServerFlinkProperties flinkProperties;

    public static int PARALLELISM = 1;
    public static int WINDOW_TIME = 29;

    public Properties init(StreamExecutionEnvironment env) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", flinkProperties.getFlink().getKafkaServer());
        properties.setProperty("zookeeper.connect", flinkProperties.getFlink().getZookeeperServer());
        properties.setProperty("group.id", flinkProperties.getFlink().getGroupId());
        env.setParallelism(ExecutionConfig.PARALLELISM_DEFAULT);
        env.enableCheckpointing(10000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        return properties;
    }

    public void runFlink() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", flinkProperties.getFlink().getKafkaServer());
        properties.setProperty("zookeeper.connect", flinkProperties.getFlink().getZookeeperServer());
        properties.setProperty("group.id", flinkProperties.getFlink().getGroupId());
        env.setParallelism(ExecutionConfig.PARALLELISM_DEFAULT);
        env.enableCheckpointing(10000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 创建kafka消费者 消费数据
        DataStream<String> stream =
                env.addSource(new FlinkKafkaConsumer<String>(flinkProperties.getFlink().getTopicName(),
                        new SimpleStringSchema(),
                        properties))
                        .setParallelism(PARALLELISM);

        // 转换json数据 封装java对象
        DataStream<DataLog> counts = stream.filter(data -> {
            // 这里编写过滤条件 垃圾数据过滤不处理
            return true;
        }).map(data -> { // json转换类型
            JSONObject jsonObject = JSON.parseObject(data);
            DataLog dataLog = jsonObject.toJavaObject(DataLog.class);
            return dataLog;
        }).setParallelism(PARALLELISM);

        way1(counts);

        way2(counts);

        way3(counts);

        // 执行代码
        env.execute();
    }

    public void way1(DataStream<DataLog> counts) {
        //流量概括
        dataFlow(counts);

        //业绩管理
        performance(counts);

        //业绩管理被转发次数计算
        pTranspond(counts);

        //业绩管理浏览数
        pVisitors(counts);

        //分享历史
        shareHistory(counts);

        //分享历史转发人数计算
        sHistoryTranspond(counts);

        //分享历史浏览人数计算
        sHistoryBrowse(counts);

        //画册视频组件
        catalogVideo(counts);
    }

    public void dataFlow(DataStream<DataLog> counts) {
        //流量统计计算
        DataStream dataFlow = counts
                .filter(dataLog -> (dataLog.getInid().equals(1)
                        || dataLog.getInid().equals(8)
                        || (dataLog.getInid().equals(2)
                        && dataLog.getTp().equals("com"))))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new DataFlowDayAggregate())
                .setParallelism(PARALLELISM);
        // 流量保存
        dataFlow.addSink(new MysqlDataFlowDaySkin()).setParallelism(PARALLELISM);
    }

    public void flowBrowse(DataStream<DataLog> counts) {
        //流量统计计算
        DataStream flowBrowse = counts
                .filter(dataLog -> dataLog.getInid().equals(1))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new DataFlowBrowseDayAggregate())
                .setParallelism(PARALLELISM);
        // 流量保存
        flowBrowse.addSink(new MysqlDataFlowBrowseDaySkin()).setParallelism(PARALLELISM);
    }

    public void flowNewBrowse(DataStream<DataLog> counts) {
        //流量统计计算
        DataStream flowNewBrowse = counts
                .filter(dataLog -> dataLog.getInid().equals(1))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new DataFlowNewBrowseDayAggregate())
                .setParallelism(PARALLELISM);
        // 流量保存
        flowNewBrowse.addSink(new MysqlDataFlowNewBrowseDaySkin()).setParallelism(PARALLELISM);
    }

    public void flowCollect(DataStream<DataLog> counts) {
        //流量统计收藏人数计算
        DataStream flowCollect = counts
                .filter(dataLog -> dataLog.getInid().equals(2)
                && dataLog.getTp().equals("com"))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new DataFlowCollectDayAggregate())
                .setParallelism(PARALLELISM);
        // 流量收藏人数保存
        flowCollect.addSink(new MysqlDataFlowCollectDaySkin()).setParallelism(PARALLELISM);
    }

    public void performance(DataStream<DataLog> counts) {
        //业绩管理计算
        DataStream performance = counts
                .filter(dataLog -> (dataLog.getInid().equals(9)
                        || dataLog.getInid().equals(1))
                        && dataLog.getBuid() > 0)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new PerformanceDayAggregate())
                .setParallelism(PARALLELISM);
        //业绩管理保存
        performance.addSink(new MysqlPerformanceDaySkin()).setParallelism(PARALLELISM);
    }

    public void pTranspond(DataStream<DataLog> counts) {
        //业绩管理被转发次数计算
        DataStream pTranspond = counts
                .filter(dataLog -> dataLog.getInid().equals(6)
                        && dataLog.getBuid() > 0)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new PerformanceTranspondDayAggregate())
                .setParallelism(PARALLELISM);
        //业绩管理被转发次数保存
        pTranspond.addSink(new MysqlPerformanceDaySkin()).setParallelism(PARALLELISM);
    }

    public void pVisitors(DataStream<DataLog> counts) {
        //业绩管理计算
        DataStream pVisitors = counts
                .filter(dataLog -> dataLog.getInid().equals(1)
                        && dataLog.getBuid() > 0)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new PerformanceVisitorsDayAggregate())
                .setParallelism(PARALLELISM);
        //业绩管理保存
        pVisitors.addSink(new MysqlPerformanceVisitorsDaySkin()).setParallelism(PARALLELISM);
    }

    public void shareHistory(DataStream<DataLog> counts) {
        //分享历史计算
        DataStream shareHistory = counts
                .filter(dataLog -> (dataLog.getInid().equals(9)
                        || dataLog.getInid().equals(6)
                        || dataLog.getInid().equals(1))
                        && dataLog.getBuid() > 0)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new ShareHistoryDayAggregate())
                .setParallelism(PARALLELISM);
        //分享历史保存
        shareHistory.addSink(new MysqlShareHistoryDaySkin()).setParallelism(PARALLELISM);
    }

    public void sHistoryTranspond(DataStream<DataLog> counts) {
        //分享历史转发人数计算
        DataStream sHistoryTranspond = counts
                .filter(dataLog -> !dataLog.getUid().substring(0, 1).equals("-")
                        && (dataLog.getInid().equals(9)
                        || dataLog.getInid().equals(6))
                        && dataLog.getBuid() > 0
                        && !dataLog.getUid().equals(dataLog.getBuid()))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new ShareHistoryTranspondDayAggregate())
                .setParallelism(PARALLELISM);
        //分享历史转发人数计算
        sHistoryTranspond.addSink(new MysqlShareHistoryTranspondDaySkin()).setParallelism(PARALLELISM);
    }

    public void sHistoryBrowse(DataStream<DataLog> counts) {
        //分享历史浏览人数计算
        DataStream sHistoryBrowse = counts
                .filter(dataLog -> dataLog.getInid().equals(1)
                        && dataLog.getBuid() > 0)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new ShareHistoryBrowseDayAggregate())
                .setParallelism(PARALLELISM);
        //分享历史浏览人数计算
        sHistoryBrowse.addSink(new MysqlShareHistoryBrowseDaySkin()).setParallelism(PARALLELISM);
    }

    public void catalogVideo(DataStream<DataLog> counts) {
        //画册视频组件计算
        DataStream catalogVideo = counts
                .filter(dataLog -> (dataLog.getInid().equals(10)
                        || dataLog.getInid().equals(12))
                        && dataLog.getTp().equals("alb")
                        && dataLog.getMt().equals(1))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new CatalogVideoDayAggregate())
                .setParallelism(PARALLELISM);
        catalogVideo.addSink(new MysqlCatalogVideoDaySkin()).setParallelism(PARALLELISM);
    }

    public void catalogVr(DataStream<DataLog> counts) {
        //画册视频组件计算
        DataStream catalogVr = counts
                .filter(dataLog -> (dataLog.getInid().equals(10)
                        || dataLog.getInid().equals(12))
                        && dataLog.getTp().equals("alb")
                        && dataLog.getMt().equals(1))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(new CatalogVideoDayAggregate())
                .setParallelism(PARALLELISM);
        catalogVr.addSink(new MysqlCatalogVideoDaySkin()).setParallelism(PARALLELISM);
    }

    public void way2(DataStream<DataLog> counts) {

        // 画册访客数、浏览次数
        DataStream catalogStream = common(counts, 1, "alb", new CatalogContentAggregate());
        catalogStream.addSink(new MysqlCatalogContentSkin()).setParallelism(PARALLELISM);

        // 画册收藏人数
        DataStream albFav = commonForFav(counts, 2, 0, "alb", new CatalogContentFavAggregate());
        albFav.addSink(new MysqlCatalogContentFavSkin());

        // 画册页面留存分析
        DataStream catalogKeepContentStream = common(counts, 5, "alb", new CatalogKeepContentDayAggregate());
        catalogKeepContentStream.addSink(new MysqlCatalogKeepContentSkin());

        // 画册平均浏览深度
        DataStream albAvgDp = common(counts, 3, "alb", new CatalogContentAvgDpAggregate());
        albAvgDp.addSink(new MysqlCatalogContentAvgDpSkin());

        // 分享人数、分享次数
        DataStream forShare = commonForShare(counts, 6, "alb", new CatalogContentForShareAggregate());
        forShare.addSink(new MysqlCatalogContentForShareSkin());

        // 分享访问人数、分享访问次数
        DataStream albSv = common(counts, 7, "alb", new CatalogContentSvAggregate());
        albSv.addSink(new MysqlCatalogContentVsSkin());

    }

    public DataStream common(DataStream<DataLog> counts, int inid, String tp, AggregateFunction o) {
        DataStream catalogStream = counts
                .filter(h -> h.getInid() == inid && tp.equals(h.getTp()))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(o)
                .setParallelism(PARALLELISM);
        return catalogStream;
    }

    public DataStream commonForFav(DataStream<DataLog> counts, int inid, int fav, String tp, AggregateFunction o) {
        DataStream catalogStream = counts
                .filter(h -> h.getInid() == inid && tp.equals(h.getTp()) && h.getFav() > fav)
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(o)
                .setParallelism(PARALLELISM);
        return catalogStream;
    }

    public DataStream commonForShare(DataStream<DataLog> counts, int inid, String tp, AggregateFunction o) {
        DataStream catalogStream = counts
                .filter(h -> h.getInid() == inid || h.getInid() == 9 && tp.equals(h.getTp()))
                .keyBy("inid")
                .timeWindow(Time.seconds(WINDOW_TIME))
                .aggregate(o)
                .setParallelism(PARALLELISM);
        return catalogStream;
    }

    public void way3(DataStream<DataLog> counts) {
        //用户转发次数计算
        DataStream performance =
                counts.filter(dataLog -> !dataLog.getUid().substring(0, 1).equals("-") && dataLog.getInid().equals(9) || dataLog.getInid().equals(6))
                        .keyBy("inid")
                        .timeWindow(Time.seconds(WINDOW_TIME))
                        .aggregate(new DataShareRelationAggregate())
                        .setParallelism(PARALLELISM);

        // 转发保存
        performance.addSink(new ShareStatisticsOpenNumberSkin())
                .setParallelism(PARALLELISM);
    }
}
