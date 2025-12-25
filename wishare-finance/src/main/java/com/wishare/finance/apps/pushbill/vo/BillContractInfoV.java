package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("报账单对应账单-合同相关字段")
public class BillContractInfoV {
    @ApiModelProperty("合同id")
    private String ContractId;

    @ApiModelProperty("结算单id")
    private String settlementId;

}
