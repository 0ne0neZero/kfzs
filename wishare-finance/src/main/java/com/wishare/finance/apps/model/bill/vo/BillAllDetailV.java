package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.apps.model.voucher.vo.VoucherV;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceDetailDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("账单所有详细信息")
public class BillAllDetailV extends BillDetailV {

    @ApiModelProperty("申请信息")
    private List<BillApproveApplyV> approves;

    @ApiModelProperty("调整明细")
    private List<BillAdjustV> billAdjustDtos;

    @ApiModelProperty("结转明细")
    private List<BillCarryoverV> billCarryoverDtos;

    @ApiModelProperty("结算明细")
    private List<BillSettleV> billSettleDtos;

    @ApiModelProperty("退款明细")
    private List<BillRefundV> billRefundDtos;

    @ApiModelProperty("扣款明细")
    private List<BillDeductionV> billDeductionDtos;

    @ApiModelProperty("收据明细")
    private List<ReceiptV> receiptVos;

    @ApiModelProperty("发票明细")
    private List<InvoiceV> invoiceVos;

    @ApiModelProperty("凭证明细")
    private List<VoucherV> voucherVS;

    @ApiModelProperty("收款流水")
    private List<FlowInvoiceDetailDto> gatherBillFlows;

    @ApiModelProperty("退款流水")
    private List<FlowInvoiceDetailDto> payBillFlows;
}
