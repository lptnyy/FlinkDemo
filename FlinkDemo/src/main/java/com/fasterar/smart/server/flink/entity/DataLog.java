package com.fasterar.smart.server.flink.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author mango
 */
@Data
public class DataLog {

    private BigInteger id;

    /**
     * 指标id
     */
    private Integer inid;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 企业id
     */
    private Integer cid;

    /**
     * 文件名称
     */
    private String tn;

    /**
     * 画（商，名，官根据tp区分）id
     */
    private Integer tid;

    /**
     * 类型
     */
    private String tp;

    private String ip;

    /**
     * 当前时间戳
     */
    private Date ts;

    /**
     * 被访问用户id（浏览小程序名片夹记员工id 分享buid为空时记sid）
     */
    private Integer buid;

    /**
     * 1 是 2否
     */
    private Integer fav;

    /**
     * 收藏人id
     */
    private Integer cuid;

    /**
     * 浏览总页数
     */
    private Integer ps;

    /**
     * 本次浏览第几页
     */
    private Integer p;

    /**
     * 本次浏览深度
     */
    private Integer dp;

    /**
     * 分享人id
     */
    private Integer sid;

    /**
     * 开始时间
     */
    private String sts;

    /**
     * 结束时间
     */
    private String ets;

    /**
     * 类型 0小程序 1h5
     */
    private Integer st;

    /**
     * 来源
     */
    private Integer ri;

    /**
     * 来源名称(微页画册)
     */
    private String rin;

    /**
     * 组件名称
     */
    private String mn;

    /**
     * 组件url
     */
    private String mu;

    /**
     * 组件url
     */
    private Integer mt;

    /**
     * 组件url
     */
    private Integer inp;

    /**
     * 观看百分比
     */
    private double bfb;

    /**
     * 创建时间
     */
    private Date createTime;
}
