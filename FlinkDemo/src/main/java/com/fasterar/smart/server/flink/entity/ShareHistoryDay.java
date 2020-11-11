package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author mango
 */
@Data
@TableName("share_history_day")
public class ShareHistoryDay {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "link_id")
    private Integer linkId;

    @TableField(value = "link_title")
    private String linkTitle;

    @TableField(value = "link_type")
    private String linkType;

    @TableField(value = "transpond_number")
    private int transpondNumber;

    @TableField(value = "transponds")
    private int transponds;

    @TableField(value = "browse_number")
    private int browseNumber;

    @TableField(value = "browses")
    private int browses;

    @TableField(value = "statistics_time")
    private String statisticsTime;

    @TableField(value = "share_time")
    private Date shareTime;
}
