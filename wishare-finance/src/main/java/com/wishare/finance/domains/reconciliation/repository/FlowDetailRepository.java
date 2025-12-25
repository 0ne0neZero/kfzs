package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.reconciliation.dto.FlowStatisticsDto;
import com.wishare.finance.apps.model.reconciliation.vo.FlowDetailComplexV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowDetailV;
import com.wishare.finance.domains.reconciliation.FlowDetailVo;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceDetailDto;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.dto.FlowReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationFlowDetailOBV;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowTypeStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowDetailMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流水明细
 *
 * @author yancao
 */
@Service
public class FlowDetailRepository extends ServiceImpl<FlowDetailMapper, FlowDetailE> {

    /**
     * 根据流水号获取流水明细
     *
     * @param serialNumber 流水号
     * @return FlowDetailE
     */
    public FlowDetailE queryBySerialNumber(String serialNumber) {
        LambdaQueryWrapper<FlowDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowDetailE::getSerialNumber, serialNumber);
        return getOne(queryWrapper);
    }

    /**
     * 根据id更新流水认领状态
     *
     * @param flowIdList  流水id集合
     * @param claimStatus 认领状态
     */
    public void updateClaimStatusByIdList(List<Long> flowIdList, int claimStatus) {
        LambdaUpdateWrapper<FlowDetailE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FlowDetailE::getClaimStatus,claimStatus);
        updateWrapper.in(FlowDetailE::getId, flowIdList);
        update(updateWrapper);
    }

    /**
     * 根据流水id对本方账户进行分组
     *
     * @param flowIdList 流水id集合
     * @param type 账单类型
     * @return List
     */
    public List<FlowDetailE> queryGroupByOurAccount(List<Long> flowIdList, Integer type) {
        LambdaQueryWrapper<FlowDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowDetailE::getId, flowIdList);
        queryWrapper.eq(Objects.nonNull(type),FlowDetailE::getType, type);
        if(FlowTypeStatusEnum.收入.equalsByCode(type)){
            queryWrapper.groupBy(FlowDetailE::getOurName);
            queryWrapper.groupBy(FlowDetailE::getOurBank);
            queryWrapper.groupBy(FlowDetailE::getOurAccount);
        }else if(FlowTypeStatusEnum.退款.equalsByCode(type)){
            queryWrapper.groupBy(FlowDetailE::getOperatorName);
            queryWrapper.groupBy(FlowDetailE::getOppositeBank);
            queryWrapper.groupBy(FlowDetailE::getOppositeAccount);
        }
        return list(queryWrapper);
    }

    /**
     * 根据流水号获取流水明细
     *
     * @param serialNumberList 流水号集合
     * @return List
     */
    public List<FlowDetailE> queryBySerialNumberList(List<String> serialNumberList) {
        LambdaQueryWrapper<FlowDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowDetailE::getSerialNumber, serialNumberList);
        return list(queryWrapper);
    }

    /**
     * 根据id集合获取流水总金额
     *
     * @param idList 流水id集合
     * @return Long
     */
    public FlowStatisticsDto statisticsAmount(List<Long> idList) {
        return baseMapper.statisticsAmount(idList);
    }
    /**
     * 根据id集合获取流水总金额
     *
     * @return Long
     */
    public FlowStatisticsDto statisticsAmount2(QueryWrapper<?> queryWrapper) {
        return baseMapper.statisticsAmount2(queryWrapper);
    }

    /**
     * 根据票据id获取流水信息
     * @param invoiceIds
     * @param status      流水状态
     * @return
     */
    public List<FlowInvoiceDetailDto> getListByInvoiceIds(List<Long> invoiceIds, FlowClaimDetailStatusEnum status) {
        return CollectionUtils.isNotEmpty(invoiceIds)? baseMapper.getListByInvoiceIds(invoiceIds, (status == null ? null : status.getCode())) : new ArrayList<>();
    }


    public List<FlowInvoiceReconciliationDetailDto> getReconciliationFlows(List<Long> invoiceIds, FlowClaimDetailStatusEnum status) {
        return CollectionUtils.isNotEmpty(invoiceIds)? baseMapper.getReconciliationFlows(invoiceIds, (status == null ? null : status.getCode())) : new ArrayList<>();

    }


    public List<FlowReconciliationDetailDto> getNewReconciliationFlows(String supCpUnitId){
        return baseMapper.getNewReconciliationFlows(supCpUnitId);
    }


    public  int updateFlowClaimRecordFlag(List<ReconciliationFlowDetailOBV> list){
        List<Long> longList = list.stream().map(ReconciliationFlowDetailOBV::getInvoiceId).collect(Collectors.toList());
        return baseMapper.updateFlowClaimRecordFlag(longList);
    }
    public  int updateReconcileFlag(Long flowClaimRecordId,Integer reconcileFlag){
        return baseMapper.updateReconcileFlag(flowClaimRecordId, reconcileFlag);
    }

    public List<FlowClaimDetailE> getFlowClaimDetailByInvoiceIds(List<ReconciliationFlowDetailOBV> list){
        List<Long> longList = list.stream().map(ReconciliationFlowDetailOBV::getInvoiceId).collect(Collectors.toList());
        return baseMapper.getFlowClaimDetailByInvoiceIds(longList);
    }


    public List<FlowClaimDetailE> getByFlowClaimRecordId(List<Long> list){

        return baseMapper.getByFlowClaimRecordId(list);
    }

    public Set<String> selectPayTime (List<Long> invoiceIds) {
        return baseMapper.selectPayTime(invoiceIds);
    }


    /**
     * 根据流水id获取流水并根据金额降序
     *
     * @param flowIdList flowIdList
     * @return List
     */
    public List<FlowDetailE> listByIdsOrderBySettleAmountDesc(List<Long> flowIdList) {
        LambdaQueryWrapper<FlowDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(FlowDetailE::getSettleAmount);
        queryWrapper.in(FlowDetailE::getId, flowIdList);
        return list(queryWrapper);
    }


    public FlowDetailE getTenantId() {
        return baseMapper.getTenantId();
    }

    /**
     * 根据条件获取流水详情
     * @param fieldList
     * @return
     */
    public List<FlowDetailE> listByConditions(List<Field> fieldList) {
        SearchF<FlowDetailE> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<FlowDetailE> queryModel = searchF.getQueryModel();
        if (CollectionUtils.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                switch (field.getMethod()) {
                    case 1:
                        queryModel.eq(field.getName(), field.getValue());
                        break;
                    case 6:
                        queryModel.le(field.getName(), field.getValue());
                        break;
                    case 4:
                        queryModel.ge(field.getName(), field.getValue());
                        break;
                    case 15:
                        queryModel.in(field.getName(), field.getValue());
                        break;
                    case 16:
                        queryModel.notIn(field.getName(), field.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        return baseMapper.listByConditions(queryModel);
    }

    /**
     * 根据流水id列表获取流水认领id列表
     * @param flowIds
     * @return
     */
    public List<Long> getIdsByFlowIds(List<Long> flowIds) {
        return CollectionUtils.isNotEmpty(flowIds) ? baseMapper.getIdsByFlowIds(flowIds) : new ArrayList<>();
    }

    /**
     * 分页查询流水
     * 可用成本中心id查询
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<FlowDetailE> queryDetailPage(Page<FlowDetailE> page, QueryWrapper<FlowDetailE> queryWrapper) {
        return baseMapper.queryDetailPage(page, queryWrapper);
    }

    /**
     * 查询流水明细
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<FlowDetailComplexV> queryFlowDetailComplexPage(Page<FlowDetailE> page, QueryWrapper<FlowDetailE> queryWrapper) {
        return baseMapper.queryFlowDetailComplexPage(page, queryWrapper);
    }

    /**
     * 分页查询流水V2 包含流水认领记录id和报账单id
     **/
    public IPage<FlowDetailVo> queryDetailPage2(Page<FlowDetailE> page, QueryWrapper<FlowDetailE> queryWrapper) {
        return baseMapper.queryDetailPage2(page, queryWrapper);
    }

    public List<FlowDetailE> queryDetailList(QueryWrapper<FlowDetailE> queryWrapper) {
        return baseMapper.queryDetailList(queryWrapper);
    }

    public Set<String> querySerialNumbers() {
        return baseMapper.querySerialNumbers(DataDeletedEnum.NORMAL.getCode());
    }

    public FlowClaimRecordE getFlowClaimRecord(String flowId){
        return baseMapper.getFlowClaimRecord(flowId);
    }
}
