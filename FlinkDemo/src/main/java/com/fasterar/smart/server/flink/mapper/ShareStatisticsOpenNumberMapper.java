package com.fasterar.smart.server.flink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterar.smart.server.flink.entity.ShareStatisticsOpenNumber;
import com.fasterar.smart.server.flink.entity.input.ShareStatisticsOpenNumberInput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper
 *
 * @author hjx
 * @date 2020-11-02 14:32:44
 */
public interface ShareStatisticsOpenNumberMapper extends BaseMapper<ShareStatisticsOpenNumber> {

    List<ShareStatisticsOpenNumber> queryShareStatisticsOpenNumber(
        @Param("data") ShareStatisticsOpenNumberInput shareStatisticsOpenNumberInput);

}
