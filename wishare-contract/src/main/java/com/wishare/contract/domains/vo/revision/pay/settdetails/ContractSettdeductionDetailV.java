package com.wishare.contract.domains.vo.revision.pay.settdetails;

import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementPeriodE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("扣款明细")
public class ContractSettdeductionDetailV {

    @ApiModelProperty("区域")
    private Integer belongRegion;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合作方名称")
    private String oppositeOne;

    @ApiModelProperty("实际结算金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("计量周期")
    private List<ContractPayConcludeSettlementPeriodE> periods;

    @ApiModelProperty("计量周期")
    private String periodDisplay;

    @ApiModelProperty("扣款原因id")
    private String typeId;

    @ApiModelProperty("扣款原因")
    private String type;

    @ApiModelProperty("当前扣款金额")
    private String amount;

}
