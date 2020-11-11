package com.fasterar.smart.server.flink.skin;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.ShareHistoryTranspondDay;
import com.fasterar.smart.server.flink.mapper.ShareHistoryTranspondDayMapper;
import com.fasterar.smart.server.flink.mapper.SysUserWechatMapper;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 * 分享历史转发人数数据操作
 */
@Slf4j
public class MysqlShareHistoryTranspondDaySkin extends RichSinkFunction<ConcurrentHashMap<String,
        ShareHistoryTranspondDay>> {
    ShareHistoryTranspondDayMapper dayMapper;
    SysUserWechatMapper sysUserWechatMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        dayMapper = (ShareHistoryTranspondDayMapper) SpringContextUtils.getBean("shareHistoryTranspondDayMapper");
        sysUserWechatMapper = (SysUserWechatMapper) SpringContextUtils.getBean("sysUserWechatMapper");
        super.open(parameters);
    }


    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, ShareHistoryTranspondDay> value, Context context) throws Exception {
        value.forEach((k, v) -> {
            try {
                RedissonUtil.lock(k, 100);
                Map<String, Object> map = this.sysUserWechatMapper.getUserId(v.getUserId());
                if (StringUtil.isNotBlank((String) map.get("userId"))) {
                    v.setUserId((Integer) map.get("userId"));
                }
                Map<String, Object> maps = this.sysUserWechatMapper.getUserId(v.getBeVisitedId());
                if (StringUtil.isNotBlank((String) maps.get("userId"))) {
                    v.setBeVisitedId((Integer) maps.get("userId"));
                }
                this.dayMapper.insert(v);
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
