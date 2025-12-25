package com.wishare.finance.domains.voucher.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("审批流程申请结果")
public class ApproveResultDto {

    @ApiModelProperty("0, 审批中 1  无需审批")
    private Integer approveState = 1;



}
