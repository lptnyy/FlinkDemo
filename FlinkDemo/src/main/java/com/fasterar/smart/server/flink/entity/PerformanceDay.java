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
@TableName("performance_day")
public class PerformanceDay {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 员工姓名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 企业id
     */
    @TableField("company_id")
    private Integer companyId;

    /**
     * 累计分享次数
     */
    @TableField("shares")
    private int shares;

    /**
     * 累计访问次数
     */
    @TableField("browses")
    private int browses;

    /**
     * 累计被转发次数
     */
    @TableField("transponds")
    private int transponds;

    /**
     * 贡献指数
     */
    @TableField("contributions")
    private int contributions;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;

    /**
     * 累计访客数
     */
    @TableField(exist = false)
    private int visitors;
}
