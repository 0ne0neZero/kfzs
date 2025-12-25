package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.consts.enums.BillAdjustWayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("调整明细")
public class BillAdjustDto {

    @ApiModelProperty(value = "调整id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "调整内容")
    private String content;

    @ApiModelProperty(value = "付款方ID")
    private String payerId;

    @ApiModelProperty(value = "付款方名称")
    private String payerName;

    @ApiModelProperty("源收费对象ID")
    private String originalPayerId;

    @ApiModelProperty("源收费对象名称")
    private String originalPayerName;

    @ApiModelProperty("源收费对象类型")
    private Integer originalPayerType;

    @ApiModelProperty(value = "调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他")
    private Integer adjustWay;

    @ApiModelProperty("调整方式描述")
    private String adjustWayStr;

    @ApiModelProperty(value = "调整金额(减免时为负数，调高时为正数) (单位： 分)")
    private Long adjustAmount;

    @ApiModelProperty(value = "申请调整时间")
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "调整时间")
    private LocalDateTime adjustTime;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "调整时拆单的账单编号")
    private String separateBillNo;

    @ApiModelProperty(value = "调整状态：0待审核，1审核中,2已生效，3未生效")
    private Integer state;


    @ApiModelProperty(value = "调整原因")
    private Integer reason;

    @ApiModelProperty(value = "原账单金额 (单位： 分)")
    private Long billAmount;

    @ApiModelProperty(value = "调整类型： 1减免，2调高，3调低")
    private Integer adjustType;

    @ApiModelProperty(value = "调整比例，区间[0.01, 100]")
    private BigDecimal adjustRatio;

    @ApiModelProperty(value = "审核记录id")
    private Long billApproveId;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("费项ID")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    public String getAdjustWayStr() {
        BillAdjustWayEnum billAdjustWayEnum = BillAdjustWayEnum.valueOfByCode(this.adjustWay);
        return  billAdjustWayEnum.getValue();
    }


}
