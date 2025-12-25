package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.PayDetail;
import com.wishare.finance.domains.bill.repository.mapper.PayDetailMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 付款明细资源库
 *
 * @author yancao
 * @since 2022-12-19
 */
@Service
public class PayDetailRepository extends ServiceImpl<PayDetailMapper, PayDetail> {

    /**
     * 根据付款单id获取付款单明细
     *
     * @param payBillId 付款单id
     * @return List
     */
    public List<PayDetail> queryByPayBillId(Long payBillId) {
        LambdaQueryWrapper<PayDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayDetail::getPayBillId, payBillId);
        return list(queryWrapper);
    }

    /**
     * 根据付款单id获取付款单明细
     *
     * @param payBillIdList 付款单id
     * @return List
     */
    public List<PayDetail> queryByPayBillIdList(List<Long> payBillIdList) {
        LambdaQueryWrapper<PayDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(payBillIdList), PayDetail::getPayBillId, payBillIdList);
        return list(queryWrapper);
    }

    /**
     * 修改账单详情的推凭状态
     * @param concatIds
     * @param state
     */
    public void batchUpdateDetailInferenceSate(List<Long> concatIds, int state) {
        baseMapper.batchUpdateDetailInferenceSate(concatIds, state);
    }

    /**
     * 根据id和推凭状态查询账单
     * @param billId
     * @param inferenceState
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIdAndInfer(Long billId, int inferenceState, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq("b.id", billId);
        queryWrapper.eq("bd.inference_state", inferenceState);
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.eq("bd.deleted", 0);
        queryWrapper.eq("b.sup_cp_unit_id", supCpUnitId);
        queryWrapper.eq("bd.sup_cp_unit_id", supCpUnitId);
        return baseMapper.listInferenceInfoByIdAndInfer(queryWrapper);
    }

    /**
     * 根据id和推凭状态查询账单
     * @param billIds
     * @param inferenceState
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIdsAndInfer(List<Long> billIds, int inferenceState, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper();
        queryWrapper.in("b.id", billIds);
        queryWrapper.eq("bd.inference_state", inferenceState);
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.eq("bd.deleted", 0);
        queryWrapper.eq("b.sup_cp_unit_id", supCpUnitId);
        queryWrapper.eq("bd.sup_cp_unit_id", supCpUnitId);
        return baseMapper.listInferenceInfoByIdAndInfer(queryWrapper);
    }
}
