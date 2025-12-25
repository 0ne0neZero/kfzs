package com.wishare.contract.domains.vo.revision.income.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "支出合同结算单-详情-合同信息")
public class ContractIncomeSettlementContractInfoV {


    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmountOriginalRate;

    @ApiModelProperty("合同金额-无用字段")
    private BigDecimal contractAmount;

    @ApiModelProperty("合同变更后金额")
    private BigDecimal changContractAmount;

    @ApiModelProperty("我方单位id")
    private String ourPartyId;

    @ApiModelProperty("我方单位名称")
    private String ourParty;

    @ApiModelProperty("对方单位id")
    private String oppositeOneId;

    @ApiModelProperty("对方单位名称")
    private String oppositeOne;

    @ApiModelProperty("对方单位id-2 合同页面没用这个")
    private String oppositeTwoId;

    @ApiModelProperty("对方单位名称-2 合同页面没用这个")
    private String oppositeTwo;

    private String qydws;

}
