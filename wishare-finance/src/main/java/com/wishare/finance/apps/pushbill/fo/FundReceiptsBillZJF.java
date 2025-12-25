package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ApiModel("生产资金收款单")
public class FundReceiptsBillZJF {

    @ApiModelProperty("认领批次号id")
    @NotNull(message = "认领批次不能为空")
    private List<Long> ids;

    @ApiModelProperty("项目id")
    private List<String> supCpUnitIds;
}
