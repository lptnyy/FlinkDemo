package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.DataFlowCollectDay;
import com.fasterar.smart.server.flink.mapper.DataFlowCollectDayMapper;
import com.fasterar.smart.server.flink.mapper.SysUserWechatMapper;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 */
@Slf4j
public class MysqlDataFlowCollectDaySkin extends RichSinkFunction<ConcurrentHashMap<String, DataFlowCollectDay>> {
    DataFlowCollectDayMapper dayMapper;
    SysUserWechatMapper sysUserWechatMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        dayMapper = (DataFlowCollectDayMapper) SpringContextUtils.getBean("dataFlowCollectDayMapper");
        sysUserWechatMapper = (SysUserWechatMapper) SpringContextUtils.getBean("sysUserWechatMapper");
        super.open(parameters);
    }


    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, DataFlowCollectDay> value, Context context) throws Exception {
        value.forEach((k, v) -> {
            try {
                RedissonUtil.lock(k, 100);
                Map<String, Object> map = this.sysUserWechatMapper.getUserId(v.getUserId());
                v.setUserId((Integer) map.get("userId"));
                LambdaQueryWrapper<DataFlowCollectDay> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DataFlowCollectDay::getUserId, v.getUserId())
                        .eq(DataFlowCollectDay::getCompanyId, v.getCompanyId())
                        .eq(DataFlowCollectDay::getStatisticsTime, v.getStatisticsTime());
                DataFlowCollectDay collectDay = this.dayMapper.selectOne(wrapper);
                if (collectDay != null) {
                    this.dayMapper.updateById(v);
                } else {
                    this.dayMapper.insert(v);
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
}
