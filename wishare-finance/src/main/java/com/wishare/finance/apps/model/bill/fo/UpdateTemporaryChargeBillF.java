package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("更新临时收费账单请求信息")
@Valid
public class UpdateTemporaryChargeBillF extends UpdateBillF{

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @NotBlank(message = "账单id不能为空")
    private String id;


    @ApiModelProperty(value = "应收金额", required = true)
    @NotNull(message = "应收金额不能为空")
    @Max(value = 1000000000, message = "应收金额格式不正确，允许最大值为1000000000")
    private Long receivableAmount;

    @ApiModelProperty(value = "结算单id", required = true)
    private String extField7;


    @ApiModelProperty(value = "应收金额", required = true)
    private BigDecimal taxAmountNew;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

    @ApiModelProperty(value = "收入报表使用-合同 业务系统-确收状态： 0-未确收，1-确收中，2-已确收")
    private Integer businessReceiptConfirmationStatus;

    @ApiModelProperty(value = "确收时间 收入确认单审批通过时间")
    private LocalDateTime receiptConfirmationTime;
    //减免金额
    private BigDecimal reductionAmount;
}
