package com.wishare.contract.domains.vo.revision.income.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author longhuadmin
 */
@ApiModel(value = "结算单计量周期列表信息返回对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractIncomePlanPeriodV {

    @ApiModelProperty(value = "周期列表",notes = "周期列表")
    private List<IncomePlanPeriodV> incomePlanPeriodList;

}
