package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品链接组件分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:30
 */
@Data
@TableName("product_url_module")
public class ProductUrlModule {

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
     * 链接标题
     */
    @TableField("url_name")
    private String urlName;

    /**
     * 链接类型
     */
    @TableField("url_type")
    private Integer urlType;

    /**
     * 点击次数
     */
    @TableField("clicks")
    private Integer clicks;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

}