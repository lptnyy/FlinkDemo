package com.fasterar.smart.server.flink.service;

import com.fasterar.smart.server.flink.entity.ShareStatisticsOpenNumber;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterar.smart.common.QueryRequest;
import com.fasterar.smart.server.flink.entity.input.RelationshipItem;
import com.fasterar.smart.server.flink.entity.input.ShareStatisticsOpenNumberInput;

import java.util.List;

/**
 *  Service接口
 *
 * @author hjx
 * @date 2020-11-02 14:32:44
 */
public interface IShareStatisticsOpenNumberService extends IService<ShareStatisticsOpenNumber> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param shareStatisticsOpenNumber shareStatisticsOpenNumber
     * @return IPage<ShareStatisticsOpenNumber>
     */
    IPage<ShareStatisticsOpenNumber> findShareStatisticsOpenNumbers(QueryRequest request,
        ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     * 查询（所有）
     *
     * @param shareStatisticsOpenNumber shareStatisticsOpenNumber
     * @return List<ShareStatisticsOpenNumber>
     */
    List<ShareStatisticsOpenNumber> findShareStatisticsOpenNumbers(
        ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     * 新增
     *
     * @param shareStatisticsOpenNumber shareStatisticsOpenNumber
     */
    void createShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     * 修改
     *
     * @param shareStatisticsOpenNumber shareStatisticsOpenNumber
     */
    void updateShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     * 删除
     *
     * @param shareStatisticsOpenNumber shareStatisticsOpenNumber
     */
    void deleteShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     * 查询一条数据
     * @param shareStatisticsOpenNumber
     * @return
     */
    ShareStatisticsOpenNumber queryShareStatisticsOpenNumber(
        ShareStatisticsOpenNumber shareStatisticsOpenNumber);

    /**
     *
     * @param
     * @return
     */
    RelationshipItem findShareStatisticsOpenNumbersInput(
        ShareStatisticsOpenNumberInput shareStatisticsOpenNumberInput);
}
