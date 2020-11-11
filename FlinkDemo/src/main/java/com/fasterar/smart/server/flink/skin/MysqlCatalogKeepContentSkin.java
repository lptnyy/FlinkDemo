package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.CatalogContentDay;
import com.fasterar.smart.server.flink.entity.CatalogKeepContentDay;
import com.fasterar.smart.server.flink.mapper.CatalogContentDayMapper;
import com.fasterar.smart.server.flink.mapper.CatalogKeepContentDayMapper;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlCatalogKeepContentSkin extends RichSinkFunction <ConcurrentHashMap<String, CatalogKeepContentDay>>{

    CatalogKeepContentDayMapper catalogKeepContentDayMapper;

    public void getConnection() throws ClassNotFoundException, SQLException {
//        HikariConfigration hikariConfigration = HikariConfigration.getInstance();
//        connection = hikariConfigration.getDs().getConnection();
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        catalogKeepContentDayMapper = (CatalogKeepContentDayMapper) SpringContextUtils.getBean("catalogKeepContentDayMapper");
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
    @Override
    public void invoke(ConcurrentHashMap<String, CatalogKeepContentDay> value, Context context) throws Exception {
        value.forEach((k,v) ->{
                RedissonUtil.lock(k,5);
                LambdaQueryWrapper<CatalogKeepContentDay> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CatalogKeepContentDay::getCatalogId, v.getCatalogId())
                .eq(CatalogKeepContentDay::getStatisticsTime, v.getStatisticsTime())
                .eq(CatalogKeepContentDay::getPage, v.getPage());
            CatalogKeepContentDay catalogKeepContentDay = catalogKeepContentDayMapper.selectOne(queryWrapper);
                if (catalogKeepContentDay == null) {
                    v.setStatisticsTime(DateUtil.ofDateDdString(new Date()));
                    catalogKeepContentDayMapper.insert(v);
                } else {
                    catalogKeepContentDay.setBrowses(catalogKeepContentDay.getBrowses()+ v.getBrowses());
                    catalogKeepContentDayMapper.updateById(catalogKeepContentDay);
                }
                RedissonUtil.unlock(k);
        });
    }
}
