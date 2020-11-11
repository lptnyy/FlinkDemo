package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 画册内容分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 16:03:08
 */
@Data
@TableName("catalog_content_day")
public class CatalogContentDay {

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
     * 画册名称
     */
    @TableField("catalog_name")
    private String catalogName;

    /**
     * 画册分类
     */
    @TableField("catalog_type")
    private String catalogType;

    /**
     * 访客数
     */
    @TableField("visitors")
    private Integer visitors;

    /**
     * 浏览次数
     */
    @TableField("browses")
    private Integer browses;

    /**
     * 分享次数
     */
    @TableField("shares")
    private Integer shares;

    /**
     * 传播系数
     */
    @TableField("spread")
    private Integer spread;

    /**
     * 浏览深度
     */
    @TableField("browse_depth")
    private Double browseDepth;

    /**
     * 分享人数
     */
    @TableField("share_number")
    private Integer shareNumber;

    /**
     * 分享访问次数
     */
    @TableField("share_visitors")
    private Integer shareVisitors;

    /**
     * 分享访问人数
     */
    @TableField("share_visitor_number")
    private Integer shareVisitorNumber;

    /**
     * 0 小程序 1 h5
     */
    @TableField("state")
    private Integer state;

    /**
     * 收藏人数
     */
    @TableField("collect_number")
    private Integer collectNumber;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private String statisticsTime;

    /**
     * 企业id
     */
    @TableField("company_id")
    private Integer companyId;

}