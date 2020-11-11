package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品视频组件分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:34
 */
@Data
@TableName("product_video_module")
public class ProductVideoModule {

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
     * 视频标题
     */
    @TableField("video_name")
    private String videoName;

    /**
     * 播放次数
     */
    @TableField("plays")
    private Integer plays;

    /**
     * 点击次数
     */
    @TableField("clicks")
    private Integer clicks;

    /**
     * 完播数
     */
    @TableField("after_play")
    private Integer afterPlay;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

}