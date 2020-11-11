package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品留存分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:24
 */
@Data
@TableName("product_keep_content")
public class ProductKeepContent {

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
     * 浏览次数
     */
    @TableField("browses")
    private Integer browses;

    /**
     * 分享次数
     */
    @TableField("shares")
    private Integer shares;

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