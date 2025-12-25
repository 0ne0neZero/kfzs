package com.wishare.finance.domains.invoicereceipt.aggregate;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.fo.EntryInvoiceBillF;
import com.wishare.finance.domains.invoicereceipt.command.invocing.EnterInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.*;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
@Getter
@Setter
public class EnterInvoiceA {

    /**
     * 发票主表信息
     */
    private InvoiceReceiptE invoiceReceiptE;

    /**
     * 发票表信息
     */
    private InvoiceE invoiceE;

    /**
     * 发票明细表信息
     */
    private List<InvoiceReceiptDetailE> invoiceReceiptDetailEList;

    /**
     * 构建进项发票信息
     *
     * @param command
     * @param billDetailMoreVList
     */
    public EnterInvoiceA(EnterInvoiceCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        invoiceReceiptE = getInvoiceReceipt(command,billDetailMoreVList.get(0));
        invoiceE = getInvoice(command, invoiceReceiptE.getId());
        invoiceReceiptDetailEList = getInvoiceReceiptDetail(command, invoiceReceiptE.getId(),billDetailMoreVList);
    }

    /**
     * 构建发票主表
     *
     * @param command
     * @param billDetailMoreV
     * @return
     */
    private InvoiceReceiptE getInvoiceReceipt(EnterInvoiceCommand command, BillDetailMoreV billDetailMoreV) {
        InvoiceReceiptE entity = new InvoiceReceiptE();
        entity.setInvoiceReceiptNo(command.getInvoiceNo());
        entity.setType(command.getType());
        entity.setApplyTime(command.getApplyTime());
        entity.setBillingTime(command.getBillingTime());
        entity.setPriceTaxAmount(command.getPriceTaxAmount());
        entity.setState(InvoiceReceiptStateEnum.开票成功.getCode());
        entity.setSysSource(Objects.nonNull(billDetailMoreV.getSysSource()) ? billDetailMoreV.getSysSource() : command.getSysSource());
        entity.setInvSource(InvSourceEnum.收入的发票.getCode());
        entity.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        entity.setAppId(billDetailMoreV.getAppId());
        entity.setAppName(billDetailMoreV.getAppName());
        entity.setCommunityId(billDetailMoreV.getCommunityId());
        entity.setCommunityName(billDetailMoreV.getCommunityName());
        entity.setStatutoryBodyId(billDetailMoreV.getStatutoryBodyId());
        entity.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        entity.setCostCenterId(billDetailMoreV.getCostCenterId());
        entity.setCostCenterName(billDetailMoreV.getCostCenterName());
        entity.setCustomerId(null);
        entity.setCustomerName(billDetailMoreV.getPayerName());
        entity.setCustomerPhone(null);
        entity.setClerk(command.getClerk());
        return entity;
    }

    /**
     * 构建发票表
     *
     * @param command
     * @param invoiceReceiptId
     * @return
     */
    private InvoiceE getInvoice(EnterInvoiceCommand command, Long invoiceReceiptId) {
        InvoiceE entity = new InvoiceE();
        entity.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
        entity.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
        entity.setInvoiceReceiptId(invoiceReceiptId);
        entity.setInvoiceTitleType(command.getInvoiceTitleType());
        entity.setInvoiceCode(command.getInvoiceCode());
        entity.setInvoiceNo(command.getInvoiceNo());
        return entity;
    }

    /**
     * 构建发票明细表关联账单
     *
     * @param command
     * @param invoiceReceiptId
     * @param billDetailMoreVList
     * @return
     */
    private List<InvoiceReceiptDetailE> getInvoiceReceiptDetail(EnterInvoiceCommand command, Long invoiceReceiptId, List<BillDetailMoreV> billDetailMoreVList) {
//        Map<Long, List<EntryInvoiceBillF>> entryInvoiceBillMap = command.getEntryInvoiceBillFList().stream().collect(Collectors.groupingBy(EntryInvoiceBillF::getBillId));
        List<InvoiceReceiptDetailE> detailEList = Lists.newArrayList();
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            InvoiceReceiptDetailE entity = new InvoiceReceiptDetailE();
            entity.setInvoiceReceiptId(invoiceReceiptId);
            entity.setBillId(billDetailMoreV.getBillId());
            entity.setBillNo(billDetailMoreV.getBillNo());
            entity.setGatherBillId(billDetailMoreV.getGatherBillId());
            entity.setGatherBillNo(billDetailMoreV.getGatherBillNo());
            entity.setGatherDetailId(billDetailMoreV.getGatherDetailId());
            /*EntryInvoiceBillF entryInvoiceBillF = entryInvoiceBillMap.get(billDetailMoreV.getBillId()).get(0);
            if (entryInvoiceBillF.getInvoiceAmount() != null) {
                entity.setInvoiceAmount(entryInvoiceBillF.getInvoiceAmount());
            } else {
                entity.setInvoiceAmount(billDetailMoreV.getActualPayAmount() - billDetailMoreV.getInvoiceAmount());
            }*/
            entity.setInvoiceAmount(billDetailMoreV.getCanInvoiceAmount());
            entity.setBillType(Integer.parseInt(billDetailMoreV.getBillType()));
            entity.setPriceTaxAmount(command.getPriceTaxAmount());
            entity.setGoodsName(billDetailMoreV.getChargeItemName());
            detailEList.add(entity);
        }
        return detailEList;
    }
}
