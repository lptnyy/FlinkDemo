package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.PerformanceDay;
import com.fasterar.smart.server.flink.mapper.PerformanceDayMapper;
import com.fasterar.smart.server.flink.mapper.SysUserWechatMapper;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 */
@Slf4j
public class MysqlPerformanceDaySkin extends RichSinkFunction<ConcurrentHashMap<String, PerformanceDay>> {
    //FlinkBaseServices services = null;
    PerformanceDayMapper performanceDayMapper;
    SysUserWechatMapper sysUserWechatMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        //services = (FlinkBaseServices) SpringContextUtils.getBean("flinkBaseServices");
        //services.getCardContentDayService().getById(1);
        performanceDayMapper = (PerformanceDayMapper) SpringContextUtils.getBean("performanceDayMapper");
        sysUserWechatMapper = (SysUserWechatMapper) SpringContextUtils.getBean("sysUserWechatMapper");
        super.open(parameters);
    }


    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, PerformanceDay> value, Context context) throws Exception {
        value.forEach((k, v) -> {
            try {
                RedissonUtil.lock(k, 100);
                Map<String, Object> map = this.sysUserWechatMapper.getUserId(v.getUserId());
                v.setUserId((Integer) map.get("userId"));
                v.setUserName((String) map.get("userName"));
                v.setCompanyId((Integer) map.get("companyId"));
                LambdaQueryWrapper<PerformanceDay> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(PerformanceDay::getUserId, v.getUserId())
                        .eq(PerformanceDay::getStatisticsTime, v.getStatisticsTime());
                PerformanceDay performanceDay = this.performanceDayMapper.selectOne(wrapper);
                if (performanceDay == null) {
                    this.performanceDayMapper.insert(v);
                } else {
                    performanceDay.setShares(performanceDay.getShares() + v.getShares());
                    performanceDay.setBrowses(performanceDay.getBrowses() + v.getBrowses());
                    performanceDay.setTransponds(performanceDay.getTransponds() + v.getTransponds());
                    this.performanceDayMapper.updateById(performanceDay);
                }
                try {
                    RedissonUtil.unlock(k);
                } catch (Exception e) {
                    log.error("redisson分布式锁异常", e);
                }
            } catch (Exception e) {
                RedissonUtil.unlock(k);
                log.error("执行数据库操作异常", e);
            }
        });
    }

    public void updateAggregateSum(PerformanceDay value) throws SQLException {
    }

    public void saveAggregateSum(PerformanceDay value) throws SQLException {
    }

    /**
     * 查询是否存在
     *
     * @param value
     * @return
     * @throws SQLException
     */
    public PerformanceDay queryAggregateSum(PerformanceDay value) throws SQLException {
        return null;
    }

    private Integer checkInt(Integer op) {
        if (op == null) {
            return 0;
        }
        return op;
    }

    private String checkStr(Object str) {
        if (str == null) {
            return "";
        }
        return str.toString();
    }
}
