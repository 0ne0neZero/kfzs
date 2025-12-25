package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单统一入账信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一入账信息")
public class BillUnitaryEnterCommand {

    @ApiModelProperty(value = "账单类型 1:应收，2:预收，3:临时，4:应付，6:付款， 7:收款", required = true)
    private Integer billType;

    @ApiModelProperty(value = "法定单位id, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty(value = "费项id，费项id和编码二选一条件", required = true)
    private String chargeItemId;

    @ApiModelProperty(value = "费项编码，费项id和编码二选一条件", required = true)
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称", required = true)
    private String chargeItemName;

    @ApiModelProperty(value = "成本中心id，成本中心id和编码二选一条件")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心编码，成本中心id和编码二选一条件")
    private String costCenterCode;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "上级收费单元id", required = true)
    private String supCpUnitId = "上级收费单元id, 账单最小维度的上级维度，应用系统自定义（如项目id）可用于后续业务操作";

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName = "";

    @ApiModelProperty(value = "收费单元id, 账单最小维度，应用系统自定义（如房号id）可用于后续业务操作", required = true)
    private String cpUnitId = "";

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName = "";

    @ApiModelProperty("收费组织id")
    private String cpOrgId;

    @ApiModelProperty("收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "计费方式(1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天，), 应收账单计费方式必填")
    private Integer billMethod;

    @ApiModelProperty("计费额度")
    @Digits(integer = 18, fraction = 6, message = "计费额度格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMax(value = "1000000000.000000", message = "计费额度格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMin(value = "0.000001", message = "计费额度格式不正确，允许区间为[0.000001, 1000000000.000000]")
    private BigDecimal chargingArea;

    @ApiModelProperty("计费数量，如计费方式为单价*数量时，计费")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    @Max(value = 1000000000, message = "单价格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "单价格式不正确，允许区间为[1, 1000000000]")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "账单说明", required = true)
    @NotBlank(message = "账单说明不能为空")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（默认：CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额 （区间为[1, 1000000000]）", required = true)
    @NotNull(message = "账单金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    private Long totalAmount;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "账单来源", required = true)
    @NotBlank(message = "账单来源不能为空")
    private String source;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段2")
    private String extField2;

    @ApiModelProperty("扩展字段3")
    private String extField3;

    @ApiModelProperty("扩展字段4")
    private String extField4;

    @ApiModelProperty("扩展字段5")
    private String extField5;

    @ApiModelProperty("扩展字段6")
    private String extField6;

    @ApiModelProperty("扩展字段7")
    private String extField7;

    @ApiModelProperty("扩展字段8")
    private String extField8;

    @ApiModelProperty("扩展字段9")
    private String extField9;

    @ApiModelProperty("扩展字段10")
    private String extField10;

    @ApiModelProperty(value = "结算信息， （预收、临时、应付、付款、收款）类型的账单必填")
    private BillSettleCommand settleInfo;


    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty("增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据")
    private String invoiceType;


    @ApiModelProperty(value = "收款人id")
    private String payeeId;

    @ApiModelProperty("收款方名称")
    private String payeeName;

    @ApiModelProperty(value = "收款方手机号")
    private String payeePhone;

    @ApiModelProperty("收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payeeType;

    @ApiModelProperty("是否已审核 true已审核，false待审核")
    private Boolean approvedFlag;

    @ApiModelProperty("结算金额 (单位： 分)")
    private Long settleAmount;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "归属月（账期）,格式：yyyy-MM")
    private LocalDate accountDate;

    @ApiModelProperty(value = "渠道交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "支付时间 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty("购方名称（付款方名称）")
    private String payerName;

    @ApiModelProperty("付款方手机号")
    private String payerPhone;

    @ApiModelProperty(value = "业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty(name = "是否签约", hidden = true)
    private Boolean isSign;

    @ApiModelProperty(value = "付款方id (结算的收费对象为账单的付款方)")
    private String payerId;

    public String getStatutoryBodyId() {
        return (StringUtils.isEmpty(statutoryBodyId)||StringUtils.equals(statutoryBodyId,"null"))?null:statutoryBodyId;
    }

    public String getCostCenterId() {
        return (StringUtils.isEmpty(costCenterId)||StringUtils.equals(costCenterId,"null"))?null:costCenterId;
    }

    public String getChargeItemId() {
        return (StringUtils.isEmpty(chargeItemId)||StringUtils.equals(chargeItemId,"null"))?null:chargeItemId;
    }
}
