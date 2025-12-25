package com.wishare.finance.apps.model.bill.fo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("减免管理详情查询dto")
public class ChargeDeductionDetailF {

    @ApiModelProperty(value = "账单调整id集合",required = true)
    List<Long> adjustIds;

}
