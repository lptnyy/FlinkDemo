package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.CatalogContentDay;
import com.fasterar.smart.server.flink.mapper.CatalogContentDayMapper;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlCatalogContentFavSkin extends RichSinkFunction <ConcurrentHashMap<String, CatalogContentDay>>{

    CatalogContentDayMapper catalogContentDayMapper;

    public void getConnection() throws ClassNotFoundException, SQLException {
//        HikariConfigration hikariConfigration = HikariConfigration.getInstance();
//        connection = hikariConfigration.getDs().getConnection();
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        catalogContentDayMapper = (CatalogContentDayMapper) SpringContextUtils.getBean("catalogContentDayMapper");
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
    @Override
    public void invoke(ConcurrentHashMap<String, CatalogContentDay> value, Context context) throws Exception {
        value.forEach((k,v) ->{
                RedissonUtil.lock(k,5);
                LambdaQueryWrapper<CatalogContentDay> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CatalogContentDay::getCatalogId, v.getCatalogId())
                .eq(CatalogContentDay::getStatisticsTime, v.getStatisticsTime());
                CatalogContentDay catalogContent = catalogContentDayMapper.selectOne(queryWrapper);
                if (catalogContent == null) {
                    v.setStatisticsTime(DateUtil.ofDateDdString(new Date()));
                    v.setCatalogType("a");
                    v.setCollectNumber(1);
                    catalogContentDayMapper.insert(v);
                } else {
                    catalogContent.setCollectNumber(catalogContent.getCollectNumber()+ v.getCollectNumber());
                    catalogContentDayMapper.updateById(catalogContent);
                }
                RedissonUtil.unlock(k);
        });
    }
}
