package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-21
 */
@Data
@ApiModel("支付申请-财务初审核")
public class FinancialPreliminaryReviewF {

    @ApiModelProperty("支付申请单id")
    @NotEmpty(message = "支付申请单id不能为空")
    private String payApplicationFormId;

    /**初审支付明细*/

    @ApiModelProperty("现金流量id")
    @NotEmpty(message = "现金流量id不能为空")
    private String cashFlow;

    @ApiModelProperty("现金流量名称")
    @NotEmpty(message = "现金流量名称不能为空")
    private String cashFlowName;

    @ApiModelProperty("结算方式id")
    @NotEmpty(message = "结算方式id不能为空")
    private String paymentMethod;

    @ApiModelProperty("结算方式名称")
    @NotEmpty(message = "结算方式名称不能为空")
    private String paymentMethodName;

    @ApiModelProperty("付款账户ID")
    @NotEmpty(message = "付款账户ID不能为空")
    private String payAccountId;

    @ApiModelProperty("付款账户名称")
    @NotEmpty(message = "付款账户名称不能为空")
    private String  nameOfPayAccount;

    @ApiModelProperty("付款银行账号")
    @NotEmpty(message = "付款银行账号不能为空")
    private String payBankAccountNumber;

    @ApiModelProperty("付款账户开户行")
    @NotEmpty(message = "付款账户开户行不能为空")
    private String payOpeningBank;

    @ApiModelProperty("票据支付方式")
    private String paymentMethodForBills;

    @ApiModelProperty("票据数量")
    private String billsNumbers;

    @ApiModelProperty("付款详细说明")
    private String payDesc;

    @ApiModelProperty("转账附言")
    @NotEmpty(message = "转账附言不能为空")
    private String transferRemarks;
}
