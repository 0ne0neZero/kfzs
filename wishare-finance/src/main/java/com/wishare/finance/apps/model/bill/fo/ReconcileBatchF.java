package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("批量对账请求参数")
public class ReconcileBatchF {

    @ApiModelProperty("账单id列表")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty("对账结果(false:未对平，true:已对平)")
    private boolean result;

}
