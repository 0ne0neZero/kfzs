package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.repository.mapper.ReconciliationDetailMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对账单明细资源库
 *
 * @author dxclay
 * @since 2022-10-13
 */
@Service
public class ReconciliationDetailRepository extends ServiceImpl<ReconciliationDetailMapper, ReconciliationDetailE> {

    /**
     * 根据账单id查询对象明细
     * @param billId
     * @param reconciliationId
     * @return
     */
    public ReconciliationDetailE getByBillId(Long billId, Long reconciliationId){
        return getOne(new LambdaQueryWrapper<ReconciliationDetailE>()
                .eq(ReconciliationDetailE::getBillId, billId)
                .eq(ReconciliationDetailE::getReconciliationId, reconciliationId), false);
    }

    public List<ReconciliationDetailE>  getByReconciliation(Long reconciliationId) {
        return list(new LambdaQueryWrapper<ReconciliationDetailE>()
                .eq(ReconciliationDetailE::getReconciliationId, reconciliationId));
    }

    public ReconciliationClearStatisticsV reconciliationClearDetailStatistics(QueryWrapper<ReconciliationDetailE> queryWrapper){

        return baseMapper.reconciliationClearDetailStatistics(queryWrapper);
    }


    public ReconciliationStatisticsV reconciliationDetailStatistics(QueryWrapper<ReconciliationDetailE> queryWrapper){
        return baseMapper.reconciliationDetailStatistics(queryWrapper);
    }

    public List<ReconciliationDetailE> getByBillIds(List<Long> billIds){
        return baseMapper.getByBillIds(billIds);
    }

    public List<ReconciliationDetailE> getByBillIdsAndReconciliationId(List<Long> billIds, Long reconciliationId ){
        return baseMapper.getByBillIdsAndReconciliationId(billIds, reconciliationId);
    }
    public List<ReconciliationDetailE> getByReconciliationId(Long reconciliationId){
        return baseMapper.getByReconciliationId(reconciliationId);
    }




    public Page<ReconciliationDetailE> reconciliationDetailPageYuan(Page<T> of, QueryWrapper<ReconciliationDetailE> queryModel) {
        return baseMapper.reconciliationDetailPageYuan(of,queryModel);
    }

    public Page<ReconciliationDetailE> reconciliationDetailPageNoYuan(Page<Object> of, QueryWrapper<ReconciliationDetailE> queryModel) {
        return baseMapper.reconciliationDetailPageNoYuan(of,queryModel);
    }

    public List<ReconciliationDetailE> getByBillIdsAndResult(List<Long> billIds) {
        return baseMapper.getByBillIdsAndResult(billIds);
    }
}
