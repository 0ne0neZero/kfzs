package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.reconciliation.vo.FlowClaimRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowClaimRecordMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水领用记录
 * @author yancao
 */
@Service
public class FlowClaimRecordRepository extends ServiceImpl<FlowClaimRecordMapper, FlowClaimRecordE> {

    /**
     * 分页查询领用记录
     *
     * @param page page
     * @param queryWrapper queryWrapper
     * @return IPage
     */
    public IPage<FlowClaimRecordE> queryRecordPage(Page<?> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.queryRecordPage(page, queryWrapper);
    }

    /**
     * 根据流水ID获取流水认领记录ID
     * @param flowId
     * @return
     */
    public Long queryIdByFlowId(Long flowId) {
        return baseMapper.queryIdByFlowId(flowId);
    }

    public List<Long> queryIdByFlowIds(List<Long> flowIds) {
        return baseMapper.queryIdByFlowIds(flowIds);
    }

    public IPage<FlowRecordPageV> getPageRecord(Page<?> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.getPageRecord(page, queryWrapper);
    }
    public FlowRecordStatisticsV getFlowRecordStatistics (QueryWrapper<?> queryWrapper) {
        return baseMapper.getFlowRecordStatistics(queryWrapper);
    }

    public List<Long> queryIdByRecordIds(List<Long> recordIds) {
        return baseMapper.queryIdByRecordIds(recordIds);
    }
}
