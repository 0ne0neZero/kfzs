package com.wishare.contract.apps.fo.revision.pay.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "更新结算单步骤信息请求对象")
public class ContractPaySettlementStepF {

    @ApiModelProperty(value = "结算单id")
    @NotBlank(message = "结算单id不能为空")
    private String settlementId;

    @ApiModelProperty(value = "步骤")
    @NotNull(message = "步骤不能为空")
    private Integer step;

}
