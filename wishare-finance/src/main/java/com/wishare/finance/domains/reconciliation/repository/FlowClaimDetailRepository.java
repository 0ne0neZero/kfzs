package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowClaimDetailMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流水领用记录
 *
 * @author yancao
 */
@Service
public class FlowClaimDetailRepository extends ServiceImpl<FlowClaimDetailMapper, FlowClaimDetailE> {

    /**
     * 根据认领记录id获取流水明细
     *
     * @param recordIdList 认领记录id
     * @return List
     */
    public List<FlowClaimDetailE> queryByFlowClaimRecordId(List<Long> recordIdList) {
        LambdaQueryWrapper<FlowClaimDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowClaimDetailE::getState, FlowClaimDetailStatusEnum.正常.getCode());
        queryWrapper.eq(FlowClaimDetailE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        queryWrapper.in(FlowClaimDetailE::getFlowClaimRecordId, recordIdList);
        return list(queryWrapper);
    }

    /**
     * 更新认领明细状态
     *
     * @param flowClaimIdList 流水认领记录id
     * @param state 状态
     */
    public void updateStateByFlowClaimIdList(List<Long> flowClaimIdList, int state) {
        LambdaUpdateWrapper<FlowClaimDetailE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(FlowClaimDetailE::getFlowClaimRecordId,flowClaimIdList);
        updateWrapper.set(FlowClaimDetailE::getState, state);
        updateWrapper.set(FlowClaimDetailE::getDeleted, DataDeletedEnum.DELETED.getCode());
        update(updateWrapper);
    }

    /**
     * 根据流水认领记录ID获取流水认领详情关联的业务数据ID（invoice_receipt ID 或 gather_bill/pay_bill ID）
     * @param flowClaimRecordId
     * @return
     */
    public List<Long> queryInvoiceIdsByFlowClaimRecordId(Object flowClaimRecordId) {
        Long recordId = Long.valueOf(String.valueOf(flowClaimRecordId));
        LambdaQueryWrapper<FlowClaimDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowClaimDetailE::getState, FlowClaimDetailStatusEnum.正常.getCode());
        queryWrapper.eq(FlowClaimDetailE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq(FlowClaimDetailE::getFlowClaimRecordId, recordId);
        List<FlowClaimDetailE> flowClaimDetailList = list(queryWrapper);
        return flowClaimDetailList.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
    }
}
