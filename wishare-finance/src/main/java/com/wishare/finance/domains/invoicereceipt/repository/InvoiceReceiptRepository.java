package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.PushStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceReceiptMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
public class InvoiceReceiptRepository extends ServiceImpl<InvoiceReceiptMapper, InvoiceReceiptE> {

    /**
     * 根据账单id获取发票收据信息
     *
     * @param billId
     * @return
     */
    public List<InvoiceReceiptE> getByBillId(Long billId, List<Integer> types) {
        return baseMapper.getByBillId(billId, types);
    }

    /**
     * 根据账单id批量获取发票数据
     *
     * @param advanceBillIds
     * @param billType
     * @return
     */
    public List<InvoiceBillDto> getBillInvoiceList(List<Long> advanceBillIds, Integer billType) {
        return baseMapper.getBillInvoiceList(advanceBillIds, billType);
    }

    /**
     * 分页查询发票和收据列表
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<InvoiceAndReceiptDto> queryDetailPage(Page<Object> page, QueryWrapper<?> queryWrapper,String receivableBillName) {
        return baseMapper.queryDetailPage(page, queryWrapper, receivableBillName);
    }

    /**
     * 根据状态查询发票和收据合计金额(用于流水领用)
     *
     * @param idList 发票id集合
     * @return Long
     */
    public InvoiceAndReceiptStatisticsDto queryAmount(List<Long> idList, Integer sysSource, String supCpUnitId,String receivableBillName) {
        return baseMapper.queryAmount(idList, sysSource, receivableBillName);
    }

    /**
     * 根据状态查询发票和收据合计金额(用于流水领用)
     *
     * @return Long
     */
    public InvoiceAndReceiptStatisticsDto queryAmount2(QueryWrapper<?> queryWrapper, String oneTable) {
        return baseMapper.queryAmount2(queryWrapper,oneTable);
    }

    /**
     * 更新票据领用状态
     *
     * @param invoiceIdList 票据id集合
     * @param claimState    领用状态
     */
    public Boolean updateClaimStatusByIdList(List<Long> invoiceIdList, int claimState) {
        LambdaUpdateWrapper<InvoiceReceiptE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(InvoiceReceiptE::getClaimStatus, claimState);
        updateWrapper.in(InvoiceReceiptE::getId, invoiceIdList);
        return update(updateWrapper);
    }

    /**
     * 根据票据编号获取主表信息
     *
     * @param invoiceReceiptNo
     * @param invoiceReceiptId
     * @return
     */
    public InvoiceReceiptE getByInvoiceReceiptNo(String invoiceReceiptNo, Long invoiceReceiptId) {
        LambdaQueryWrapper<InvoiceReceiptE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(invoiceReceiptNo),InvoiceReceiptE::getInvoiceReceiptNo, invoiceReceiptNo);
        wrapper.eq(invoiceReceiptId != null,InvoiceReceiptE::getId, invoiceReceiptId);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据id获取发票并根据开票金额降序
     *
     * @param idList idList
     * @return List
     */
    public List<InvoiceReceiptE> listByIdsOrderByPriceTaxAmountDesc(List<Long> idList) {
        LambdaQueryWrapper<InvoiceReceiptE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoiceReceiptE::getId, idList);
        wrapper.orderByDesc(InvoiceReceiptE::getPriceTaxAmount);
        return list(wrapper);
    }

    /**
     * 设置发票主表状态
     * @param invoiceReceiptId
     * @param invoiceReceiptStateEnum
     */
    public void setInvoiceReceiptState(Long invoiceReceiptId, InvoiceReceiptStateEnum invoiceReceiptStateEnum) {
        InvoiceReceiptE invoiceReceiptE = baseMapper.selectById(invoiceReceiptId);
        invoiceReceiptE.setState(invoiceReceiptStateEnum.getCode());
        baseMapper.updateById(invoiceReceiptE);
    }

    /**
     * 根据状态获取发票主表数据
     *
     * @param invoiceReceiptIds
     * @param states
     */
    public List<InvoiceReceiptE> getByState(List<Long> invoiceReceiptIds, List<Integer> states) {
        LambdaQueryWrapper<InvoiceReceiptE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoiceReceiptE::getId, invoiceReceiptIds);
        wrapper.in(InvoiceReceiptE::getState, states);
        return list(wrapper);
    }



    /**
     * 根据账单id获取开票金额
     *
     * @param billIds
     * @return
     */
    public Long invoiceReceiptAmount(List<Long> billIds) {
        return baseMapper.invoiceReceiptAmount(billIds, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(),InvoiceReceiptStateEnum.部分红冲.getCode(), InvoiceReceiptStateEnum.已红冲.getCode()));
    }

    /**
     * 根据业务id获取开票成功的票
     * @param outBusId
     * @return
     */
    public List<InvoiceReceiptE> getSucceedByInvRecUnitId(String outBusId) {
        return list(new LambdaQueryWrapper<InvoiceReceiptE>()
                .eq(InvoiceReceiptE::getInvoiceReceiptNo, outBusId)
                .in(InvoiceReceiptE::getState, InvoiceReceiptE.succeedStateCodes()));
    }

    /**
     * 设置票据推送状态
     * @param invoiceReceiptNo
     * @param pushStateEnum
     */
    public void setPushState(String invoiceReceiptNo, PushStateEnum pushStateEnum) {
        this.update(new LambdaUpdateWrapper<InvoiceReceiptE>()
                .eq(InvoiceReceiptE::getInvoiceReceiptNo, invoiceReceiptNo)
                .set(InvoiceReceiptE::getPushState, pushStateEnum.getCode()));
    }


    /**
     *  根据主键查询信息
     * @param invoiceReceiptId
     * @return
     */
    public InvoiceReceiptE queryById(Long invoiceReceiptId) {
        return baseMapper.queryById(invoiceReceiptId);
    }

    /**
     * 物理删除
     * @param invoiceReceiptId
     * @return
     */
    public void deletePhysically(Long invoiceReceiptId) {
        baseMapper.deletePhysically(invoiceReceiptId);
    }


    public List<InvoiceE> listInvoices(){
      return baseMapper.listInvoices();
    }

    public long countByInvoiceNo(String invoiceNo, List<Integer> state) {
        return baseMapper.countByInvoiceNo(invoiceNo,state);
    }

    public List<InvoiceDto> queryInvoiceDetailByGatherBillIds(List<Long> gatherBillIds) {
        return baseMapper.queryInvoiceDetailByGatherBillIds(gatherBillIds);
    }

    public String getLastReceiptNo() {
        return baseMapper.getLastReceiptNo();
    }

    public int queryByRedInvoiceReceipt(String invoiceReceiptNo) {
       return baseMapper.queryByRedInvoiceReceipt(invoiceReceiptNo);
    }
}
