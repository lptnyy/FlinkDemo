package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author mango
 */
@Data
@TableName("catalog_video_module_day")
public class CatalogVideoModuleDay {

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
     * 视频标题
     */
    @TableField("video_name")
    private String videoName;

    /**
     * 视频标题
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 所在页码
     */
    @TableField("place_page")
    private int placePage;

    /**
     * 播放人数
     */
    @TableField("play_number")
    private int playNumber;

    /**
     * 播放次数
     */
    @TableField("plays")
    private int plays;

    /**
     * 点击人数
     */
    @TableField("click_number")
    private int clickNumber;

    /**
     * 点击次数
     */
    @TableField("clicks")
    private int clicks;

    /**
     * 完播数
     */
    @TableField("after_play")
    private int afterPlay;

    /**
     * 进度比
     */
    @TableField("progress_than")
    private double progressThan;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;

}