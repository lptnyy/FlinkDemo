package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.ShareHistoryDay;
import com.fasterar.smart.server.flink.mapper.ShareHistoryDayMapper;
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
 * 分享历史数据操作
 */
@Slf4j
public class MysqlShareHistoryDaySkin extends RichSinkFunction<ConcurrentHashMap<String, ShareHistoryDay>> {
    ShareHistoryDayMapper shareHistoryDayMapper;
    SysUserWechatMapper sysUserWechatMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        shareHistoryDayMapper = (ShareHistoryDayMapper) SpringContextUtils.getBean("shareHistoryDayMapper");
        sysUserWechatMapper = (SysUserWechatMapper) SpringContextUtils.getBean("sysUserWechatMapper");
        super.open(parameters);
    }


    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, ShareHistoryDay> value, Context context) throws Exception {
        value.forEach((k, v) -> {
            try {
                RedissonUtil.lock(k, 100);
                Map<String, Object> map = this.sysUserWechatMapper.getUserId(v.getUserId());
                v.setUserId((Integer) map.get("userId"));
                LambdaQueryWrapper<ShareHistoryDay> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShareHistoryDay::getUserId, v.getUserId())
                        .eq(ShareHistoryDay::getLinkId, v.getLinkId())
                        .eq(ShareHistoryDay::getStatisticsTime, v.getStatisticsTime());
                ShareHistoryDay shareHistoryDay = this.shareHistoryDayMapper.selectOne(wrapper);
                if (shareHistoryDay == null) {
                    this.shareHistoryDayMapper.insert(v);
                } else {
                    shareHistoryDay.setTranspondNumber(shareHistoryDay.getTranspondNumber() + v.getTranspondNumber());
                    shareHistoryDay.setTransponds(shareHistoryDay.getTransponds() + v.getTransponds());
                    shareHistoryDay.setBrowseNumber(shareHistoryDay.getBrowseNumber() + v.getBrowseNumber());
                    shareHistoryDay.setBrowses(shareHistoryDay.getBrowses() + v.getBrowses());
                    this.shareHistoryDayMapper.updateById(shareHistoryDay);
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

    public void updateAggregateSum(ShareHistoryDay value) throws SQLException {
    }

    public void saveAggregateSum(ShareHistoryDay value) throws SQLException {
    }

    /**
     * 查询是否存在
     *
     * @param value
     * @return
     * @throws SQLException
     */
    public ShareHistoryDay queryAggregateSum(ShareHistoryDay value) throws SQLException {
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
