package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("减免管理")
public class RemissionManagementDTO {
    private String communityId;
    private String communityName;
    @ApiModelProperty("批次号")
    private String batchNumber;
    @ApiModelProperty("账单减免数量")
    private Long billCount;
    @ApiModelProperty("账单总金额")
    private BigDecimal billTotalAmount;
    @ApiModelProperty("减免总金额")
    private BigDecimal reductionTotalAmount;
    @ApiModelProperty("减免场景调整方式")
    private Integer adjustWay;
    private String adjustWayStr;
    @ApiModelProperty("调整原因编码")
    private Integer reason;
    private String reasonStr;
    @ApiModelProperty("审核状态：0-未审核，1-审核中，2-已审核，3-未通过，4-待发起")
    private Integer approvedState;
    private String approvedStateStr;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("减免比例")
    private BigDecimal adjustRatio;
    @ApiModelProperty("减免形式")
    private Integer deductionMethod;
    private String deductionMethodStr;
    @ApiModelProperty("减免金额分配")
    private String distributionOfReductionAmountStr;
    /**
     * 应收总金额
     */
    private BigDecimal receivableAmount;
    /**
     * 实收总金额
     */
    private BigDecimal actualPayAmount;
    /**
     * 可减免金额
     */
    private BigDecimal totalDeductibleAmount;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 优惠单据号码
     */
    private String voucher;

    @ApiModelProperty("流程id")
    private String processId;
}
