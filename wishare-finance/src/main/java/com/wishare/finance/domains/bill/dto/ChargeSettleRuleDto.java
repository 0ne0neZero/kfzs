package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "缴费规则请求对象", description = "缴费规则请求对象")
public class ChargeSettleRuleDto {

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private String itemId;

    @ApiModelProperty("费项名称")
    private String itemName;

    @ApiModelProperty("缴费周期 1-月 2-季度 3-半年度 4-年")
    private Integer type;

}
