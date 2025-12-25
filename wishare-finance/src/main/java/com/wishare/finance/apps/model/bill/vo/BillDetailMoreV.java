package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description: 账单详情
 */
@Getter
@Setter
@ApiModel("账单详情（包含明细）")
public class BillDetailMoreV extends BillAllDetailV{

    @ApiModelProperty("计费面积")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("联系人手机号")
    private String contactPhone;

    @ApiModelProperty("联系人名称")
    private String contactName;

    @ApiModelProperty("应收账龄(月)")
    private Integer receivableAge;

    @ApiModelProperty("账单说明")
    private String description;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("业务类型")
    private String billType;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "冻结类型（0：无类型，1：通联银行代扣）")
    private Integer freezeType;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("应收日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate receivableDate;

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;

    @ApiModelProperty(value = "收款单明细id")
    private Long gatherDetailId;

    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue;

    @ApiModelProperty("应收违约金, (单位： 分)")
    private Long receivableOverdueAmount;

    @ApiModelProperty("实收违约金 (单位： 分)")
    private Long actualPayOverdueAmount;

    @ApiModelProperty("减免违约金 (单位： 分)")
    private Long deductionOverdueAmount;

    @ApiModelProperty("未收违约金 (单位： 分)")
    private Long notReceivedOverdueAmount;

    @ApiModelProperty("账单id-收据单独使用")
    private Long receiptBillId;

    @ApiModelProperty("账单开票状态-收据单独使用")
    private Integer receiptBillInvoiceState;

    @ApiModelProperty("收款明细id-收据单独使用")
    private Long receiptGatherDetailBillId;

    @ApiModelProperty("收款明细开票状态-收据单独使用")
    private Integer receiptGatherDetailInvoiceState;

    @ApiModelProperty("类型名称标识")
    private String typeNameFlag;

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
        return  Objects.nonNull(resDate) ? resDate : Objects.nonNull(endTime) ? endTime.toLocalDate() : null;
    }

}
