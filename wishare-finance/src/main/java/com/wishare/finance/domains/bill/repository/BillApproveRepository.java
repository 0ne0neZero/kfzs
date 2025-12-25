package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.BillApplyInfoF;
import com.wishare.finance.domains.bill.aggregate.BillApproveA;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.repository.mapper.BillApproveMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 账单审核记录基础资源库(该资源库为基础数据资源库，只能被查询接口/聚合资源库使用，不允许直接使用)
 *
 * @Author dxclay
 * @Date 2022-08-23
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillApproveRepository extends ServiceImpl<BillApproveMapper, BillApproveE> {


    /**
     * 审核更新
     * @param approvingA
     * @return
     */
    public <B extends Bill> boolean approve(BillApproveA<B> approvingA){
        //billRepository.updateById(approvingA.getBill());
        return this.update(
                approvingA,
                new LambdaUpdateWrapper<BillApproveE>()
                        .eq(BillApproveE::getSupCpUnitId, approvingA.getSupCpUnitId())
                        .in(BillApproveE::getId, approvingA.getId())
        );
    }

    /**
     * 批量审核
     * @param approvingAS
     * @return
     */
    public <B extends Bill> boolean approveBatch(List<BillApproveA<B>> approvingAS){
        //批量新增审核记录
        //billRepository.updateBatchById(approvingAS.stream().map(BillApproveA::getBill).collect(Collectors.toList()));
        return this.saveBatch(Global.mapperFacade.mapAsList(approvingAS, BillApproveE.class));
    }

    /**
     * 查询审核中的账单
     * @param billId
     * @return
     */
    public BillApproveE getApprovingAByBillId(Long billId, String supCpUnitId){
        return getOne(new QueryWrapper<BillApproveE>().eq("bill_id", billId)
            .eq("sup_cp_unit_id", supCpUnitId)
                .in("approved_state", Bill.getApprovingState())
                .eq("deleted", 0)
        );
    }

    /**
     * 查询审核记录
     * @param billApplyInfoF billApplyInfoF
     * @return
     */
    public List<BillApproveE> getApproveInfoList(BillApplyInfoF billApplyInfoF){
        return list(new QueryWrapper<BillApproveE>().eq("bill_id", billApplyInfoF.getBillId())
                .eq("sup_cp_unit_id", billApplyInfoF.getSupCpUnitId())
                .in("approved_state", billApplyInfoF.getApproveStatus())
                .eq("deleted", 0)
        );
    }



    /**
     * 根据账单集合查询审核中的账单
     * @param billIdList 账单id集合
     * @return List
     */
    public List<BillApproveE> getApprovingByBillIdList(List<Long> billIdList, String supCpUnitId){
        return list(new LambdaQueryWrapper<BillApproveE>().eq(BillApproveE::getSupCpUnitId, supCpUnitId)
                .in(BillApproveE::getBillId, billIdList)
                .in(BillApproveE::getApprovedState, Bill.getApprovingState()));
    }

    /**
     * 查询审核中的账单
     * @param billId
     * @return
     */
    public List<BillApproveE> listByBillId(Long billId, String supCpUnitId){
        return list(new LambdaQueryWrapper<BillApproveE>()
                .eq(StringUtils.isNotBlank(supCpUnitId), BillApproveE::getSupCpUnitId, supCpUnitId)
                .eq(BillApproveE::getBillId, billId));
    }

    /**
     * 是否审核过
     * @param billId
     * @return
     */
    public boolean hasApproved(Long billId, String supCpUnitId){
        return count(new LambdaQueryWrapper<BillApproveE>().eq(BillApproveE::getSupCpUnitId, supCpUnitId).eq(BillApproveE::getBillId, billId).eq(BillApproveE::getOperateType, BillApproveOperateTypeEnum.生成审核.getCode())) > 0;
    }

    /**
     * 根据账单id获取审核通过的记录(用于判断账单是否反审)
     * @param billIdList 账单id
     * @return List
     */
    public List<BillApproveE> queryApprovedRecord(List<Long> billIdList, String supCpUnitId){
        LambdaQueryWrapper<BillApproveE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillApproveE::getSupCpUnitId, supCpUnitId);
        queryWrapper.in(BillApproveE::getBillId, billIdList);
        queryWrapper.in(BillApproveE::getApprovedState, List.of(BillApproveStateEnum.已审核.getCode(),BillApproveStateEnum.待审核.getCode()
                ,BillApproveStateEnum.审核中.getCode())
        );
        return list(queryWrapper);
    }

    /**
     * 根据外部审批标识获取审核记录
     * @param outApprovedIdList 外部审批标识
     * @return List
     */
    public List<BillApproveE> queryApproveByOutApprovedId(List<String> outApprovedIdList, String supCpUnitId){
        LambdaQueryWrapper<BillApproveE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillApproveE::getSupCpUnitId, supCpUnitId);
        queryWrapper.in(BillApproveE::getOutApproveId, outApprovedIdList);
        return list(queryWrapper);
    }

    /**
     * 根据查询条件获取历史审核记录
     *
     * @param billIds
     * @param outApproveId
     * @return
     */
    public List<BillApproveE> approveHistory(List<Long> billIds, String outApproveId, String supCpUnitId) {
        LambdaQueryWrapper<BillApproveE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(supCpUnitId), BillApproveE::getSupCpUnitId, supCpUnitId);
        queryWrapper.eq(StringUtils.isNotBlank(outApproveId),BillApproveE::getOutApproveId,outApproveId);
        queryWrapper.in(!CollectionUtils.isEmpty(billIds), BillApproveE::getBillId, billIds);
        queryWrapper.orderByDesc(BillApproveE::getGmtCreate);
        return  list(queryWrapper);
    }

    public List<BillApproveE> outApprove(String outApproveId, String supCpUnitId) {
        LambdaQueryWrapper<BillApproveE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(supCpUnitId), BillApproveE::getSupCpUnitId, supCpUnitId);
        queryWrapper.eq(StringUtils.isNotBlank(outApproveId),BillApproveE::getOutApproveId,outApproveId);
        queryWrapper.eq(BillApproveE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq(BillApproveE::getApprovedState, 1);
        return  list(queryWrapper);
    }

    public List<BillApproveE> selectBillApproveEList(Long operationId, String supCpUnitId) {
        return getBaseMapper().selectBillApproveEList(operationId,supCpUnitId);
    }

    /**
     * 查询结转的账单
     * @param billId
     * @return
     */
    public BillApproveE getCarryoverApproveByBillId(Long billId, String supCpUnitId){
        QueryWrapper<BillApproveE> eq = new QueryWrapper<BillApproveE>().eq("bill_id", billId)
                .eq("sup_cp_unit_id", supCpUnitId)
                .eq("approved_state", BillApproveStateEnum.已审核.getCode())
                .eq("deleted", 0).orderByDesc(List.of("gmt_create","id"));
        List<BillApproveE> list = list(eq);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }
}
