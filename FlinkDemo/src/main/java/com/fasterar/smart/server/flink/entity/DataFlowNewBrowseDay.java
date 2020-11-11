package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author mango
 * 浏览概括新访客人数
 */
@Data
@TableName("data_flow_new_browse_day")
public class DataFlowNewBrowseDay {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 企业id
     */
    @TableField("company_id")
    private Integer companyId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 创建时间
     */
    @TableField("statisticsTime")
    private String statisticsTime;
}
