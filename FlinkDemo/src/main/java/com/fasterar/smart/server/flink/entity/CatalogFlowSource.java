package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 画册流量来源表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:14:33
 */
@Data
@TableName("catalog_flow_source")
public class CatalogFlowSource {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 画册id
     */
    @TableField("catalog_id")
    private Integer catalogId;

    /**
     * 来源类型
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     * 访客数
     */
    @TableField("visitors")
    private Integer visitors;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 0 小程序 1 h5
     */
    @TableField("state")
    private Integer state;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

}