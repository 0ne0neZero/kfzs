package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimGatherDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowClaimGatherDetailMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowClaimGatherDetailRepository extends ServiceImpl<FlowClaimGatherDetailMapper, FlowClaimGatherDetailE> {

    public List<Long> queryIdByRecordIds(List<Long> recordIds) {
        return baseMapper.queryIdByRecordIds(recordIds);
    }
    public List<Long> queryGaterDetailsByFlowClaimRecordId(Object flowClaimRecordId) {
        Long recordId = Long.valueOf(String.valueOf(flowClaimRecordId));
        LambdaQueryWrapper<FlowClaimGatherDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowClaimGatherDetailE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq(FlowClaimGatherDetailE::getFlowClaimRecordId, recordId);
        List<FlowClaimGatherDetailE> flowClaimDetailList = list(queryWrapper);
        return flowClaimDetailList.stream().map(FlowClaimGatherDetailE::getGatherDetailId).collect(Collectors.toList());
    }
}
