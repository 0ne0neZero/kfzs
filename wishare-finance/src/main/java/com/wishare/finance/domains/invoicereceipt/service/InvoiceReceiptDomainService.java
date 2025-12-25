package com.wishare.finance.domains.invoicereceipt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.bill.event.BillRefundMqDetail;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/10/21
 * @Description: 发票收据领域层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceReceiptDomainService {
    private final BillFacade billFacade;
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;
    private final InvoiceDomainService invoiceDomainService;
    private final ReceiptDomainService receiptDomainService;

    /**
     * 根据账单id作废，红冲相关票据
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean invalidedInvoiceReceipt(Long billId) {
        //根据账单id查询已开票状态的
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.getByBillId(null, billId);
        if (CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
            log.info("账单id:" + billId + "无需处理相关票据");
            return true;
        }
        //具有的发票收据id
        // 收款单对应一张发票或收据-------------->通过收款单 ID 找到对应发票红冲------------>修改对应账单、收款单、收款明细的开票状态和开票金额
        // 账单对应两个收款单对应一张票----------->通过收款单 ID 或账单 ID 找到对应发票红冲--->修改对应账单、收款单、收款明细的开票状态和开票金额
        List<Long> invoiceReceiptIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getInvoiceReceiptId).collect(Collectors.toList());
        List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.getByState(invoiceReceiptIds, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(), InvoiceReceiptStateEnum.部分红冲.getCode()));
        if (CollectionUtils.isNotEmpty(invoiceReceiptES)) {
            for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptES) {
                List<InvoiceReceiptDetailE> detailEList = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptE.getId()));
                switch (InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType())) {
                    case 收据:
                    case 电子收据:
                    case 纸质收据:
                        voidInvoiceReceipt(invoiceReceiptE, detailEList);
                        break;
                    case 增值税普通发票:
                    case 定额发票:
                        voidInvoiceReceipt(invoiceReceiptE, detailEList);
                        break;
                    case 增值税电子发票:
                    case 全电普票:
                    case 全电专票:
                        invoiceDomainService.invoicePartRedByBillId(billId, invoiceReceiptE.getId());
                        break;
                    case 增值税专用发票:
                    case 增值税电子专票:
                        break;
                }

            }
        }
        return true;
    }

    /**
     * 通过收款单id作废收据、红冲发票
     * @param gatherBillId
     */
    public void invalidInvoiceReceiptByGatherBill(Long gatherBillId, String supCpUnitId) {
        // 查询未红冲或未作废的发票
        List<Long> invoiceReceiptEIds = invoiceReceiptDetailRepository.getValidInvoiceReceiptIds(gatherBillId, supCpUnitId);
        log.info("需红冲发票id:{}", JSON.toJSONString(invoiceReceiptEIds));
        if (CollectionUtils.isEmpty(invoiceReceiptEIds)) {
            return;
        }
        for (Long invoiceReceiptEId : invoiceReceiptEIds) {
            String messageKey = "invalidInvoiceReceipt" + invoiceReceiptEId;
            // 同一张发票加锁处理
            if (RedisHelper.setNotExists(messageKey, "1")) {
                log.info("开始红冲发票：{}", invoiceReceiptEId);
                RedisHelper.expire(messageKey, 10*60L);
                InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptEId);
                // 加锁二次确认
                if (InvoiceReceiptStateEnum.开票成功.equalsByCode(invoiceReceiptE.getState()) ||
                        InvoiceReceiptStateEnum.部分红冲.equalsByCode(invoiceReceiptE.getState())) {
                    switch (InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType())) {
                        case 收据:
                        case 电子收据:
                        case 纸质收据:
                            receiptDomainService.voidReceipt(invoiceReceiptEId, supCpUnitId);
                            break;
                        case 增值税普通发票:
                        case 定额发票:
                            invoiceDomainService.voidInvoice(invoiceReceiptEId);
                            break;
                        case 增值税电子发票:
                        case 全电普票:
                        case 全电专票:
                        case 增值税专用发票:
                        case 增值税电子专票:
                            invoiceDomainService.invoiceBatchRed(invoiceReceiptEId, null);
                            break;
                    }

                }
            }
        }

    }

    /**
     * 作废发票，收据金额
     *
     * @param invoiceReceiptE
     * @return
     */
    private Boolean voidInvoiceReceipt(InvoiceReceiptE invoiceReceiptE) {
        List<InvoiceReceiptDetailE> detailEList = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptE.getId()));
        invoiceReceiptE.setState(InvoiceReceiptStateEnum.已作废.getCode());
        invoiceReceiptRepository.updateById(invoiceReceiptE);
        return billFacade.invoiceVoidBatch(detailEList, invoiceReceiptE.getCommunityId());
    }

    /**
     * 作废发票，收据金额
     *
     * @param invoiceReceiptDetailEList
     */
    private Boolean voidInvoiceReceipt(InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList) {
        invoiceReceiptE.setState(InvoiceReceiptStateEnum.已作废.getCode());
        invoiceReceiptRepository.updateById(invoiceReceiptE);
        return billFacade.invoiceVoidBatch(invoiceReceiptDetailEList, invoiceReceiptE.getCommunityId());
    }


    /**
     * 根据退款金额进行 作废，红冲相关票据
     *
     * @param billId
     * @param billRefundMqDetail
     */
    public boolean invalidedInvoiceReceiptByRefund(Long billId, BillRefundMqDetail billRefundMqDetail) {

        //根据账单id查询已开票状态的
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.getByBillId(null, billId);
        if (CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
            log.info("账单id:" + billId + "无需处理相关票据");
            return true;
        }
        //具有的发票收据id
        List<Long> invoiceReceiptIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getInvoiceReceiptId).collect(Collectors.toList());
        List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.getByState(invoiceReceiptIds, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(), InvoiceReceiptStateEnum.部分红冲.getCode()));

        if (CollectionUtils.isNotEmpty(invoiceReceiptES)) {
            Long refundAmount = billRefundMqDetail.getRefundAmount();
            for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptES) {
                switch (InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType())) {
                    case 收据:
                    case 电子收据:
                    case 纸质收据:
                        voidInvoiceReceipt(invoiceReceiptE);
                        break;
                    case 增值税普通发票:
                    case 定额发票:
                        if (TenantUtil.bf16() && billRefundMqDetail.getSource() == 2) {
                            // 慧享云环境发票不进行红冲
                            break;
                        }
                        voidInvoiceReceipt(invoiceReceiptE);
                        break;
                    case 全电普票:
                    case 全电专票:
                    case 增值税电子发票:
                        if (TenantUtil.bf16() && billRefundMqDetail.getSource() == 2) {
                            // 慧享云环境发票不进行红冲
                            break;
                        }
                        //档次该张票红冲后的下一张票的可红冲金额
                        if (refundAmount == 0L) {
                            break;
                        }
                        refundAmount = invoiceDomainService.invoicePartRedByBillIdAndRedAmount(billId, invoiceReceiptE.getId(), refundAmount);
                        break;
                    case 增值税专用发票:
                    case 增值税电子专票:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * 获取账单的开票金额
     *
     * @param billIds
     * @return
     */
    public Long invoiceReceiptAmount(List<Long> billIds) {
        return invoiceReceiptRepository.invoiceReceiptAmount(billIds);
    }

    /**
     * 设置开票状态
     * @param invoiceReceiptId
     * @param invoiceReceiptStateEnum
     */
    public void setInvoiceReceiptState(Long invoiceReceiptId, InvoiceReceiptStateEnum invoiceReceiptStateEnum) {
        invoiceReceiptRepository.setInvoiceReceiptState(invoiceReceiptId, invoiceReceiptStateEnum);
    }
}
