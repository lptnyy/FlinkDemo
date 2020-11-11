package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品图片组件分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:25
 */
@Data
@TableName("product_photo_module")
public class ProductPhotoModule {

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
     * 图片缩略图
     */
    @TableField("photo_url")
    private String photoUrl;

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