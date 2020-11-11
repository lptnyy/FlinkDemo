package com.fasterar.smart.server.flink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterar.smart.common.QueryRequest;
import com.fasterar.smart.server.flink.entity.DataShare;
import com.fasterar.smart.server.flink.entity.DataShareRelation;
import com.fasterar.smart.server.flink.entity.ShareStatisticsOpenNumber;
import com.fasterar.smart.server.flink.entity.SysUserWechat;
import com.fasterar.smart.server.flink.entity.input.RelationshipItem;
import com.fasterar.smart.server.flink.entity.input.RelationshipLink;
import com.fasterar.smart.server.flink.entity.input.RelationshipNode;
import com.fasterar.smart.server.flink.entity.input.ShareStatisticsOpenNumberInput;
import com.fasterar.smart.server.flink.mapper.DataShareMapper;
import com.fasterar.smart.server.flink.mapper.DataShareRelationMapper;
import com.fasterar.smart.server.flink.mapper.ShareStatisticsOpenNumberMapper;
import com.fasterar.smart.server.flink.mapper.SysUserWechatMapper;
import com.fasterar.smart.server.flink.service.IDataShareRelationService;
import com.fasterar.smart.server.flink.service.IShareStatisticsOpenNumberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service实现
 *
 * @author hjx
 * @date 2020-11-02 14:32:44
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ShareStatisticsOpenNumberServiceImpl extends ServiceImpl<ShareStatisticsOpenNumberMapper, ShareStatisticsOpenNumber> implements IShareStatisticsOpenNumberService {

    @Autowired
    private ShareStatisticsOpenNumberMapper shareStatisticsOpenNumberMapper;

    @Autowired
    private IDataShareRelationService dataShareRelationService;

    @Autowired
    private DataShareRelationMapper dataShareRelationMapper;

    @Autowired
    private SysUserWechatMapper sysUserWechatMapper;

    @Autowired
    private DataShareMapper dataShareMapper;


    @Override
    public IPage<ShareStatisticsOpenNumber> findShareStatisticsOpenNumbers(QueryRequest request, ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        LambdaQueryWrapper<ShareStatisticsOpenNumber> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<ShareStatisticsOpenNumber> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ShareStatisticsOpenNumber> findShareStatisticsOpenNumbers(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        LambdaQueryWrapper<ShareStatisticsOpenNumber> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        this.save(shareStatisticsOpenNumber);
    }

    @Override
    @Transactional
    public void updateShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        this.saveOrUpdate(shareStatisticsOpenNumber);
    }

    @Override
    @Transactional
    public void deleteShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        LambdaQueryWrapper<ShareStatisticsOpenNumber> wapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wapper);
    }


    @Override
    public RelationshipItem findShareStatisticsOpenNumbersInput(ShareStatisticsOpenNumberInput shareStatisticsOpenNumberInput) {
        LambdaQueryWrapper<ShareStatisticsOpenNumber> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DataShare> dq = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DataShareRelation> dsr = new LambdaQueryWrapper<>();

        Long wxId = sysUserWechatMapper.getWxId(shareStatisticsOpenNumberInput.getUid());

        dq.eq(DataShare::getWxId, wxId);
        dq.eq(DataShare::getTypeId, shareStatisticsOpenNumberInput.getTypeId());
        dq.eq(DataShare::getType, shareStatisticsOpenNumberInput.getType());
        DataShare dataShare = dataShareMapper.selectOne(dq);

        dsr.eq(DataShareRelation::getState, 1);
        dsr.eq(DataShareRelation::getShareId, dataShare.getId());
        List<DataShareRelation> dataShareRelations = dataShareRelationMapper.selectList(dsr);


        RelationshipItem relationshipItem = new RelationshipItem();
        relationshipItem.setRootId(String.valueOf(wxId));

        List<Long> l = new ArrayList<>();
        l.add(wxId);
        dataShareRelations.stream().forEach(d -> {
            l.add(d.getShareOpenId());
        });

        List<SysUserWechat> sysUserWechatList = sysUserWechatMapper.getSysUserWechatList(l);

        queryWrapper.eq(ShareStatisticsOpenNumber::getType, shareStatisticsOpenNumberInput.getType());
        queryWrapper.eq(ShareStatisticsOpenNumber::getTypeId, shareStatisticsOpenNumberInput.getTypeId());
        queryWrapper.in(ShareStatisticsOpenNumber::getUid, l);
        List<ShareStatisticsOpenNumber> shareStatisticsOpenNumbers = this.baseMapper.selectList(queryWrapper);


        List<RelationshipNode> nodeList = new ArrayList<>();
        List<RelationshipLink> relationshipLinks = new ArrayList<>();

        sysUserWechatList.stream().forEach(d -> {
            RelationshipNode relationshipNode = new RelationshipNode();
            relationshipNode.setId(String.valueOf(d.getId()));
            relationshipNode.setText(d.getNickName());
            relationshipNode.setUrl(d.getAvatarUrl());
            shareStatisticsOpenNumbers.stream().forEach(d1 -> {
                if (d.getId().longValue() == d1.getUid().longValue()) {
                    relationshipNode.setShareNumber(String.valueOf(d1.getShareNumber()));
                }
            });
            nodeList.add(relationshipNode);
        });
        nodeList.stream().forEach(d -> {
            if (StringUtils.isEmpty(d.getShareNumber())) {
                d.setShareNumber("0");
            }
        });
        dataShareRelations.stream().forEach(d -> {
            RelationshipLink relationshipLink = new RelationshipLink();
            relationshipLink.setFrom(String.valueOf(d.getShareWxId()));
            relationshipLink.setTo(String.valueOf(d.getShareOpenId()));
            relationshipLinks.add(relationshipLink);
        });

        relationshipItem.setLinks(relationshipLinks);
        relationshipItem.setNodes(nodeList);
        return relationshipItem;

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShareStatisticsOpenNumber queryShareStatisticsOpenNumber(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        System.out.println("--->" + shareStatisticsOpenNumber);
        LambdaQueryWrapper<ShareStatisticsOpenNumber> wapper = new LambdaQueryWrapper<>();
        wapper.eq(ShareStatisticsOpenNumber::getType, shareStatisticsOpenNumber.getType());
        wapper.eq(ShareStatisticsOpenNumber::getTypeId, shareStatisticsOpenNumber.getTypeId());
        wapper.eq(ShareStatisticsOpenNumber::getUid, shareStatisticsOpenNumber.getUid());
        ShareStatisticsOpenNumber shareStatisticsOpenNumber1 = this.baseMapper.selectOne(wapper);
        if (shareStatisticsOpenNumber1 == null) {
            SysUserWechat sysUserWechat = sysUserWechatMapper.getSysUserWechat(shareStatisticsOpenNumber.getUid().intValue());
//            shareStatisticsOpenNumber.setUserName(sysUserWechat.getNickName());
//            shareStatisticsOpenNumber.setAvatarUrl(sysUserWechat.getAvatarUrl());
            shareStatisticsOpenNumber.setStatisticsTime(new Date());
            createShareStatisticsOpenNumber(shareStatisticsOpenNumber);
            updateDataShareRelation(shareStatisticsOpenNumber);
        } else {
            shareStatisticsOpenNumber.setStatisticsTime(new Date());
            shareStatisticsOpenNumber1.setShareNumber(shareStatisticsOpenNumber1.getShareNumber() + shareStatisticsOpenNumber.getShareNumber());
            updateById(shareStatisticsOpenNumber1);
            updateDataShareRelation(shareStatisticsOpenNumber);
        }
        return shareStatisticsOpenNumber;
    }


    private void updateDataShareRelation(ShareStatisticsOpenNumber shareStatisticsOpenNumber) {
        LambdaQueryWrapper<DataShareRelation> dataShareRelationwapper = new LambdaQueryWrapper<>();
        dataShareRelationwapper.eq(DataShareRelation::getState, 1);
        dataShareRelationwapper.eq(DataShareRelation::getTypeId, shareStatisticsOpenNumber.getTypeId());
        dataShareRelationwapper.eq(DataShareRelation::getType, shareStatisticsOpenNumber.getType());
        dataShareRelationwapper.eq(DataShareRelation::getShareOpenId, shareStatisticsOpenNumber.getUid());
        DataShareRelation dataShareRelation = dataShareRelationMapper.selectOne(dataShareRelationwapper);
        if (dataShareRelation != null) {
            LambdaQueryWrapper<ShareStatisticsOpenNumber> wapper = new LambdaQueryWrapper<>();
            wapper.eq(ShareStatisticsOpenNumber::getType, dataShareRelation.getType());
            wapper.eq(ShareStatisticsOpenNumber::getTypeId, dataShareRelation.getTypeId());
            wapper.eq(ShareStatisticsOpenNumber::getUid, dataShareRelation.getShareWxId());
            ShareStatisticsOpenNumber shareStatisticsOpenNumber1 = this.baseMapper.selectOne(wapper);
            shareStatisticsOpenNumber1.setShareNumber(shareStatisticsOpenNumber1.getShareNumber() + shareStatisticsOpenNumber.getShareNumber());
            shareStatisticsOpenNumber1.setStatisticsTime(new Date());
            updateById(shareStatisticsOpenNumber1);
            shareStatisticsOpenNumber1.setShareNumber(shareStatisticsOpenNumber.getShareNumber());
            updateDataShareRelation(shareStatisticsOpenNumber1);
        }
    }
}
