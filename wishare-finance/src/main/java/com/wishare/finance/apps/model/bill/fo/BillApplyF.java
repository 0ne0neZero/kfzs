package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("账单申请信息")
public class BillApplyF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）")
    private Integer billType;

    @ApiModelProperty(value = "审核原因")
    private String reason;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer approveOperateType;

    @ApiModelProperty(value = "审核详情（该字段为JSON字符串，根据不同操作传不同参数）")
    private String detail;

    @ApiModelProperty("扩展字段1（冲销时如果需要生成新的应收传入：ReversedInitBill）(BPM审核的时候传BPM)")
    private String extField1;

    @ApiModelProperty(value = "操作id")
    private Long operationId;

    @ApiModelProperty(value = "操作备注")
    private String operationRemark;
}
