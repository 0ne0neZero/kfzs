package com.wishare.finance.domains.invoicereceipt.facade;

import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceRedA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceRedApplyE;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.ElectronInvoiceRedApplyF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.ElectronInvoiceRedQueryF;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;

import java.util.List;
import java.util.Map;

/**
 * 发票外部申请接口
 * @author luzhonghe
 * @version 1.0
 * @since 2023/6/20
 */
public interface InvoiceExternalService {

    /**
     * 申请增值税电子发票开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @param invoiceTypeEnum
     * @return 发票流水号
     */
    String nuonuoBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                            List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                            String nuonuoCommunityId,
                            Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                            InvoiceTypeEnum invoiceTypeEnum);

    /**
     * 开具纸质发票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param invoiceDetailDtoList
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @param invoiceTypeEnum
     * @return
     */
    String paperInvoices(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                            List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                            String nuonuoCommunityId,
                            Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                            InvoiceTypeEnum invoiceTypeEnum, String billInfoNo);

    /**
     *
     * 申请纸质发票打印
     * @param form
     * @param tenantId
     * @return
     */
    String invoicesPrints(InvoicePrintF form, String tenantId);
    /**
     * 申请全电开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @return 发票流水号
     */
    String opeMplatformBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                  List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                  String nuonuoCommunityId,
                                  Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId);

    /**
     * 全电红冲申请单申请
     * @param invoiceRedA
     * @return
     */
    String electronInvoiceRedApply(InvoiceRedA invoiceRedA);

    /**
     * 红字确认单查询接口
     * @param applyE
     * @return
     */
    List<NuonuoRedApplyQueryV> electronInvoiceRedApplyQuery(InvoiceRedApplyE applyE, InvoiceE invoiceE);

    /**
     * 发票结果查询接口
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @return
     */
    List<QueryInvoiceResultV> queryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo);

    /**
     * 全电发票快捷红冲接口
     * @param invoiceE
     * @param queryV
     * @return
     */
    String electronInvoiceRed(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV);

    /**
     * 全电开票查询接口
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @return
     */
    List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo,InvoiceE invoiceE);

    /**
     * 组装红字确认单申请入参
     * @param invoiceE
     * @param invoiceReceiptRedE
     * @param billId
     * @return
     */
    ElectronInvoiceRedApplyF generateRedApply(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptRedE, String billId);

    /**
     * 生成红字确认单查询入参
     * @param applyE
     * @param taxnum
     * @return
     */
    ElectronInvoiceRedQueryF generateRedApplyQuery(InvoiceRedApplyE applyE, String taxnum);

    /**
     * 开票重试接口（暂不需要）
     * @param tenantId
     * @param serialNum
     * @return
     */
    String reInvoice(String tenantId, String serialNum);


    /**
     * 开票之前添加销方名称
     * @param invoiceBlueA
     * @return
     */
    InvoiceBlueA preInvoice(InvoiceBlueA invoiceBlueA);


    /**
     * 经营租赁租赁费 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     */
    void checkRealPropertyRentInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                              List<Long> chargeItemIds ,  InvoiceReceiptE invoiceReceiptE );

    /**
     * 建筑服务 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     */
    void checkBuildingServiceInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                                  List<Long> chargeItemIds , InvoiceReceiptE invoiceReceiptE,
                                  InvoiceBuildingServiceInfoF serviceInfoF );


     InvoiceBuildingServiceInfoF handleBuildingServiceInfo(List<Long> chargeItemIds,List<BillDetailMoreV> billDetailMoreVList);
    /**
     * 查询纸质发票信息
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @param orderNo
     * @return
     */
    List<QueryInvoiceResultV> queryPaperInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo);


    /**
     * 诺税通saas红字专用发票信息表申请接口
     * @param
     * @return
     */
    String invoiceRedApply(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                           List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                           String nuonuoCommunityId,
                           Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                           InvoiceTypeEnum invoiceTypeEnum);
}
