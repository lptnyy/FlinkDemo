package com.fasterar.smart.server.flink.service.impl;

import com.fasterar.smart.server.flink.entity.CardContentDay;
import com.fasterar.smart.server.flink.mapper.CardContentDayMapper;
import com.fasterar.smart.server.flink.service.ICardContentDayService;
import com.fasterar.smart.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 名片内容分析表 Service实现
 *
 * @author hjx
 * @date 2020-10-30 17:17:00
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CardContentDayServiceImpl extends ServiceImpl<CardContentDayMapper, CardContentDay> implements ICardContentDayService {

    @Autowired
    private CardContentDayMapper cardContentDayMapper;

    @Override
    public IPage<CardContentDay> findCardContentDays(QueryRequest request, CardContentDay cardContentDay) {
        LambdaQueryWrapper<CardContentDay> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<CardContentDay> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<CardContentDay> findCardContentDays(CardContentDay cardContentDay) {
	    LambdaQueryWrapper<CardContentDay> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createCardContentDay(CardContentDay cardContentDay) {
        this.save(cardContentDay);
    }

    @Override
    @Transactional
    public void updateCardContentDay(CardContentDay cardContentDay) {
        this.saveOrUpdate(cardContentDay);
    }

    @Override
    @Transactional
    public void deleteCardContentDay(CardContentDay cardContentDay) {
        LambdaQueryWrapper<CardContentDay> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}
}
