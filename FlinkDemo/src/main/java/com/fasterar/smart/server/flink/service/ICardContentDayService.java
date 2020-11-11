package com.fasterar.smart.server.flink.service;

import com.fasterar.smart.server.flink.entity.CardContentDay;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterar.smart.common.QueryRequest;
import java.util.List;

/**
 * 名片内容分析表 Service接口
 *
 * @author hjx
 * @date 2020-10-30 17:17:00
 */
public interface ICardContentDayService extends IService<CardContentDay> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param cardContentDay cardContentDay
     * @return IPage<CardContentDay>
     */
    IPage<CardContentDay> findCardContentDays(QueryRequest request, CardContentDay cardContentDay);

    /**
     * 查询（所有）
     *
     * @param cardContentDay cardContentDay
     * @return List<CardContentDay>
     */
    List<CardContentDay> findCardContentDays(CardContentDay cardContentDay);

    /**
     * 新增
     *
     * @param cardContentDay cardContentDay
     */
    void createCardContentDay(CardContentDay cardContentDay);

    /**
     * 修改
     *
     * @param cardContentDay cardContentDay
     */
    void updateCardContentDay(CardContentDay cardContentDay);

    /**
     * 删除
     *
     * @param cardContentDay cardContentDay
     */
    void deleteCardContentDay(CardContentDay cardContentDay);
}
