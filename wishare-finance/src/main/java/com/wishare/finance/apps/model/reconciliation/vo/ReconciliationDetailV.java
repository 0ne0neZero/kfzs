package com.wishare.finance.apps.model.reconciliation.vo;

import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillRefundOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationFlowDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationInvoiceDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账单明细
 *
 * @author dxclay
 * @since 2022-10-13
 */
@Getter
@Setter
@ApiModel("对账单详情信息")
public class ReconciliationDetailV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "对账记录id")
    private Long reconciliationId;

    @ApiModelProperty(value = "对账结果：0未对平，1已对平")
    private Integer result;

    @ApiModelProperty(value = "应收金额")
    private Long receivableAmount;

    @ApiModelProperty(value = "实收金额")
    private Long actualAmount;

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount;

    @ApiModelProperty(value = "流水认领金额")
    private Long flowClaimAmount;

    @ApiModelProperty(value = "对账日期")
    private LocalDateTime ReconcileTime;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    //@ApiModelProperty(value = "支付信息列表")
    //private List<ReconciliationSettleDetailOBV> settleDetails;

    @ApiModelProperty(value = "对账流水信息列表")
    private List<ReconciliationFlowDetailOBV> flowDetails;

    @ApiModelProperty(value = "对账票据信息列表")
    private List<ReconciliationInvoiceDetailOBV> invoiceDetails;

    @ApiModelProperty(value = "对账应收账单列表")
    private List<ReconciliationRecBillDetailOBV> recBillDetails;

    @ApiModelProperty(value = "对账退款单列表")
    private List<ReconciliationBillRefundOBV> refundDetails;

    @ApiModelProperty(value = "费项信息")
    private List<ChargeItem> chargeItemList;

}
