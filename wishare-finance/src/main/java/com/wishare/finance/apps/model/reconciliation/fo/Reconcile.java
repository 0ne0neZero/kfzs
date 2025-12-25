package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("商户清分账票对账")
public class Reconcile {

    @ApiModelProperty("项目id")
    public String communityId;

    @ApiModelProperty("项目名称")
    public String communityName;
}
