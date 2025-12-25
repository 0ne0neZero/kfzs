package com.wishare.finance.apps.pushbill.vo;

import com.wishare.finance.apps.service.pushbill.mdm63.BaseWriteOffObj;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 报账明细
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentApplicationKXDetailV extends BaseWriteOffObj {
    @ApiModelProperty("核销应付ID")
    private String ftId;
    @ApiModelProperty("核销应付编号")
    @NotEmpty(message = "核销应付编号不能为空")
    private String heyfbh;

    @ApiModelProperty("未核销金额(原币)")
    private BigDecimal writeOffInfoAmount;

    @ApiModelProperty("业务科目")
    private String subjectName;

    @ApiModelProperty("应收应付摘要")
    private String summary;

    @ApiModelProperty("资金计划编号")
    private String fundingPlanNumber;

    @ApiModelProperty(value = "预算科目编码", required = true)
    private String subjectCode;

    @ApiModelProperty(value = "预算科目名称")
    private String budgetAccountName;

    @ApiModelProperty("期望结算方式")
    @NotEmpty(message = "期望结算方式不能为空")
    private String payWay;

    @ApiModelProperty("合计金额")
    private BigDecimal amount;

    @ApiModelProperty("到期日期")
    @NotNull(message = "到期日期不能为空")
    private LocalDate dueDate;

}
