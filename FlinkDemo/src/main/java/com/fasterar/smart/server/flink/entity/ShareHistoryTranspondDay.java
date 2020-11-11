package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author mango
 * 分享历史转发人数
 */
@Data
@TableName("share_history_transpond_day")
public class ShareHistoryTranspondDay {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 链接id
     */
    @TableField("link_id")
    private Integer linkId;
    /**
     * 链接类型
     */
    @TableField("link_type")
    private String linkType;

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
