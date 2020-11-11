package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.CatalogContentDay;
import com.fasterar.smart.server.flink.mapper.CatalogContentDayMapper;
import com.fasterar.smart.server.flink.utils.DateUtil;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlCatalogContentSkin extends RichSinkFunction <ConcurrentHashMap<String, CatalogContentDay>>{

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
                    v.setVisitors(1);
                    v.setBrowses(1);
                    v.setStatisticsTime(DateUtil.ofDateDdString(new Date()));
                    v.setCatalogType("a");
                    catalogContentDayMapper.insert(v);
                } else {
                    catalogContent.setVisitors(catalogContent.getVisitors()+ v.getVisitors());
                    catalogContent.setBrowses(catalogContent.getBrowses() + v.getBrowses());

                    catalogContentDayMapper.updateById(catalogContent);
                }
                RedissonUtil.unlock(k);
        });
    }

//    public CatalogContent selectCatalogContent (CatalogContent v) throws SQLException {
//        CatalogContent catalogContent = new CatalogContent();
//        String dateString = DateUtil.ofDateString(v.getStatisticsTime());
//        String sql = "select ccd.id,ccd.catalog_id,ccd.catalog_name,ccd.visitors,ccd.browses,ccd.shares,ccd.spread,ccd.browse_depth," +
//                "ccd.share_number,ccd.share_visitors,ccd.share_visitor_number,ccd.state,ccd.collect_number,ccd.statistics_time,ccd.company_id "
//                + "from catalog_content_day ccd "
//                + " where ccd.catalog_id='"+v.getCatalogId()+"'"
//                + "and ccd.statistics_time like '"+dateString+"%'";
//        System.out.println("sql:" + sql);
//        stmt = connection.prepareStatement(sql);
//        ResultSet resultSet = stmt.executeQuery();
//        while (resultSet.next()){
//            catalogContent.setBrowses(resultSet.getInt("browses"));
//            catalogContent.setVisitors(resultSet.getInt("visitors"));
//            catalogContent.setCatalogName(resultSet.getString("catalog_name"));
//            catalogContent.setId(resultSet.getInt("id"));
//        }
//        // try 里释放掉 测试demo 随意写
//        stmt.close();
//        resultSet.close();
//        return catalogContent;
//    }

//    public void saveCatalogContent (CatalogContent v) throws SQLException {
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("insert into catalog_content_day(catalog_id,catalog_name,visitors,browses,statistics_time,catalog_type,company_id,state)");
//        stringBuffer.append("values (?,?,?,?,?,?,?,?)");
//        stmt = connection.prepareStatement(stringBuffer.toString());
//        stmt.setInt(1, v.getCatalogId());
//        stmt.setString(2, v.getCatalogName());
//        stmt.setInt(3, 1);
//        stmt.setInt(4, 1);
//        stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
//        stmt.setString(6, "a");
//        stmt.setInt(7, v.getCompanyId());
//        stmt.setInt(8, v.getState());
//        stmt.executeUpdate();
//    }

//    public void updateCatalogContent (CatalogContent v) throws SQLException {
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("update catalog_content_day ");
//        stringBuffer.append(" set visitors='").append(v.getVisitors()).append("'");
//        stringBuffer.append(",browses='").append(v.getBrowses()).append("'");
//        stringBuffer.append(" where id=").append(v.getId());
//        stmt = connection.prepareStatement(stringBuffer.toString());
//        stmt.executeUpdate();
//    }
}
