package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("财务单据数据状态信息查询")
public class OrderStatusResult {

    @ApiModelProperty("true 成功 false 失败")
    private Boolean result;
    @ApiModelProperty("0 成功 1 失败")
    private Integer code;
    @ApiModelProperty("")
    private String data;
    @ApiModelProperty("成功 空 失败返回失败原因")
    private String message;
}
