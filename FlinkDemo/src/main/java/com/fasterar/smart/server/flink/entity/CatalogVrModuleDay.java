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
@TableName("catalog_vr_module_day")
public class CatalogVrModuleDay {

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
     * VR标题
     */
    @TableField("vr_name")
    private String vrName;

    /**
     * VR标题
     */
    @TableField("vr_url")
    private String vrUrl;

    /**
     * 所在页码
     */
    @TableField("place_page")
    private int placePage;

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
     * 进度比
     */
    @TableField("progress_than")
    private int progressThan;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;

}