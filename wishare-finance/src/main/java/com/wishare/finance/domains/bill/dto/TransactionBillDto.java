package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交易账单信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
public class TransactionBillDto {

    @ApiModelProperty(value = "账单id", required = true)
    private String billId;

    @ApiModelProperty(value = "账单类型： 1应收单, 2预收单, 3临时收费单, 4应付单, 5退款单, 6付款单, 7收款单", required = true)
    private String billType;

    @ApiModelProperty(value = "账单编号", required = true)
    private String billNo;

    @ApiModelProperty(value = "法定单位id", required = true)
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码", required = true)
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty(value = "费项id", required = true)
    private String chargeItemId;

    @ApiModelProperty(value = "费项编码", required = true)
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称", required = true)
    private String chargeItemName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "上级收费单元id", required = true)
    private String supCpUnitId = "上级收费单元id, 账单最小维度的上级维度，应用系统自定义（如项目id）可用于后续业务操作";

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id, 账单最小维度，应用系统自定义（如房号id）可用于后续业务操作", required = true)
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "行政组织编码")
    private String orgCode;

    @ApiModelProperty(value = "行政组织名称")
    private String orgName;

    @ApiModelProperty(value = "收款账号id")
    private String sbAccountId;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号， 可用于账单展示信息", required = true)
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id， 业务唯一标识如订单号等")
    private String outBusId;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    @ApiModelProperty(value = "账单说明", required = true)
    @NotBlank(message = "账单说明不能为空")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（默认：CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "账单开始时间，账单的开始账期", required = true)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间，账单的结束账期", required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "账单归属月")
    private LocalDate billMonth;

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


}
