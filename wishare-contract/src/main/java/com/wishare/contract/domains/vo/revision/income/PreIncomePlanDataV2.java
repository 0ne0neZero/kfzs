package com.wishare.contract.domains.vo.revision.income;

import com.wishare.contract.domains.vo.settle.PayConcludePlanV2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "预生成收款计划数据V2")
public class PreIncomePlanDataV2 {

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("费项ID")
    private String chargeItemId;

    @ApiModelProperty("成本预估金额合计")
    private BigDecimal groupPlannedCollectionAmount;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "结算周期名称")
    private String splitModeName;

    @ApiModelProperty(value = "收款计划分组数据")
    private List<IncomeConcludePlanV2> planVList;

    @ApiModelProperty(value = "补充合同ID")
    private String supplyContractId;

    @ApiModelProperty(value = "补充合同开始日期")
    private LocalDate supplyStartTime;

    @ApiModelProperty(value = "补充合同结束日期")
    private LocalDate supplyEndTime;

    //收费方式ID
    private String chargeMethodId;
    //收费方式
    private String chargeMethodName;
    //税率ID
    private String taxRateId;
    //税率
    private String taxRate;
    //款项类型ID
    private String typeId;
    //款项类型
    private String type;
}
