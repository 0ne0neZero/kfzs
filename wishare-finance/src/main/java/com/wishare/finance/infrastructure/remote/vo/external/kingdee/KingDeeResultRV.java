package com.wishare.finance.infrastructure.remote.vo.external.kingdee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "金蝶返回结果", description = "金蝶返回结果")
public class KingDeeResultRV {

    @ApiModelProperty("返回代码 0000代表成功，0001代表失败")
    private String code;

    @ApiModelProperty("返回信息")
    private String mess;

    @ApiModelProperty("金蝶单据编号")
    private String billno;

}
