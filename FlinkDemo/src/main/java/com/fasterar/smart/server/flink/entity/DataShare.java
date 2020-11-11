package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *  Entity
 *
 * @author hjx
 * @date 2020-11-09 14:32:57
 */
@Data
@TableName("data_share")
public class DataShare {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 类型id
     */
    @TableField("type_id")
    private Long typeId;

    /**
     * 微信id
     */
    @TableField("wx_id")
    private Long wxId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}