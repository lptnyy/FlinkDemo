package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 画册VR组件分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:19
 */
@Data
@TableName("catalog_vr_module")
public class CatalogVrModule {

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
     * 所在页码
     */
    @TableField("place_page")
    private Integer placePage;

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
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

}