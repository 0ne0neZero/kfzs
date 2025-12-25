package com.wishare.finance.apps.service.strategy;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.domains.invoicereceipt.aggregate.ReceiptA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;

/**
 * 区分环境，针对处理
 */
public interface ReceiptTenant {


    /**
     * 校检该收据号是否被使用
     *
     * @param receiptNo
     */
    void verifyReceiptNoCanUse(Long receiptNo);

    /**
     * 校验一致性
     * 法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)
     *
     * @param billOjvs
     */
    void verifyConsistency(List<BillDetailMoreV> billOjvs);

    /**
     * 校检是否存在已经开票账单
     *
     * @param billDetailMoreVList
     * @param map
     */
    void verifyInvoiceState(List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map);

    /**
     * 校检是否对应模板可用
     *
     * @param command
     */
    void verifyTemplate(AddReceiptCommand command);

    /**
     * 对 InvoiceReceipt 增加额外参数
     *
     * @param command
     * @param receiptA
     */
    void increaseInvoiceReceiptE(AddReceiptCommand command, ReceiptA receiptA,List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map);

    /**
     * 生成的pdf文件是否需要盖章以及章来源的一个处理标记
     *
     * @param receiptTemplateE
     * @param signStatus
     */
    void signOnPdf(ReceiptTemplateE receiptTemplateE, Integer signStatus,Map<String, Object> map);

    /**
     * pdfLogo
     *
     * @param path
     * @param templateData
     */
    void pdfLogo(String path, HashMap<String, Object> templateData);

    /**
     * 外部签章
     *
     * @param vo
     * @return
     */
    boolean signExternalSeal(SignExternalSealVo vo);

    /**
     * 付款方信息推送
     *
     * @param receiptVDto 数据对象集
     */
    int receiptSend(ReceiptVDto receiptVDto);


    /**
     *
     */
    void afterReceiptSend(ReceiptVDto receiptVDto, Integer way);


    /**
     * 收据作废
     * @param receiptVDto
     * @return false 已经向外部发起调用 true 没有
     */
    boolean voidReceiptV(ReceiptVDto receiptVDto);


    /**
     * 获取签署结果
     * @param signFlowId
     * @return
     */
    EsignResult signResult(String signFlowId);

    /**
     * 获取签署结果 之后的操作
     * @param receiptEId
     * @param esignResult
     * @return
     */
    ReceiptE signResultAfter(Long receiptEId, EsignResult esignResult);

    /**
     * 获取作废结果
     * @param signFlowId
     * @return
     */
    EsignResult voidResult(String signFlowId);

    /**
     * 获取签署结果 之后的操作
     * @param receiptEId
     * @param esignResult
     * @return
     */
    ReceiptE voidResultAfter(Long receiptEId, EsignResult esignResult);


    /**
     * 获取模板路劲
     * @param templateE
     * @return
     */
    String getReceiptTemplatePath(ReceiptTemplateE templateE);

    /**
     * 重新置换账单状态
     * @param invoiceReceiptDetailEList
     * @param command
     */
    void replaceAgainInvoiceState(List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, AddReceiptCommand command);

    /**
     * 开具成功\开票调用财务中心完成开票
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param status 开票\开具状态 true：成功 false：失败
     * @param invoiceFlowMonitorId 流程id
     * @param signStatus 是否需要eSign
     * BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), invoiceReceiptDetailEList, true, invoiceReceiptE.getCommunityId()
     */
    void handleBillStateFinishInvoice(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, boolean status,
            Long invoiceFlowMonitorId,Map<String, Object> map,Integer signStatus);

    /**
     * 修改账单状态为开票中
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param command
     */
    void invoiceBatch(InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, AddReceiptCommand command,Map<String, Map<Long, Integer>> stringMapMap);

    /**
     * 提供链接
     * 针对eSign远洋需要特殊处理 不再直接提供文件地址 而是进行代理 再收据回收的时候提示收据已失效
     * @param no
     * @return
     */
    String eSignUrl(String no);


    /**
     * eSign成功之后
     *  中交记录流程
     *  远洋完成开收据数据变更逻辑
     * @param receiptVDto
     */
    void flowRecordSignSuccAfter(ReceiptVDto receiptVDto);


    /**
     * 签署失败
     * @param invoiceReceiptId
     * @param errorMsg
     */
    void signError(Long invoiceReceiptId,String errorMsg);
}
