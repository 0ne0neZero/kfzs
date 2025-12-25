package com.wishare.contract.apps.fo.revision.pay.settlement;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "结算计划-生成成本计划-请求信息")
public class GenerateCostPlanF {

    @NotBlank(message = "结算计划id不能为空")
    private String planId;

}
