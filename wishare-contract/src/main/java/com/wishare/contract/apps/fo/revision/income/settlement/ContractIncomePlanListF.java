package com.wishare.contract.apps.fo.revision.income.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "结算单页面-查询成本预估计划请求信息")
public class ContractIncomePlanListF {

    @ApiModelProperty(value = "合同id")
    @NotBlank(message = "合同id不能为空")
    private String contractId;

    @ApiModelProperty(value = "计量周期列表")
    @NotEmpty(message = "计量周期不能为空")
    private List<IncomePlanPeriodF> periodList;

}
