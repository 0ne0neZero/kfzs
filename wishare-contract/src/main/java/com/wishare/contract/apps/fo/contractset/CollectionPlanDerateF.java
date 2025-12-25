package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("减免申请参数")
@Data
public class CollectionPlanDerateF {

    @ApiModelProperty("合同Id")
    @NotNull(message = "合同Id不可为空")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    @NotNull(message = "收款计划id不可为空")
    private Long collectionPlanId;
    @ApiModelProperty("减免金额（元）")
    @NotNull(message = "减免金额不可为空")
    private BigDecimal derateAmount;
    @ApiModelProperty("减免原因")
    @NotNull(message = "减免原因不可为空")
    private String derateReason;
}
