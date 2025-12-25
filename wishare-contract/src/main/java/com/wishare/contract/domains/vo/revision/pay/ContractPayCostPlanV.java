package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hhb
 * @describe
 * @date 2025/11/6 15:49
 */
@Data
public class ContractPayCostPlanV {

    @ApiModelProperty(value = "成本-费项编码")
    private String accountItemCode;
    @ApiModelProperty(value = "成本-费项名称")
    private String accountItemName;
    @ApiModelProperty(value = "成本-费项全码")
    private String accountItemFullCode;
    @ApiModelProperty(value = "成本-费项全称")
    private String accountItemFullName;
    @ApiModelProperty(value = "成本-合约规划可用金额")
    private BigDecimal summarySurplusAmount;
}
