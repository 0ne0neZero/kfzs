package com.wishare.finance.domains.invoicereceipt.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceDetailAndReceiptV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.domains.invoicereceipt.command.invocing.GatherInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.PayInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.ReceivableInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.consts.enums.AfterPaymentEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceReceiptDetailMapper;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
public class InvoiceReceiptDetailRepository extends ServiceImpl<InvoiceReceiptDetailMapper, InvoiceReceiptDetailE> {

    /**
     * 根据发票收据主表id获取发票明细信息
     *
     * @param invoiceReceiptIds
     * @return
     */
    public List<InvoiceReceiptDetailE> queryByInvoiceReceiptIds(List<Long> invoiceReceiptIds) {
        LambdaQueryWrapper<InvoiceReceiptDetailE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoiceReceiptDetailE::getInvoiceReceiptId, invoiceReceiptIds);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据票据主表id获取票据明细
     *
     * @param invoiceReceiptId
     * @return
     */
    public  List<InvoiceReceiptDetailE> getBillIdsByInvoiceReceiptId(Long invoiceReceiptId) {
        return baseMapper.getByInvoiceReceiptId(invoiceReceiptId);
    }

    /**
     * 根据账单id查询票据明细
     * @param billIds
     * @return
     */
    public List<InvoiceReceiptDetailE> listByBillIds(List<Long> billIds) {
        LambdaQueryWrapper<InvoiceReceiptDetailE> wrapper =
                new LambdaQueryWrapper<InvoiceReceiptDetailE>().in(InvoiceReceiptDetailE::getBillId, billIds);
        return this.list(wrapper);
    }

    /**
     * 根据账单id获取发票收据明细
     *
     * @param invoiceReceiptId
     * @param billId
     * @return
     */
    public List<InvoiceReceiptDetailE> getByBillId(Long invoiceReceiptId, Long billId) {
        if (Objects.isNull(invoiceReceiptId) && Objects.isNull(billId)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<InvoiceReceiptDetailE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(billId != null,InvoiceReceiptDetailE::getBillId, billId);
        wrapper.eq(invoiceReceiptId != null, InvoiceReceiptDetailE::getInvoiceReceiptId, invoiceReceiptId);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据票据类型查询账单ids
     * @param billIds
     * @param compare
     * @param values
     * @return
     */
    public List<InvoiceReceiptDetailV> getBillIdsByType(List<Long> billIds, Integer compare, JSONArray values) {

        SearchF<InvoiceReceiptE> conditions = new SearchF<>();
        QueryWrapper<InvoiceReceiptE> queryModel = conditions.getQueryModel();
        queryModel.in("ird.bill_id", billIds);
        if (compare == 1) {
            queryModel.eq("ir.type", values.getString(0));
        } else if (compare == 15) {
            queryModel.in("ir.type", values);
        } else if (compare == 16) {
            queryModel.notIn("ir.type", values);
        }
        return baseMapper.getBillIdsByType(queryModel);
    }

    public List<InvoiceReceiptDetailE> getBillIdsByBillIdsAndTaxRate(List<Long> billIds, Integer compare, JSONArray values) {
        LambdaQueryWrapper<InvoiceReceiptDetailE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoiceReceiptDetailE::getBillId);
        if (compare == 1) {
            wrapper.eq(InvoiceReceiptDetailE::getTaxRate, values.getString(0));
        } else if (compare == 15) {
            wrapper.in(InvoiceReceiptDetailE::getTaxRate, values);
        } else if (compare == 16) {
            wrapper.notIn(InvoiceReceiptDetailE::getTaxRate, values);
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据账单id和票据id获取票据信息
     * @param billIds 收款/付款id列表
     * @param recIds 应收id列表
     * @return
     */
    public List<InvoiceBillDetailDto> getByBillAndInvoice(List<Long> billIds, List<Long> recIds) {
        return CollectionUtils.isNotEmpty(billIds) ? baseMapper.getInvoiceBillDetails(billIds, recIds) : new ArrayList<>();
    }

    /**
     * 查询账单对应红冲的票据
     * @param invoiceReceiptIds
     * @param billId
     * @return
     */
    public List<InvoiceBillDto> listPartRedInvoices(List<Long> invoiceReceiptIds, Long billId) {
        return baseMapper.listPartRedInvoices(invoiceReceiptIds, billId);
    }


    /**
     * 根据收款单信息查询票据信息
     * @param query 收款单ids
     * @return
     */
    public List<InvoiceBillDetailDto> getByGatherBillIds(GatherInvoiceQuery query,  String gatherDetailName ){

        List<InvoiceBillDetailDto> byGatherBillIds = baseMapper.getByGatherBillIds(query,  gatherDetailName );
        for (int i = 0; i < byGatherBillIds.size(); i++) {
            byGatherBillIds.get(i).setBillId(query.getGatherBillIds().get(i));
        }
        return byGatherBillIds;
    }


    /**
     * 根据收款单信息查询票据信息
     * @param query 收款单ids
     * @return
     */
    public List<InvoiceBillDetailDto> getReceiptByGatherDetailIds(List<Long> gatherDetailIds,String gatherDetailName){
        return baseMapper.getReceiptByGatherDetailIds(gatherDetailIds);
    }





    /**
     * 根据收款单信息查询票据信息
     * @param query
     * @return
     */
    public List<InvoiceBillDetailDto> getInvoiceByRecBillIds(ReceivableInvoiceQuery query){

        List<InvoiceBillDetailDto> getInvoiceByRecBillIds = baseMapper.getInvoiceByRecBillIds(query);
        return getInvoiceByRecBillIds;
    }

    /**
     * 根据付款单信息查询票据信息
     * @param query
     * @return
     */
    public List<InvoiceBillDetailDto> getByPayBillIds(PayInvoiceQuery query){
        return baseMapper.getByPayBillIds(query);
    }


    public List<InvoiceReceiptDetailE> getByInvoiceReceipt(Long invoiceReceiptId) {
        return list(new LambdaQueryWrapper<InvoiceReceiptDetailE>().eq(InvoiceReceiptDetailE::getInvoiceReceiptId, invoiceReceiptId));
    }

    /**
     * 根据开票单元id获取开票信息
     * @param invRecUnitId
     * @return
     */
    public List<InvoiceBillDetailDto> getInvoiceByInvRecUnitId(String invRecUnitId) {
        return baseMapper.getInvoiceByInvRecUnitId(invRecUnitId);
    }

    /**
     * 更新收据的账单开票日期
     * @param billType
     * @param billId
     * @param billPayTime
     */
    public void updateBillPayTime(Integer billType, Long billId, LocalDateTime billPayTime) {
        baseMapper.updateBillPayTime(billType, billId, billPayTime, AfterPaymentEnum.直接开具.getCode());
    }

    /**
     * 物理删除
     * @param invoiceReceiptId
     * @return
     */
    public void deletePhysically(Long invoiceReceiptId) {
        baseMapper.deletePhysically(invoiceReceiptId);
    }


    public List<InvoiceDetailAndReceiptV> queryInvoiceDetailList(String supCpUnitId, Long billId){
        return  baseMapper.queryInvoiceDetailList(supCpUnitId,billId);
    }

    public InvoiceDetailAndReceiptV queryEmptyInvoiceDetail(String supCpUnitId, Long billId){
        return  baseMapper.queryEmptyInvoiceDetail(supCpUnitId,billId);
    }

    /**
     * 获取未被完全红冲/作废的发票和收据
     * @param gatherBillId
     * @param supCpUnitId
     * @return
     */
    public List<Long> getValidInvoiceReceiptIds(Long gatherBillId, String supCpUnitId) {
        return baseMapper.getValidInvoiceReceiptIds(gatherBillId, supCpUnitId);
    }

    /**
     * 获取未被完全红冲/作废的发票
     * @param gatherBillId
     * @return
     */
    public List<Long> getValidInvoiceIdsByGatherBillIds(Long gatherBillId) {
        return baseMapper.getValidInvoiceIdsByGatherBillIds(gatherBillId);
    }

    /**
     * 获取未被完全红冲/作废的发票
     * @param billId
     * @return
     */
    public List<Long> getValidInvoiceIdsByBillId(Long billId) {
        return baseMapper.getValidInvoiceIdsByBillId(billId);
    }


    /**
     * 查询老的没有绑定收款单信息的发票的项目id
     * @return
     */
    public List<String> findOldCommunityToRefresh() {
        return baseMapper.findOldCommunityToRefresh();
    }
}
