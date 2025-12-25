package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("新增逾期说明")
@Data
public class CollectionPlanOverdueStatementF {

    @ApiModelProperty(value = "收款计划id", required = true)
    @NotNull(message = "收款计划id不能为空")
    private Long collectionPlanId;

    @ApiModelProperty("逾期原因")
    private String overdueReason;

    @ApiModelProperty("逾期说明")
    private String overdueStatement;

    @ApiModelProperty("逾期原因id集")
    private String overdueReasonIds;
}
