package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("汇总单据删除请求参数")
public class DelPushBillF {
    @ApiModelProperty(value = "汇总单据id")
    private Long id;
}
