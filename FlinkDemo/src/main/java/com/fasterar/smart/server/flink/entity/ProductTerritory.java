package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品地域表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:29
 */
@Data
@TableName("product_territory")
public class ProductTerritory {

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
     * 访客数
     */
    @TableField("visitors")
    private Integer visitors;

    /**
     * 市
     */
    @TableField("city")
    private String city;

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