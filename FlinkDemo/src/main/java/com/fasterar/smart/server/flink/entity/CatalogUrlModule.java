package com.fasterar.smart.server.flink.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 画册链接组件分析表 Entity
 *
 * @author hjx
 * @date 2020-10-28 10:16:16
 */
@Data
@TableName("catalog_url_module")
public class CatalogUrlModule {

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
     * 链接标题
     */
    @TableField("url_name")
    private String urlName;

    /**
     * 所在页码
     */
    @TableField("place_page")
    private Integer placePage;

    /**
     * 链接类型
     */
    @TableField("url_type")
    private Integer urlType;

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