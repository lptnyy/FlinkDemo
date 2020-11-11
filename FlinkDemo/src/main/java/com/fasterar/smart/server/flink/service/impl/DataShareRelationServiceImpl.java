package com.fasterar.smart.server.flink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterar.smart.common.QueryRequest;
import com.fasterar.smart.server.flink.service.IDataShareRelationService;
import com.fasterar.smart.server.flink.entity.DataShareRelation;
import com.fasterar.smart.server.flink.mapper.DataShareRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  Service实现
 *
 * @author hjx
 * @date 2020-10-27 09:53:52
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataShareRelationServiceImpl extends ServiceImpl<DataShareRelationMapper, DataShareRelation> implements IDataShareRelationService {

    @Autowired
    private DataShareRelationMapper dataShareRelationMapper;

    @Override
    public IPage<DataShareRelation> findDataShareRelations(QueryRequest request, DataShareRelation dataShareRelation) {
        LambdaQueryWrapper<DataShareRelation> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<DataShareRelation> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<DataShareRelation> findDataShareRelations(DataShareRelation dataShareRelation) {
	    LambdaQueryWrapper<DataShareRelation> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
        queryWrapper.eq(DataShareRelation::getState, 1);
//        queryWrapper.eq(DataShareRelation::getShareOpenId, dataShareRelation.getShareOpenId());
        queryWrapper.eq(DataShareRelation::getType, dataShareRelation.getType());
//        queryWrapper.eq(DataShareRelation::getTypeId, dataShareRelation.getTypeId());
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDataShareRelation(DataShareRelation dataShareRelation) {
        this.save(dataShareRelation);
    }

    @Override
    @Transactional
    public void updateDataShareRelation(DataShareRelation dataShareRelation) {
        this.saveOrUpdate(dataShareRelation);
    }

    @Override
    @Transactional
    public void deleteDataShareRelation(DataShareRelation dataShareRelation) {
        LambdaQueryWrapper<DataShareRelation> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}

    @Override
    public DataShareRelation queryDataShareRelation(DataShareRelation dataShareRelation) {
        LambdaQueryWrapper<DataShareRelation> wapper = new LambdaQueryWrapper<>();
        wapper.eq(DataShareRelation::getState, 1);
        wapper.eq(DataShareRelation::getTypeId, dataShareRelation.getTypeId());
        wapper.eq(DataShareRelation::getType, dataShareRelation.getType());
        wapper.eq(DataShareRelation::getShareOpenId, dataShareRelation.getShareOpenId());
        DataShareRelation dataShareRelation1 = this.baseMapper.selectOne(wapper);
        return dataShareRelation1;
    }
}
