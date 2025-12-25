package com.wishare.contract.apps.fo.revision.pay.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "更新结算单步骤信息请求对象")
public class ContractPaySettlementF {

    @ApiModelProperty(value = "结算单id")
    private String settlementId;

    @ApiModelProperty(value = "结算单名称")
    private String title;

    @ApiModelProperty(value = "实际结算金额")
    private BigDecimal totalAmount;

}
