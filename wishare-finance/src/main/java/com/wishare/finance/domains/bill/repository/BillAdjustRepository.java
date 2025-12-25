package com.wishare.finance.domains.bill.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustWayEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillAdjustDto;
import com.wishare.finance.domains.bill.dto.ChargeDeductionDetailDto;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.repository.mapper.BillAdjustMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 账单调整基础资源库(该资源库为基础数据资源库，只能被查询接口/聚合资源库使用，不允许直接使用)
 * @Author dxclay
 * @Date 2022-08-23
 * @Version 1.0
 */
@Service
public class BillAdjustRepository extends ServiceImpl<BillAdjustMapper, BillAdjustE> {

    /**
     * 根据审核记录获取调整信息
     * @param approveId
     * @return
     */
    public BillAdjustE getByApproveId(Long approveId){
        return getOne(new LambdaQueryWrapper<BillAdjustE>().eq(BillAdjustE::getBillApproveId, approveId));
    }

    /**
     * 根据账单id获取调整记录
     *
     * @param billId
     * @return
     */
    public List<BillAdjustE> listByBillId(Long billId) {
        QueryWrapper<BillAdjustE> wrapper = new QueryWrapper<>();
        wrapper.eq("bill_id", billId);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据账单id获取审核通过的减免记录
     *
     * @param billId
     * @return
     */
    public List<BillAdjustE> listByApproveBillId(Long billId) {
        LambdaQueryWrapper<BillAdjustE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillAdjustE::getBillId, billId);
        wrapper.eq(BillAdjustE::getState, 2);
        wrapper.eq(BillAdjustE::getAdjustType, 1);
        wrapper.eq(BillAdjustE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据账单类型查找审核通过的已调整账单
     * @param billTypeEnum
     * @return
     */
    public List<BillAdjustE> listByBillType(BillTypeEnum billTypeEnum) {
        LambdaQueryWrapper<BillAdjustE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillAdjustE::getBillType, billTypeEnum.getCode());
        wrapper.eq(BillAdjustE::getState, 2);
        wrapper.eq(BillAdjustE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        return baseMapper.selectList(wrapper);
    }

    /**
     * 批量更新已推凭的调整
     * @param concatIds
     * @param state
     */
    public void batchUpdateInferenceSate(List<Long> concatIds, int state) {
        baseMapper.batchUpdateInferenceSate(concatIds, state);
    }


    /**
     * "RemoteRefundIdentityInfo_"
     * @param page 分页参数
     * @param wrapper wrapper
     * @return {@link PageV}<>{@link BillAdjustV}</>
     */
    public Page<BillAdjustDto> queryPageBySearch(Page<SearchF<?>> page,QueryWrapper<?> wrapper) {
        return baseMapper.queryPageBySearch(page,wrapper);
    }


    public IPage<ChargeDeductionDetailDto> getDeductionDetail(Page<Object> page, QueryWrapper<?> queryWrapper, String receivableBillName) {
        return baseMapper.getDeductionDetail(page, queryWrapper, receivableBillName);
    }

    //获取减免审核中的账单数据
    public List<BillAdjustE> listByReductionApprove(List<Long> billIds) {
        LambdaQueryWrapper<BillAdjustE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillAdjustE::getState, 0);
        wrapper.eq(BillAdjustE::getAdjustType, 1);
        wrapper.eq(BillAdjustE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        wrapper.in(BillAdjustE::getBillId,billIds);
        return baseMapper.selectList(wrapper);
    }

    public List<BillAdjustE> queryList(QueryWrapper<?> queryWrapper) {

        return baseMapper.queryList(queryWrapper);
    }

    public void updateBillInferenceState(List<Long> idList, Integer inferenceState)   {
        baseMapper.updateBillInferenceState(idList,inferenceState );
    }
    public void updateBillInferenceStateByGmtModify(List<Long> idList, Integer inferenceState, LocalDateTime gmtCreate)   {
        baseMapper.updateBillInferenceStateByGmtModify(idList,inferenceState , gmtCreate);
    }
    public List<Long> listBySeparateBillNo(Set<String> separateBillNoSet) {
        if (CollUtil.isEmpty(separateBillNoSet)){
            return Collections.emptyList();
        }
        List<BillAdjustE> list = baseMapper.selectList(Wrappers.<BillAdjustE>lambdaQuery()
                .select(BillAdjustE::getBillId)
                .eq(BillAdjustE::getAdjustWay, BillAdjustWayEnum.PAYMENT_ON_BEHALF.getCode())
                .in(BillAdjustE::getSeparateBillNo,separateBillNoSet));

        return list.stream().map(BillAdjustE::getBillId).collect(Collectors.toList());
    }
}
