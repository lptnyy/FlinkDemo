package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author mango
 * 业绩管理浏览人数
 */
@Data
@TableName("performance_visitors_day")
public class PerformanceVisitorsDay {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 被访问用户id
     */
    @TableField("be_visited_id")
    private Integer beVisitedId;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;
}
