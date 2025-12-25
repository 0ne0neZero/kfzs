package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票收据明细")
public class InvoiceReceiptDetailV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("单价（单位：分）")
    private String price;

    @ApiModelProperty("数量")
    private String num;

    @ApiModelProperty("数量描述（计量）")
    private String numStr;

    @ApiModelProperty("开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty("账单的原始结算金额（单位：分）")
    private Long settleAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("费用期间(计费周期)")
    private String expensePeriod;

    @ApiModelProperty("费用期间(计费周期)列表")
    private List<String> expensePeriodList;

    @ApiModelProperty("票据明细表ids")
    private List<Long> invoiceReceiptDetails;

    @ApiModelProperty("备注-默认收据取")
    private String remarkNew;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty(value = "实收金额")
    private Long actualPayAmount;

    @ApiModelProperty(value = "仪器最小读数")
    private BigDecimal minNum;

    @ApiModelProperty(value = "仪器最大读数")
    private BigDecimal maxNum;

    @ApiModelProperty(value = "倍率")
    private BigDecimal magnification;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("设备度数")
    private String instrumentStr;

    public void initInstrumentStr() {
        if (Objects.isNull(this.minNum) || Objects.isNull(this.maxNum) || Objects.isNull(this.magnification)) {
            return;
        }
        this.instrumentStr = "(" + this.minNum + "-" + this.maxNum + ")" + "*" + this.magnification;
    }
}
