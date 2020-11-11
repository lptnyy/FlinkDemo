package com.fasterar.smart.server.flink.mapper;

import com.fasterar.smart.server.flink.entity.SysUserWechat;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mango
 * 微信id转用户id
 */
public interface SysUserWechatMapper {
    /**
     * 通过微信id 获取后台用户id
     * @param wxId
     * @return
     */
    Map<String, Object> getUserId(@Param("wxId") int wxId);

    /**
     * 通过微信id获取用户信息(微信头像, 名称)
     * @param wxId
     * @return
     */
    SysUserWechat getSysUserWechat(@Param("wxId") int wxId);

    /**
     * 通过userId获取用户wxId
     * @param userId
     * @return
     */
    Long getWxId(@Param("userId") Long userId);

    List<SysUserWechat> getSysUserWechatList(@Param("list") List<Long> list);
}
