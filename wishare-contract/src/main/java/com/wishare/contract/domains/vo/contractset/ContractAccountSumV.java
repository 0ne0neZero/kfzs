package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* 合同订立信息表
* </p>
*
* @author wangrui
* @since 2022-12-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractAccountSumV {

    @ApiModelProperty("合同总金额")
    private BigDecimal amountSum;
    @ApiModelProperty("累计收款金额")
    private BigDecimal paymentAmountSum;
    @ApiModelProperty("累计开票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("累计收入确认金额")
    private BigDecimal profitAmount;
    @ApiModelProperty("累计计划收款金额")
    private BigDecimal collectedSum;
    @ApiModelProperty("累计已到期金额")
    private BigDecimal dueSumAmount;
}
