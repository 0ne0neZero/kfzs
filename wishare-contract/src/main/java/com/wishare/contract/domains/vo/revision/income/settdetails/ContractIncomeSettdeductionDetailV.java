package com.wishare.contract.domains.vo.revision.income.settdetails;

import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeSettlementPeriodE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("减免明细")
public class ContractIncomeSettdeductionDetailV {

    @ApiModelProperty("区域")
    private Integer belongRegion;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合作方名称")
    private String oppositeOne;

    @ApiModelProperty("实际确收金额")
    private BigDecimal realIncomeAmount;

    @ApiModelProperty("计量周期")
    private List<ContractIncomeConcludeSettlementPeriodE> periods;

    @ApiModelProperty("计量周期")
    private String periodDisplay;

    @ApiModelProperty("减免原因id")
    private String typeId;

    @ApiModelProperty("减免原因")
    private String type;

    @ApiModelProperty("当期减免金额")
    private String amount;

}
