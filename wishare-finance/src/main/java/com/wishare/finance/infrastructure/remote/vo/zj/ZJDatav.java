package com.wishare.finance.infrastructure.remote.vo.zj;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("中交返参data解析")
public class ZJDatav {

    @ApiModelProperty("错误编号")
    private String Code;
    @ApiModelProperty("错误编号")
    private String Level;
    @ApiModelProperty("错误级别")
    private String Message;
    @ApiModelProperty("错误详细描述")
    private String Detail;
    @ApiModelProperty("请求ID")
    private String RequestId;
    @ApiModelProperty("错误信息补充说明")
    private String extensionMessage;

}
