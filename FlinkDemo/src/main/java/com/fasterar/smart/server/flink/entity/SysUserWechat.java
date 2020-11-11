package com.fasterar.smart.server.flink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 微信用户 Entity
 *
 * @author Jason
 * @date 2019-12-23 09:25:14
 */
@Data
@TableName("sys_user_wechat")
public class SysUserWechat {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField("open_id")
    private String openId;

    /**
     * 
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 系统随机生成的用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 头像url
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 性别(1:男 2:女)
     */
    @TableField("gender")
    private String gender;

    /**
     * 状态 0锁定 1有效
     */
    @TableField("status")
    private String status;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 设备
     */
    @TableField("device")
    private String device;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 地区
     */
    @TableField("area")
    private String area;

    /**
     * 最后一次登录时间（访问时间）
     */
    @TableField("last_login_time")
    private Date lastLoginTime;

    /**
     * 
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField("update_time")
    private Date updateTime;
}