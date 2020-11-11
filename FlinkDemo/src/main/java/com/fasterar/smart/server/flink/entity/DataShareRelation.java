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
 * @date 2020-10-27 09:53:52
 */
@Data
@TableName("data_share_relation")
public class DataShareRelation {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享id
     */
    @TableField("share_id")
    private Long shareId;

    /**
     * 分享人
     */
    @TableField("share_wx_id")
    private Long shareWxId;

    /**
     * 打开人(关联人)
     */
    @TableField("share_open_id")
    private Long shareOpenId;

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
     * 是否参与计算(免计算0 计算1)
     */
    @TableField("state")
    private Integer state;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}