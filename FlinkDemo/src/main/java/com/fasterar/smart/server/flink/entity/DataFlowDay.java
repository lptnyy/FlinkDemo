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
@TableName("data_flow_day")
public class DataFlowDay {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 访客数
     */
    @TableField("visitors")
    private int visitors;

    /**
     * 新访客数
     */
    @TableField("new_visitors")
    private int newVisitors;

    /**
     * 浏览次数
     */
    @TableField("browses")
    private int browses;

    /**
     * 停留时长
     */
    @TableField("stay_time")
    private int  stayTime;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 0 小程序 1 h5
     */
    @TableField("state")
    private int state;

    /**
     * 收藏人数
     */
    @TableField("collect_number")
    private int collectNumber;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;
}
