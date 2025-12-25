package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("应收账单详情(包含各个操作信息)")
public class ReceivableBillAllDetailV extends BillAllDetailV {

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("是否逾期：0未逾期，1已逾期")
    private Integer overdueState;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("业务类型")
    private String billType;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "冻结类型（0：无类型，1：通联银行代扣）")
    private Integer freezeType;

    @ApiModelProperty("应收日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate receivableDate;

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


    public void billInvoiceAmount(){
        if (getSettleState() == BillSettleStateEnum.未结算.getCode()) {
            setCanInvoiceAmount(getReceivableAmount() -
                    getDiscountAmount() - getInvoiceAmount());
        } else {
            if (TenantUtil.bf24()) {
                // 方圆慧享云能开出结转出去的金额，结转进来的金额不能开(押金结转进来的金额能开)
                List<PayInfo> payInfos = getPayInfos();
                long carriedInAmount = 0L;
                if (CollectionUtils.isNotEmpty(payInfos)) {
                    carriedInAmount = payInfos.stream()
                            .filter(info -> StringUtils.equals(info.getPayChannel(), SettleChannelEnum.结转.getCode()))
                            .mapToLong(PayInfo::getAmount)
                            .sum();
                }
                setCanInvoiceAmount(getActualPayAmount() - getInvoiceAmount() + getCarriedAmount() - carriedInAmount);
            } else {
                setCanInvoiceAmount(getActualPayAmount() - getInvoiceAmount());
            }
        }
    }
}
