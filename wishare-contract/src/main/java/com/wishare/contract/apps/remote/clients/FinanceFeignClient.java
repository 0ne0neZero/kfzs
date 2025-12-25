package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.fo.revision.remote.ContractInvoiceInfoF;
import com.wishare.contract.apps.remote.finance.fo.AddPayBillRF;
import com.wishare.contract.apps.remote.finance.vo.PayBillRV;
import com.wishare.contract.apps.remote.fo.*;
import com.wishare.contract.apps.remote.fo.finance.InvoiceBatchBlueRf;
import com.wishare.contract.apps.remote.vo.*;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 财务中台调用
 * @author ljx
 */
@OpenFeignClient(name = "wishare-finance", serverName = "财务中台", path = "/finance")
public interface FinanceFeignClient {

    /**
     * 开具发票1
     */
    @PostMapping(value = "/invoice/invoiceBatch")
    Long invoiceBatch(@RequestBody InvoiceBatchRf invoiceBatchRF);

    /**
     * 获取发票信息1
     *
     * @return
     */
    @PostMapping(value = "/invoice/listByBillIds", name = "获取费项名称")
    List<InvoiceInfoRv> listByBillIds(@RequestBody InvoiceInfoF form);

    /**
     * 分页查询账单列表1
     */
    @PostMapping("/bill/page")
    PageV<BillPageInfoRv> getBillPage(@Validated @RequestBody PageF<SearchF<?>> queryF);

    /**
     * 合同系统推送收票信息且生成对下结算单
     */
    @PostMapping("/contract/acceptInfo")
    Long acceptContractInvoice(@RequestBody ContractInvoiceInfoF contractInvoiceInfoF);

    /**
     * 发起审核申请1
     */
    @PostMapping("/bill/apply")
    Boolean billApply(@Validated @RequestBody BillApplyRf from);

    /**
     * 批量发起审核申请1
     */
    @PostMapping("/bill/apply/batch")
    Boolean billApplyBatch(@Validated @RequestBody BillApplyBatchRf from);

    /**
     * 统计账单金额总数1
     */
    @PostMapping("/statistics/bills")
    BillTotalRv statisticsBills(@Validated @RequestBody StatisticsBillTotalRf from);

    /**
     * 开收据1
     */
    @PostMapping("/receipt/receiptBatch")
    Long receiptBatch(@Validated @RequestBody ReceiptBatchRf from);

    /**
     * 收据详情1
     */
    @PostMapping("/receipt/detail")
    ReceiptDetailRv receiptDetail(@RequestBody ReceiptDetailRf from);

    /**
     * 获取费项名称
     *
     * @return
     */
    @GetMapping(value = "/charge/query/name/{id}", name = "获取费项名称")
    String chargeName(@PathVariable("id") Long id);

    /**
     *根据ID查费项数据
     */
    @GetMapping("/charge/query/id/{id}")
    ChargeItemRv chargeGetById(@PathVariable("id") Long id);

    @ApiOperation(value = "根据ID集合查费项数据", notes = "根据ID集合查费项数据")
    @PostMapping("/charge/query/idList")
    List<ChargeItemRv> getByIdList(@RequestBody List<Long> idList);

    /**
     * 批量新增应收账单1
     *
     * @return
     */
    @GetMapping(value = "/receivable/add/batch", name = "批量新增应收账单")
    List<ReceivableBillDetailRv> addBatch(@RequestBody @Validated List<AddReceivableBillRf> receivableBillRf);

    /**
     * 批量新增应付账单1
     *
     * @return
     */
    @GetMapping(value = "/payable/add/batch", name = "批量新增应付账单")
    List<ReceivableBillDetailRv> addPayableBatch(@RequestBody @Validated List<AddReceivableBillRf> receivableBillRf);

    @PostMapping(value = "/payable/add",name = "新增应付账单")
    PayableBillDetailRv addPayable(@RequestBody AddPayableBillRf addPayableBillF);

    /**
     * 分页查询应收账单列表1
     */
    @PostMapping("/receivable/page")
    PageV<ReceivablePageRv> receivablePage(@RequestBody PageF<SearchF<Field>> from);

    /**
     * 分页查询应付账单列表1
     */
    @PostMapping("/payable/page")
    PageV<ReceivablePageRv> payablePage(@RequestBody PageF<SearchF<Field>> from);

    /**
     * 批量删除应收账单1
     */
    @DeleteMapping("/receivable/delete/batch")
    ReceivableDeleteRv receivabledelete(@RequestBody DeleteReceivableBillRf deleteReceivableBillRf);

    /**
     * 批量删除应付账单1
     */
    @DeleteMapping("/payable/delete/batch")
    ReceivableDeleteRv payabledelete(@RequestBody DeleteReceivableBillRf deleteReceivableBillRf);

    /**
     * 分页查询临时收费账单列表1
     */
    @PostMapping("/temporary/page")
    PageV<TemporaryChargeBillPageV> temporaryQueryPage(@Validated @RequestBody PageF<SearchF<?>> queryF);

    /**
     *批量新增临时收费账单1
     */
    @PostMapping("/temporary/add/batch")
    List<TemporaryChargeBillPageV> temporaryAddBatch(@RequestBody List<AddTemporaryChargeBillRf> addTemporaryChargeBillFs);

    @PostMapping("/temporary/update/batch")
    Boolean temporaryUpdateBatch(@Validated @RequestBody List<UpdateTemporaryChargeBillF> updateTemporaryChargeBillFs);

    /**
     * 批量结算0
     */
    @PostMapping("/temporary/settle/batch")
    Boolean temporarySettleBatch(@Validated @RequestBody List<AddBillSettleRf> form);

    /**
     * 账单退款0
     */
    @PostMapping("/temporary/refund")
    Boolean temporaryRefund(@Validated @RequestBody BillRefundRf billRefundRf);

    /**
     * 批量删除临时收费账单1
     */
    @DeleteMapping("/temporary/delete/batch")
    BillDeleteBatchResultDto temporaryDeleteBatch(@Validated @RequestBody DeleteBatchBillRf deleteBatchBillRf);

    /**
     * 设置引用临时收费账单1
     */
    @PostMapping("/temporary/reference")
    Boolean temporaryReference(@Validated @RequestBody ReferenceBillRf referenceBillRf);

    /**
     * 批量审核临时收费账单1
     */
    @PostMapping("/temporary/approve/batch")
    Boolean temporaryApproveBatch(@Validated @RequestBody ApproveBatchTemporaryChargeBillRf approveBatchTemporaryChargeBillF);

    /**
     * 账单结转1
     */
    @PostMapping("/temporary/carryover")
    Boolean temporaryCarryover(@Validated @RequestBody TemporaryChargeBillCarryoverRf temporaryChargeBillCarryoverF);

    /**
     * 应收账单批量结算0
     */
    @PostMapping("/receivable/settle/batch")
    Boolean receivableSettleBatch(@Validated @RequestBody List<AddBillSettleRf> form);

    /**
     * 应付账单批量结算0
     */
    @PostMapping("/payable/settle/batch")
    Boolean payableSettleBatch(@Validated @RequestBody List<AddBillSettleRf> form);

    /**
     * 批量审核应付账单1
     */
    @PostMapping("/payable/approve/batch")
    Boolean payableApproveBatch(@Validated @RequestBody ApproveBatchPayableBillRf approveBatchPayableBillF);

    /**
     * 批量审核应收账单1
     */
    @PostMapping("/receivable/approve/batch")
    Boolean approveBatch(@Validated @RequestBody ApproveBatchReceivableBillRf approveBatchReceivableBillF);

    /**
     * 批量新增收款单1
     */
    @PostMapping("/gather/add/batch")
    List<AddGatherBillF> gatherAddBatch(@Validated @RequestBody List<AddGatherBillF> addGatherBillFList);

    /**
     * 新增付款单1
     * @param addPayBillF
     * @return
     */
    @PostMapping(value = "/payBill/add",name = "新增付款单")
    PayBillRV addPayBill(@Validated @RequestBody AddPayBillRF addPayBillF) ;

    @PostMapping(value = "/invoice/invoiceBatchBlue", name = "开具蓝票(无校检)")
    Long invoiceBatchBlue(@Validated @RequestBody InvoiceBatchBlueRf f);

    /**
     * 获取发票信息
     *
     * @return
     */
    @PostMapping(value = "/invoice/listByBillIds", name = "获取发票信息")
    List<InvoiceInfoRv> listByBillIdsHeader(@RequestHeader("tenantId") String tenantId, @RequestBody InvoiceInfoF form);

    /**
     * 根据发票id列表获取发票信息
     *
     * @return
     */
    @PostMapping(value = "/invoice/invoices", name = "根据发票id列表获取发票信息")
    List<InvoiceInfoRv> invoices(@RequestHeader("tenantId") String tenantId, @RequestBody List<Long> invoiceIds);


    @PostMapping(value = "/voucherbillDxZJ/generate/settlement",name = "通过临时账单创建报账单")
    Boolean createCheckSheet(@RequestBody VoucherBillGenerateOnContractSettlementF voucherBillGenerateOnContractSettlementF);

    @PostMapping(value = "/voucherbillDxZJ/updateBilDxZjFromContract",name = "更改对下实签来自合同数据")
    Boolean updateBilDxZjFromContract(@RequestParam("settlementId") String settlementId, @RequestParam("otherBusinessReasons") String otherBusinessReasons, @RequestParam("externalDepartmentCode") String externalDepartmentCode,@RequestParam("calculationMethod") Integer calculationMethod);

    @PostMapping(value = "/voucherbillDxZJ/updateBilDxZjReceiptRemark",name = "更改对下实签业务事由")
    Boolean updateBilDxZjReceiptRemark(@RequestParam("settlementId") String settlementId, @RequestParam("otherBusinessReasons") String otherBusinessReasons);

    @ApiOperation(value = "根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID")
    @GetMapping("/receivable/getReceivableBillIdList")
    List<String> getReceivableBillIdList(@RequestParam(value = "contractId")  String contractId,
                                                @RequestParam(value = "communityId")  String communityId);

    @ApiOperation(value = "根据临时账单ID删除临时账单及对应合同报账单中对下-计提")
    @GetMapping("/receivable/deleteReceivableAndHtBzd")
    void deleteReceivableAndHtBzd(@RequestParam(value = "billIdList") List<String> billIdList,
                                                @RequestParam(value = "communityId")  String communityId);


    @ApiOperation(value = "[校验]根据临时账单ID获取对应报账单/合同报账单数据")
    @GetMapping("/receivable/getVoucherBillByReceivableId")
    String getVoucherBillByReceivableId(@RequestParam(value = "billIdList") List<String> billIdList, @RequestParam(value = "communityId")  String communityId);

    @ApiOperation(value = "根据临时账单ID删除对应临时账单数据")
    @GetMapping("/receivable/deleteReceivableBillById")
    void deleteReceivableBillById(@RequestParam(value = "billIdList") List<String> billIdList, @RequestParam(value = "communityId")  String communityId);

    @ApiOperation(value = "[校验]根据临时账单ID获取报账单数据-实签")
    @GetMapping("/receivable/getVoucherBillSq")
    String getVoucherBillSq(@RequestParam(value = "billIdList") List<String> billIdList, @RequestParam(value = "communityId")  String communityId);

    @ApiOperation(value = "根据临时账单ID还原对应临时账单数据及删除合同报账单")
    @PostMapping("/receivable/deleteReceivableBillAndVoucher")
    void deleteReceivableBillAndVoucher(@RequestBody List<ResultTemporaryChargeBillF> updateTemporaryChargeBillF);

}
