package com.wishare.finance.infrastructure.remote.vo.fy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("方圆返参")
public class FYSendresultV {
    @ApiModelProperty("")
    private Integer code;
    @ApiModelProperty("")
    private String msg;
}
