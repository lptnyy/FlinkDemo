package com.fasterar.smart.server.flink.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 *  Entity
 *
 * @author hjx
 * @date 2020-10-29 17:55:07
 */
@Data
@TableName("share_statistics_open_number")
public class ShareStatisticsOpenNumber {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享类型
     */
    @TableField("type")
    private String type;

    /**
     * 分享类型id
     */
    @TableField("type_id")
    private Long typeId;

    /**
     * 分享人id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 被分享人id
     */
    @TableField("buid")
    private Long buid;

    /**
     * 分享次数
     */
    @TableField("share_number")
    private Integer shareNumber;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

    @TableField("user_name")
    private String userName;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField(exist = false)
    private Integer oid;
}