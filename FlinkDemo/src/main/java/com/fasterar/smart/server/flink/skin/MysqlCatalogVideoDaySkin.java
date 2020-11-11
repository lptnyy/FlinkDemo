package com.fasterar.smart.server.flink.skin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterar.smart.server.flink.configure.SpringContextUtils;
import com.fasterar.smart.server.flink.entity.CatalogVideoModuleDay;
import com.fasterar.smart.server.flink.mapper.CatalogVideoModuleDayMapper;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mango
 */
@Slf4j
public class MysqlCatalogVideoDaySkin extends RichSinkFunction<ConcurrentHashMap<String, CatalogVideoModuleDay>> {

    CatalogVideoModuleDayMapper dayMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        dayMapper = (CatalogVideoModuleDayMapper) SpringContextUtils.getBean("catalogVideoModuleDayMapper");
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(ConcurrentHashMap<String, CatalogVideoModuleDay> value, Context context) throws Exception {
        value.forEach((k, v) -> {
            try {
                RedissonUtil.lock(k, 100);
                LambdaQueryWrapper<CatalogVideoModuleDay> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CatalogVideoModuleDay::getCatalogId, v.getCatalogId())
                        .eq(CatalogVideoModuleDay::getVideoName, v.getVideoName())
                        .eq(CatalogVideoModuleDay::getVideoUrl, v.getVideoUrl())
                        .eq(CatalogVideoModuleDay::getStatisticsTime, v.getStatisticsTime());
                CatalogVideoModuleDay videoModuleDay = this.dayMapper.selectOne(wrapper);
                if (videoModuleDay == null) {
                    this.dayMapper.insert(v);
                } else {
                    videoModuleDay.setPlayNumber(videoModuleDay.getPlayNumber() + v.getPlayNumber());
                    videoModuleDay.setPlays(videoModuleDay.getPlays() + v.getPlays());
                    videoModuleDay.setClickNumber(videoModuleDay.getClickNumber() + v.getClickNumber());
                    videoModuleDay.setClicks(videoModuleDay.getClicks() + v.getClicks());
                    videoModuleDay.setAfterPlay(videoModuleDay.getAfterPlay() + v.getAfterPlay());
                    if (v.getProgressThan() > videoModuleDay.getProgressThan()) {
                        videoModuleDay.setProgressThan(v.getProgressThan());
                    }
                    this.dayMapper.updateById(videoModuleDay);
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
