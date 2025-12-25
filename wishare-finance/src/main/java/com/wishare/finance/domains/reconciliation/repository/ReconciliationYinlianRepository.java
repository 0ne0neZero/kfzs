package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimStatisticsV;
import com.wishare.finance.domains.reconciliation.dto.ChannelFlowClaimRecordDto;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.repository.mapper.ReconciliationYinlianMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Service
public class ReconciliationYinlianRepository extends ServiceImpl<ReconciliationYinlianMapper, ReconciliationYinlianE> {
    public ReconciliationYinlianE getReconciliationYinlianEByFileName(String fileName){
        return baseMapper.getReconciliationYinlianEByFileName(fileName);
    }

    public IPage<ChannelFlowClaimRecordDto> pageRecord(String shareTableName, Page<?> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.pageRecord(page, queryWrapper, shareTableName);
    }


    public IPage<ChannelFlowClaimRecordDto> pageRecordForYY(String shareTableName, Page<?> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.pageRecordForYY(page, queryWrapper, shareTableName);
    }

    public List<ReconciliationYinlianE> getReconciliationYinlianBySeqId(List<String> seqList){
        return baseMapper.getReconciliationYinlianBySeqId(seqList);
    }

    public List<ReconciliationYinlianE> getReconciliationYinlianByRefNo(List<String> seqList){
        return baseMapper.getReconciliationYinlianByRefNo(seqList);
    }

    public ChannelFlowClaimStatisticsV getStatistics(String shareTableName,QueryWrapper<?> queryWrapper){
        return baseMapper.getStatistics(shareTableName,queryWrapper);
    }


    public ChannelFlowClaimStatisticsV getYYStatistics(String shareTableName,QueryWrapper<?> queryWrapper){
        return baseMapper.getYYStatistics(shareTableName,queryWrapper);
    }

    public List<ChannelFlowClaimRecordDto> pageRecordMap(Set<String> collect) {
        return baseMapper.pageRecordMap(collect);
    }

    public List<ChannelFlowClaimRecordDto> pageRecordMapYY(Set<String> collect) {
        return baseMapper.pageRecordMapYY(collect);
    }
}
