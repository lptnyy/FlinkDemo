package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品内容分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 16:03:56
 */
@Data
@TableName("product_content")
public class ProductContent {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 浏览次数
     */
    @TableField("browses")
    private Integer browses;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品分类
     */
    @TableField("product_type")
    private String productType;

    /**
     * 访客数
     */
    @TableField("visitors")
    private Integer visitors;

    /**
     * 传播系数
     */
    @TableField("spread")
    private Integer spread;

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
     * 分享次数
     */
    @TableField("shares")
    private Integer shares;

    /**
     * 分享人数
     */
    @TableField("share_number")
    private Integer shareNumber;

    /**
     * 收藏人数
     */
    @TableField("collect_number")
    private Integer collectNumber;

    /**
     * 浏览深度
     */
    @TableField("browse_depth")
    private Integer browseDepth;

    /**
     * 0 小程序 1 h5
     */
    @TableField("state")
    private Integer state;

    /**
     * 统计时间
     */
    @TableField("statistics_time")
    private Date statisticsTime;

    /**
     * 企业id
     */
    @TableField("company_id")
    private Integer companyId;

}