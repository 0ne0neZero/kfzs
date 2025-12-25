package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.entity.PayInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ApiModel(value = "临时账单分页列表", parent = BillPageV.class)
public class TemporaryChargeBillPageV extends BillPageV {

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty("账单说明")
    private String description;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "是否引用：0未被引用，1已被引用")
    private Integer referenceState;

    @ApiModelProperty(value = "收付类型：0收款（临时收款），1付款（临时付款）")
    private String payType;

    @ApiModelProperty(value = "收费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("费项类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("支付信息")
    private List<PayInfo> payInfos;

    @ApiModelProperty("收款方式拼接")
    private String payInfosString;

    @ApiModelProperty("扣款金额")
    private Long deductionAmount;

    @ApiModelProperty("计费额度")
    private BigDecimal chargingArea;

    @ApiModelProperty("计费数量")
    private Integer chargingCount;

    @ApiModelProperty(value = "缴费人手机号")
    private String payerPhone;

    @ApiModelProperty("优惠金额 (单位： 分)")
    private Long preferentialAmount;

    @ApiModelProperty("优惠退款金额 (单位： 分)")
    private Long preferentialRefundAmount;

    @ApiModelProperty("是否暂估收入")
    private String estimatedMark;

    @ApiModelProperty("收费对象类型名称")
    private String payerTypeStr;
    @ApiModelProperty("应收日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate receivableDate;

    @ApiModelProperty("归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty("冻结类型 1跳收 2代扣")
    private Integer freezeType;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

    @ApiModelProperty(value = "结算状态： 0-未结算， 1-结算中， 2-已结算")
    private Integer settlementStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer provisionVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer settlementVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer receiptConfirmationVoucherPushingStatus;
    private String uniqueId;

    /**
     * 应收日先取ExtField8, 为空的话取receivableDate, receivableDate为空的话取结束时间
     * IFNULL(b.ext_field8,DATE_FORMAT(b.end_time, '%Y-%m-%d'))
     * @return
     */
    public LocalDate getReceivableDate() {
        LocalDate resDate = this.receivableDate;
        if (Objects.nonNull(resDate)) {
            return resDate;
        }

        try {
            if (getExtField8() != null){
                resDate = LocalDate.parse(getExtField8(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }catch (Exception e){
        }

        final LocalDateTime endTime = getEndTime();
        return  Objects.nonNull(resDate) ? resDate : endTime.toLocalDate();
    }

}
