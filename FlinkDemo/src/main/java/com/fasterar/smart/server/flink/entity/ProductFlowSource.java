package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品流量来源表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:23
 */
@Data
@TableName("product_flow_source")
public class ProductFlowSource {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    @TableField("product_id")
    private Integer productId;

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