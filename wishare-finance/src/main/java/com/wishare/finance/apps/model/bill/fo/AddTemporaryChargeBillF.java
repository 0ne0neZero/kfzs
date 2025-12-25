package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("创建临时收费账单请求信息")
@Valid
public class AddTemporaryChargeBillF extends AddBillF {

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "计费方式", required = true)
    @NotNull(message = "计费方式不能为空")
    private Integer billMethod;

    @ApiModelProperty(value = "计费额度")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    //@Max(value = 1000000000, message = "单价格式不正确，允许区间为[1, 1000000000]")
    //@Min(value = 1, message = "单价格式不正确，允许区间为[1, 1000000000]")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "是否引用：0未被引用，1已被引用")
    private String referenceState;

    @ApiModelProperty(value = "收付类型：0收款（临时收款），1付款（临时付款）")
    private String payType;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否")
    private Integer isExact;

    @ApiModelProperty(value = "应收日")
    private LocalDate receivableDate;

    @ApiModelProperty("空间路径")
    private String path;

    @ApiModelProperty("是否签约")
    private Boolean isSign ;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

    @ApiModelProperty(value = "收入报表使用-合同 业务系统-确收状态： 0-未确收，1-确收中，2-已确收")
    private Integer businessReceiptConfirmationStatus;

    @ApiModelProperty(value = "确收时间 收入确认单审批通过时间")
    private LocalDateTime receiptConfirmationTime;

    @ApiModelProperty(value = "结算状态： 0-未结算， 1-结算中， 2-已结算")
    private Integer settlementStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer provisionVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer settlementVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer receiptConfirmationVoucherPushingStatus;

    //应收金额
    private Long receivableAmount  = 0L;

    @ApiModelProperty(value = "唯一标识")
    private String uniqueId;

}
