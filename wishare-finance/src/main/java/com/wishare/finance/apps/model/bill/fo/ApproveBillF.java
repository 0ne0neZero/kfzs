package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("审核单条账单信息")
public class ApproveBillF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单 7-收款账单")
    private Integer billType;

    @ApiModelProperty(value = "审核状态：2已审核，3未通过 4待发起 5审批中 6 通过 7驳回")
    @NotNull(message = "审核状态不能为空")
    private Integer approveState;

    @ApiModelProperty(value = "退款方式(1汇款，2支票，3其他，4现金，5线下-支付宝，6线下-微信,7-原路退款)")
    private String refundMethod;

    @ApiModelProperty(value = "驳回理由")
    private String rejectReason;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer operateType;

    @ApiModelProperty(value = "审核操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty(value = "已审核已结算账单调低和减免审核后操作：1退款，2转预收")
    private Integer approvedAction;

    @ApiModelProperty(value = "转预收的费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "转预收的费项名称")
    private String chargeItemName;

    @ApiModelProperty("外部审批标识")
    private String outApproveId;

    @ApiModelProperty("审核类型： 0内部审核，1外部审核")
    private Integer approveType;
}
