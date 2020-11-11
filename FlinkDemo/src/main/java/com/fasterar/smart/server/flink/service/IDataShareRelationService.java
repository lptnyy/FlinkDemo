package com.fasterar.smart.server.flink.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterar.smart.common.QueryRequest;
import com.fasterar.smart.server.flink.entity.DataShareRelation;

import java.util.List;

/**
 *  Service接口
 *
 * @author hjx
 * @date 2020-10-27 09:53:52
 */
public interface IDataShareRelationService extends IService<DataShareRelation> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param dataShareRelation dataShareRelation
     * @return IPage<DataShareRelation>
     */
    IPage<DataShareRelation> findDataShareRelations(QueryRequest request,
        DataShareRelation dataShareRelation);

    /**
     * 查询（所有）
     *
     * @param dataShareRelation dataShareRelation
     * @return List<DataShareRelation>
     */
    List<DataShareRelation> findDataShareRelations(DataShareRelation dataShareRelation);

    /**
     * 新增
     *
     * @param dataShareRelation dataShareRelation
     */
    void createDataShareRelation(DataShareRelation dataShareRelation);

    /**
     * 修改
     *
     * @param dataShareRelation dataShareRelation
     */
    void updateDataShareRelation(DataShareRelation dataShareRelation);

    /**
     * 删除
     *
     * @param dataShareRelation dataShareRelation
     */
    void deleteDataShareRelation(DataShareRelation dataShareRelation);

    DataShareRelation queryDataShareRelation(DataShareRelation dataShareRelation);

}
