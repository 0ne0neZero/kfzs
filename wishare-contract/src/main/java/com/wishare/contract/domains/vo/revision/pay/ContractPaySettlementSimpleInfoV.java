package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("财务结算表单信息展示VO")
public class ContractPaySettlementSimpleInfoV {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("结算单编号")
    private String payFundNumber;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("供应商名称")
    private String merchantName;

    @ApiModelProperty("实际结算金额")
    private BigDecimal realSettlementAmount;

}
