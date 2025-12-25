package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("导入账单结果")
public class AddBatchBillV {

    @ApiModelProperty(value = "数据行号")
    private Integer index;

    @ApiModelProperty(value = "处理结果")
    private String result;

}
