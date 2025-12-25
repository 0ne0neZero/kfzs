package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataV {
    @ApiModelProperty(value = "批量打印编号")
    public String batchId;
}
